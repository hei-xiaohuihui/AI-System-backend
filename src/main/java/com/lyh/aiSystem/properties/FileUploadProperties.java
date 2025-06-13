package com.lyh.aiSystem.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author BigHH
 *  文件上传配置读取类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "lyh.uploads")
public class FileUploadProperties {

    // 文件上传基础路径
    private String basePath;

    // pdf类型文件
    private PathGroup pdf;

    // word类型文件
    private PathGroup word;

    @Data
    public static class PathGroup {
        private String knowledgePath;
        private String lecturePath;
    }
}
