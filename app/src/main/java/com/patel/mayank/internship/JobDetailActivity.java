package com.patel.mayank.internship;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class JobDetailActivity extends Fragment {

    String key;

    TextView txt_title,txt_desc,txt_loc,txt_respon,txt_crlvl,txt_skil,txt_cat;
    ImageButton apply_btn;

    String uid = "";

    FirebaseDatabase fd = FirebaseDatabase.getInstance();
    DatabaseReference db = fd.getReference("Jobseekers");

    ArrayList<Integer> ids;

    int mx;

    public JobDetailActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_job_detail);

        Bundle arguments = getArguments();

        key = arguments.getString("keyId");

        getMaxid();
        uid = getUid();
        setData();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_job_detail,container,false);

        txt_title = (TextView) view.findViewById(R.id.jobd_title);
        txt_loc = (TextView) view.findViewById(R.id.jobd_location);
        txt_desc = (TextView) view.findViewById(R.id.jobd_desc);
        txt_respon = (TextView) view.findViewById(R.id.jobd_responsibilities);
        txt_crlvl = (TextView) view.findViewById(R.id.jobd_crlevel);
        txt_skil = (TextView) view.findViewById(R.id.jobd_skills);
        txt_cat = (TextView) view.findViewById(R.id.jobd_category);
        apply_btn = (ImageButton) view.findViewById(R.id.applied_btn);


        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jobApply();
                Toast.makeText(getActivity(),"Successfully Applied!",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    public void setData()
    {
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference db = fd.getReference("Intership");

        db.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String title,desc,location,respns,skill,crlvl,cat;

                title = dataSnapshot.child("title").getValue().toString();
                desc = dataSnapshot.child("desc").getValue().toString();
                crlvl = dataSnapshot.child("career_level").getValue().toString();
                location = dataSnapshot.child("location").getValue().toString();
                respns = dataSnapshot.child("responsbility").getValue().toString();
                skill = dataSnapshot.child("skill").getValue().toString();
                cat = dataSnapshot.child("category").getValue().toString();

                txt_title.setText("Title : " +title);
                txt_desc.setText("Description : "+desc);
                txt_crlvl.setText("Career : " + crlvl);
                txt_loc.setText("Location : "+location);
                txt_respon.setText("Responsibility : "+respns);
                txt_skil.setText("Skills : "+skill);
                txt_cat.setText("Category : "+cat);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void jobApply()
    {

        if(ids != null && !ids.isEmpty())
        {
            mx = Collections.max(ids);
            mx++;
        }else
        {
            mx = 0;
        }

        String inkey = String.valueOf(mx);

        System.out.println("Check Max id "+inkey);

        db.child(key).child(inkey).setValue(uid);

    }

    public String getUid() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        System.out.println("From GetUid Method  "+id);

        return id;
    }

    public void  getMaxid()
    {
        ids = new ArrayList<Integer>();

        db.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    System.out.println("Check ids  "+ds.getKey());

                    ids.add(Integer.parseInt(ds.getKey()));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
