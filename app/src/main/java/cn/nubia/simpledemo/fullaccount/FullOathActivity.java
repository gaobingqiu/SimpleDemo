package cn.nubia.simpledemo.fullaccount;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.account.IAccountAuth;
import com.android.account.IAccountInfoListener;
import com.android.account.IAccountSignInListener;

import java.text.DateFormat;
import java.util.Date;

import cn.nubia.simpledemo.Base.BaseActivity;
import cn.nubia.simpledemo.R;

/**
 * 验证百度云相关服务
 * Created by gbq on 2017-3-15.
 */

public class FullOathActivity extends BaseActivity implements View.OnClickListener {
	private final static String TAG = "FullOathActivity";
	private IAccountAuth mAccountAuth;
	private String mResponse;
	private EditText mUserNameEt;
	private EditText mPasswordEt;
	private LinearLayout mSignLL;

	//标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
	private boolean mBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_oath);
		findIds();
		startService();
	}

	private void findIds() {
//		findViewById(R.id.bt_bind_service).setOnClickListener(this);
//		findViewById(R.id.bt_bind_service).setVisibility(View.INVISIBLE);
		findViewById(R.id.bt_get_nubia_token).setOnClickListener(this);
		findViewById(R.id.bt_get_baidu_token).setOnClickListener(this);
		findViewById(R.id.bt_get_baidu_uid).setOnClickListener(this);
		findViewById(R.id.bt_bind_baidu).setOnClickListener(this);
//		findViewById(R.id.bt_refresh_baidu_token).setOnClickListener(this);
		findViewById(R.id.bt_login).setOnClickListener(this);
		findViewById(R.id.bt_get_nubia_token_key).setOnClickListener(this);
		findViewById(R.id.bt_confirm).setOnClickListener(this);

		mUserNameEt = (EditText) findViewById(R.id.et_username);
		mPasswordEt = (EditText) findViewById(R.id.et_password);

		mSignLL = (LinearLayout) findViewById(R.id.ll_sign);
		mSignLL.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
//			case R.id.bt_bind_service:
//				if (!mBound) {
//					Toast.makeText(this, "连接中...", Toast.LENGTH_SHORT).show();
//					startService();
//				}
//				break;
			case R.id.bt_get_nubia_token:
				if (mAccountAuth == null) {
					showResponse("服务未绑定");
				} else {
					try {
						mResponse = mAccountAuth.getNubiaToken();
						showResponse(mResponse);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				break;
			case R.id.bt_get_baidu_token:
				if (mAccountAuth == null) {
					showResponse("服务未绑定");
				} else {
					try {
						mResponse = mAccountAuth.getBaiduAccountToken();
						showResponse(mResponse);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				break;
			case R.id.bt_get_baidu_uid:
				if (mAccountAuth == null) {
					showResponse("服务未绑定");
				} else {
					try {
						mResponse = mAccountAuth.getBaiduAccountId();
						showResponse(mResponse);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				break;
			case R.id.bt_bind_baidu:
				if (mAccountAuth == null) {
					showResponse("服务未绑定");
				} else {
					try {
						mAccountAuth.startBindBaiduAccount(false, mAccountInfoListener);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				break;
//			case R.id.bt_refresh_baidu_token:
//				if (mAccountAuth == null) {
//					showResponse("服务未绑定");
//				} else {
//					try {
//						mAccountAuth.refreshNubiaToken();
//						showResponse("refresh...");
//					} catch (RemoteException e) {
//						e.printStackTrace();
//					}
//				}
//				break;
			case R.id.bt_login:
				if (mAccountAuth == null) {
					showResponse("服务未绑定");
				} else {
					mSignLL.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.bt_get_nubia_token_key:
				if (mAccountAuth == null) {
					showResponse("服务未绑定");
				} else {
					try {
						mResponse = mAccountAuth.getNubiaTokenKey();
						showResponse(mResponse);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				break;

			case R.id.bt_confirm:
				if (mAccountAuth == null) {
					showResponse("服务未绑定");
				} else {
					try {
						mAccountAuth.signIn(mUserNameEt.getText().toString(), mPasswordEt.getText().toString(),
								mAccountSignInListener);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				mSignLL.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "onServiceConnected");
			mBound = true;
			mAccountAuth = IAccountAuth.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "onServiceDisconnected");
			mBound = false;
			mAccountAuth = null;
		}
	};

	/**
	 * 开始服务
	 */
	private void startService() {
		Intent intent = new Intent();
		intent.setAction("com.android.account.bind_baidu");
		intent.setPackage("cn.nubia.accounts");
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	/**
	 * 停止服务
	 */
	private void endService() {
		if (mAccountAuth != null && mServiceConnection != null) {
			unbindService(mServiceConnection);
			mAccountAuth = null;
			mBound = false;
			Toast.makeText(this, "服务解除绑定", Toast.LENGTH_SHORT).show();
		}
	}

	// 构建Runnable对象，在runnable中更新界面
	Runnable runnableUi = new Runnable() {
		@Override
		public void run() {
			//更新界面
			showResponse(mResponse);
		}

	};

	public void showResponse(final String response) {
		final DateFormat df = DateFormat.getTimeInstance(); //设置日期格式
		final String time = df.format(new Date());
		final TextView responseView = (TextView) findViewById(R.id.tv_response);
		responseView.setText(getString(R.string.response_default) + time + "\n" + response);
	}

	private IAccountInfoListener mAccountInfoListener = new IAccountInfoListener.Stub() {
		@Override
		public void onComplete(String baiduToken, String uid, String expires) throws RemoteException {
			mResponse = baiduToken + "&&" + uid + expires;
			FullOathActivity.this.runOnUiThread(runnableUi);
		}

		@Override
		public void onException(String errorMsg) throws RemoteException {
			mResponse = errorMsg;
			FullOathActivity.this.runOnUiThread(runnableUi);
		}

		@Override
		public void onCancel() throws RemoteException {
			mResponse = "BindBaiduAccount onCancel";
			FullOathActivity.this.runOnUiThread(runnableUi);
		}
	};

	private IAccountSignInListener mAccountSignInListener = new IAccountSignInListener.Stub() {
		@Override
		public void onComplete(String nubiaToken, String tokenKey) throws RemoteException {
			Log.d(TAG,"onComplete");
			mResponse = nubiaToken + "&&" + tokenKey;
			Log.d(TAG,mResponse);
			FullOathActivity.this.runOnUiThread(runnableUi);
		}

		@Override
		public void onException(String errorMsg) throws RemoteException {
			Log.d(TAG,"onException");
			mResponse = errorMsg;
			Log.d(TAG,mResponse);
			FullOathActivity.this.runOnUiThread(runnableUi);
		}

		@Override
		public void onCancel() throws RemoteException {
			Log.d(TAG,"onCancel");
			mResponse = "sign onCancel";
			Log.d(TAG,mResponse);
			FullOathActivity.this.runOnUiThread(runnableUi);
		}
	};

	@Override
	protected void onDestroy(){
		super.onDestroy();
		if (mBound) {
			endService();
		}
	}
}
