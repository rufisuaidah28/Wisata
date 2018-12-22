package com.example.alhuzwiri.wisata;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
public class Main6Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecycleviewAdapter adapter;
    private ArrayList<post> mahasiswaArrayList;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        this.db = FirebaseFirestore.getInstance();
        this.mahasiswaArrayList = new ArrayList<>();
        addData();
        recyclerView = (RecyclerView) findViewById(R.id.recyle);

        adapter = new RecycleviewAdapter(mahasiswaArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Main6Activity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }
    void addData(){
        db.collection("content")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mahasiswaArrayList.clear();
                            for (QueryDocumentSnapshot content : task.getResult()) {
                                mahasiswaArrayList.add(new post(
                                       content.getString("UserId"),
                                       content.getString("namee"),
                                       content.getString("desc"),
                                       content.getString("time")
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
