// INubiaAccountAuth.aidl
package cn.nubia.accounts.auth;
import cn.nubia.accounts.auth.IAuthListener;

interface INubiaAccountAuth {
   void getAccountInfo(IAuthListener listener);
   void getCloudSpace(IAuthListener listener);
}
