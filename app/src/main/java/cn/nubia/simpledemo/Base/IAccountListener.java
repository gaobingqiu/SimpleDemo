package cn.nubia.simpledemo.Base;

import android.graphics.Bitmap;

/**
 * 返回信息
 * Created by gbq on 2017-1-24.
 */

public interface IAccountListener {
	void getAccountResponse(String response);
	void showHeader(Bitmap headImg);

	void startWebSynLogin(String response);
}
