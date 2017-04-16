// IAuthListener.aidl
package cn.nubia.accounts.auth;
import cn.nubia.accounts.auth.NubiaAccountInfo;
// Declare any non-default types here with import statements

interface IAuthListener {
   void onComplete(in NubiaAccountInfo accountInfo);
   void onException(int errorCode, String errorMsg);
}
