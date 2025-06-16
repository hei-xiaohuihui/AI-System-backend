package com.lyh.aiSystem.tool;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author BigHH
 */
@SpringBootTest
public class DataTimeToolsTest {

    @Autowired
    private DataTimeTools dataTimeTools;

    @Test
    public void testGetCurrentDataTime() {
        System.out.println(dataTimeTools.getCurrentDataTime());
    }
}
