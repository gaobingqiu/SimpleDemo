package cn.nubia.simpledemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.util.Date;

import cn.nubia.accountsdk.simpleclient.AccountSimpleClient;
import cn.nubia.base.LogUtils;
import cn.nubia.simpledemo.Base.BaseActivity;
import cn.nubia.simpledemo.Base.IAccountListener;
import cn.nubia.simpledemo.auth.OathActivity;
import cn.nubia.simpledemo.fullaccount.FullOathActivity;
import cn.nubia.simpledemo.manager.SimpleClientManager;
import cn.nubia.simpledemo.manager.WebActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener, IAccountListener {
    private SimpleClientManager mManager;
    private Handler mHandler = null;
    private String mResponse;
    private ImageView mImageView;
    private Bitmap mBitmap;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findIds();
        mManager = new SimpleClientManager(this);
        mManager.setAccountListener(this);
        mHandler = new Handler();
        LogUtils.d(getCurProcessName(this));
    }

    String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    private void findIds() {
        findViewById(R.id.bt_to_login).setOnClickListener(this);
        findViewById(R.id.bt_relogin).setOnClickListener(this);
        findViewById(R.id.bt_to_detail).setOnClickListener(this);
        findViewById(R.id.bt_to_certification).setOnClickListener(this);
        findViewById(R.id.bt_check_password).setOnClickListener(this);
        findViewById(R.id.bt_web_syn_login).setOnClickListener(this);
        findViewById(R.id.bt_get_account_info).setOnClickListener(this);
        findViewById(R.id.bt_get_third_info).setOnClickListener(this);
        findViewById(R.id.bt_get_baidu_info).setOnClickListener(this);
        findViewById(R.id.bt_get_rebaidu_info).setOnClickListener(this);
        findViewById(R.id.bt_get_space_info).setOnClickListener(this);
        findViewById(R.id.bt_test_nubia_service).setOnClickListener(this);
        findViewById(R.id.bt_test_account_service).setOnClickListener(this);
        findViewById(R.id.ll_service).setVisibility(View.GONE);
        findViewById(R.id.tv_service_tips).setVisibility(View.GONE);
        mImageView = (ImageView) findViewById(R.id.iv_header);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_to_login:
//				if (Constants.isLogin()) {
//					showConfirmDialog(1);
//					return;
//				}
                mManager.toAccountLogin(this);
                break;
            case R.id.bt_relogin:
//				if (Constants.isLogin()) {
//					showConfirmDialog(2);
//					return;
//				}
                mManager.reAccountLogin(this);
                break;
            case R.id.bt_to_detail:
                mManager.toAccountDetail(this);
                break;
            case R.id.bt_to_certification:
                mManager.toCertification(this);
                break;
            case R.id.bt_check_password:
                inputPasswordDialog();
                break;

            case R.id.bt_web_syn_login:
                showSingleChoiceDialog();
                break;
            case R.id.bt_get_account_info:
                mManager.getAccountInfo();
                break;
            case R.id.bt_get_third_info:
                mManager.getThirdBindInfo();
                break;
            case R.id.bt_get_baidu_info:
                mManager.getBaiduInfo();
                break;
            case R.id.bt_get_rebaidu_info:
                mManager.reBindBaidu(true);
                break;
            case R.id.bt_get_space_info:
                mManager.getCloudSpace();
                break;
            case R.id.bt_test_nubia_service:
                Intent intent = new Intent();
                intent.setClass(this, OathActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_test_account_service:
                intent = new Intent();
                intent.setClass(this, FullOathActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.LOGIN_REQUEST_CODE) {
            showResponse("reLogin,please reTry.");
        }
        if(requestCode == AccountSimpleClient.REQUEST_TOKEN_CERTIFICATION && resultCode != 0 ){
            showResponse(requestCode + ":" + resultCode);
        }
    }

    public void showResponse(final String response) {
        final DateFormat df = DateFormat.getTimeInstance(); //设置日期格式
        final String time = df.format(new Date());
        final TextView responseView = (TextView) findViewById(R.id.tv_response);
        responseView.setText(getString(R.string.response_default) + time + "\n" + response);
    }

    private void inputPasswordDialog() {
        final EditText inputText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Constants.PLEASE_INPUT).setIcon(android.R.drawable.ic_dialog_info).setView(inputText)
                .setNegativeButton("取消", negativeButtonListener);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mManager.checkPassword(inputText.getText().toString());
            }
        });
        builder.show();

    }

    private int mChoice;
    private void showSingleChoiceDialog() {
        final String[] items = {"http://pre.m.nubia.com/", "http://bbs3.server.ztemt.com.cn/"};
        final AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(MainActivity.this);
        singleChoiceDialog.setTitle("请选择url：");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mManager.webSynLogin(items[mChoice]);
                        mUrl = items[mChoice];
                    }
                });
        singleChoiceDialog.show();
    }

    private void showConfirmDialog(final int type) {
        final TextView tipsTV = new TextView(this);
        tipsTV.setText(Constants.ACCOUNT_IS_LOGIN);

//		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) tipsTV.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
//		linearParams.setMargins(20,10,0,0);
//		tipsTV.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Constants.WARMING).setIcon(android.R.drawable.ic_dialog_info).setView(tipsTV)
                .setNegativeButton("取消", negativeButtonListener);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (type) {
                    case 1:
                        mManager.toAccountLogin(MainActivity.this);
                        break;
                    case 2:
                        mManager.reAccountLogin(MainActivity.this);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();

    }

    private DialogInterface.OnClickListener negativeButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };

    @Override
    public void getAccountResponse(String response) {
        mResponse = response;
        //mHandler.post(runnableUi);
        this.runOnUiThread(runnableUi);
    }

    @Override
    public void showHeader(Bitmap headImg) {
        mBitmap = headImg;
    }

    @Override
    public void startWebSynLogin(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra("url",mUrl);
                intent.putExtra("syn_url",response);
                intent.setClass(MainActivity.this, WebActivity.class);
                startActivity(intent);
            }
        });
    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            //更新界面
            showResponse(mResponse);
            if (mBitmap != null) {
                mImageView.setImageBitmap(mBitmap);
                handler.postDelayed(runnable, 3000); //每隔1s执行
            }
        }

    };

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
        Constants.setIsLogin(sharedPre.getBoolean(Constants.ACCOUNT_STATUS, false));
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                mImageView.setImageBitmap(null);
                mBitmap = null;
                System.out.println("do...");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };
}
