package com.fh.util;

import org.springframework.context.ApplicationContext;
/**
 * 项目名称：
 * @author:fh qq313596790[青苔]
 * 修改日期：2015/11/2
*/
public class Const {
	
	//redis sessionId key:sessionId:appUser:			
	public static final String REDIS_APPUSER_PHONCE_TYPE="REDIS_SMS_PHONE_TYPE_";
	
	//redis sessionId key:sessionId:appUser:			
	public static final String REDIS_APPUSER_SESSIONID="sessionId:appUser:";
	
	//redis sessionId key:sessionId:appUser:					 
	public static final String REDIS_APPUSER_LOGIN_TENCENTTOKEN="tencentToken:";
	
	
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";	//验证码
	public static final String SESSION_USER = "sessionUser";				//session用的用户
	public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";
	public static final String sSESSION_ROLE_RIGHTS = "sessionRoleRights";
	public static final String SESSION_menuList = "menuList";				//当前菜单
	public static final String SESSION_allmenuList = "allmenuList";			//全部菜单
	public static final String SESSION_QX = "QX";
	public static final String SESSION_userpds = "userpds";			
	public static final String SESSION_USERROL = "USERROL";					//用户对象
	public static final String SESSION_USERNAME = "USERNAME";			//用户名
	public static final String SESSION_ROLENAME = "ROLENAME";			//角色名
	public static final String SESSION_CHANNEL_CODE="CHANNEL_CODE";		//所属渠道
	public static final String DEPARTMENT_IDS = "DEPARTMENT_IDS";			//当前用户拥有的最高部门权限集合
	public static final String DEPARTMENT_ID = "DEPARTMENT_ID";				//当前用户拥有的最高部门权限
	public static final String TRUE = "T";
	public static final String FALSE = "F";
	public static final String LOGIN = "/login_toLogin.do";					//登录地址
	public static final String SYSNAME = "admin/config/SYSNAME.txt";		//系统名称路径
	public static final String PAGE	= "admin/config/PAGE.txt";				//分页条数配置路径
	public static final String EMAIL = "admin/config/EMAIL.txt";			//邮箱服务器配置路径
	public static final String SMS1 = "admin/config/SMS1.txt";				//短信账户配置路径1
	public static final String SMS2 = "admin/config/SMS2.txt";				//短信账户配置路径2
	public static final String FWATERM = "admin/config/FWATERM.txt";		//文字水印配置路径
	public static final String IWATERM = "admin/config/IWATERM.txt";		//图片水印配置路径
	public static final String WEIXIN	= "admin/config/WEIXIN.txt";		//微信配置路径
	public static final String WEBSOCKET = "admin/config/WEBSOCKET.txt";	//WEBSOCKET配置路径
	public static final String LOGINEDIT = "admin/config/LOGIN.txt";		//登录页面配置
	public static final String FILEPATHIMG = "uploadFiles/uploadImgs/";		//图片上传路径
	public static final String FILEPATHFILE = "uploadFiles/file/";			//文件上传路径
	public static final String FILEPATHFILEOA = "uploadFiles/uploadFile/";	//文件上传路径(oa管理)
	public static final String FILEPATHTWODIMENSIONCODE = "uploadFiles/twoDimensionCode/"; //二维码存放路径
	public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(logout)|(sms)|(html)|(api)|(gateway)|(code)|(app)|(weixin)|(static)|(paycard)|(main)|(DollImage)|(websocket)|(uploadImgs)|(srs)).*";	//不对匹配该值的访问路径拦截（正则）
	public static ApplicationContext WEB_APP_CONTEXT = null; //该值会在web容器启动时由WebAppContextListener初始化
	
	
	public static final String MCHNT_ADMIN_ROLE_NAME="MCHNT_ADMIN"; //代理商户角色名称
	
	/**
	 * APP Constants
	 */
	//系统用户注册接口_请求协议参数)
	public static final String[] SYSUSER_REGISTERED_PARAM_ARRAY = new String[]{"USERNAME","PASSWORD","NAME","EMAIL","rcode"};
	public static final String[] SYSUSER_REGISTERED_VALUE_ARRAY = new String[]{"用户名","密码","姓名","邮箱","验证码"};
	
	//app根据用户名获取会员信息接口_请求协议中的参数
	public static final String[] APP_GETAPPUSER_PARAM_ARRAY = new String[]{"USERNAME"};
	public static final String[] APP_GETAPPUSER_VALUE_ARRAY = new String[]{"用户名"};

	//app根据用户名密码登录接口_请求协议中的参数
	public static final String[] APP_LOGIN_PARAM_ARRAY = new String[]{"USERNAME","PASSWORD"};
	public static final String[] APP_LOGIN_VALUE_ARRAY = new String[]{"用户名","密码"};
	
	
	/**
	 * 
	 * @author JAVA_DEV
	 *
	 */
	public enum AppPhoneSmsMenu{
		
		
		PHONE_SMS_TYPE_1000("注册短信码","1000",60*30),
		PHONE_SMS_TYPE_2000("绑定手机号","2000",60*30),
		PHONE_SMS_TYPE_3000("修改银行卡信息","3000",60*30),
		PHONE_SMS_TYPE_4000("提现短信码","4000",60*30);
		
		private  String name;
		private  String code;
		private  long expirytime; //失效时间

		AppPhoneSmsMenu(String name, String code,long expirytime) {
			this.name = name;
			this.code = code;
			this.expirytime=expirytime;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}

		public long getExpirytime() {
			return expirytime;
		}

		public void setExpirytime(long expirytime) {
			this.expirytime = expirytime;
		}

		public static AppPhoneSmsMenu findByCode(String code) {
			for (AppPhoneSmsMenu t : AppPhoneSmsMenu.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}

	public enum pointsMallType{

		points_type00("每日首次登录游戏",1),
		points_type01("每日首次分享1次游戏",2),
		points_type02("每邀请1位好友注册",3),
		points_type03("每日首次抓取娃娃",4),
		points_type04("每日参与10次推币机",5),
		points_type05("每日累计消耗200金币",6),
		points_type06("每日首次充值任意金额",7),
		points_type07("每日首次参与竞猜",8),
 		points_type09("兑换礼品",10),
		points_type08("累积消费100金币奖励",9);
		private  String name;
		private  Integer value;

		pointsMallType(String name,Integer value){
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}
	}
	
	/**
	 * 
	 * @author JAVA_DEV
	 *
	 */
	public enum PlayMentCostType{
		
		cost_type00("游戏扣款","0"),
		cost_type01("竞猜扣款","1"),
		cost_type02("机器故障返款","2"),
		cost_type03("竞猜成功返款","3"),
		cost_type04("单向竞猜返款 ","4"),
		cost_type05("充值加款","5"),
		cost_type06("发货运费抵扣","6"),
		cost_type07("兑换娃娃","7"),
		cost_type08("签到奖励","8"),
		cost_type09("充值奖励","9"),
		cost_type10("内调","10"),
		cost_type11("邀请码兑换奖励","11"),
		cost_type12("邀请码分享奖励","12"),
		cost_type13("推广收入","13"),
		cost_type14("注册赠送","14"),
		cost_type15("竞猜追投","15"),
		cost_type17("推币机游戏自动兑换","17"),
		cost_type18("推币机奖励","18"),
		cost_type19("充值周卡","19"),
		cost_type20("输入加盟码","20"),
		cost_type21("充值月卡","21"),
		cost_type22("周卡每日赠送","22"),
		cost_type23("月卡每日赠送","23"),
		cost_type24("用户首充","24"),
		cost_type25("集积分送金币","25"),
		cost_type26("金币兑换金豆","26"),
		cost_type27("金豆兑换金币","27"),
		cost_type28("娃娃自动兑换金币","28"),
		cost_type29("金币总换娃娃","29");

		
		private  String name;
		private  String value;

		PlayMentCostType(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static PlayMentCostType findByCode(String value) {
			for (PlayMentCostType t : PlayMentCostType.values()) {
				if (t.value.equalsIgnoreCase(value)) {
					return t;
				}
			}
			return null;
		}
	}
	
	

	/**
	 * SDK TYPE
	 *
	 */
	public enum SDKMenuType{
		
		YSDK("YSDK"),
		ASDK("ASDK"),
		W8SDK("W8SDK");
		
		private String value;
		
		SDKMenuType(String value){
			this.value=value;
		}
		public String getValue() {
			return value;
		}
	}
	
	
	/**
	 * Channel TYPE
	 *
	 */
	public enum ChannelMenuType{
		
		H5("H5"),
		ANDROID("ANDROID"),
		IOS("IOS");
		
		private String value;
		
		ChannelMenuType(String value){
			this.value=value;
		}
		public String getValue() {
			return value;
		}
	}
	
	
	/**
	 * 
	 * @author JAVA_DEV
	 *
	 */
	public enum RedisDictKeyConst{
		
		USER_AWARD_CODE_NUM("邀请兑换次数","USER_AWARD_CODE_NUM"),
		USER_AWARD_CODE_AMOUNT("兑换奖励金额","USER_AWARD_CODE_AMOUNT"),
		USER_AWARD_CODE_INVITE_AMOUNT("邀请奖励金额","USER_AWARD_CODE_INVITE_AMOUNT"),
		USER_AWARD_CODE_MAX_AMOUNT("兑换奖励总金额","USER_AWARD_CODE_MAX_AMOUNT");
		
		private  String name;
		private  String value;

		RedisDictKeyConst(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static PlayMentCostType findByCode(String value) {
			for (PlayMentCostType t : PlayMentCostType.values()) {
				if (t.value.equalsIgnoreCase(value)) {
					return t;
				}
			}
			return null;
		}
	}
	
	
	
	
	/**
	 * 
	 * @author JAVA_DEV
	 *
	 */
	public enum BaseDictRedisHsetKey{
		
		USER_AWARD_REDIS_HSET("金币兑换","USER_AWARD_REDIS_HSET");
		
		private  String name;
		private  String value;

		BaseDictRedisHsetKey(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static PlayMentCostType findByCode(String value) {
			for (PlayMentCostType t : PlayMentCostType.values()) {
				if (t.value.equalsIgnoreCase(value)) {
					return t;
				}
			}
			return null;
		}
	}
	
	
	/**
	 * 
	 * @author JAVA_DEV
	 *
	 */
	public enum OrderPayType{
		
		R_TYPE("金币充值","R"),
		P_TYPE("用户购买加盟权益","P");
		
		private  String name;
		private  String value;

		OrderPayType(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static OrderPayType findByCode(String value) {
			for (OrderPayType t : OrderPayType.values()) {
				if (t.value.equalsIgnoreCase(value)) {
					return t;
				}
			}
			return null;
		}
	}
	
	
	public enum AccountTransType{
		TRANS_OTHER("其他","0000"),
		TRANS_1000("充值","1000"),
		TRANS_1001("推广分成","1001"),
		TRANS_2000("消费","2000"),
		TRANS_3000("转账","3000"),
		TRANS_4000("内扣","4000"),
		TRANS_5000("提现","5000");
		
		private  String name;
		private  String value;

		AccountTransType(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static AccountTransType findByCode(String value) {
			for (AccountTransType t : AccountTransType.values()) {
				if (t.value.equalsIgnoreCase(value)) {
					return t;
				}
			}
			return TRANS_OTHER;
		}
	}
	
	/**
	 * 获取redis 存储手机验证的kEY
	 * @param userId
	 * @param smsType
	 * @return
	 */
	public static String getReidsSmsKey(String userId,String smsType){
		return Const.REDIS_APPUSER_PHONCE_TYPE+userId+userId+smsType;
	}
}
