package sp.com.concord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class UserContacts extends AppCompatActivity {
    RecyclerView contactView;


    contactsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contacts);
        contactView = (RecyclerView) findViewById(R.id.contactList);
        contactView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Contact> options =
                new FirebaseRecyclerOptions.Builder<Contact>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Contacts"), Contact.class)
                        .build();


        adapter=new contactsAdapter(options);
        contactView.setAdapter(adapter);
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

    public class contactsAdapter extends FirebaseRecyclerAdapter<Contact, contactsAdapter.contactsHolder>{

        public contactsAdapter(@NonNull FirebaseRecyclerOptions<Contact> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull contactsHolder contactsHolder, int i, @NonNull Contact contact) {
            contactsHolder.name.setText(contact.Cname);
            contactsHolder.desc.setText(contact.Cdesc);
            contactsHolder.tel.setText(contact.Ctel);

        }

        @NonNull
        @Override
        public contactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact,parent,false);
            return new contactsHolder(view);
        }

        class contactsHolder extends RecyclerView.ViewHolder {
            TextView name, desc, tel;

            public contactsHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.contactName);
                desc = (TextView) itemView.findViewById(R.id.contactDesc);
                tel = (TextView) itemView.findViewById(R.id.contactTel);
            }
        }
    }

}