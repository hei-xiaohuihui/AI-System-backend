package com.lyh.aiSystem.tool;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.aiSystem.pojo.dto.LecturePageDtoForUser;
import com.lyh.aiSystem.pojo.vo.LecturePageVoForUser;
import com.lyh.aiSystem.pojo.vo.LecturePageVoForUserEnroll;
import com.lyh.aiSystem.service.LectureEnrollService;
import com.lyh.aiSystem.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author BigHH
 *  讲座工具——提供给AI调用
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LectureTools {

    private final LectureService lectureService;

    private final LectureEnrollService lectureEnrollService;

    @Tool(name = "getLectures", description = "查询所有讲座信息")
    public List<LecturePageVoForUser> getLectures() {
        List<LecturePageVoForUser> lectures = lectureService.getAllLectures();
        log.debug("查询到的所有讲座信息:{}", lectures);
        return lectures;
    }

    @Tool(name = "enrollLecture", description = "用户报名讲座")
    public String enrollLecture(@ToolParam(description = "讲座id") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("讲座id不能为空！");
        }
        log.debug("用户想报名的讲座id:{}", id);
        return lectureEnrollService.enrollLecture(id);
    }

    @Tool(name = "getEnrollLectures", description = "查询用户已报名的讲座")
    public List<LecturePageVoForUserEnroll> getEnrollLectures() {
        List<LecturePageVoForUserEnroll> lectures = lectureEnrollService.getEnrollLectures();
        log.debug("已报名的讲座信息:{}", lectures);
        return lectures;
    }

    @Tool(name = "cancelLecture", description = "用于取消报名讲座")
    public String cancelLecture(@ToolParam(description = "讲座id") Long lectureId) {
        return lectureEnrollService.cancelEnroll(lectureId);
    }

}
