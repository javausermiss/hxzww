package com.fh.entity.system;

import lombok.Data;

/**
 * 推币机游戏的单场游戏记录(上机--->下机)
 * @author wjy
 * @date 2018/09/04
 */
@Data
public class PusherGameDetail {

    private String id;
    private String gameId;
    private String roomId;
    private String userId;
    private Integer expenditure;
    private Integer income;
    private String createTime;
    private String tag;
    private String updateTime;
    private Integer versionLock;

}
