package sp.com.concord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Appointments> arrayList;

    public EventRecyclerAdapter(Context context, ArrayList<Appointments> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_row_layout, parent, false );


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Appointments appointments = arrayList.get(position);
        holder.Event.setText("From: " + appointments.getFrom());
        holder.DateTxt.setText(appointments.getDate());
        holder.Time.setText("To: " + appointments.getTo());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView DateTxt, Event,Time;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DateTxt = itemView.findViewById(R.id.event_DATE);
            Event = itemView.findViewById(R.id.event_TIME_from);
            Time = itemView.findViewById(R.id.event_TIME_to);
        }
    }
}

