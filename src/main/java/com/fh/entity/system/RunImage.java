package com.fh.entity.system;

import lombok.Data;

@Data
public class RunImage {
    private String IMAGE_URL;
    private String RUN_NAME;
    private String RUNIMAGE_ID;
    private String CONTENT;//内容
    private String TIME;
    private String HREF_ST;
    private String LIVESTREAM;//流媒体名称
    private String SERVER_NAME;//推流服务名称
    private String RTMP_URL;//RTMP拉流地址
    private String H5_URL;
    private String DEVICE_STATE;//设备状态
    private String STATE;//状态
    private String DEVICE_CHANNEL_TYPE;//渠道类型
    private String CHANNEL_NAME;//APP包名称
    private String SHOWSTATE;
    
}
