package com.lyh.aiSystem.service;

import com.lyh.aiSystem.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author BigHH
 *  使用redis实现缓存服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisQACacheService {

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisProperties redisProperties;

    // redis缓存的key前缀
    // 公共前缀
    private static final String QA_COMMON_KEY_PREFIX = "qa:";
    // 回答缓存的key前缀
    private static final String CACHE_KEY_PREFIX = "cache:";
    // 缓存访问计数的key前缀
    private static final String COUNTER_KEY_PREFIX = "counter:";

    /**
     * 缓存问答对
     * @param sessionId
     * @param question
     * @param answer
     */
    public void setQACache(String sessionId, String question, String answer) {
        // 将问题和sessionId进行哈希处理和key前缀进行拼接作为缓存的key
        String questionHash = getStringHash(question);
        String sessionIdHash = getStringHash(sessionId) + ":";
        // 问答缓存的key
        String cacheKey = QA_COMMON_KEY_PREFIX + CACHE_KEY_PREFIX + sessionIdHash + questionHash;
        // 访问计数的key
        String counterKey = QA_COMMON_KEY_PREFIX + COUNTER_KEY_PREFIX + sessionIdHash + questionHash;

        // 判断缓存是否已满
        log.debug("Cache size:{}", redisTemplate.keys(QA_COMMON_KEY_PREFIX + CACHE_KEY_PREFIX + "*").size());
        if (redisTemplate.keys(QA_COMMON_KEY_PREFIX + CACHE_KEY_PREFIX + "*").size() >= redisProperties
                .getMaxCacheSize()) {
            evictLFUCache(); // 执行LFU缓存失效策略
        }

        // 缓存存储答案
        redisTemplate.opsForValue().set(cacheKey, answer, redisProperties.getCacheExpireTime(), TimeUnit.MILLISECONDS);
        // 存储访问计数: 初始化值为1
        redisTemplate.opsForValue().setIfAbsent(counterKey, "1", redisProperties.getCacheExpireTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * 获取问题的缓存答案
     * @param sessionId
     * @param question
     * @return
     */
    public String getAnswerCache(String sessionId, String question) {
        String questionHash = getStringHash(question);
        String sessionIdHash = getStringHash(sessionId) + ":";
        String cacheKey = QA_COMMON_KEY_PREFIX + CACHE_KEY_PREFIX + sessionIdHash + questionHash;
        String counterKey = QA_COMMON_KEY_PREFIX + COUNTER_KEY_PREFIX + sessionIdHash + questionHash;

        String answer = redisTemplate.opsForValue().get(cacheKey);

        if (answer != null) { // 缓存命中
            log.debug("Cache hit for question:{}", question);
            // 增加访问计数
            redisTemplate.opsForValue().increment(counterKey);
            // 更新过期时间
            redisTemplate.expire(counterKey, redisProperties.getCacheExpireTime(), TimeUnit.MILLISECONDS);
        } else {
            log.debug("Cache miss for question:{}", question);
        }
        return answer;
    }

    /**
     * 基于LFU的缓存失效策略
     */
    private void evictLFUCache() {
        // 获取所有访问计数器key
        // todo 待改进：使用keys可能引发性能问题
        Set<String> counterKeys = redisTemplate.keys(QA_COMMON_KEY_PREFIX + COUNTER_KEY_PREFIX + "*");

        if (CollectionUtils.isEmpty(counterKeys)) {
            return;
        }

        // 找到访问次数最少的计数器key
        String leastFrequentCounterKey = counterKeys.stream()
                .min(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        String count1 = redisTemplate.opsForValue().get(o1);
                        String count2 = redisTemplate.opsForValue().get(o2);
                        // 返回两者按升序比较的结果
                        return Long.compare(
                                Long.parseLong(count1 != null ? count1 : "0"),
                                Long.parseLong(count2 != null ? count2 : "0"));
                    }
                }).orElse(null);

        if (leastFrequentCounterKey != null) {
            // 获取缓存答案对应的key
            String cacheKey = QA_COMMON_KEY_PREFIX + CACHE_KEY_PREFIX +
                    leastFrequentCounterKey.substring((QA_COMMON_KEY_PREFIX + COUNTER_KEY_PREFIX).length());

            // 删除缓存和计数器
            redisTemplate.delete(cacheKey);
            redisTemplate.delete(leastFrequentCounterKey);
        }
    }

    /**
     * 根据会话id删除该会话下对应的所有缓存（包括问答缓存和计数缓存）
     * @param sessionId
     */
    public void deleteCacheBySessionId(String sessionId) {
        // 根据sessionId获取对应的缓存key
        String sessionIdHash = getStringHash(sessionId) + ":";
        // 获取所有对应的问答缓存key
        String cacheKey = QA_COMMON_KEY_PREFIX + CACHE_KEY_PREFIX + sessionIdHash + "*";
        // 获取所有对应的访问计数缓存key
        String counterKey = QA_COMMON_KEY_PREFIX + COUNTER_KEY_PREFIX + sessionIdHash + "*";

        // 删除所有对应的缓存和计数缓存
        // todo 待改进：使用keys会扫描整个key空间，可能引发效率问题
        redisTemplate.delete(redisTemplate.keys(cacheKey));
        redisTemplate.delete(redisTemplate.keys(counterKey));
    }

    /**
     * 将字符串转为哈希值的字符串形式
     */
    private String getStringHash(String question) {
        // 处理问题字符串
        String processedQuestion = question.trim() // 去除字符串前后的空格
                .toLowerCase(); // 统一转为小写，忽略大小写的差异（提高对同义问题的识别能力）
        // 计算哈希值并转为正数后再转为字符串返回
        return String.valueOf(Math.abs(processedQuestion.hashCode()));
    }
}
