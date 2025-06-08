package com.lyh.aiSystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyh.aiSystem.pojo.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    @Select("SELECT * FROM cs_chat_message WHERE session_id = #{conversationId} ORDER BY created_at ASC")
    List<ChatMessage> getMessagesBySessionId(@Param("conversationId") String conversationId);
}
