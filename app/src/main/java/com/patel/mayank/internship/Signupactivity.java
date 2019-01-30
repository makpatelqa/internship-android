package com.patel.mayank.internship;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signupactivity extends AppCompatActivity {


    private FirebaseAuth raut;
    private FirebaseAuth.AuthStateListener mautListner;

    EditText edt_email,edt_pass,edt_name,edt_bod;
    Button btn_register;
    String email,pass;

    FirebaseDatabase fd;
    DatabaseReference db;

    RadioGroup radioGroup;
    RadioButton radioutype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);
        raut = FirebaseAuth.getInstance();



        edt_email = (EditText) findViewById(R.id.reg_email);
        edt_pass = (EditText) findViewById(R.id.reg_pass);
        btn_register = (Button) findViewById(R.id.btn_reg);
        edt_name = (EditText) findViewById(R.id.reg_name);
        edt_bod = (EditText) findViewById(R.id.reg_bod);
        radioGroup = (RadioGroup) findViewById(R.id.reg_rgrp);


        //System.out.println("On create Check this   "+email +"   "+ pass);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edt_email.getText().toString();
                pass = edt_pass.getText().toString();
                System.out.println("Check this   "+email +"   "+ pass);

                signupUser(email,pass);
            }
        });

    }


    public void signupUser(final String email, String pass)
    {
        raut.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    FirebaseUser user = raut.getCurrentUser();

                    if(user != null)
                    {
                        String uid = user.getUid();
                        String name = edt_name.getText().toString();
                        String bod = edt_bod.getText().toString();
                        int slectedId = radioGroup.getCheckedRadioButtonId();

                        radioutype = (RadioButton) findViewById(slectedId);
                        String utype = radioutype.getText().toString();

                        fd = FirebaseDatabase.getInstance();
                        db  = fd.getReference("User");

                        Users users = new Users(email,name,bod,utype);

                        db.child(uid).setValue(users);

                        Toast.makeText(Signupactivity.this,"Successfully Account Created!",Toast.LENGTH_LONG).show();

                    }

                }else {
                    Toast.makeText(Signupactivity.this,"Account Creation Failed!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
