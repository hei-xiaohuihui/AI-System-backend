package com.lyh.aiSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyh.aiSystem.constant.AdminRoleConstant;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.mapper.KnowledgeDocMapper;
import com.lyh.aiSystem.pojo.dto.KnowledgeDocCreateDto;
import com.lyh.aiSystem.pojo.dto.KnowledgeDocPageDto;
import com.lyh.aiSystem.pojo.dto.KnowledgeDocUpdateDto;
import com.lyh.aiSystem.pojo.entity.KnowledgeDoc;
import com.lyh.aiSystem.pojo.vo.FileSaveVo;
import com.lyh.aiSystem.pojo.vo.KnowledgeDocVo;
import com.lyh.aiSystem.repository.FileRepository;
import com.lyh.aiSystem.service.AdminService;
import com.lyh.aiSystem.service.KnowledgeDocService;
import com.lyh.aiSystem.service.MilvusVectorStoreService;
import com.lyh.aiSystem.utils.AdminContextUtil;
import com.lyh.aiSystem.utils.UrlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@Service
public class KnowledgeDocServiceImpl implements KnowledgeDocService {

    private final KnowledgeDocMapper knowledgeDocMapper;

    private final AdminContextUtil adminContextUtil;

    private final FileRepository  fileRepository;

    private final AdminService adminService;

    private final MilvusVectorStoreService vectorStoreService;

    private final UrlUtil urlUtil;

    /**
     * 插入知识文档数据到知识文档表中
     *
     * @param dto
     * @param file
     */
    @Override
    public void createKnowledgeDoc(KnowledgeDocCreateDto dto, MultipartFile file) {
        isSuperAdmin();
        KnowledgeDoc knowledgeDoc = new KnowledgeDoc();
        // 拷贝属性
        BeanUtils.copyProperties(dto, knowledgeDoc);

        // 将pdf文件保存到本地
        FileSaveVo saveVo = adminService.uploadFile(file);
        // 将文档存入向量数据库中
        Resource resource = new FileSystemResource(new File(urlUtil.getLocalFilePath(saveVo.getResourceUrl()))); // 将MultipartFile转换为Resource
        vectorStoreService.save(resource, saveVo.getRagDocId()); // 保存pdf文件到向量数据库中

        // 设置resourceUrl和ragDocId
        knowledgeDoc.setResourceUrl(saveVo.getResourceUrl());
        // 设置ragDocId
        knowledgeDoc.setRagDocId(saveVo.getRagDocId());
        // 插入数据库
        int insertResult = knowledgeDocMapper.insert(knowledgeDoc);
        if(insertResult == 0) {
            // todo 数据库插入失败时删除本地和向量数据库中保存的文档
            throw new BaseException(ExceptionEnum.DB_INSERT_ERROR);
        }
    }

    /**
     *  更新知识文档数据
     * @param dto
     */
    @Override
    public void updateKnowledgeDoc(KnowledgeDocUpdateDto dto) {
        isSuperAdmin();
        // 检查是否有字段要更新
        if(!StringUtils.hasText(dto.getTitle()) && !StringUtils.hasText(dto.getDescription()) && !StringUtils.hasText(dto.getResourceUrl())) {
            return;
        }

        KnowledgeDoc knowledgeDoc = knowledgeDocMapper.selectById(dto.getId());
        if(knowledgeDoc == null) {
            throw new BaseException(ExceptionEnum.KNOWLEDGE_DOC_NOT_EXIST);
        }
        // todo 如果要更新文档
            // todo 从向量数据库中删除旧文档
            // todo 将新文档存入向量数据库中，获取 ragDocId
        BeanUtils.copyProperties(dto, knowledgeDoc);
        int updateResult = knowledgeDocMapper.updateById(knowledgeDoc);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_ERROR);
        }
    }

    /**
     *  获取知识文档详情
     * @param id
     * @return
     */
    @Override
    public KnowledgeDocVo getKnowledgeDocDetail(Long id) {
        isSuperAdmin();
        KnowledgeDoc knowledgeDoc = knowledgeDocMapper.selectById(id);
        if(knowledgeDoc != null) {
            KnowledgeDocVo knowledgeDocVo = new KnowledgeDocVo();
            BeanUtils.copyProperties(knowledgeDoc, knowledgeDocVo);
            return knowledgeDocVo;
        }
        throw new BaseException(ExceptionEnum.KNOWLEDGE_DOC_NOT_EXIST);
    }

    /**
     * 删除知识文档
     * @param id
     * @param resourceUrl
     */
    @Override
    public void deleteKnowledgeDocById(Long id, String resourceUrl) {
        isSuperAdmin();
        KnowledgeDoc knowledgeDoc = knowledgeDocMapper.selectById(id);
        if(knowledgeDoc == null) {
            throw new BaseException(ExceptionEnum.KNOWLEDGE_DOC_NOT_EXIST);
        }
        // 删除本地保存的对应文件
        fileRepository.delete(urlUtil.extractPath(resourceUrl));
        // 从向量数据库中删除对应文档
        vectorStoreService.delete(urlUtil.extractFileName(resourceUrl));

        int deleteResult = knowledgeDocMapper.deleteById(id);
        if(deleteResult == 0) {
            throw new BaseException(ExceptionEnum.DB_DELETE_ERROR);
        }
    }

    /**
     *  分页查询知识文档
     * @param dto
     * @return
     */
    @Override
    public IPage<KnowledgeDocVo> pageKnowledgeDocs(KnowledgeDocPageDto dto) {
        isSuperAdmin();
        Page<KnowledgeDoc> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<KnowledgeDoc> queryWrapper = new QueryWrapper<>();
        // 设置查询条件
        if(StringUtils.hasText(dto.getTitle())) {
            queryWrapper.like("title", dto.getTitle());
        }
        if(StringUtils.hasText(dto.getDescription())) {
            queryWrapper.like("description", dto.getDescription());
        }
        queryWrapper.orderByDesc("created_at");
        IPage<KnowledgeDoc> pageResult = knowledgeDocMapper.selectPage(page, queryWrapper);
        // 装为vo
        List<KnowledgeDocVo> voList = pageResult.getRecords().stream().map(knowledgeDoc -> {
            KnowledgeDocVo vo = new KnowledgeDocVo();
            BeanUtils.copyProperties(knowledgeDoc, vo);
            return vo;
        }).collect(Collectors.toList());

        // 封装分页对象返回
        Page<KnowledgeDocVo> knowledgeDocPage = new Page<>();
        knowledgeDocPage.setCurrent(pageResult.getCurrent());
        knowledgeDocPage.setSize(pageResult.getSize());
        knowledgeDocPage.setTotal(pageResult.getTotal());
        knowledgeDocPage.setRecords(voList);
        return knowledgeDocPage;
    }

    /**
     *  判断当前用户是否是超级管理员
     */
    private void isSuperAdmin() {
        if(!adminContextUtil.getAdminRole().equals(AdminRoleConstant.ADMIN_ROLE_SUPER_ADMIN)){
            throw new BaseException(ExceptionEnum.ADMIN_NOT_SUPPER_ADMIN);
        }
    }
}
