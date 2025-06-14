package com.lyh.aiSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.aiSystem.pojo.dto.KnowledgeDocCreateDto;
import com.lyh.aiSystem.pojo.dto.KnowledgeDocPageDto;
import com.lyh.aiSystem.pojo.dto.KnowledgeDocUpdateDto;
import com.lyh.aiSystem.pojo.vo.KnowledgeDocVo;
import org.springframework.web.multipart.MultipartFile;

public interface KnowledgeDocService {

    void createKnowledgeDoc(KnowledgeDocCreateDto dto, MultipartFile file);

    void updateKnowledgeDoc(KnowledgeDocUpdateDto dto);

    KnowledgeDocVo getKnowledgeDocDetail(Long id);

    void deleteKnowledgeDocById(Long id, String resourceUrl);

    IPage<KnowledgeDocVo> pageKnowledgeDocs(KnowledgeDocPageDto dto);
}
