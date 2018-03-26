package com.fh.entity.system;

import lombok.Data;

/**
 * 追投实体
 */
@Data
public class AfterVoting {
    private String ID;
    private String USER_ID;
    private String ROOM_ID;
    private Integer AFTER_VOTING;//追投期数 1 ,5 , 10 , 20
    private String LOTTERY_NUM;//竞猜数字(0-9)
    private Integer  MULTIPLE; //倍数

}
