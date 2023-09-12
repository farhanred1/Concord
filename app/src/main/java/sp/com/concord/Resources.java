package sp.com.concord;

public class Resources {
    public String description, wName, wImgUrl, wUrl;

    public Resources() {

    }


    public Resources(String description, String wName, String wImgUrl, String wUrl) {
        this.description = description;
        this.wName = wName;
        this.wImgUrl = wImgUrl;
        this.wUrl = wUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getwName() {
        return wName;
    }

    public void setwName(String wName) {
        this.wName = wName;
    }

    public String getwImgUrl() {
        return wImgUrl;
    }

    public void setwImgUrl(String wImgUrl) {
        this.wImgUrl = wImgUrl;
    }

    public String getwUrl() {
        return wUrl;
    }

    public void setwUrl(String wUrl) {
        this.wUrl = wUrl;
    }
}
