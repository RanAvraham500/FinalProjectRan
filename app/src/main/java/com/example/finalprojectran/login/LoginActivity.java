package com.example.finalprojectran.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalprojectran.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends LoginBasic implements
        View.OnClickListener,
        TextWatcher,
        FirebaseAuth.AuthStateListener,
        RecyclerViewAdapter.MyViewHolder.RecyclerOnClickListener {

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
        //btnNext.setVisibility(View.INVISIBLE);//
        btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(this);
        //btnPrev.setVisibility(View.INVISIBLE);//
        btnLogOut = findViewById(R.id.btnLogOut);
        hideButton(btnLogOut);
        btnLogOut.setOnClickListener(this);//
        btnAddCar = findViewById(R.id.btnAddCar);
        btnAddCar.setOnClickListener(this);
        btnReduceCar = findViewById(R.id.btnReduceCar);
        btnReduceCar.setOnClickListener(this);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnAddStudent.setOnClickListener(this);
        btnReduceStudent = findViewById(R.id.btnReduceStudent);
        btnReduceStudent.setOnClickListener(this);//

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
        etCarNum.setEnabled(false);
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
                setUpArr(x, 'C');
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(LoginActivity.this, content);
                rvCars.setAdapter(adapter);
                rvCars.setLayoutManager(new LinearLayoutManager(LoginActivity.this));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etStudentNum = findViewById(R.id.etStudentNum);
        etStudentNum.setEnabled(false);
        etStudentNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int x;
                if (count == 0) {
                    x = 0;
                } else {
                    x = Integer.parseInt(s.toString());
                }
                setUpArr(x, 'S');
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(LoginActivity.this, content);
                rvStudents.setAdapter(adapter);
                rvStudents.setLayoutManager(new LinearLayoutManager(LoginActivity.this));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //setting up textview
        tvCurrUser = findViewById(R.id.tvCurrUser);

        //google login
        createRequest();
        fbUser = fbAuth.getCurrentUser();
        if (fbUser != null) {
            showButton(btnLogOut);
        }

        //setting up the view flipper on click listener
        RecyclerViewAdapter.MyViewHolder.setRecyclerOnClickListener(this);
    }
    /**
     * The onClick method, is where we know what button was pressed*/
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
            case R.id.btnAddCar:
                carCount++;
                etCarNum.setText(carCount+"");
                break;
            case R.id.btnReduceCar:
                if (carCount != 0) carCount--;
                etCarNum.setText(carCount+"");
                break;
            case R.id.btnAddStudent:
                studentCount++;
                etStudentNum.setText(studentCount+"");
                break;
            case R.id.btnReduceStudent:
                if (studentCount != 0) studentCount--;
                etStudentNum.setText(studentCount+"");
                break;
        }
    }

    /**
     * Creates the "bouncing" button click animation*/
    public void btnAnimation(View view) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_bounce);
        view.startAnimation(anim);
    }

    /**
     * Creates the moving background animation*/
    private void backgroundAnimation() {
        AnimationDrawable animationDrawable = (AnimationDrawable) LLMain.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
    }

    /**
     * Moves to next view (flips forwards)
     * */
    public void flipNext() {
        vf.setInAnimation(this, R.anim.slide_in_right_next);
        vf.setOutAnimation(this, R.anim.slide_in_left_next);
        vf.showNext();
        updateNextAndPrevButtons();
    }

    /**
    * Moves to previous view (flips backwards)
     */
    public void flipPrev() {
        vf.setInAnimation(this, R.anim.slide_in_left_prev);
        vf.setOutAnimation(this, R.anim.slide_in_right_prev);
        vf.showPrevious();
        updateNextAndPrevButtons();
    }

    /**
     * Updates the visibility of the next & prev buttons
     */
    public void updateNextAndPrevButtons() {
        switch (vf.getDisplayedChild()) {
            case 0:
                if (fbUser != null) {
                    hideButton(btnPrev);
                    showButton(btnNext);
                } else {
                    hideButton(btnPrev);
                    hideButton(btnNext);
                }
                //test
                /*hideButton(btnPrev);
                showButton(btnNext);*/
                //Toast.makeText(this, "Page 0", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (!allETFilled()) hideButton(btnNext);
                showButton(btnPrev);
                //Toast.makeText(this, "Page 1", Toast.LENGTH_SHORT).show();

                break;
            case 2:
                showButton(btnPrev);
                showButton(btnNext);
                //Toast.makeText(this, "Page 2", Toast.LENGTH_SHORT).show();

                break;
            case 3:
                showButton(btnPrev);
                hideButton(btnNext);
                //Toast.makeText(this, "Page 3", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     *All of the necessary "preparations" for the google sign in  */
    public void createRequest() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this
                ,googleSignInOptions);
        fbAuth = FirebaseAuth.getInstance();
        fbAuth.addAuthStateListener(this);
    }

    /**
     * Opens the google sign in intent*/
    public void createSignInIntent() {
        // Initialize sign in intent
        Intent intent = googleSignInClient.getSignInIntent();
        // Start activity for result
        startActivityForResult(intent,REQ_CODE);
    }

    /**
     * Whenever the user connectivity was changed (sign in or sign out)*/
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        fbUser = firebaseAuth.getCurrentUser();
        if (fbUser != null) {
            tvCurrUser.setText("שלום " + fbUser.getDisplayName() + "!");
        } else {
            tvCurrUser.setText("שלום אורח!");
        }
        updateNextAndPrevButtons();
    }

    /**
     * Retrieves the google sign in intent results (signed in account)*/
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
                                            showButton(btnLogOut);
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

    /**
     * Signs out the user*/
    public void logOut() {
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    fbAuth.signOut();
                    fbUser = null;
                    Log.i(TAG, "onComplete: Logged out");
                    hideButton(btnLogOut);
                }
            }
        });
    }

    /**
     * Sets up an array of the required, for the recycler view (cars or students)
     * <p>S = students, C = cars</p>*/
    public void setUpArr(int len, char type) {
        content = new CardComponents[len];
        for (int i = 1; i < len+1; i++) {
            if (type == 'S') {
                content[i-1] = new CardComponents(R.color.card_blue, "תלמיד " + i, R.drawable.ic_person);
            } else {
                content[i-1] = new CardComponents(R.color.card_pink, "מכונית " + i, R.drawable.ic_car);
            }
        }

    }

    /**
     * Edit Texts' on text changed listeners, on the second page (page 1)*/
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //check if all edit texts are full
        if (allETFilled()) {
            showButton(btnNext);
        } else {
            hideButton(btnNext);
        }
    }
    private boolean allETFilled() {
        /*return !etName.getText().toString().equals("")
                && !etLessonFare.getText().toString().equals("")
                && !etPhoneNum.getText().toString().equals("");*/
        return true;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * Recycler view on view clicked method*/
    @Override
    public void recyclerOnClick(View view) {
        //firstly check if an object was already assigned to the view
        //if not let the user assign an object to the view, using edit texts in the dialog
        //if an object is already assigned show the details of the object, and give the user a way to change the assigned object

        if (vf.getDisplayedChild() == 2) {
            Toast.makeText(this, "Car", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
        }
    }
}