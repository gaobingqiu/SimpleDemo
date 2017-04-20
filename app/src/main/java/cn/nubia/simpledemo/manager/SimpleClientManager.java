package cn.nubia.simpledemo.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import cn.nubia.accountsdk.aidl.IAppWebSynLoginListener;
import cn.nubia.accountsdk.aidl.ICheckPasswordListener;
import cn.nubia.accountsdk.aidl.IGetAccountInfoListener;
import cn.nubia.accountsdk.aidl.IGetBaiduAccountInfoListener;
import cn.nubia.accountsdk.aidl.IGetThirdBindInfoListener;
import cn.nubia.accountsdk.aidl.SystemAccountInfo;
import cn.nubia.accountsdk.aidl.ThirdAccountBindInfo;
import cn.nubia.accountsdk.common.CetificationLackingException;
import cn.nubia.accountsdk.simpleclient.AccountSimpleClient;
import cn.nubia.base.LogUtils;
import cn.nubia.simpledemo.Base.IAccountListener;

/**
 * 简版sdk的管理者
 * Created by gbq on 2017-1-22.
 */

public class SimpleClientManager {
	private AccountSimpleClient mSimpleClient;
	private IAccountListener mAccountListener;
	private Context mContext;

	public SimpleClientManager(Context context) {
		mContext = context;
		mSimpleClient = AccountSimpleClient.get(mContext);
	}

	public boolean isSupportNewApi() {
		return mSimpleClient.isSurportNewApi();
	}

	public boolean isNubiaRom() {
		return mSimpleClient.isNubiaRom();
	}

	public void getAccountInfo() {
		try {
			mSimpleClient.getSystemAccountInfo(new IGetAccountInfoListener.Stub() {
				@Override
				public void onComplete(SystemAccountInfo systemAccountInfo) throws RemoteException {
					Log.d("systemAccountInfo", String.valueOf(systemAccountInfo));
					mAccountListener.getAccountResponse(String.valueOf(systemAccountInfo));
					mAccountListener.showHeader(systemAccountInfo.getHeadImage());
				}

				@Override
				public void onException(int i, String s) throws RemoteException {
					mAccountListener.getAccountResponse(i + ";" + s);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getThirdBindInfo() {
//		if(!Constants.isLogin()){
//			return;
//		}
		try {
			mSimpleClient.getThirdBindInfo(new IGetThirdBindInfoListener.Stub() {
				@Override
				public void onComplete(ThirdAccountBindInfo thirdAccountBindInfo) throws RemoteException {
					mAccountListener.getAccountResponse(String.valueOf(thirdAccountBindInfo));
				}

				@Override
				public void onException(int i, String s) throws RemoteException {
					mAccountListener.getAccountResponse(s);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getCloudSpace() {
//		if(!Constants.isLogin()){
//			return;
//		}
		try {
			mSimpleClient.getCloudSpace(new IGetAccountInfoListener.Stub() {
				@Override
				public void onComplete(SystemAccountInfo systemAccountInfo) throws RemoteException {
					mAccountListener.getAccountResponse( String.valueOf(systemAccountInfo) + "&&space:"
							+ systemAccountInfo.getString(SystemAccountInfo.KEY_MY_CLOUD_SPACE)
					);
				}

				@Override
				public void onException(int i, String s) throws RemoteException {
					mAccountListener.getAccountResponse(s);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getBaiduInfo() {
//		if(!Constants.isLogin()){
//			return;
//		}
		try {
			mSimpleClient.getBaiduAccountInfo(new IGetBaiduAccountInfoListener.Stub() {
				@Override
				public void onComplete(String s, String s1, String s2) throws RemoteException {
					mAccountListener.getAccountResponse(s + ";" + s1 + ";" + s2);
				}

				@Override
				public void onException(String s) throws RemoteException {
					mAccountListener.getAccountResponse(s);
				}

				@Override
				public void onCancel() throws RemoteException {
					mAccountListener.getAccountResponse("onCancel");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reBindBaidu(boolean isToExplicit) {
		try {
			mSimpleClient.startBindBaiduAccount(isToExplicit, new IGetBaiduAccountInfoListener.Stub() {
				@Override
				public void onComplete(String s, String s1, String s2) throws RemoteException {
					mAccountListener.getAccountResponse(s + ";" + s1 + ";" + s2);
				}

				@Override
				public void onException(String s) throws RemoteException {
					mAccountListener.getAccountResponse(s);
				}

				@Override
				public void onCancel() throws RemoteException {
					mAccountListener.getAccountResponse("onCancel");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkPassword(String password) {
//		if(!Constants.isLogin()){
//			return;
//		}
		try {
			mSimpleClient.checkPassword(password, new ICheckPasswordListener.Stub() {
				@Override
				public void onComplete(boolean b) throws RemoteException {
					mAccountListener.getAccountResponse(String.valueOf(b));
				}

				@Override
				public void onException(String s) throws RemoteException {
					mAccountListener.getAccountResponse(s);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void webSynLogin(final String url) {
//		if(!Constants.isLogin()){
//			return;
//		}
		LogUtils.d(url);
		try {
			mSimpleClient.appWebSynlogin(url, new IAppWebSynLoginListener.Stub() {
				@Override
				public void onComplete(String s) throws RemoteException {
					mAccountListener.startWebSynLogin(s);
				}

				@Override
				public void onException(String s) throws RemoteException {
					mAccountListener.getAccountResponse(s);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void toAccountLogin(Activity activity) {
		mSimpleClient.loginOrRegister(activity);
	}

	public void toAccountDetail(Activity activity) {
//		if(!Constants.isLogin()){
//			return;
//		}
		mSimpleClient.jumptoAccountDetailActivity(activity);
	}

	public void toCertification(Activity activity) {
//		if(!Constants.isLogin()){
//			return;
//		}
		try {
			mSimpleClient.jumptoCertificationActivity(activity);
		}
		catch (CetificationLackingException e){
			mAccountListener.getAccountResponse("CetificationLackingException");
		}
	}

	public void reAccountLogin(Activity activity) {
		mSimpleClient.reLoginWhenTokenInvalid(activity);
	}

	public void setAccountListener(IAccountListener mAccountListener) {
		this.mAccountListener = mAccountListener;
	}
}
