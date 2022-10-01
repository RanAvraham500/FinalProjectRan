package com.example.finalprojectran.login;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.example.finalprojectran.R;

public class LoginActivity extends LoginBasic implements View.OnClickListener{
    Button btnSignInWithGoogle, btnNext, btnPrev;
    EditText etName, etPhoneNum, etCarNum;
    ViewFlipper vf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setting up background animation:
        LinearLayout linearLayout = findViewById(R.id.LL);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        //setting up the buttons
        btnSignInWithGoogle = findViewById(R.id.btnSignInWithGoogle);
        btnSignInWithGoogle.setOnClickListener(this);//
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);//
        btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(this);
        btnPrev.setAlpha(0);//

        //setting up view flipper
        vf = findViewById(R.id.vp);
        vf.setDisplayedChild(0);

        //setting up the editTexts
        etName = findViewById(R.id.etName);
        etPhoneNum = findViewById(R.id.etPhoneNum);

        createRequest();
    }


    @Override
    public void onClick(View view) {
        Button tmp = (Button) view;
        btnAnimation(tmp);

        if (tmp.equals(btnSignInWithGoogle)) {
            if (signInIntent()) {
                flipNext(vf, btnPrev, btnNext);
            }
        } else if (tmp.equals(btnNext)) {
            flipNext(vf, btnPrev, btnNext);
        } else {
            flipPrev(vf, btnPrev, btnNext);
        }
    }
}