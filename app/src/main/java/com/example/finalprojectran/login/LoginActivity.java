package com.example.finalprojectran.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.example.finalprojectran.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends LoginBasic implements View.OnClickListener, TextWatcher {
    Button btnNext, btnPrev,
            btnAddCar, btnReduceCar,
            btnAddStudent, btnReduceStudent,
            btnLogOut;
    SignInButton btnSignInWithGoogle;
    EditText etName, etLessonFare, etPhoneNum, etCarNum, etStudentNum;
    ViewFlipper vf;
    String[] carTvIndexes, studentTvIndexes;
    RecyclerView rvCars, rvStudents;
    LinearLayout LLMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setting up background animation:
        LLMain = findViewById(R.id.LLMain);
        backgroundAnimation();

        //setting up the buttons
        btnSignInWithGoogle = findViewById(R.id.btnSignInWithGoogle);
        btnSignInWithGoogle.setOnClickListener(this);//
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        btnNext.setVisibility(View.INVISIBLE);//
        btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(this);
        btnPrev.setVisibility(View.INVISIBLE);//
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setVisibility(View.GONE);
        btnLogOut.setOnClickListener(this);//

        //setting up view flipper
        vf = findViewById(R.id.vp);

        //setting up the recyclerView
        rvCars = findViewById(R.id.rvCars);
        rvStudents = findViewById(R.id.rvStudents);

        //setting up the editTexts
        etName = findViewById(R.id.etName);
        etName.addTextChangedListener(this);
        etLessonFare = findViewById(R.id.etLessonFare);
        etLessonFare.addTextChangedListener(this);
        etPhoneNum = findViewById(R.id.etPhoneNum);
        etPhoneNum.addTextChangedListener(this);
        etCarNum = findViewById(R.id.etCarNum);
        etCarNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //creating car recyclerView
                int x;
                if (count == 0) {
                    x = 0;
                } else {
                    x = Integer.parseInt(s.toString());
                }
                setUpArr(x);
                Car_RecyclerViewAdapter adapter = new Car_RecyclerViewAdapter(LoginActivity.this, carTvIndexes);
                rvCars.setAdapter(adapter);
                rvCars.setLayoutManager(new LinearLayoutManager(LoginActivity.this));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etStudentNum = findViewById(R.id.etStudentNum);
        etStudentNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //google login
        createRequest();
        FirebaseUser firebaseUser = fbAuth.getCurrentUser();
        // Check condition
        if(firebaseUser != null) {
            // When user already sign in
            // redirect to profile activity
            btnLogOut.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if(requestCode == REQ_CODE) {
            // When request code is equal to REQ_CODE
            // Initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            // check condition
            if(signInAccountTask.isSuccessful()) {
                // When google sign in successful
                Log.i(TAG, "onActivityResult: Google sign in successful");
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);
                    // Check condition
                    if(googleSignInAccount != null) {
                        // When sign in account is not equal to null
                        // Initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        ,null);
                        // Check credential
                        fbAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // Check condition
                                        if (task.isSuccessful()) {
                                            // When task is successful
                                            btnLogOut.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            // When task is unsuccessful

                                        }
                                    }
                                });
                    }
                }
                catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void logOut() {
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    fbAuth.signOut();
                    Log.i(TAG, "onComplete: Logged out");
                    btnLogOut.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    private void backgroundAnimation() {
        AnimationDrawable animationDrawable = (AnimationDrawable) LLMain.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
    }

    public void flipNext() {
        vf.setInAnimation(this, R.anim.slide_in_right_next);
        vf.setOutAnimation(this, R.anim.slide_in_left_next);
        switch (vf.getDisplayedChild()) {
            case 0:
                vf.showNext();
                if (!allETFilled()) btnNext.setVisibility(View.INVISIBLE);
                btnPrev.setVisibility(View.VISIBLE);
                break;
            case 3:
                //go to next activity
                break;
            default:
                vf.showNext();
                break;
        }
    }
    public void flipPrev() {
        vf.setInAnimation(this, R.anim.slide_in_left_prev);
        vf.setOutAnimation(this, R.anim.slide_in_right_prev);
        switch (vf.getDisplayedChild()) {
            case 1:
                vf.showPrevious();
                btnPrev.setVisibility(View.INVISIBLE);
                break;
            default:
                vf.showPrevious();
                break;
        }
    }

    public void setUpArr(int len) {
        carTvIndexes = new String[len];
        for (int i = 1; i < len+1; i++) {
            carTvIndexes[i-1] = ("מכונית " + i);
        }
    }

    @Override
    public void onClick(View view) {
        btnAnimation(view);
        switch (view.getId()){
            case R.id.btnSignInWithGoogle:
                createSignInIntent();
                break;
            case R.id.btnNext:
                flipNext();
                break;
            case R.id.btnPrev:
                flipPrev();
                break;
            case R.id.btnLogOut:
                logOut();
                break;
        }
    }

    //second page on text change listeners
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //check if all edit texts are full
        if (allETFilled()) {
            btnNext.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.INVISIBLE);
        }
    }
    private boolean allETFilled() {
        return etName.getText() != null
                && etLessonFare.getText() != null
                && etPhoneNum.getText() != null;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}