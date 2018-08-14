package com.fh.entity.system;

import lombok.Data;

/**
 * 积分收支记录
 */
@Data
public class PointsDetail {
    private String pointsDetail_Id;
    private String userId;
    private String channel;
    private Integer pointsValue;
    private String type;
    private String createTime;
}
