package com.example.page3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wangdan.lvbao.FragmentFoue;
import com.example.wangdan.lvbao.R;

import java.util.HashMap;
import java.util.List;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * Created by wangdan on 2016/8/9.
 */
public class login extends Activity {
    private ImageView back;
    private Button login;
    private Button register;
    private EditText password;
    private EditText telephone;
    private String phone2 = null;
    private String tele;
    private String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        SMSSDK.initSDK(this, "15d74bdedb465", "31953a2e4dc45fc98716be888cd4c46a");
        Bmob.initialize(this, "ead7f58b019d1b4e49e4fbc678ce78f4");
        back = (ImageView) findViewById(R.id.back);
        login = (Button) findViewById(R.id.log);
        register = (Button) findViewById(R.id.register);
        password = (EditText) findViewById(R.id.password);
        telephone = (EditText) findViewById(R.id.phone);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开注册页面
                //打开注册页面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
// 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");

// 提交用户信息（此方法可以不调用）
                            init(country, phone);
                        }
                    }
                });
                registerPage.show(login.this);


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tele = telephone.getText().toString();
                pass = password.getText().toString();
                Log.i(tele,pass);
                BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
                bmobQuery.addWhereEqualTo("address", tele);

                bmobQuery.findObjects(new FindListener<Person>() {
                    @Override
                    public void done(List<Person> object, BmobException e) {


                        if (e == null) {
                            int m = object.size();
                            if (m == 0) {
                                Toast.makeText(login.this, "用户名不存在", Toast.LENGTH_SHORT).show();


                            } else if (m > 0) {
                                String  tele1= object.get(0).getAddress();
                                String  pass1= object.get(0).getName();
                                Log.i(tele1+"###",pass1+"###");

                                if (tele.equals(tele1) & pass.equals(pass1)) {

                                     finish();

                                } else
                                    Toast.makeText(login.this, "密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }
        });
    }
    public void init(String country1, String phone1) {
        //判断是否已经注册

        phone2 = phone1;
        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
        bmobQuery.addWhereEqualTo("address",phone2);

        bmobQuery.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> object, BmobException e) {
                if (e == null) {
                    int m = object.size();
                   if(m==0)
                   {
                       Intent register = new Intent(login.this,register.class);
                       Bundle bundle = new Bundle();
                       bundle.putString("key_address", phone2);
                       register.putExtras(bundle);
                       startActivity(register);

                   }

                    else if(m>0)
                   {
                       Toast.makeText(login.this, "该手机号已注册，请直接登录", Toast.LENGTH_SHORT).show();
                   }

                } else {
                    Log.i("查不到",phone2);

                }
            }
        });
    }
}
