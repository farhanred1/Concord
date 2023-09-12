package sp.com.concord;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyGridAdapter extends ArrayAdapter {
    private static final String TAG = "FARHAN";

    List<Date> dates;
    Calendar currentDate;
    List<Appointments> appointments;
    LayoutInflater inflater;

    public MyGridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Appointments> appointments) {
        super(context, R.layout.single_cell);

        this.dates = dates;
        this.currentDate = currentDate;
        this.appointments = appointments;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentdate = currentDate.get(Calendar.DAY_OF_MONTH);


        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.single_cell,parent,false);
        }

        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.white));

        } else {
            view.setVisibility(View.INVISIBLE);//view.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }
        Calendar newcalendar = Calendar.getInstance();
        if(currentdate == DayNo && (displayMonth == newcalendar.get(Calendar.MONTH)+1) && (currentYear==newcalendar.get(Calendar.YEAR))){
            view.setBackgroundColor(Color.parseColor("#64A9FA"));
        }



        TextView Day_Number = view.findViewById(R.id.calendar_day);
        TextView EventNumber = view.findViewById(R.id.events_id);
        Day_Number.setText(String.valueOf(DayNo));
        Calendar eventCalendar = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i =0; i<appointments.size();i++){
            eventCalendar.setTime(ConvertStringToDate(appointments.get(i).getDate()));
            if( DayNo == eventCalendar.get(Calendar.DAY_OF_MONTH) && (displayMonth == eventCalendar.get(Calendar.MONTH)+1) && displayYear == eventCalendar.get(Calendar.YEAR)) {
                arrayList.add(appointments.get(i).getFrom());
                EventNumber.setText(arrayList.size()+" Events");
                String no = String.valueOf(arrayList.size()) + ", day" + String.valueOf(DayNo);
                Log.d(TAG, no);

            }

        }

        return view;

    }

    private Date ConvertStringToDate (String eventDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


    @Override
    public int getCount() {
        return dates.size();
    }


    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }



    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }
}
