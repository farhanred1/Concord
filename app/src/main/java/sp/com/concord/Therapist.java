package sp.com.concord;

public class Therapist {
    public String name, email, passwd, phone, specialisation, type, imgUrl;
    public int ttl_patients;

    public Therapist() {

    }

    public Therapist(String name,String email,String passwd, String phone, String specialisation, String type, String imgUrl, int ttl_patients) {
        this.name = name;
        this.email = email;
        this.passwd = passwd;
        this.phone = phone;
        this.specialisation = specialisation;
        this.imgUrl = imgUrl;
        this.type = type;
        this.ttl_patients = ttl_patients;

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }
}
