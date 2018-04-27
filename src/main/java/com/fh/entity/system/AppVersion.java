package com.fh.entity.system;

import lombok.Data;

@Data
public class AppVersion {
    private String APPVERSION_ID;
    private String DOWNLOAD_URL;//apk地址
    private String CONTENT;//更新内容
    private String STATE;//状态
    private String VERSION;//版本号
    private String CREATE_TIME;
    private String FLAG;
    private String UPDATE_TIME;
    
    
}
