package cn.nubia.accounts.auth;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class NubiaAccountInfo implements Parcelable {

    public static final String KEY_HEAD_IMG = "key_nubia_account_head_image";
    public static final String KEY_MY_CLOUD_SPACE = "key_my_cloud_space";
    private final Bundle mKeyValueBundle = new Bundle();
    private String mTokenId = "";
    private String mUsername = "";
    private String mTokenKey = "";
    private Bitmap mHeadImage;
    public NubiaAccountInfo(String tokenId) {
        this.mTokenId = tokenId;
    }

    public NubiaAccountInfo(String tokenId, String username, String tokenKey) {
        this.mTokenId = tokenId;
        this.mUsername = username;
        this.mTokenKey = tokenKey;
    }

    public String getTokenId() {
        return mTokenId;
    }

    public String getTokenKey() {
        return mTokenKey;
    }

    public String getUsername() {
        return mUsername;
    }

    public Bitmap getmHeadImage() {
        if (mHeadImage == null && mKeyValueBundle.containsKey(KEY_HEAD_IMG)) {
            mHeadImage = mKeyValueBundle.getParcelable(KEY_HEAD_IMG);
        }
        return mHeadImage;
    }

    public void put(String key, String value) {
        mKeyValueBundle.putString(key, value);
    }

    public void putParcelable(String key, Parcelable value) {
        mKeyValueBundle.putParcelable(key, value);
    }

    public String getString(String key) {
        return mKeyValueBundle.getString(key);
    }

    public static final Creator<NubiaAccountInfo> CREATOR = new Creator<NubiaAccountInfo>() {
        @Override
        public NubiaAccountInfo createFromParcel(Parcel in) {
            return new NubiaAccountInfo(in);
        }

        @Override
        public NubiaAccountInfo[] newArray(int size) {
            return new NubiaAccountInfo[size];
        }
    };

    public NubiaAccountInfo() {
    }

    private NubiaAccountInfo(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        mKeyValueBundle.readFromParcel(in);
        mTokenId = in.readString();
        mUsername = in.readString();
        mTokenKey = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        mKeyValueBundle.writeToParcel(dest, flags);
        dest.writeString(mTokenId);
        dest.writeString(mUsername);
        dest.writeString(mTokenKey);
    }

    @Override
    public String toString() {
        return "NubiaAccountInfo{" +
                "mTokenId='" + mTokenId + '\'' +
                ", mUsername='" + mUsername + '\'' +
                ", mTokenKey='" + mTokenKey + '\'' +
                '}';
    }
}