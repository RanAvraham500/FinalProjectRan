package com.example.finalprojectran.login;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.IntentSender;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectran.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;

public class LoginBasic extends AppCompatActivity {

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private FirebaseAuth mAuth;
    public static ArrayList<String> carModels;

    private int pageCounter = 0;
    public void flipNext(ViewFlipper vf, Button btnPrev, Button btnNext) {
        vf.setInAnimation(this, R.anim.slide_in_right_next);
        vf.setOutAnimation(this, R.anim.slide_in_left_next);
        if (pageCounter != 1) {
            vf.showNext();
            pageCounter++;
            btnPrev.setAlpha(1);
        }
        if (pageCounter == 1) {
            btnNext.setAlpha(0);;
        }
    }
    public void flipPrev(ViewFlipper vf, Button btnPrev, Button btnNext) {
        vf.setInAnimation(this, R.anim.slide_in_left_prev);
        vf.setOutAnimation(this, R.anim.slide_in_right_prev);
        if (pageCounter != 0) {
            vf.showPrevious();
            pageCounter--;
            btnNext.setAlpha(1);
        }
        if (pageCounter == 0) {
            btnPrev.setAlpha(0);
        }
    }
    public void btnAnimation(Button button) {
        Animation btnAnim = AnimationUtils.loadAnimation(this, R.anim.button_bounce);
        button.startAnimation(btnAnim);
    }

    public void createRequest() {
        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // show all accounts
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();
    }
    public boolean signInIntent() {
        final boolean[] returnThis = {false};
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0);
                            returnThis[0] = true;

                        } catch (IntentSender.SendIntentException e) {
                            Log.e("MyLog", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No Google Accounts found. Just continue presenting the signed-out UI.
                        Log.d("MyLog", e.getLocalizedMessage());
                    }
                });
        return returnThis[0];
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = googleCredential.getGoogleIdToken();

                if (idToken != null) {
                    // Got an ID token from Google. Use it to authenticate
                    // with Firebase.
                    // Initialize Firebase Auth
                    mAuth = FirebaseAuth.getInstance();
                    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                    mAuth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("MyLog", "signInWithCredential:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        //updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("MyLog", "signInWithCredential:failure", task.getException());
                                        //updateUI(null);
                                    }
                                }
                            });
                }
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case CommonStatusCodes.CANCELED:
                        Log.d("MyLog", "One-tap dialog was closed.");
                        // Don't re-prompt the user.
                        break;
                    case CommonStatusCodes.NETWORK_ERROR:
                        Log.d("MyLog", "One-tap encountered a network error.");
                        // Try again or just ignore.
                        break;
                    default:
                        Log.d("MyLog", "Couldn't get credential from result." + e.getLocalizedMessage());
                        break;
                }
            }
        } else {
            //if the code isn't correct
        }
    }

}
