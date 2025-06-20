package com.lyh.aiSystem.constant;

import java.util.Set;

/**
 * @author BigHH
 *  讲座状态常量
 */
public class LectureStatusConstant {

    // 待审核
    public static final String LECTURE_STATUS_PENDING = "PENDING";

    // 审核通过
    public static final String LECTURE_STATUS_APPROVED = "APPROVED";

    // 审核未通过
    public static final String LECTURE_STATUS_REJECTED = "REJECTED";

    public static final Set<String> LECTRUE_STATUS_SET = Set.of(LECTURE_STATUS_PENDING, LECTURE_STATUS_APPROVED, LECTURE_STATUS_REJECTED);
}
