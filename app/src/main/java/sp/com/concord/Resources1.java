package sp.com.concord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Resources1 extends AppCompatActivity {
    RecyclerView recyclerView;
    resadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources1);

        recyclerView=(RecyclerView)findViewById(R.id.resRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Resources> options =
                new FirebaseRecyclerOptions.Builder<Resources>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Resources"), Resources.class)
                        .build();

        adapter=new resadapter(options);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.resources_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.Mcontacts):
                Intent intent = new Intent(Resources1.this, UserContacts.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }





    public class resadapter extends FirebaseRecyclerAdapter<Resources, resadapter.myresholder> {
        private static final String TAG = "FARHAN";
        public resadapter(@NonNull FirebaseRecyclerOptions<Resources> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myresholder myresholder, @SuppressLint("RecyclerView") int i, @NonNull Resources resources) {
            myresholder.webName.setText(resources.getwName());
            myresholder.webDescription.setText(resources.getDescription());
            Glide.with(myresholder.webImage.getContext()).load(resources.getwImgUrl()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(myresholder.webImage);

            myresholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference refrence = getRef(i);
                    refrence.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("wName").getValue() != null) {

                                String url = snapshot.child("wUrl").getValue().toString();
                                Log.d(TAG,url);
                                Intent intent = new Intent(v.getContext(),Resources2.class);
                                intent.putExtra("url",url);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG,error.getMessage());

                        }
                    });
                }
            });
        }

        @NonNull
        @Override
        public myresholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_resources,parent,false);
            return new resadapter.myresholder(view);
        }

        class myresholder extends RecyclerView.ViewHolder{
            ImageView webImage;
            TextView webName, webDescription;
           public myresholder(@NonNull View itemView) {
               super(itemView);
               webImage = (ImageView) itemView.findViewById(R.id.resourceImg);
               webName = (TextView) itemView.findViewById(R.id.resourcesName);
               webDescription = (TextView) itemView.findViewById(R.id.resourcesDes);
           }
       }
    }
}