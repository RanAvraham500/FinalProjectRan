package com.example.finalprojectran.login;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginBasic extends AppCompatActivity {
    public final String TAG = "MyLog";
    public final int REQ_CODE = 2;  // Can be any integer unique to the Activity.
    GoogleSignInClient googleSignInClient;
    FirebaseAuth fbAuth;
    Button btnNext, btnPrev,
            btnAddCar, btnReduceCar,
            btnAddStudent, btnReduceStudent,
            btnLogOut;
    SignInButton btnSignInWithGoogle;
    EditText etName, etLessonFare, etPhoneNum,
            etCarNum, etStudentNum;
    TextView tvCurrUser;
    ViewFlipper vf;
    CardComponents[] content;
    RecyclerView rvCars, rvStudents;
    LinearLayout LLMain;
    FirebaseUser fbUser;

    static int carCount, studentCount = 0;

    /**
     * Methods to hide or show a button on the app*/
    public void hideButton(Button btn) {
        btn.setAlpha(0);
        btn.setClickable(false);
    }
    public void showButton(Button btn) {
        btn.setAlpha(1);
        btn.setClickable(true);
    }

    /**
     * A class which contains two views as it's attributes
     * One is a text view and the other is an image view
     * this class is used to create an array that creates the recycler view cards*/
    public static class CardComponents {
        int carBackgroundRes;
        String text;
        int imageRes;

        public CardComponents(int carBackgroundRes, String text, int imageRes) {
            this.carBackgroundRes = carBackgroundRes;
            this.text = text;
            this.imageRes = imageRes;
        }

    }


    public static class Student {

    }
    public static class Teacher {

    }
    public static class Car {

    }
}
