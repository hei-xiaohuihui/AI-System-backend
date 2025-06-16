package com.lyh.aiSystem.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author BigHH
 *  获取当前日期和时间的工具
 */
@Component
public class DataTimeTools {

    @Tool(description = "获取当前日期和时间")
    public String getCurrentDataTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
