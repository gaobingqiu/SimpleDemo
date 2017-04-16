package cn.nubia.simpledemo.Base;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * 基础的活动
 * Created by gbq on 2017-1-19.
 */

public class BaseActivity extends AppCompatActivity {

	public void showToast(String message){
		Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
	}
}
