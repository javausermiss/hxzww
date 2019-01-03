package com.fh.vo.system;

import lombok.Data;

import java.util.List;

@Data
public class RedVo {
    private String id;
    private String userId;
    private String redGold;
    private String redNum;
    private String version;
    private String tag;
    private String createTime;
    private String nickname;
    private String imgurl;
    private List<UserInfoVo> userInfo;
}
