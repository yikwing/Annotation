package com.rongyi.fanshe;

import android.util.Log;

/**
 * Created by rongyi on 2017/8/1.
 */

public class UserBean {

    private String name = "张三";
    private String age = "23";

    private void Test(String s) {
        Log.d("this", "反射成功" + ":" + s);
    }

}
