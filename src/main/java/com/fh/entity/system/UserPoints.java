package com.fh.entity.system;

import lombok.Data;

@Data
public class UserPoints {

    //未完成任务默认值为"0" 完成为"1"
    private String id;
    private String userId;
    private String loginGame;
    private String shareGame;
    private String inviteGame;
    private String poohGame;
    private Integer pusherGame;
    private String firstPay;
    //累积消耗金币
    private Integer costGoldSum;
    //消耗金币赠送标签
    private String costGoldSum_Tag;
    //今日积分
    private Integer todayPoints;
    //积分奖励金币标签
    private String pointsReward_Tag;
    //竞猜游戏
    private String betGame;


}
