package com.example.finalprojectran.login;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectran.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginBasic extends AppCompatActivity {
    public final String TAG = "MyLog";
    public final int REQ_CODE = 2;  // Can be any integer unique to the Activity.
    GoogleSignInClient googleSignInClient;
    FirebaseAuth fbAuth;
    FirebaseAuth.AuthStateListener fbAuthListener;
    public void btnAnimation(View view) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_bounce);
        view.startAnimation(anim);
    }
    public void createRequest() {
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this
                ,googleSignInOptions);
        fbAuth = FirebaseAuth.getInstance();
        fbAuth.addAuthStateListener(fbAuthListener);
        fbAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }
    public void createSignInIntent() {
        // Initialize sign in intent
        Intent intent = googleSignInClient.getSignInIntent();
        // Start activity for result
        startActivityForResult(intent,REQ_CODE);
    }
}
