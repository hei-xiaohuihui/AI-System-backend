package com.lyh.aiSystem;

import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class AiSystemApplicationTests {

    @Autowired
    private OpenAiEmbeddingModel embeddingModel;

    @Test
    void contextLoads() {

        float[] floats = embeddingModel.embed("hello world");
        System.out.println(Arrays.toString(floats));
    }

}
