package com.rongyi.fanshe;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.textview)
    TextView textView;

    @ViewById(R.id.tvtitle)
    TextView title;

    private UserBean mUserBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inject(this);

        fanshe();


        textView.setText("xili");
        title.setText("你好");


    }

    private void fanshe() {
        /*Class<UserBean> userBeanClass = UserBean.class;
        try {
            mUserBean = userBeanClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }*/


        mUserBean = new UserBean();
        Class<? extends UserBean> aClass = mUserBean.getClass();


        try {
            Method test = aClass.getDeclaredMethod("Test", String.class);
            test.setAccessible(true);
            test.invoke(mUserBean, "厉害了");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


        try {
            Field name = aClass.getDeclaredField("name");
            Field age = aClass.getDeclaredField("age");
            name.setAccessible(true);
            age.setAccessible(true);
            Log.d("MainActivity", (String) name.get(mUserBean));
            Log.d("MainActivity", (String) age.get(mUserBean));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    public static void findViewById(Activity activity) {
        Class<?> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                int viewId = viewById.value();
                View view = activity.findViewById(viewId);
                try {
                    //两个参数 1.该属性在哪个对象里面  2.给属性设置的值
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private static void onClick(final Activity activity) {
        Class<?> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (final Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                int[] viewIds = onClick.value();
                for (int viewId : viewIds) {
                    View view = activity.findViewById(viewId);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            method.setAccessible(true);
                            try {
                                method.invoke(activity);//无参的方法
                            } catch (Exception e) {
                                try {
                                    method.invoke(activity, v);//有参的方法 v代表当前点击的view对象
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    public static void inject(Activity activity) {
        findViewById(activity);
        onClick(activity);
    }

    @OnClick(R.id.btnClick)
    private void btnClick(Button btn) {
        Toast.makeText(this, btn.getText().toString(), Toast.LENGTH_SHORT).show();
    }

}
