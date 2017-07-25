package com.example.mrwen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.interfaces.InterfaceTeacher;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetPasswordActivity extends Activity {
    @Bind(R.id.verification_username_layout)
    LinearLayout verification_username_layout;
    @Bind(R.id.et_username)
    EditText et_username;
    @Bind(R.id.verification_method_layout)
    LinearLayout verification_method_layout;
    @Bind(R.id.bt_phone_verification)
    Button bt_phone_verification;
    @Bind(R.id.bt_email_verification)
    Button bt_email_verification;
    @Bind(R.id.verification_info_layout)
    LinearLayout verification_info_layout;
    @Bind(R.id.tv_email_or_phone)
    TextView tv_email_or_phone;
    @Bind(R.id.et_email_or_phone)
    EditText et_email_or_phone;
    @Bind(R.id.bt_next_step)
    Button bt_next_step;

    //用于标识验证方式，手机验证为0，邮箱验证为1
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

        bt_email_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                setView(flag);
            }
        });

        bt_phone_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 0;
                setView(flag);
            }
        });

        bt_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String verifyInfo = et_email_or_phone.getText().toString();
                if(username.equals("")||verifyInfo.equals("")){
                    Toast.makeText(ForgetPasswordActivity.this, "请输入用户名或验证信息", Toast.LENGTH_SHORT).show();
                }else{
                    verifyInfo(username, verifyInfo);
                }

            }
        });
    }

    private void setView(int flag){
        verification_method_layout.setVisibility(View.GONE);
        verification_username_layout.setVisibility(View.VISIBLE);
        verification_info_layout.setVisibility(View.VISIBLE);
        bt_next_step.setVisibility(View.VISIBLE);
        if(flag==0){
            tv_email_or_phone.setText("注册所填手机：");
        }else if(flag==1){
            tv_email_or_phone.setText("注册所填邮箱：");
        }
    }

    private void verifyInfo(final String username, String vrfInfo){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher verifyInfo = retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call = verifyInfo.verifyPhoneOrEmail(flag, username, vrfInfo);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body().getResultCode()==1) {
                    Toast.makeText(ForgetPasswordActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    Intent newPasswordIntent = new Intent(ForgetPasswordActivity.this, NewPasswordActivity.class);
                    newPasswordIntent.putExtra("username", username);
                    startActivity(newPasswordIntent);
                }else{
                    if(flag==0)
                        alertDialog.showAlertDialog(ForgetPasswordActivity.this, "验证失败，请填入正确手机号");
                    else
                        alertDialog.showAlertDialog(ForgetPasswordActivity.this, "验证失败，请填入正确邮箱号");
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialog(ForgetPasswordActivity.this,t.toString());
            }
        });
    }
}
