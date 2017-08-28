package com.dfc.tips.com.dfc.tips.msg;

import android.net.Uri;
import android.os.Parcelable;
import android.os.Parcel;


/**
 * Created by dfc on 2017/7/11.
 */

public class Tips  implements Parcelable {
    private String tips_name;
    private String tips_content;
    private String tips_type;
    private String tips_createtime;
    private String tips_img;
    private String tips_username;

    public String getTips_img() {
        return tips_img;
    }

    public void setTips_img(String tips_img) {
        this.tips_img = tips_img;
    }


    public String getTips_username() {
        return tips_username;
    }

    public void setTips_username(String tips_username) {
        this.tips_username = tips_username;
    }

    public String getTips_content() {
        return tips_content;
    }

    public void setTips_content(String tips_content) {
        this.tips_content = tips_content;
    }

    public String getTips_type() {
        return tips_type;
    }

    public void setTips_type(String tips_type) {
        this.tips_type = tips_type;
    }

    public String getTips_createtime() {
        return tips_createtime;
    }

    public void setTips_createtime(String tips_createtime) {
        this.tips_createtime = tips_createtime;
    }

    public String getTips_name() {
        return tips_name;
    }

    public void setTips_name(String tips_name) {
        this.tips_name = tips_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tips_name);
        dest.writeString(tips_content);
        dest.writeString(tips_type);
        dest.writeString(tips_createtime);
        dest.writeString(tips_img);
        dest.writeString(tips_username);
    }
    public static final Parcelable.Creator<Tips> CREATOR = new Creator<Tips>() {
        public Tips createFromParcel(Parcel source) {
            Tips mTips = new Tips();
            mTips.tips_name = source.readString();
            mTips.tips_content = source.readString();
            mTips.tips_type = source.readString();
            mTips.tips_createtime = source.readString();
            mTips.tips_img = source.readString();
            mTips.tips_username = source.readString();
            return mTips;
        }
        public Tips[] newArray(int size) {
            return new Tips[size];
        }
    };
}
