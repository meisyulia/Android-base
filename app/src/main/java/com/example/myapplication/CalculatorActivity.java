package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_result;
    private String inputText;
    private static final String TAG = "CalculatorActivity";
    private String showText=""; // 显示的文本内容
    private String operator=""; //操作符
    private String firstNum=""; //前一个操作数
    private String nextNum=""; //后一个操作数
    private String result=""; //当前计算的结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        // 从布局文件中获取名叫tv_result的文本视图
        tv_result = findViewById(R.id.tv_result);
        // 下面给每个按钮控件都注册了点击监听器
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_divide).setOnClickListener(this); // “除法”按钮
        findViewById(R.id.btn_multiply).setOnClickListener(this); // “乘法”按钮
        findViewById(R.id.btn_clear).setOnClickListener(this); // “清除”按钮
        findViewById(R.id.btn_seven).setOnClickListener(this); // 数字7
        findViewById(R.id.btn_eight).setOnClickListener(this); // 数字8
        findViewById(R.id.btn_nine).setOnClickListener(this); // 数字9
        findViewById(R.id.btn_plus).setOnClickListener(this); // “加法”按钮
        findViewById(R.id.btn_four).setOnClickListener(this); // 数字4
        findViewById(R.id.btn_five).setOnClickListener(this); // 数字5
        findViewById(R.id.btn_six).setOnClickListener(this); // 数字6
        findViewById(R.id.btn_minus).setOnClickListener(this); // “减法”按钮
        findViewById(R.id.btn_one).setOnClickListener(this); // 数字1
        findViewById(R.id.btn_two).setOnClickListener(this); // 数字2
        findViewById(R.id.btn_three).setOnClickListener(this); // 数字3
        findViewById(R.id.btn_zero).setOnClickListener(this); // 数字0
        findViewById(R.id.btn_dot).setOnClickListener(this); // “小数点”按钮
        findViewById(R.id.btn_equal).setOnClickListener(this); // “等号”按钮
        findViewById(R.id.ib_sqrt).setOnClickListener(this); // “开平方”按钮
    }

    @Override
    public void onClick(View v) {
        int resid = v.getId();
        if(resid == R.id.ib_sqrt)
        {
            inputText = "√";
        }else{
            inputText = ((TextView) v).getText().toString();
        }
        Log.d(TAG, "onClick: resid="+resid+",inputText="+inputText);

        if(resid == R.id.btn_clear)
        {
            clear("");
        }else if(resid == R.id.btn_cancel){
            if(operator.equals("")==true){
                if(firstNum.length()==1)
                {
                    firstNum = "0";
                }else if(firstNum.length()>0){
                    firstNum = firstNum.substring(0,firstNum.length()-1);
                }else{
                    Toast.makeText(this, "没有可以取消的数字了", Toast.LENGTH_SHORT).show();
                    return;
                }
                showText = firstNum;
                tv_result.setText(showText);
            }else{
                if(nextNum.length()==1){
                    nextNum="0";
                }else if(nextNum.length()>0){
                    nextNum = nextNum.substring(0,nextNum.length()-1);
                }else{
                    Toast.makeText(this, "没有可以取消的数字了", Toast.LENGTH_SHORT).show();
                    return;
                }
                showText = showText.substring(0,showText.length()-1);
                tv_result.setText(showText);
            }
        }else if(resid == R.id.btn_equal){
            if(operator.length()==0 || operator.equals("=")==true){
                Toast.makeText(this, "请输入运算符", Toast.LENGTH_SHORT).show();
                return;
            }else if(nextNum.length()<=0){
                Toast.makeText(this, "请输入数字", Toast.LENGTH_SHORT).show();
                return;
            }
            if(calculate()==true){
                operator = inputText;
                showText = showText+"="+result;
                tv_result.setText(showText);
            }
        }else if(resid == R.id.btn_plus || resid == R.id.btn_minus|| resid == R.id.btn_multiply||resid == R.id.btn_divide){
            if(firstNum.length()<=0){
                Toast.makeText(this, "请输入数字", Toast.LENGTH_SHORT).show();
                return;
            }
            if(operator.length()==0||operator.equals("=")==true||operator.equals("√")==true){
                operator = inputText;
                showText = showText+operator;
                tv_result.setText(showText);
            }else{
                Toast.makeText(this, "请输入数字", Toast.LENGTH_SHORT).show();
                return;
            }
        }else if(resid==R.id.ib_sqrt){
            if(firstNum.length()<=0){
                Toast.makeText(this, "请输入数字", Toast.LENGTH_SHORT).show();
                return;
            }else if(Double.parseDouble(firstNum)<0){
                Toast.makeText(this, "开根号的数值不能小于0", Toast.LENGTH_SHORT).show();
                return;
            }
            result = String.valueOf(Math.sqrt(Double.parseDouble(firstNum)));
            firstNum=result;
            nextNum="";
            operator=inputText;
            showText = showText+"√="+result;
            tv_result.setText(showText);
            Log.d(TAG, "onClick: result="+result+",firstNum="+firstNum+",operator="+operator);
        }else{
            if(operator.equals("=")==true){
                operator="";
                firstNum="";
                showText="";
            }
            if(resid==R.id.btn_dot){
                inputText=".";
            }
            if(operator.equals("")==true){
                firstNum = firstNum+inputText;
            }else{
                nextNum = nextNum+inputText;
            }
            showText = showText+inputText;
            tv_result.setText(showText);
        }
    }

    private boolean calculate() {
        BigDecimal BigFirst = new BigDecimal(firstNum);
        BigDecimal BigNext = new BigDecimal(nextNum);
        //加减乘除
        if(operator.equals("+")==true){
            result = String.valueOf(BigFirst.add(BigNext));
        }else if(operator.equals("-")==true){
            result = String.valueOf(BigFirst.subtract(BigNext));
        }else if(operator.equals("×")==true){
            result =String.valueOf(BigFirst.multiply(BigNext));
        }else if(operator.equals("÷")==true){
            if("0".equals(nextNum)){
                Toast.makeText(this, "被除数不能为零", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                result = String.valueOf(BigFirst.divide(BigNext));
            }
        }
        firstNum=result;
        nextNum="";
        return true;
    }

    private void clear(String text) { // 清空并初始化
        showText = text;
        tv_result.setText(showText);
        operator = "";
        firstNum = "";
        nextNum = "";
        result = "";
    }
}