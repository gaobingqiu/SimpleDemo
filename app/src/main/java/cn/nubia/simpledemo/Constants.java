package cn.nubia.simpledemo;

/**
 * 常量
 * Created by gbq on 2017-1-19.
 */

public class Constants {
	public final static boolean DEBUG = true;
	static final String ACCOUNT_CHANGE = "change";
	static final String ACCOUNT_LOGIN = "login";
	static final String ACCOUNT_LOGOUT = "logout";
	static final String ACCOUNT_STATUS = "isLogin";
	static final long LOGIN_REQUEST_CODE = 10000;
	static final String WARMING = "警告";
	static final String ACCOUNT_IS_LOGIN = "当前账户已经登录，确认重新登录吗？";
	static final String PLEASE_INPUT = "请输入";

	private static boolean isLogin;

	public static boolean isLogin() {
		return isLogin;
	}

	static void setIsLogin(boolean isLogin) {
		Constants.isLogin = isLogin;
	}

	//正式
//	public final static String[] WEB_URL = {
//			"http://bbs.nubia.cn/",
//			"http://m.nubia.com/",
//			"https://account.ztehn.com/",
//			"http://open.nubia.com/",
//			"http://m.ztehn.com/",
//			"http://bbs.ui.nubia.cn/",
//	};

	//集测
	public final static String[] WEB_URL = {
			"http://pre.m.nubia.com/",
			"http://bbs3.server.ztemt.com.cn/",
	};
}
