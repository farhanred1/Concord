package sp.com.concord;

public class Approved {
    String docName, docEmail, docFrom, docTo, docDate, docId;

    public Approved() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocEmail() {
        return docEmail;
    }

    public void setDocEmail(String docEmail) {
        this.docEmail = docEmail;
    }

    public String getDocFrom() {
        return docFrom;
    }

    public void setDocFrom(String docFrom) {
        this.docFrom = docFrom;
    }

    public String getDocTo() {
        return docTo;
    }

    public void setDocTo(String docTo) {
        this.docTo = docTo;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public Approved(String docName, String docEmail, String docFrom, String docTo, String docDate, String docId) {
        this.docName = docName;
        this.docEmail = docEmail;
        this.docFrom = docFrom;
        this.docTo = docTo;
        this.docDate = docDate;
        this.docId = docId;
    }
}
