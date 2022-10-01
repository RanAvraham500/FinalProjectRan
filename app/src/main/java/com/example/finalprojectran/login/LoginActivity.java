package com.example.finalprojectran.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

public class LoginActivity extends LoginBasic implements View.OnClickListener{
    Button btnSignInWithGoogle, btnNext, btnPrev;
    EditText etName, etPhoneNum, etCarNum;
    ViewFlipper vf;
    ArrayList<String> carTvIndexes = new ArrayList<>();
    RecyclerView rvCars;
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
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //creating car recyclerView
                rvCars = findViewById(R.id.rvCars);
                setUpArrayList(Integer.parseInt((String) s));
                Car_RecyclerViewAdapter adapter = new Car_RecyclerViewAdapter(LoginActivity.this, carTvIndexes);
                rvCars.setAdapter(adapter);
                rvCars.setLayoutManager(new LinearLayoutManager(LoginActivity.this));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPhoneNum = findViewById(R.id.etPhoneNum);
        etCarNum = findViewById(R.id.etCarNum);

        //google login
        createRequest();

    }
    public void setUpArrayList(int len) {
        for (int i = 0; i < len; i++) {
            carTvIndexes.add("מכונית" + i);
        }
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