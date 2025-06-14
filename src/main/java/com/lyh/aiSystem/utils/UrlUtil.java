package com.lyh.aiSystem.utils;

import com.lyh.aiSystem.properties.FileUploadProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author BigHH
 *  处理url的工具类
 */
@RequiredArgsConstructor
@Component
public class UrlUtil {

    private final FileUploadProperties fileUploadProperties;

    /**
     *  从url中提取出路径部分：
     *    http://localhost:7816/uploads/pdf/Lecture/f60f2120-ebf8-4964-b676-d5de04426b45.pdf
     *    -> /uploads/pdf/Lecture/f60f2120-ebf8-4964-b676-d5de04426b45.pdf
     * @param resourceUrl
     * @return
     */
    public String extractPath(String resourceUrl) {
        try {
            URI uri = new URI(resourceUrl);
            return uri.getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  提取url中的文件名：
     *     http://localhost:7816/uploads/pdf/Lecture/f60f2120-ebf8-4964-b676-d5de04426b45.pdf
     *     -> f60f2120-ebf8-4964-b676-d5de04426b45
     * @param resourceUrl
     * @return
     */
    public String extractFileName(String resourceUrl) {
        String file = resourceUrl.substring(resourceUrl.lastIndexOf("/") + 1);// f60f2120-ebf8-4964-b676-d5de04426b45.pdf
        return file.substring(0, file.lastIndexOf(".")); // 去调后缀获取文件名，如: f60f2120-ebf8-4964-b676-d5de04426b45
    }

    /**
     *  获取文档本地保存的绝对路径：
     *     http://localhost:7816/uploads/pdf/Lecture/f60f2120-ebf8-4964-b676-d5de04426b45.pdf
     *     -> D:/Intellij Idea Project/AI-System/uploads/pdf/Lecture/f60f2120-ebf8-4964-b676-d5de04426b45.pdf
     *     即basePath + /uploads/pdf/Lecture/f60f2120-ebf8-4964-b676-d5de04426b45.pdf
     * @param resourceUrl
     * @return
     */
    public String getLocalFilePath(String resourceUrl) {
        return fileUploadProperties.getBasePath() + this.extractPath(resourceUrl);
    }

}
