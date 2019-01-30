package com.patel.mayank.internship;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Jobposted extends Fragment {



    ListView lstjoblist ;
    ArrayList<Jobs> arrayList;
    ListAdapter lstadpt;

    private FirebaseAuth raut;
    private FirebaseAuth.AuthStateListener mautListner;

    String uid = "";

    public Jobposted() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_jobposted);

        raut = FirebaseAuth.getInstance();

        uid = getUid();

        getJobs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_jobposted,container,false);

        lstjoblist = (ListView) view.findViewById(R.id.jobposted_list);

        return view;
    }

    public void getJobs()
    {
        arrayList = new ArrayList<>();

        FirebaseDatabase fd  = FirebaseDatabase.getInstance();
        DatabaseReference db = fd.getReference("Intership");

        db.addValueEventListener(new ValueEventListener() {
            String tit,loc,id;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds  : dataSnapshot.getChildren())
                {
                    String eid = ds.child("employer_id").getValue().toString();

                    System.out.println("Uid "+eid+" user "+uid);

                    if(eid.equals(uid))
                    {
                         tit = ds.child("title").getValue().toString();
                        loc = ds.child("location").getValue().toString();
                        id = ds.getKey();

                        arrayList.add(new Jobs(id,tit,loc));

                    }

                }

                lstadpt = new ListAdapter(arrayList,getActivity());
                lstjoblist.setAdapter(lstadpt);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getUid() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        System.out.println("From GetUid Method  "+id);

        return id;
    }
}
