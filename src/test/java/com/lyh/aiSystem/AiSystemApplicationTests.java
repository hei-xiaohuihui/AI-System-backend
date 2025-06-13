package com.lyh.aiSystem;

import org.junit.jupiter.api.Test;

import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class AiSystemApplicationTests {

    @Autowired
    private OpenAiEmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;

    /**
     * 测试向量数据库
     */
    @Test
    void testVectorStore() {
        Resource resource = new FileSystemResource("src/test/resources/教资知识笔记.pdf");
        // 1.创建PDF的读取器
        PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(
                resource,
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 将每页pdf作为一个Document
                        .build()
        );
        // 2.读取PDF文档，拆分为Document
        List<Document> documents = pagePdfDocumentReader.read();
        // 3.写入向量数据库
        vectorStore.add(documents);
        // 4.搜索向量数据库
        SearchRequest request = SearchRequest.builder()
                .query("论语中教育的目的是什么")
                .topK(1) // 搜索返回的分数最高的1个结果
                .similarityThreshold(0.5f) // 搜索相似度阈值
                .filterExpression("file_name == '教资知识笔记.pdf'")
                .build();
        List<Document> docs = vectorStore.similaritySearch(request);
        if(docs == null) {
            System.out.println("没有找到结果");
            return;
        }
        for(Document doc : docs) {
            System.out.println(doc.getId());
            System.out.println(doc.getScore());
            System.out.println(doc.getText());
        }
    }

    /**
     *  测试向量模型
     */
    @Test
    void contextLoads() {

        float[] floats = embeddingModel.embed("hello world");
        System.out.println(Arrays.toString(floats));
    }



}
