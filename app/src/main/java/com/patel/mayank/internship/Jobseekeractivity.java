package com.patel.mayank.internship;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class Jobseekeractivity extends Fragment {

    private FirebaseAuth raut;
    private FirebaseAuth.AuthStateListener mautListner;

    ArrayList<Jobs> arrayList;
    ListAdapter lstadpt;
    ListView lstjob;

    String uid = "";

    public Jobseekeractivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        getJobs();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_jobseekeractivity,container,false);

        lstjob = (ListView) view.findViewById(R.id.lst_jobs);

        return view;
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

    public void getJobs()
    {
        arrayList = new ArrayList<>();
        FirebaseDatabase fd  = FirebaseDatabase.getInstance();
        DatabaseReference db = fd.getReference("Intership");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds  : dataSnapshot.getChildren())
                {
                    String tit = ds.child("title").getValue().toString();
                    String loc = ds.child("location").getValue().toString();
                    String id = ds.getKey();

                    System.out.println("Jobs "+tit+" "+loc);
                    arrayList.add(new Jobs(id,tit,loc));
                }


              lstadpt = new ListAdapter(arrayList,getActivity());
                lstjob.setAdapter(lstadpt);

                lstjob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //String key = ((TextView) view.findViewById(R.id.txt_name)).getText().toString();

                        String key =arrayList.get(position).getId();

                        JobDetailActivity jobDetailActivity = new JobDetailActivity();
                        Bundle argument = new Bundle();
                        argument.putString("keyId",key);
                        jobDetailActivity.setArguments(argument);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.add(R.id.jobseekcon,jobDetailActivity,"NewFragmentTag");
                        ft.addToBackStack(null);
                        ft.hide(Jobseekeractivity.this);
                        ft.commit();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
