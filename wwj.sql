DROP TABLE IF EXISTS `TB_TRANS_LOG`;
CREATE TABLE `TB_TRANS_LOG` (
		`TRANS_ID` BIGINT(20)  unsigned NOT NULL AUTO_INCREMENT COMMENT '交易流水号',
		`ORG_TRANS_ID` BIGINT(20) DEFAULT NULL COMMENT '原交易流水号',
		`DMS_RELATED_KEY` varchar(32) DEFAULT NULL COMMENT '外部系统流水号',
		`ORG_DMS_RELATED_KEY` varchar(32) DEFAULT NULL COMMENT '原外部系统交易流水号',
		`ORDER_ST` varchar(4) DEFAULT NULL COMMENT '交易类型',
		`TRANS_ST` varchar(1) DEFAULT NULL COMMENT '交易状态',
		`TRANS_CODE` varchar(32) DEFAULT NULL COMMENT '终端设备号',
		`RESP_CODE` varchar(1) DEFAULT NULL COMMENT '响应码',
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
		`CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建人',
		`CREATE_DATE` varchar(32) DEFAULT NULL COMMENT '创建时间',
		`UPDATE_USER` varchar(32) DEFAULT NULL COMMENT '最后修改人',
		`UPDATE_DATE` varchar(32) DEFAULT NULL COMMENT '最后修改时间',
		`LOCK_VERSION` int(11) NOT NULL COMMENT '乐观锁版本号',
		`MCHNT_ID` varchar(32) DEFAULT NULL COMMENT '所属商户',
  		PRIMARY KEY (`TRANS_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=888888000000 DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `TB_ACCOUNT_LOG`;
CREATE TABLE `TB_ACCOUNT_LOG` (
		`LOG_ID` BIGINT(20)  unsigned NOT NULL AUTO_INCREMENT COMMENT '日志主键',
		`ACC_ID` BIGINT(20) DEFAULT NULL COMMENT '账户主键',
		`LAST_TXN_DATE` varchar(8) DEFAULT NULL COMMENT '账户交易日期',
		`LAST_TXN_TIME` varchar(6) DEFAULT NULL COMMENT '账户交易时间',
		`TRANS_ID` BIGINT(20) DEFAULT NULL COMMENT '交易流水号',
		`TRANS_AMT` varchar(16) DEFAULT NULL COMMENT '交易金额',
		`ACC_AMT` varchar(16) DEFAULT NULL COMMENT '账户处理金额',
		`ACC_TOTAL_AMT` varchar(16) DEFAULT NULL COMMENT '账户处理后总余额',
		`RES_COLUMN1` varchar(128) DEFAULT NULL COMMENT '备用字段1',
		`RES_COLUMN2` varchar(128) DEFAULT NULL COMMENT '备用字段2',
		`RES_COLUMN3` varchar(128) DEFAULT NULL COMMENT '备用字段3',
		`CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建人',
		`CREATE_DATE` varchar(32) DEFAULT NULL COMMENT '创建时间',
		`UPDATE_USER` varchar(32) DEFAULT NULL COMMENT '最后修改人',
		`UPDATE_DATE` varchar(32) DEFAULT NULL COMMENT '最后修改时间',
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
		`CREATE_DATE` varchar(32) DEFAULT NULL COMMENT '创建时间',
		`UPDATE_USER` varchar(32) DEFAULT NULL COMMENT '最后修改人',
		`UPDATE_DATE` varchar(32) DEFAULT NULL COMMENT '最后修改时间',
		`LOCK_VERSION` int(11) NOT NULL COMMENT '乐观锁版本号',
  		PRIMARY KEY (`ACC_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8;