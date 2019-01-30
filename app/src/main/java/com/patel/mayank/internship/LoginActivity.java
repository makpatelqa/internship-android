package com.patel.mayank.internship;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button txt_reg;

    private FirebaseAuth raut;
    private FirebaseAuth.AuthStateListener mautListner;

    EditText edt_email,edt_pass;

    FirebaseDatabase fd;
    DatabaseReference db;

    String uid = "";

    Button btn_log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edt_email = (EditText) findViewById(R.id.log_email);
        edt_pass = (EditText) findViewById(R.id.log_pass);
        btn_log = (Button) findViewById(R.id.log_btn);
        //btn_out = (Button) findViewById(R.id.log_out);

        raut = FirebaseAuth.getInstance();


        mautListner = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    uid = user.getUid();
                }else
                {

                }
            }
        };



        txt_reg = (Button) findViewById(R.id.log_reg);

        txt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,Signupactivity.class);
                startActivity(i);
            }
        });

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,pass;

                email = edt_email.getText().toString();
                pass = edt_pass.getText().toString();
                if(email.equals("")){
                    edt_email.setError("can't be blank");
                }
                else if(pass.equals("")){
                    edt_pass.setError("can't be blank");
                }
                signInUser(email,pass);

            }
        });

//        btn_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                raut.signOut();
//            }
//        });

    }


    public void signInUser(String email, String pass)
    {

        raut.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this,"Successfully Login!",Toast.LENGTH_LONG).show();

                    fd = FirebaseDatabase.getInstance();
                    db = fd.getReference("User");

                    db.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String utype = dataSnapshot.child("utype").getValue().toString();

                                if (utype.equals("Job Seeker"))
                                {
                                    Intent i = new Intent(LoginActivity.this,Jobseek.class);
                                    startActivity(i);
                                    finish();
                                }else if(utype.equals("Employer"))
                                {
                                    Toast.makeText(LoginActivity.this,"Login As Employer!",Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(LoginActivity.this,EmployerActivity.class);
                                    startActivity(i);
                                    finish();
                                }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else
                {
                    Toast.makeText(LoginActivity.this,"Login Failed!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        raut.addAuthStateListener(mautListner);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mautListner != null) {
           raut.removeAuthStateListener(mautListner);
        }
    }
}


