package cn.nubia.simpledemo.auth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import cn.nubia.accounts.auth.IAuthListener;
import cn.nubia.accounts.auth.INubiaAccountAuth;
import cn.nubia.accounts.auth.NubiaAccountInfo;
import cn.nubia.simpledemo.Base.BaseActivity;
import cn.nubia.simpledemo.R;

/**
 * 测试账户服务绑定
 * Created by gbq on 2017-3-15.
 */

public class OathActivity extends BaseActivity implements View.OnClickListener {
	private final static String TAG = "OathActivity";
	private INubiaAccountAuth mINubiaAccountAuthService;
	private String mResponse;
	private ImageView mImageView;
	private NubiaAccountInfo mNubiaAccountInfo;

	//标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
	private boolean mBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oath);
		findIds();
		startService();
	}

	private void findIds() {
		findViewById(R.id.bt_to_get_info).setOnClickListener(this);
		findViewById(R.id.bt_get_cloud_space).setOnClickListener(this);
		mImageView = (ImageView) findViewById(R.id.iv_header);
//		findViewById(R.id.bt_bind_service).setOnClickListener(this);
//		findViewById(R.id.bt_bind_service).setVisibility(View.GONE);
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
			case R.id.bt_to_get_info:
				if (mINubiaAccountAuthService == null) {
					showResponse("服务未绑定");
				} else {
					try {
						mINubiaAccountAuthService.getAccountInfo(mAuthListener);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				break;
			case R.id.bt_get_cloud_space:
				if (mINubiaAccountAuthService == null) {
					showResponse("服务未绑定");
				} else {
					try {
						mINubiaAccountAuthService.getCloudSpace(mAuthListener);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
		}
	}

	private IAuthListener mAuthListener = new IAuthListener.Stub() {
		@Override
		public void onComplete(NubiaAccountInfo accountInfo) throws RemoteException {
			mResponse = accountInfo.toString();
			String cloudSpace = accountInfo.getString(NubiaAccountInfo.KEY_MY_CLOUD_SPACE);
			if (!TextUtils.isEmpty(cloudSpace)) {
				mResponse = mResponse + "&&cloudSpace:" + cloudSpace;
			}
			mNubiaAccountInfo = accountInfo;
			OathActivity.this.runOnUiThread(runnableUi);
		}

		@Override
		public void onException(int errorCode, String errorMsg) throws RemoteException {
			mResponse = errorCode + errorMsg;
			OathActivity.this.runOnUiThread(runnableUi);
		}
	};

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "onServiceConnected");
			mBound = true;
			mINubiaAccountAuthService = INubiaAccountAuth.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "onServiceDisconnected");
			mBound = false;
			mINubiaAccountAuthService = null;
		}
	};

	/**
	 * 开始服务
	 */
	private void startService() {
		Intent intent = new Intent();
		intent.setAction("cn.nubia.accounts.ACCOUNT_AUTH");
		intent.setPackage("cn.nubia.accounts");
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	/**
	 * 停止服务
	 */
	private void endService() {
		if (mINubiaAccountAuthService != null && mServiceConnection != null) {
			unbindService(mServiceConnection);
			mINubiaAccountAuthService = null;
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
			if (mNubiaAccountInfo == null) {
				return;
			}
			Bitmap bitmap = mNubiaAccountInfo.getmHeadImage();
			if (bitmap != null) {
				mImageView.setImageBitmap(bitmap);
				handler.postDelayed(runnable, 3000); //每隔1s执行
			}
		}
	};

	public void showResponse(final String response) {
		final DateFormat df = DateFormat.getTimeInstance(); //设置日期格式
		final String time = df.format(new Date());
		final TextView responseView = (TextView) findViewById(R.id.tv_response);
		responseView.setText(getString(R.string.response_default) + time + "\n" + response);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBound) {
			endService();
		}
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// handler自带方法实现定时器
			try {
				mImageView.setImageBitmap(null);
				mNubiaAccountInfo = null;
				System.out.println("do...");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("exception...");
			}
		}
	};
}
