package com.lyh.aiSystem.repository;

import com.lyh.aiSystem.pojo.vo.FileSaveVo;
import org.springframework.web.multipart.MultipartFile;

public interface FileRepository {

    /**
     *  保存上传的文件
     * @param file 要上传的文件
     * @param basePath 文件保存基本路径
     * @param path 文件保存路径
     * @return
     */
    FileSaveVo save(MultipartFile file, String basePath, String path);

    /**
     *  删除保存的本地文件
     * @param resourcePath 如：/uploads/pdf/Lecture/f60f2120-ebf8-4964-b676-d5de04426b45.pdf
     */
    void delete(String resourcePath);
}
