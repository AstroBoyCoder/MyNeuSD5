package com.example.astroboy.myneusd.View;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.astroboy.myneusd.Model.Constant;
import com.example.astroboy.myneusd.R;
import com.example.astroboy.myneusd.Util.InternetConnectable;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;

import okhttp3.Call;
import okhttp3.Response;

public class MySD_user_login_Activity extends AppCompatActivity {

    Button login;
    TextView register;
    TextView forgetpsw;
    CheckBox remPsw;
    CheckBox autoLogin;
    EditText userName;
    EditText userPsw;
    EditText checkCode;
    Button checkCode_btn;

    private final static String TAG = "MySD_Login";

    Bitmap bitmap;
    Handler bitmapHandler;
    Handler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_sd_user_login_activity);
        StatusBarUtil.setTranslucent(this, 0);
        init();
    }

    private void init() {
        login = (Button) findViewById(R.id.login_ensureLogin_btn);
        register = (TextView) findViewById(R.id.login_register);
        forgetpsw = (TextView) findViewById(R.id.login_forgetPassword);
        remPsw = (CheckBox) findViewById(R.id.login_set_remPassword);
        autoLogin = (CheckBox) findViewById(R.id.login_set_autoLogin);
        userName = (EditText) findViewById(R.id.login_edit_userName);
        userPsw = (EditText) findViewById(R.id.login_edit_password);
        checkCode = (EditText) findViewById(R.id.login_edit_checkCode);
        checkCode_btn = (Button) findViewById(R.id.login_checkCode_btn);

        login.setOnClickListener(new loginListener());
        checkCode_btn.setOnClickListener(new getCheckCodeListener());
    }

    private class loginListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String un = userName.getText().toString();
            String pw = userPsw.getText().toString();
            String cc = checkCode.getText().toString();
            if (un.equals("")||pw.equals("")||cc.equals("")){
                if (un.equals(""))
                    userName.setError("请输入用户名");
                if (pw.equals(""))
                    userPsw.setError("请输入密码");
                if (cc.equals(""))
                    checkCode.setError("请输入验证码");
            }else if (isInternet()){
                //PostInformsToLogin(un,pw,cc);
            }
            else {
                Toast.makeText(MySD_user_login_Activity.this,"请联网重试~",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class getCheckCodeListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String un = userName.getText().toString();
            String pw = userPsw.getText().toString();
            if (un.equals("")||pw.equals("")){
                if (un.equals(""))
                    userName.setError("请输入用户名");
                if (pw.equals(""))
                    userPsw.setError("请输入密码");
            } else if (isInternet()) {
               GetCheckCodeThread();
            }else {
                Toast.makeText(MySD_user_login_Activity.this,"请联网重试~",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*private void PostInformsToLogin(final String un, final String pw, final String cc) {
        loginHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"UserLogin["+"UserName:"+un+" PassWord:"+pw+" CheckCode:"+cc+"]");
                String getInfo;
                try {
                    getInfo = Post_UserLogin.getStringCha(Constant.login,un,pw,cc);
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", getInfo);
                    message.setData(bundle);
                    loginHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        loginHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String response = msg.getData().getString("response");
                try {
                    JSONObject json = new JSONObject(response);
                    Log.d(TAG, "Json内容" + json);
                    JSONObject data = json.getJSONObject("loginReturn");
                    Log.d("@@", "请求信息成功 loginReturn" + data);
                    String Flag = data.getJSONObject("loginFlag").toString();
                    String Msg = data.getJSONObject("msg").toString();
                    checkUserCanLogin(Flag,Msg);
                } catch (JSONException e) {
                    //String err = String.valueOf(e);
                    Toast.makeText(MySD_user_login_Activity.this, "未成功登陆,请重试", Toast.LENGTH_LONG).show();

                }
            }
        };
    }*/

    private void checkUserCanLogin(String flag, String msg) {
        Log.d(TAG,"CHECK_USER_LOGIN:["+"Flag:"+flag+" Msg:"+msg);
        boolean isRemPsw = remPsw.isChecked();
        boolean isAutoLogin = autoLogin.isChecked();
        if (flag.equals("1")){
            Toast.makeText(MySD_user_login_Activity.this, msg, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MySD_user_login_Activity.this, msg, Toast.LENGTH_LONG).show();
        }

    }

    private void GetCheckCodeThread(){
        bitmapHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkGo.get(Constant.checkCode)
                        .tag(this)
                        .execute(new BitmapCallback() {
                            @Override
                            public void onSuccess(final Bitmap bitmap, Call call, Response response) {
                                bitmapHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        checkCode_btn.setText("");
                                        checkCode_btn.setBackgroundColor(Color.parseColor("#00ffffff"));
                                        checkCode_btn.setBackground(new BitmapDrawable(bitmap));
                                    }
                                });
                            }
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                checkCode_btn.setText("重新获取");
                                checkCode_btn.setBackgroundColor(Color.parseColor("#75ffffff"));
                            }
                        });
            }

        }).start();
    }


    private boolean isInternet() {
        return InternetConnectable.is_internet(this);
    }
}
