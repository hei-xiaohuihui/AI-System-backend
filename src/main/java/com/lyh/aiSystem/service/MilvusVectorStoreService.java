package com.lyh.aiSystem.service;

import com.lyh.aiSystem.properties.MilvusProperties;
import lombok.RequiredArgsConstructor;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BigHH
 *  使用Milvus向量数据库保存上传的文档
 */
@RequiredArgsConstructor
@Service
public class MilvusVectorStoreService {

    private final VectorStore  vectorStore;

    private final MilvusProperties milvusProperties;

    /**
     *  保存上传的文档到向量数据库中
     * @param resource 上传的文档
     * @param ragDocId 向量数据库元数据中文档的唯一标识
     */
    public void save(Resource resource, String ragDocId) {
        // 1.创建pdf读取器
        PagePdfDocumentReader pagePdfReader = new PagePdfDocumentReader(
                resource,
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults()) // 使用默认的文本格式化器
                        .withPagesPerDocument(1) // 一页作为一个Document对象
                        .build());

        // 2.读取pdf文档，并进行拆分
        List<Document> documents = pagePdfReader.read().stream()
                .peek(doc -> doc.getMetadata().put(milvusProperties.getSaveKey(), ragDocId)) // 在向量数据库原数据中加入自定义字段用于唯一标识每个文档（用文档的新文件名标识）
                .collect(Collectors.toList());

        // 3.写入向量数据库中
        vectorStore.add(documents);
    }

    /**
     *  删除向量数据库中的文档数据
     * @param ragDocId 根据元数据中的唯一标识删除
     */
    public void delete(String ragDocId) {
        // 创建元数据过滤表达式
        Filter.Expression filterExpression = new Filter.Expression(
                Filter.ExpressionType.EQ,
                new Filter.Key(milvusProperties.getSaveKey()),
                new Filter.Value(ragDocId));
        // 根据过滤表达式删除文档
        vectorStore.delete(filterExpression);
    }


}
