package com.android.account;


 interface IAccountSignInListener {
 
    void onComplete(String nubiaToken,String tokenKey);

    void onException(String errorMsg);

    void onCancel();
    
}