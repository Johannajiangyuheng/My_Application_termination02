package com.johanna.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class login extends AppCompatActivity {
    private String userName,psw,spPsw;//获取的用户名，密码
    private EditText et_user_name,et_psw;//编辑框
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    private void init(){
        TextView tv_register = (TextView)findViewById(R.id.tv_register);
        TextView tv_find_psw = (TextView)findViewById(R.id.tv_find_psw);

        et_user_name = findViewById(R.id.user);
        et_psw = findViewById(R.id.pwd);
        loginButton = (Button)findViewById(R.id.login_button);

        //注册
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,Register.class);
                startActivityForResult(intent,1);
            }
        });


        //设置登陆事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName  = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();

                String md5Psw = md5(psw);
                spPsw = readPsw(userName);
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(login.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(login.this,R.string.reminder_toast,Toast.LENGTH_SHORT).show();
                } else if (md5Psw.equals(spPsw)) {
                    //一致登录成功
                    Toast.makeText(login.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
                    saveLoginStatus(true, userName);
                    //登录成功后关闭此页面进入主页
                    Intent data = new Intent();
                    //datad.putExtra( ); name , value ;
                    data.putExtra("isLogin", true);
                    //RESULT_OK为Activity系统常量，状态码为-1
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    setResult(RESULT_OK, data);
                    //销毁登录界面
                    login.this.finish();
                    //跳转到主界面，登录成功的状态传递到 MainActivity 中
                    startActivity(new Intent(login.this, MainActivity.class));
                } else if ((spPsw != null && !TextUtils.isEmpty(spPsw) && !md5Psw.equals(spPsw))) {
                    Toast.makeText(login.this, "输入的用户名和密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(login.this, "此用户名不存在", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    private String readPsw(String userName){

        SharedPreferences sp;
        sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        return sp.getString(userName ,"");
    }

    private void saveLoginStatus(boolean status,String userName){
        //文件名是loginInfo
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin",status);//存入登陆状态
        editor.putString("loginUserName",userName);//存入用户名
        editor.apply();//提交修改
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!= null){
            String userName = data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){
                et_user_name.setText(userName);
                et_user_name.setSelection(userName.length());//设置光标位置
            }
        }
    }

    public static String md5(String plainText) {
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(plainText.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        //将加密后的数据转换为16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static boolean matching(String source,String pattern){
        int slen = source.length();
        int plen = pattern.length();
        char []s = source.toCharArray();
        char []p = pattern.toCharArray();
        int i = 0,j = 0;
        if(slen <plen){
            return false;
        }else{
            while (i<slen && j<plen){
                if(s[i] == p[j]){
                    ++i;
                    ++j;
                }
                else{
                    i = i-(j-1);
                    j=0;
                }
            }
            if(j==plen)
                return true;
            else
                return false;

        }
    }


}
