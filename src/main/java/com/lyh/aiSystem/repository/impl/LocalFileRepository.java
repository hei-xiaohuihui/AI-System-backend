package com.lyh.aiSystem.repository.impl;

import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.repository.FileRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author BigHH
 *  保存传上的文件到本地
 */
@RequiredArgsConstructor
@Component
public class LocalFileRepository implements FileRepository {

    private final HttpServletRequest request;

    /**
     *
     * @param file 要上传的文件
     * @param basePath 文件保存基本路径
     * @param path 文件保存路径
     * @return 访问文件的URL
     */
    @Override
    public String save(MultipartFile file, String basePath, String path) {
        File dir = new File(basePath + path);
        // 判断保存路径是否存在，不存在则创建
        if(!dir.exists() && !dir.mkdirs()) {
            throw new BaseException(ExceptionEnum.SYSTEM_DIR_CREATE_ERROR); // 路径创建失败
        }

        // 为了避免文件命名重复和中文乱码的问题，重命名文件
        String originalFileName = file.getOriginalFilename(); // 获取原始文件名
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".")); // 获取文件后缀
        String newFileName = UUID.randomUUID() + suffix; // 生成新文件名

        // 保存文件
        File des = new File(dir, newFileName);
        try {
            file.transferTo(des);
        } catch (IOException e) {
            throw new BaseException(ExceptionEnum.FILE_UPLOAD_FAILED); // 文件上传失败
        }

        // 获取文件访问URL
        String filePath = path + newFileName; // 文件保存路径: 如/uploads/pdf/Lecture/xxx.pdf
        String url = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(filePath) // 替换请求路径
                .build()
                .toString();

        return url;
    }
}
