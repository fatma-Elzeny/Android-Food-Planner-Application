package com.example.foodplanner.mainLogin.view;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodplanner.AuthLogin.view.AuthLoginActivity;
import com.example.foodplanner.home.view.MainActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.SignUP.view.SignUpActivity;
import com.example.foodplanner.mainLogin.presenter.MainLoginPresenter;
import com.example.foodplanner.mainLogin.presenter.MainLoginPresenterImpl;
import com.example.foodplanner.profile.view.ProfileActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;



import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements mainLoginView {


    private MainLoginPresenter mainLoginPresenter;

    private ProgressBar progressBar;
    private Button btnSignUp, btnGuest;

    private static final int RC_GOOGLE_SIGN_IN = 1001;

    private SignInButton googleBtn;

    private GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;

    private TextView btnLogin ;
    private CallbackManager callbackManager;

    int guest_flag = 0 ;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainLoginPresenter = new MainLoginPresenterImpl(this);
        progressBar = findViewById(R.id.progressBar);
        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_signup);
        btnGuest = findViewById(R.id.btn_guest);
        googleBtn = findViewById(R.id.btn_google);
        LoginButton facebookBtn = findViewById(R.id.btn_facebook);
        setGoogleButtonText(googleBtn, "Continue with Google");

        configureGoogleSignIn();

        firebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());

        btnLogin.setOnClickListener(v -> loginUser());
        btnSignUp.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
        btnGuest.setOnClickListener(v -> {
            guest_flag= 1;
            SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
            prefs.edit().remove("USER_UID").apply();
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("key",guest_flag);
            startActivity(i);
        });
        googleBtn.setOnClickListener(view -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
        });

        // Auto-login if already authenticated
  /*   if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }*/


        // Facebook Login setup

       facebookBtn.setPermissions("email", "public_profile");

        callbackManager = CallbackManager.Factory.create();

        facebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Handle login success
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                firebaseAuth.signInWithCredential(credential)
                        .addOnSuccessListener(authResult -> {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
                                prefs.edit().putString("USER_UID", uid).apply();
                            }
                            gotoMain(); // Redirect to MainActivity
                        })
                        .addOnFailureListener(e -> toast("Facebook sign-in failed."));
            }

            @Override
            public void onCancel() {
                toast("Facebook login cancelled.");
            }

            @Override
            public void onError(FacebookException error) {
                toast("Facebook error: " + error.getMessage());
            }
        });

    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setGoogleButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                ((TextView) v).setText(buttonText);
                return;
            }
        }
    }

    private void loginUser() {

                    startActivity(new Intent(LoginActivity.this, AuthLoginActivity.class));
    }
    private void gotoMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
            }
        }

        // ✅ Add this line to handle Facebook login callback properly
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid(); // 🔑 Use UID
                            SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
                            prefs.edit().putString("USER_UID", uid).apply(); 
                        }

                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Firebase Auth Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void showLoading() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onLoginFailure(String message) {
        Toast.makeText(this, "Login Failed: " + message, Toast.LENGTH_LONG).show();
    }
}

















































