package com.fh.entity.system;

import lombok.Data;

/**
 * 用户次日登陆领取昨日抓中的游戏币
 */
@Data
public class LoginRewardGold {
    private String id;
    private String userId;
    private Integer gold;
    private String createTime;
    private String tag;
    private Integer rewardGold;
}
