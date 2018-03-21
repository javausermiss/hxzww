/* 2018 03 06*/
ALTER TABLE `SYS_APP_USER` ADD COLUMN `PRO_USER_ID`  varchar(255) NULL COMMENT '渠道推广的用户ID';

ALTER TABLE `SYS_APP_ORDER` ADD COLUMN `PAY_TYPE`  varchar(50) NULL COMMENT '支付类型，R：充值，P，购买渠道推广';
ALTER TABLE `SYS_APP_ORDER` ADD COLUMN `PRO_USER_ID`  varchar(255) NULL COMMENT '渠道推广的用户ID';
ALTER TABLE `SYS_APP_ORDER` ADD COLUMN `ADD_INFO`  varchar(255) NULL COMMENT '订单追加信息';
ALTER TABLE `SYS_APP_ORDER` ADD COLUMN `OUT_ORDER_ID`  varchar(255) NULL COMMENT '外部订单';

ALTER TABLE `tb_toytype` ADD COLUMN `IMG_URL`  varchar(255) NULL COMMENT '微缩图地址';
ALTER TABLE `tb_toytype` ADD COLUMN `TOY_FLAG`  varchar(255) NULL COMMENT '分类标记  1：原生APP，2：H5跳转';
ALTER TABLE `tb_toytype` ADD COLUMN `HREF_URL`  varchar(255) NULL COMMENT '跳转地址';
ALTER TABLE `tb_toytype` AUTO_INCREMENT=200000 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `TB_TRANS_LOG`;
CREATE TABLE `TB_TRANS_LOG` (
		`TRANS_ID` BIGINT(20)  unsigned NOT NULL AUTO_INCREMENT COMMENT '交易流水号',
		`ORG_TRANS_ID` BIGINT(20) DEFAULT NULL COMMENT '原交易流水号',
		`DMS_RELATED_KEY` varchar(32) DEFAULT NULL COMMENT '外部系统流水号',
		`ORG_DMS_RELATED_KEY` varchar(32) DEFAULT NULL COMMENT '原外部系统交易流水号',
		`TRANS_TYPE` varchar(4) DEFAULT NULL COMMENT '交易类型',
		`TRANS_ST` varchar(1) DEFAULT NULL COMMENT '交易状态',
		`TRANS_CODE` varchar(32) DEFAULT NULL COMMENT '终端设备号',
		`RESP_CODE` varchar(6) DEFAULT NULL COMMENT '响应码',
		`PRI_ACC_ID` varchar(255) DEFAULT NULL COMMENT '当前操作主账号',
		`DMS_USER_ID` varchar(255) DEFAULT NULL COMMENT '外部系统用户主键',
		`DMS_USER_UNION_ID` varchar(255) DEFAULT NULL COMMENT '外部系统用户联合主键',
		`TRANS_AMT` varchar(64) DEFAULT NULL COMMENT '实际交易金额',
		`ORG_TRANS_AMT` varchar(64) DEFAULT NULL COMMENT '原交易金额',
		`TRANS_CURR_CD` varchar(4) DEFAULT NULL COMMENT '交易货币代码',
		`TRANS_CHNL` varchar(16) DEFAULT NULL COMMENT '交易渠道',
		`TRANS_FEE` varchar(16) DEFAULT NULL COMMENT '手续费',
		`TRANS_FEE_TYPE` varchar(8) DEFAULT NULL COMMENT '手续费类型',
		`TFR_IN_ACC_ID` varchar(32) DEFAULT NULL COMMENT '转入账户',
		`TFR_OUT_ACC_ID` varchar(32) DEFAULT NULL COMMENT '转出账户',
		`ADD_INFO` varchar(255) DEFAULT NULL COMMENT '附加信息',
		`REMARKS` varchar(255) DEFAULT NULL COMMENT '备注',
		`RES_COLUMN1` varchar(128) DEFAULT NULL COMMENT '备用字段1',
		`RES_COLUMN2` varchar(128) DEFAULT NULL COMMENT '备用字段2',
		`RES_COLUMN3` varchar(128) DEFAULT NULL COMMENT '备用字段3',
		`CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建人',
		`CREATE_DATE` DATETIME DEFAULT NULL COMMENT '创建时间',
		`UPDATE_USER` varchar(32) DEFAULT NULL COMMENT '最后修改人',
		`UPDATE_DATE` DATETIME DEFAULT NULL COMMENT '最后修改时间',
		`LOCK_VERSION` int(11) NOT NULL COMMENT '乐观锁版本号',
		`MCHNT_ID` varchar(32) DEFAULT NULL COMMENT '所属商户',
  		PRIMARY KEY (`TRANS_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=888888000000 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `TB_TRANS_ORDER`;
CREATE TABLE `TB_TRANS_ORDER` (
		`ORDER_ID` BIGINT(20)  unsigned NOT NULL AUTO_INCREMENT COMMENT '交易号',
		`ORG_ORDER_ID` BIGINT(20) DEFAULT NULL COMMENT '原交易号',
		`DMS_RELATED_KEY` varchar(32) DEFAULT NULL COMMENT '外部系统流水号',
		`ORG_DMS_RELATED_KEY` varchar(32) DEFAULT NULL COMMENT '原外部系统交易流水号',
		`USER_ID` varchar(32) DEFAULT NULL COMMENT '用户ID',
		`TRANS_TYPE` varchar(4) DEFAULT NULL COMMENT '交易类型',
		`ORDER_ST` varchar(1) DEFAULT NULL COMMENT '订单状态 (0:已提交，1：处理中，2：已取消，3：处理失败，9：已完成)',
		`TRANS_CODE` varchar(32) DEFAULT NULL COMMENT '终端设备号',
		`PRI_ACC_ID` varchar(255) DEFAULT NULL COMMENT '当前操作主账号',
		`DMS_USER_ID` varchar(255) DEFAULT NULL COMMENT '外部系统用户主键',
		`DMS_USER_UNION_ID` varchar(255) DEFAULT NULL COMMENT '外部系统用户联合主键',
		`TRANS_AMT` varchar(64) DEFAULT NULL COMMENT '实际交易金额',
		`ORG_TRANS_AMT` varchar(64) DEFAULT NULL COMMENT '原交易金额',
		`TRANS_CURR_CD` varchar(4) DEFAULT NULL COMMENT '交易货币代码',
		`TRANS_CHNL` varchar(16) DEFAULT NULL COMMENT '交易渠道',
		`TRANS_FEE` varchar(16) DEFAULT NULL COMMENT '手续费',
		`TRANS_FEE_TYPE` varchar(8) DEFAULT NULL COMMENT '手续费类型',
		`TFR_IN_ACC_ID` varchar(32) DEFAULT NULL COMMENT '转入账户',
		`TFR_OUT_ACC_ID` varchar(32) DEFAULT NULL COMMENT '转出账户',
		`ADD_INFO` varchar(255) DEFAULT NULL COMMENT '附加信息',
		`REMARKS` varchar(255) DEFAULT NULL COMMENT '备注',
		`RES_COLUMN1` varchar(128) DEFAULT NULL COMMENT '备用字段1',
		`RES_COLUMN2` varchar(128) DEFAULT NULL COMMENT '备用字段2',
		`RES_COLUMN3` varchar(128) DEFAULT NULL COMMENT '备用字段3',
		`CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建人',
		`CREATE_DATE` DATETIME DEFAULT NULL COMMENT '创建时间',
		`UPDATE_USER` varchar(32) DEFAULT NULL COMMENT '最后修改人',
		`UPDATE_DATE` DATETIME DEFAULT NULL COMMENT '最后修改时间',
		`LOCK_VERSION` int(11) NOT NULL COMMENT '乐观锁版本号',
		`MCHNT_ID` varchar(32) DEFAULT NULL COMMENT '所属商户',
  		PRIMARY KEY (`ORDER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=111111000000 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `TB_ACCOUNT_LOG`;
CREATE TABLE `TB_ACCOUNT_LOG` (
		`LOG_ID` BIGINT(20)  unsigned NOT NULL AUTO_INCREMENT COMMENT '日志主键',
		`ACC_ID` BIGINT(20) DEFAULT NULL COMMENT '账户主键',
		`LAST_TXN_DATE` varchar(8) DEFAULT NULL COMMENT '账户交易日期',
		`LAST_TXN_TIME` varchar(6) DEFAULT NULL COMMENT '账户交易时间',
		`TRANS_TYPE` varchar(16) DEFAULT NULL COMMENT '交易流水号',
		`TRANS_CHNL` varchar(16) DEFAULT NULL COMMENT '交易金额',
		`ORG_TRANS_AMT` varchar(16) DEFAULT NULL COMMENT '原交易金额',
		`TRANS_AMT` varchar(16) DEFAULT NULL COMMENT '交易金额',
		`ACC_AMT` varchar(16) DEFAULT NULL COMMENT '账户处理金额',
		`ACC_TOTAL_AMT` varchar(16) DEFAULT NULL COMMENT '账户处理后总余额',
		`LOG_TYPE` varchar(2) DEFAULT NULL COMMENT '日志类型：1：加款，2：减款',
		`RES_COLUMN1` varchar(128) DEFAULT NULL COMMENT '备用字段1',
		`RES_COLUMN2` varchar(128) DEFAULT NULL COMMENT '备用字段2',
		`RES_COLUMN3` varchar(128) DEFAULT NULL COMMENT '备用字段3',
		`CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建人',
		`CREATE_DATE` DATETIME DEFAULT NULL COMMENT '创建时间',
		`UPDATE_USER` varchar(32) DEFAULT NULL COMMENT '最后修改人',
		`UPDATE_DATE` DATETIME  NULL COMMENT '最后修改时间',
		`LOCK_VERSION` int(11) NOT NULL COMMENT '乐观锁版本号',
  		PRIMARY KEY (`LOG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=888888000000 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `TB_ACCOUNT_INF`;
CREATE TABLE `TB_ACCOUNT_INF` (
		`ACC_ID` BIGINT(20)  unsigned NOT NULL AUTO_INCREMENT COMMENT '账户主键',
		`MCHNT_ID` INT(4) DEFAULT NULL COMMENT '商户主键',
		`USER_ID` varchar(32) DEFAULT NULL COMMENT '用户主键',
		`ACC_TYPE` varchar(4) DEFAULT NULL COMMENT '账户类型',
		`ACC_STATE` varchar(2) DEFAULT NULL COMMENT '账户状态',
		`ACC_BAL` varchar(16) DEFAULT NULL COMMENT '可用余额明文',
		`ACC_BAL_CODE` varchar(64) DEFAULT NULL COMMENT '可用余额密文',
		`FREEZE_AMT` varchar(16) DEFAULT NULL COMMENT '冻结金额',
		`LAST_TXN_DATE` varchar(8) DEFAULT NULL COMMENT '账户交易日期',
		`LAST_TXN_TIME` varchar(6) DEFAULT NULL COMMENT '账户交易时间',
		`RES_COLUMN1` varchar(128) DEFAULT NULL COMMENT '备用字段1',
		`RES_COLUMN2` varchar(128) DEFAULT NULL COMMENT '备用字段2',
		`RES_COLUMN3` varchar(128) DEFAULT NULL COMMENT '备用字段3',
		`CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建人',
		`CREATE_DATE` DATETIME DEFAULT NULL COMMENT '创建时间',
		`UPDATE_USER` varchar(32) DEFAULT NULL COMMENT '最后修改人',
		`UPDATE_DATE` DATETIME DEFAULT NULL COMMENT '最后修改时间',
		`LOCK_VERSION` int(11) NOT NULL COMMENT '乐观锁版本号',
  		PRIMARY KEY (`ACC_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `SYS_WWJ_PROMOTE_APPUSER`;
CREATE TABLE `SYS_WWJ_PROMOTE_MANAGE` (
  `PRO_MANAGE_ID` bigint(12) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `PRO_MANAGE_NAME` varchar(50) DEFAULT NULL,
  `PAY_AMOUNT` varchar(12) DEFAULT NULL COMMENT '支付金额',
  `PAY_GOLD` varchar(12) DEFAULT NULL COMMENT '支付金币数',
  `CONVER_GOLD` varchar(12) DEFAULT NULL COMMENT '下级用户兑换金币数量',
  `IMG_URL` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `PRO_TYPE` varchar(1) DEFAULT NULL COMMENT '1:金币支付,2:金钱支付',
  `RETURN_RATIO` varchar(12) DEFAULT NULL COMMENT '返回比例',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`PRO_MANAGE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `SYS_WWJ_PROMOTE_APPUSER`;
CREATE TABLE `SYS_WWJ_PROMOTE_APPUSER` (
  `PRO_ID` bigint(12) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `PRO_MANAGE_ID` bigint(4) DEFAULT NULL COMMENT '加盟权益分成ID',
  `USER_ID` varchar(32) DEFAULT NULL COMMENT '用户主键',
  `RETURN_RATIO` varchar(12) DEFAULT NULL COMMENT '用户分成比例',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`PRO_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=utf8;



/*2018 03 12*/
DROP TABLE IF EXISTS `tb_bankcard_inf`;
CREATE TABLE `tb_bankcard_inf` (
`BANKCARD_ID`  varchar(50) NOT NULL ,
`USER_ID`  varchar(50) NULL COMMENT '用户ID' ,
`BANK_ADDRESS`  varchar(255) NULL COMMENT '开户地点' ,
`BANK_NAME`  varchar(255) NULL COMMENT '银行名称' ,
`BANK_BRANCH`  varchar(255) NULL COMMENT '开户支行' ,
`BANK_CARD_NO`  varchar(255) NULL COMMENT '银行卡号' ,
`ID_NUMBER`  varchar(255) NULL COMMENT '身份证号' ,
`USER_REA_NAME`  varchar(255) NULL COMMENT '真实姓名' ,
PRIMARY KEY (`BANKCARD_ID`)
);

ALTER TABLE `tb_bankcard_inf`
ADD COLUMN `IS_DEFAULT`  varchar(5) NULL AFTER `USER_REA_NAME`,
ADD COLUMN `UPDATE_TIME`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP AFTER `IS_DEFAULT`;
;

ALTER TABLE `tb_bankcard_inf`
ADD COLUMN `BANK_PHONE`  varchar(50) NULL AFTER `UPDATE_TIME`;