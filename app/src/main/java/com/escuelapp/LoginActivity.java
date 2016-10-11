package com.escuelapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.escuelapp.database.DatabaseAccess;
import com.escuelapp.model.BaseModel;
import com.escuelapp.model.User;
import com.escuelapp.utility.Constants;
import com.escuelapp.utility.Utility;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, DatabaseAccess.OnDatabaseResponse {

    private static final String TAG = "LoginActivity";
    private static final int CODE_SIGN_IN = 9001;
    private SignInButton signInButton;
    private LinearLayout buttonsLinear;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;
    private Context context;
    private boolean runMethod = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initializeView();
        configureGoogleSignIn();
        configureFireBase();
    }

    private void initializeView() {
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    private void configureFireBase() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (runMethod) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        runMethod = false;
                        if (Utility.getUserUid(context) == null) {
                            createOrReadUser(firebaseUser);
                        } else {
                            openApp();
                        }
                    } else {
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            showErrorAuth();
                        }
                        hideProgressDialog();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                showErrorAuth();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            default:
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, CODE_SIGN_IN);
    }

    private void openApp() {
        Utility.saveUserUid(context, firebaseAuth.getCurrentUser().getUid());
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    private void createOrReadUser(FirebaseUser firebaseUser) {
        showProgressDialog();
        DatabaseAccess.findUserByUid(firebaseUser.getUid(), this);
    }

    private void showErrorAuth() {
        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
        Utility.deleteUserUid(context);
        runMethod = true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showErrorAuth();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onDatabaseResponse(BaseModel baseModel, String operationCode) {
        switch (operationCode) {
            case Constants.SEARCH_USER:
                validateUser(baseModel);
                break;
            default:
                break;
        }
    }

    private void validateUser(BaseModel baseModel) {
        hideProgressDialog();
        if (baseModel != null) {
            openApp();
        } else {
            createUser();
        }
    }

    private void createUser() {
        User user = new User();
        user.setName(firebaseAuth.getCurrentUser().getDisplayName());
        DatabaseAccess.saveNewUser(user, firebaseAuth.getCurrentUser().getUid());
        openApp();
    }

    @Override
    public void onDatabaseError(DatabaseError databaseError) {
        showErrorAuth();
    }
}
