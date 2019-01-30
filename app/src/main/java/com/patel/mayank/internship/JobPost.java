package com.patel.mayank.internship;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class JobPost extends Fragment {


    public JobPost() {
    }

    String uid = "";
    EditText edt_title,edt_loc,edt_desc,edt_carelvl,edt_repons,edt_skill;
    Spinner cat_spin;
    ArrayList<Integer> interid;
    Button btn_jobpost;
    ArrayList<String> catarray;

    FirebaseDatabase fd = FirebaseDatabase.getInstance();
    DatabaseReference db = fd.getReference("Intership");

    int mx;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getMaxId();
        uid = getUid();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_job_post,container,false);

        edt_title = (EditText) view.findViewById(R.id.job_title);
        edt_loc = (EditText) view.findViewById(R.id.job_location);
        edt_desc = (EditText) view.findViewById(R.id.job_desc);
        edt_carelvl = (EditText) view.findViewById(R.id.job_reqcarlevel);
        edt_repons = (EditText) view.findViewById(R.id.job_respons);
        edt_skill = (EditText) view.findViewById(R.id.job_skills);
        cat_spin = (Spinner) view.findViewById(R.id.jobpost_catspin);

        btn_jobpost = (Button) view.findViewById(R.id.job_postbtn);


        getCategories();

        btn_jobpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postJob();

            }
        });

        return view;
    }

    public String getUid() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        System.out.println("From GetUid Method  "+id);

        return id;
    }

    public void postJob()
    {
        if(interid != null && !interid.isEmpty())
        {
            mx = Collections.max(interid);
            mx++;
        }else
        {
            mx = 1000;
        }


        String tit,desc,loc,skill,crlvl,respon,cat;

        tit = edt_title.getText().toString();
        desc = edt_desc.getText().toString();
        loc = edt_loc.getText().toString();
        skill = edt_skill.getText().toString();
        crlvl = edt_carelvl.getText().toString();
        respon = edt_repons.getText().toString();


        cat = cat_spin.getSelectedItem().toString();

        if (cat.equals("Select Category"))
        {
            Toast.makeText(getActivity(),"Please Selectd Category!",Toast.LENGTH_LONG).show();
        }else
        {
            String cnt = String.valueOf(mx);
            db.child(cnt).setValue(new Jobs(tit,loc,crlvl,desc,uid,respon,skill,cat));

            Toast.makeText(getActivity(),"Job Successfully Posted!",Toast.LENGTH_LONG).show();
            clearField();
        }


    }

    public void getMaxId()
    {
        interid = new ArrayList<Integer>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    System.out.println("Check this "+ds.getKey());
                    interid.add(Integer.parseInt(ds.getKey()));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clearField()
    {
        edt_title.setText("");
        edt_loc.setText("");
        edt_desc.setText("");
        edt_repons.setText("");
        edt_carelvl.setText("");
        edt_skill.setText("");
    }


    public void getCategories()
    {
        catarray = new ArrayList<>();
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference db = fd.getReference("Category");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    catarray.add(ds.getValue().toString());
                }

                SpinAdapter spnadpt = new SpinAdapter(catarray,getActivity());
                cat_spin.setAdapter(spnadpt);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
