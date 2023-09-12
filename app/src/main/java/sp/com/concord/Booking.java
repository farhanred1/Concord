package sp.com.concord;

public class Booking {
    String bName, bEmail, bFrom, bTo, bDate, bookerid;

    public Booking() {
    }



    public Booking(String bName, String bEmail, String bFrom, String bTo, String bDate, String bookerid) {
        this.bName = bName;
        this.bEmail = bEmail;
        this.bFrom = bFrom;
        this.bTo = bTo;
        this.bDate = bDate;
        this.bookerid = bookerid;
    }

    public String getBookerid() {
        return bookerid;
    }

    public void setBookerid(String bookerid) {
        this.bookerid = bookerid;
    }
    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbEmail() {
        return bEmail;
    }

    public void setbEmail(String bEmail) {
        this.bEmail = bEmail;
    }

    public String getbFrom() {
        return bFrom;
    }

    public void setbFrom(String bFrom) {
        this.bFrom = bFrom;
    }

    public String getbTo() {
        return bTo;
    }

    public void setbTo(String bTo) {
        this.bTo = bTo;
    }

    public String getbDate() {
        return bDate;
    }

    public void setbDate(String bDate) {
        this.bDate = bDate;
    }
}
