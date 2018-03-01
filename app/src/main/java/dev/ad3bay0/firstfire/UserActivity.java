package dev.ad3bay0.firstfire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class UserActivity extends AppCompatActivity {

    private TextView helloUserText;
    private Button signOutButton;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        auth = FirebaseAuth.getInstance();

        signOutButton = findViewById(R.id.signOutButton);
        helloUserText = findViewById(R.id.helloUserText);

        authListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user==null){

                    //if user is null launch login activity

                    startActivity(new Intent(UserActivity.this,LoginActivity.class));
                    finish();

                }else{
                    helloUserText.setText("Hello! "+user.getEmail());
                }



            }
        };


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutButton();
            }
        });
    }

    //sign out method
    public void signOutButton() {
        auth.signOut();
        startActivity(new Intent(UserActivity.this,LoginActivity.class));
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
