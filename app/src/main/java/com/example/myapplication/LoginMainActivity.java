package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.util.ViewUtil;

/**
 * Created by yulia on 2022/12/04
 * */
public class LoginMainActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup rg_login;
    private RadioButton rb_password;
    private RadioButton rb_verifycode;
    private EditText et_phone;
    private TextView tv_password;
    private EditText et_password;
    private Button btn_forget;
    private CheckBox ck_remember;
    private Button btn_login;

    private String[] typeArray = {"个人用户","公司用户"};
    private int mType = 0;
    private boolean bRemember = false;
    private int mRequestCode = 0;
    private String mVerifyCode;
    private String mPassword = "111111";
    private SharedPreferences mShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rg_login = (RadioGroup) findViewById(R.id.rg_login);
        rb_password = (RadioButton) findViewById(R.id.rb_password);
        rb_verifycode = (RadioButton) findViewById(R.id.rb_verifycode);
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_password = (TextView) findViewById(R.id.tv_password);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_forget = (Button) findViewById(R.id.btn_forget);
        ck_remember = (CheckBox) findViewById(R.id.ck_remember);
        btn_login = (Button) findViewById(R.id.btn_login);

        rg_login.setOnCheckedChangeListener(new RadioListener());
        ck_remember.setOnCheckedChangeListener(new CheckListener());
        et_phone.addTextChangedListener(new HideTextWatcher(et_phone));
        et_password.addTextChangedListener(new HideTextWatcher(et_password));
        btn_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, R.layout.item_select, typeArray);
        typeAdapter.setDropDownViewResource(R.layout.item_dropdown);
        Spinner sp_type = (Spinner) findViewById(R.id.sp_type);
        sp_type.setPrompt("请选择用户类型");
        sp_type.setAdapter(typeAdapter);
        sp_type.setSelection(mType);
        sp_type.setOnItemSelectedListener(new TypeSelectedListener());

        mShared = getSharedPreferences("share_login", MODE_PRIVATE);
        String phone = mShared.getString("phone", "");
        String password = mShared.getString("password", "");
        et_phone.setText(phone);
        et_password.setText(password);
    }

    @Override
    public void onClick(View v) {
        String phone = et_phone.getText().toString();
        if(v.getId() == R.id.btn_forget){
            if(phone==null || phone.length()<11){
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if(rb_password.isChecked() == true){
                Intent intent = new Intent(this, LoginForgetActivity.class);
                intent.putExtra("phone",phone);
                startActivityForResult(intent,mRequestCode);
            }else if(rb_verifycode.isChecked() == true){
                //随机生成验证码
                mVerifyCode = String.format("%06d", (int) (Math.random() * 1000000 % 1000000));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请记住验证码");
                builder.setMessage("手机号"+phone+",本次验证码是"+mVerifyCode+",请输入验证码");
                builder.setPositiveButton("好的",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }else if(v.getId() == R.id.btn_login){
            if(phone == null || phone.length()<11){
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if(rb_password.isChecked() == true){
                if(et_password.getText().toString().equals(mPassword) != true){
                    Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    loginSuccess();
                }
            }else if(rb_verifycode.isChecked() == true){
                if(et_password.getText().toString().equals(mVerifyCode) != true){
                    Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    loginSuccess();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == mRequestCode &&data != null){
            //用户密码已修改成新密码
            mPassword = data.getStringExtra("new_password");
        }
    }

    //从修改密码页面返回登录页面，要清空密码的输入框
    @Override
   protected void onRestart() {
        et_password.setText("");
        super.onRestart();
    }

    private void loginSuccess() {
        String desc = String.format("您的手机号码是%s,类型是%s。恭喜您通过登录验证，点击“确定“返回上个页面", et_phone.getText().toString(), typeArray[mType]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录成功");
        builder.setMessage(desc);
        builder.setPositiveButton("确定返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("我再看看",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //登录成功时判断是否需要记住密码
        if(bRemember){
            SharedPreferences.Editor edit = mShared.edit();
            edit.putString("phone",et_phone.getText().toString());
            edit.putString("password",et_password.getText().toString());
            edit.commit();
        }
    }

    private class RadioListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i == R.id.rb_password){
                tv_password.setText("登录密码：");
                et_password.setHint("请输入密码");
                btn_forget.setText("忘记密码");
                ck_remember.setVisibility(View.VISIBLE);
            }else if(i == R.id.rb_verifycode){
                tv_password.setText("    验证码：");
                et_password.setHint("请输入验证码");
                btn_forget.setText("获取验证码");
                ck_remember.setVisibility(View.INVISIBLE);
            }
        }
    }



    private class CheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(compoundButton.getId() == R.id.ck_remember){
                bRemember = b;
            }
        }
    }

    private class HideTextWatcher implements TextWatcher {
        private final EditText mView;
        private final int mMaxLength;
        private CharSequence mStr;

        //获取最大长度
        public HideTextWatcher(EditText v){
            super();
            mView = v;
            mMaxLength = ViewUtil.getMaxLength(v);

        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mStr = charSequence;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(mStr == null || mStr.length()==0){
                return;
            }
            if ((mStr.length() == 11 && mMaxLength == 11) ||
                    (mStr.length() == 6 && mMaxLength == 6)) {

                ViewUtil.hideOneInputMethod(LoginMainActivity.this,mView);
            }

        }
    }

    private class TypeSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            mType = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}