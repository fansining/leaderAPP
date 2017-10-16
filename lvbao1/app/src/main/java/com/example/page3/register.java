package com.example.page3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wangdan.lvbao.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by wangdan on 2016/8/10.
 */
public class register extends Activity{
    private ImageView back;
    private EditText phone;
    private EditText password;
    private EditText repassword;
    private Button register;
    private String telephone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Bundle bundle = this.getIntent().getExtras();
        telephone = bundle.getString("key_address");
        Bmob.initialize(this, "ead7f58b019d1b4e49e4fbc678ce78f4");
        back = (ImageView)findViewById(R.id.back);
        phone = (EditText)findViewById(R.id.password);
        password = (EditText)findViewById(R.id.password);
        repassword = (EditText)findViewById(R.id.repassword);
        register = (Button)findViewById(R.id.register);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                if (pass.equals(repass)) {
                    Person p2 = new Person();
                    p2.setName(pass);
                    p2.setAddress(telephone);
                    p2.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e1) {
                            if (e1 == null) {
                                // Toast.makeText(, "添加数据成功，返回objectId为：" + objectId, Toast.LENGTH_SHORT).show();
                                Toast.makeText(register.this, "创建数据失败：" + e1.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Intent comlog = new Intent(register.this, comlog.class);
                                startActivity(comlog);
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(register.this,"两次密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
