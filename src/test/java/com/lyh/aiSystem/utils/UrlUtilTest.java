package com.lyh.aiSystem.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author BigHH
 */
@SpringBootTest
public class UrlUtilTest {

    @Autowired
    private UrlUtil uriUtil;
    @Autowired
    private UrlUtil urlUtil;

    @Test
    public void testExtreactFileName() {
        String url = "http://localhost:7816/uploads/pdf/Lecture/f60f2120-ebf8-4964-b676-d5de04426b45.pdf";
        String fileName = uriUtil.extractFileName(url);
        System.out.println(fileName);

        String path = uriUtil.extractPath(url);
        System.out.println(path);

        String localPath = urlUtil.getLocalFilePath(url);
        System.out.println(localPath);

    }
}
