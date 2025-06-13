package com.lyh.aiSystem.repository;

import org.springframework.web.multipart.MultipartFile;

public interface FileRepository {

    /**
     *  保存上传的文件
     * @param file 要上传的文件
     * @param basePath 文件保存基本路径
     * @param path 文件保存路径
     * @return
     */
    String save(MultipartFile file, String basePath, String path);
}
