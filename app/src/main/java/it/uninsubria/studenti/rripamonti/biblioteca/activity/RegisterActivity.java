package it.uninsubria.studenti.rripamonti.biblioteca.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import it.uninsubria.studenti.rripamonti.biblioteca.R;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity" ;
    private Button confirm_btn;

    private EditText mName, mSurname, mPassword, mEmail;
    private View mProgressView, mRegisterFormView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference ref;
    private String name, surname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        confirm_btn = (Button) findViewById(R.id.btn_confirm_registration);
        mName = (EditText) findViewById(R.id.et_name);
        mSurname = (EditText) findViewById(R.id.et_surname);
        mPassword = (EditText) findViewById(R.id.et_password);
        mEmail = (EditText) findViewById(R.id.et_email);
        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
                //crea nuovo utente
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("RegisterActivity","onSupportNavigateUp()");
        finish();
        return true;
    }

    private void attemptRegister() {
        int currentVersion=0;
        try {
            currentVersion = getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0 ).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int versionNeed = 10240000;
        // Reset errors.
        mEmail.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        name = mName.getText().toString();
        surname = mSurname.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)){
            mName.setError(getString(R.string.error_field_required));

            focusView = mName;
            cancel = true;
        }

        if (TextUtils.isEmpty(surname)){
            mSurname.setError(getString(R.string.error_field_required));

            focusView = mSurname;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)){
            mPassword.setError(getString(R.string.error_field_required));

            focusView = mPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else if (currentVersion<versionNeed) {
            int error = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, error, 1);
            dialog.show();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                ref = FirebaseDatabase.getInstance().getReference();
                                Map<String, String> map = new HashMap<>();
                                map.put("Name",name.toString());
                                map.put("Surname", surname.toString());
                                map.put("Admin", "false");
                                ref.child("users").child(user.getUid()).setValue(map);
                                finish();

                            }
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {

                                showProgress(false);
                                if (task.getException() instanceof FirebaseNetworkException) {
                                    Toast toast = Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG);
                                    toast.show();
                                } else {
                                    mEmail.setText("");
                                    mPassword.setText("");
                                    mEmail.setError(getString(R.string.email_used));
                                    mEmail.requestFocus();
                                }

                            }

                            // ...
                        }
                    });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
