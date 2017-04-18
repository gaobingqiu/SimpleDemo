package com.android.account;


 interface IAccountInfoListener {
 /**
 baiduToken，百度token,uid，用于断点续传，expires，token有效时间戳
 */
     void onComplete(String baiduToken,String uid,String expires);

    void onException(String errorMsg);

    void onCancel();
    
}