package sp.com.concord;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList journal_id, journal_title, journal_entry, journal_date;

    CustomAdapter(Activity activity, Context context, ArrayList journal_id, ArrayList journal_title, ArrayList journal_entry, ArrayList journal_date){
        this.activity = activity;
        this.context = context;
        this.journal_id = journal_id;
        this.journal_title = journal_title;
        this.journal_entry = journal_entry;
        this.journal_date = journal_date;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_journal, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.journal_date_txt.setText(String.valueOf(journal_date.get(position)));
        holder.journal_title_txt.setText(String.valueOf(journal_title.get(position)));
        holder.journal_entry_txt.setText(String.valueOf(journal_entry.get(position)));

        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Journal3.class);
                intent.putExtra("id", String.valueOf(journal_id.get(position)));
                intent.putExtra("date", String.valueOf(journal_date.get(position)));
                intent.putExtra("title", String.valueOf(journal_title.get(position)));
                intent.putExtra("entry", String.valueOf(journal_entry.get(position)));

                activity.startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return journal_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView journal_title_txt, journal_entry_txt, journal_date_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            journal_title_txt = itemView.findViewById(R.id.titleD);
            journal_entry_txt = itemView.findViewById(R.id.entryD);
            journal_date_txt = itemView.findViewById(R.id.dateNtimeD);

            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.journal_anim);
            mainLayout.setAnimation(translate_anim);
        }

    }

}