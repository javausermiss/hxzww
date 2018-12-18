package com.fh.entity.system;

import lombok.Data;
import org.hibernate.jpa.criteria.expression.ParameterExpressionImpl;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.security.PrivateKey;
import java.util.PrimitiveIterator;

/**
 * 红包实体类
 */
@Data
public class RedPackage {
    private String REDPACKAGE_ID;
    private String USERID;
    private Integer REDGOLD;
    private Integer REDNUM;
    private String VERSION;
    private String TAG;//男女
    private String CREATETIME;
    private String UPDATETIME;
}
