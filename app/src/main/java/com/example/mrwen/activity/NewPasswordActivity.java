package com.example.mrwen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrwen.Utils.MD5Utils;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPasswordActivity extends Activity {
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.et_password_again)
    EditText et_password_again;
    @Bind(R.id.bt_pwd_clear)
    Button bt_pwd_clear;
    @Bind(R.id.bt_pwd_again_clear)
    Button bt_pwd_again_clear;
    @Bind(R.id.bt_submit)
    Button bt_submit;
    @Bind(R.id.tv_password_security_level)
    TextView tv_password_security_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");

        bt_pwd_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_password.setText("");
            }
        });

        bt_pwd_again_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_password_again.setText("");
            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                String password = editable.toString();
                if(length>6&&length<10&&containsNumberOnly(password))
                    tv_password_security_level.setText("简单");
                else if((length>6&&length<10&&!containsNumberOnly(password))||(length>10&&containsNumberOnly(password)))
                    tv_password_security_level.setText("中等");
                else if(length>10&&!containsNumberOnly(password))
                    tv_password_security_level.setText("复杂");
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = et_password.getText().toString();
                String passwordAgain = et_password_again.getText().toString();
                if(!password.equals(passwordAgain)){
                    Toast.makeText(NewPasswordActivity.this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                }else if(password.length()<6){
                    Toast.makeText(NewPasswordActivity.this, "请输入长度大于6的密码", Toast.LENGTH_SHORT).show();
                }else{
                    setNewPassword(username, MD5Utils.Encode(password));
                }
            }
        });
    }

    private boolean containsNumberOnly(String password){
        for(char c:password.toCharArray()){
            if(c<'0'||c>'9')
                return false;
        }
        return true;
    }

    private void setNewPassword(String username, String password){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher setNewPassword = retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call = setNewPassword.setNewPassword(username, password);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body().getResultCode()==1) {
                    Toast.makeText(NewPasswordActivity.this, "新密码设置成功", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(NewPasswordActivity.this, LoginInActivity.class);
                    startActivity(loginIntent);
                }else{
                    Toast.makeText(NewPasswordActivity.this, "设置失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialog(NewPasswordActivity.this,t.toString());
            }
        });
    }
}
