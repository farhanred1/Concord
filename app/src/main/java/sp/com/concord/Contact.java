package sp.com.concord;

public class Contact {
    String Cname, Ctel, Cdesc;

    public Contact() {
    }

    public Contact(String cname, String ctel, String cdesc) {
        Cname = cname;
        Ctel = ctel;
        Cdesc = cdesc;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public String getCtel() {
        return Ctel;
    }

    public void setCtel(String ctel) {
        Ctel = ctel;
    }

    public String getCdesc() {
        return Cdesc;
    }

    public void setCdesc(String cdesc) {
        Cdesc = cdesc;
    }
}
