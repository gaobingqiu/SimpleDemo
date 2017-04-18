package cn.nubia.simpledemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * 全局的监听账户情况的广播
 * Created by gbq on 2017-1-19.
 */

public class AccountChangeBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		String accountStatus = intent.getStringExtra(Constants.ACCOUNT_CHANGE);
		Log.d("AccountChangeBroadcast", action + ",accountStatus:" + accountStatus);
		if (TextUtils.equals(accountStatus, Constants.ACCOUNT_LOGIN)) {
			Constants.setIsLogin(true);
			setAccountStatus(context, true);
		} else if (TextUtils.equals(accountStatus, Constants.ACCOUNT_LOGOUT)) {
			Constants.setIsLogin(false);
			setAccountStatus(context, false);
		} else {
			Constants.setIsLogin(false);
		}
	}

	private void setAccountStatus(Context context, boolean isLogin) {
		SharedPreferences sharedPre = context.getSharedPreferences("config", MODE_PRIVATE);
		// 获取Editor对象
		SharedPreferences.Editor editor = sharedPre.edit();
		// 设置参数
		editor.putBoolean(Constants.ACCOUNT_STATUS, isLogin);
		// 提交
		editor.apply();
	}
}
