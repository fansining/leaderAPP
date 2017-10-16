package com.example.wangdan.lvbao;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.page3.Person;
import com.example.page3.comlog;
import com.example.page3.login;
import com.example.page3.register;
import com.example.wangdan.lvbao.R;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * Created by wangdan on 2016/7/28.
 */
public class FragmentFoue extends Fragment{
    View v;
    Context context;
    private Button btn;
    private LinearLayout linear1;
    private LinearLayout linear2;
    private LinearLayout linear3;
    private LinearLayout linear4;
    private LinearLayout linear5;
    private LinearLayout linear6;
    private LinearLayout linear7;
    private Button login;

    private ImageView back;
    private Button login1;
    private Button register;
    private EditText password;
    private EditText telephone;
    private String phone2 = null;
    private String tele;
    private String pass;
    private Onclik listener = new Onclik();
    private PopupWindow pop;
    private boolean flag = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_four, container, false);
        context = getActivity();
        SMSSDK.initSDK(context, "15d74bdedb465", "31953a2e4dc45fc98716be888cd4c46a");
        Bmob.initialize(context, "ead7f58b019d1b4e49e4fbc678ce78f4");

        linear1 = (LinearLayout)v.findViewById(R.id.linear1);
        linear2 = (LinearLayout)v.findViewById(R.id.linear2);
        linear3 = (LinearLayout)v.findViewById(R.id.linear3);
        linear4 = (LinearLayout)v.findViewById(R.id.linear4);
        linear5 = (LinearLayout)v.findViewById(R.id.linear5);
        linear6 = (LinearLayout)v.findViewById(R.id.linear6);
        linear7 = (LinearLayout)v.findViewById(R.id.linear7);
        login = (Button)v.findViewById(R.id.login);
        linear1.setOnClickListener(listener);
        linear2.setOnClickListener(listener);
        linear3.setOnClickListener(listener);
        linear4.setOnClickListener(listener);
        linear5.setOnClickListener(listener);
        linear6.setOnClickListener(listener);
        linear7.setOnClickListener(listener);
        login.setOnClickListener(listener);

        return v;
    }


    private class Onclik implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = layoutInflater.inflate(R.layout.login, null);
            pop = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.setFocusable(true);
            pop.showAsDropDown(contentView);
            pop.showAsDropDown(null, 0, contentView.getHeight());
            back = (ImageView) contentView.findViewById(R.id.back);
            login = (Button) contentView.findViewById(R.id.log);
            register = (Button) contentView.findViewById(R.id.register);
            password = (EditText) contentView.findViewById(R.id.password);
            telephone = (EditText) contentView.findViewById(R.id.phone);
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
                    registerPage.show(getActivity());


                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.dismiss();
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tele = telephone.getText().toString();
                    pass = password.getText().toString();
                    Log.i(tele, pass);
                    BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
                    bmobQuery.addWhereEqualTo("address", tele);

                    bmobQuery.findObjects(new FindListener<Person>() {
                        @Override
                        public void done(List<Person> object, BmobException e) {


                            if (e == null) {
                                int m = object.size();
                                if (m == 0) {
                                    Toast.makeText(getActivity(), "用户名不存在", Toast.LENGTH_SHORT).show();


                                } else if (m > 0) {
                                    String tele1 = object.get(0).getAddress();
                                    String pass1 = object.get(0).getName();
                                    Log.i(tele1 + "###", pass1 + "###");

                                    if (tele.equals(tele1) & pass.equals(pass1)) {
                                        flag = true;
                                        pop.dismiss();
                                        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.linear,new comlog());
                                        transaction.commit();
                                    /*Log.i("相等","true3");
                                    Intent p = new Intent(login.this, comlog.class);
                                    startActivity(p);*/
                                    } else
                                        Toast.makeText(getActivity(), "密码错误", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
                }
            });
        }
            /*Intent k = new Intent(getActivity(),login.class);
            startActivity(k);*/
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
                            Intent register = new Intent(getActivity(), com.example.page3.register.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("key_address", phone2);
                            register.putExtras(bundle);
                            startActivity(register);

                        }

                        else if(m>0)
                        {
                            Toast.makeText(getActivity(), "该手机号已注册，请直接登录", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.i("查不到",phone2);

                    }
                }
            });







        }
        }
    }

