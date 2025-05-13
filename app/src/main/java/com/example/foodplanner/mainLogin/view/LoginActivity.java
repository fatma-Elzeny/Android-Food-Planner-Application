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
import com.example.foodplanner.db.MealsLocalDataSourceImpl;
import com.example.foodplanner.home.view.MainActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.SignUP.view.SignUpActivity;
import com.example.foodplanner.mainLogin.presenter.MainLoginPresenter;
import com.example.foodplanner.mainLogin.presenter.MainLoginPresenterImpl;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;
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
    private TextView btnLogin;
    private CallbackManager callbackManager;
    private int guest_flag = 0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());

        mainLoginPresenter = new MainLoginPresenterImpl(this);
        initializeUIComponents();
        configureGoogleSignIn();
        setupButtonListeners();
        setupFacebookLogin();
        mainLoginPresenter.checkExistingUser();
    }

    private void initializeUIComponents() {
        progressBar = findViewById(R.id.progressBar);
        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_signup);
        btnGuest = findViewById(R.id.btn_guest);
        googleBtn = findViewById(R.id.btn_google);
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupButtonListeners() {
        btnLogin.setOnClickListener(v -> startActivity(new Intent(this, AuthLoginActivity.class)));
        btnSignUp.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
        btnGuest.setOnClickListener(v -> handleGuestLogin());
        googleBtn.setOnClickListener(v -> initiateGoogleSignIn());
    }

    private void handleGuestLogin() {
        guest_flag = 1;
        SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
        prefs.edit().remove("USER_UID").apply();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("key", guest_flag);
        startActivity(intent);
    }

    private void initiateGoogleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    private void setupFacebookLogin() {
        LoginButton facebookBtn = findViewById(R.id.btn_facebook);
        facebookBtn.setLoginText("Continue with Facebook");
        facebookBtn.setPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();

        facebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mainLoginPresenter.loginWithFacebook(loginResult.getAccessToken().getToken());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                mainLoginPresenter.loginWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                toast("Google Sign-In Failed");
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // region mainLoginView Implementation
    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoginSuccess() {
        SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
        String uid = prefs.getString("USER_UID", null);

        // 🟢 Initialize repository with UID
        MealsRepositoryImpl repository = new MealsRepositoryImpl(
                MealsRemoteDataSourceImpl.getInstance(),
                MealsLocalDataSourceImpl.getInstance(this)
        );
        repository.setCurrentUserId(uid);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onLoginFailure(String message) {
        Toast.makeText(this, "Login Failed: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void persistUser(String uid) {
        SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
        prefs.edit().putString("USER_UID", uid).apply();

    }
    // endregion

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}