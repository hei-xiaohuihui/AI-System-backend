package com.lyh.aiSystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyh.aiSystem.entity.ChatSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {

    /**
     *  根据用户id查询其所有的会话id
     * @param userId
     * @return
     */
    @Select("SELECT session_id FROM cs_chat_session WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<String> selectSessionIds(@Param("userId") Long userId);
}
