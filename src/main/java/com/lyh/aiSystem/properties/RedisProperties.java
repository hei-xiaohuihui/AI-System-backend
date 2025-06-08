package com.lyh.aiSystem.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author BigHH
 *  redis配置读取类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "lyh.redis")
public class RedisProperties {

    // 缓存过期时间
    private long cacheExpireTime;

    // 最大缓存条目数
    private long maxCacheSize;
}
