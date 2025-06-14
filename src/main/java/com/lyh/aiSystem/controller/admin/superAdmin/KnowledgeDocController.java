package com.lyh.aiSystem.controller.admin.superAdmin;

import com.lyh.aiSystem.pojo.dto.KnowledgeDocCreateDto;
import com.lyh.aiSystem.pojo.dto.KnowledgeDocPageDto;
import com.lyh.aiSystem.pojo.dto.KnowledgeDocUpdateDto;
import com.lyh.aiSystem.service.KnowledgeDocService;
import com.lyh.aiSystem.utils.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/superAdmin/knowledgeDoc")
public class KnowledgeDocController {

    private final KnowledgeDocService  knowledgeDocService;

    /**
     *  超级管理员上传/创建知识文档接口
     * @param dto
     * @param file
     * @return
     */
    @PostMapping("/create")
    public Result createKnowledgeDoc(@RequestPart @Valid KnowledgeDocCreateDto dto, @RequestPart("file") MultipartFile file) {
        knowledgeDocService.createKnowledgeDoc(dto, file);
        return Result.success();
    }

    /**
     *  超级管理员更新知识文档接口
     * @param dto
     * @return
     */
    @PutMapping("/update")
    public Result updateKnowledgeDoc(@RequestBody @Valid KnowledgeDocUpdateDto dto) {
        knowledgeDocService.updateKnowledgeDoc(dto);
        return Result.success();
    }

    /**
     *  超级管理员获取知识文档详情接口
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public Result getKnowledgeDocDetail(@RequestParam("id") Long id) {
        return Result.success(knowledgeDocService.getKnowledgeDocDetail(id));
    }

    /**
     *  超级管理员删除知识文档接口
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteKnowledgeDoc(@RequestParam("id") Long id, @RequestParam("resourceUrl") String resourceUrl) {
        knowledgeDocService.deleteKnowledgeDocById(id, resourceUrl);
        return Result.success();
    }

    /**
     *  超级管理员分页查询知识文档信息接口
     * @param dto
     * @return
     */
    @GetMapping("/page")
    public Result pageKnowledgeDocs(KnowledgeDocPageDto dto) {
        return Result.success(knowledgeDocService.pageKnowledgeDocs(dto));
    }

}
