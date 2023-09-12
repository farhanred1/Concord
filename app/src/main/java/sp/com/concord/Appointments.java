package sp.com.concord;

public class Appointments {

    String from, to, date, month, year;

    public Appointments() {

    }

    public Appointments(String from, String to, String date, String month, String year) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
