package com.lyh.aiSystem.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author BigHH
 *  文件保存后返回的视图对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileSaveVo {

    // 可以访问文件的URL
    private String resourceUrl;

    // 向量数据库中保存的文件的唯一标识
    private String ragDocId;
}
