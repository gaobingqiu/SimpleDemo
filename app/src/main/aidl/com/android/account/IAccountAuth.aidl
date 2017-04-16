package com.android.account;

import com.android.account.IAccountInfoListener;
import com.android.account.IAccountSignInListener;

interface IAccountAuth {
    //获取nubiaToken，给电话帮用
	 String getNubiaToken();
	 //获取百度token
	 String getBaiduAccountToken();
	 //获取百度Uid 用于断点续传
	 String getBaiduAccountId();
	 //账户未正常的得到百度信息时，如token 失效等，各个应用重新调起该函数,默认isToExplicit为false，设置模块为true
	 void startBindBaiduAccount(boolean isToExplicit,IAccountInfoListener listener);
	 //nubia token 失效时，刷新nubiaToken
	 void refreshNubiaToken();
	 //视界用接口
	 void signIn(String userName,String password,IAccountSignInListener signInListener);
	 //获取tokenKey
	 String getNubiaTokenKey();
}
