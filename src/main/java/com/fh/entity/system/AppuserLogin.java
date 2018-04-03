package com.fh.entity.system;

import lombok.Data;

@Data
public class AppuserLogin {
    private String APPUSERLOGININFO_ID;
    private String LOGIN_PHONE_TYPE;
    private String LOGIN_IP;
    private String USER_ID;
    private String LOGIN_TIME;
    private String LOGOUT_TIME;
    private String NICKNAME;
    private String CTYPE;
    private String ONLINE_TYPE;
    private String CHANNEL;
    private String ACCESS_TOKEN;
}
