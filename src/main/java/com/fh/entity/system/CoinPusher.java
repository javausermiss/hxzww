package com.fh.entity.system;

import lombok.Data;

/**
 * 推币机游戏记录实体类
 * @author wjy
 * @date 2018/05/30
 */
@Data
public class CoinPusher {
    private String id;
    private String roomId;
    private String userId;
    private String costGold;
    private String returnGold;
    private String finishFlag;
}
