package be.valuya.comptoir.ws.rest.api.domain.notification;

import be.valuya.comptoir.ws.rest.api.util.ZonedDateTimeXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class NotificationOptions {

    private String title;
    private String dir = "auto";
    private String lang = "";
    private String body = "";
    private String tag = "";
    private String image;
    private String icon;
    private String badge;
    private Integer[] vibrate;
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime timestamp;
    private boolean renotify;
    private boolean silent;
    private boolean requireInteraction;
    private Object data;
    private List<NotificationAction> actions = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public Integer[] getVibrate() {
        return vibrate;
    }

    public void setVibrate(Integer[] vibrate) {
        this.vibrate = vibrate;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRenotify() {
        return renotify;
    }

    public void setRenotify(boolean renotify) {
        this.renotify = renotify;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public boolean isRequireInteraction() {
        return requireInteraction;
    }

    public void setRequireInteraction(boolean requireInteraction) {
        this.requireInteraction = requireInteraction;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<NotificationAction> getActions() {
        return actions;
    }

    public void setActions(List<NotificationAction> actions) {
        this.actions = actions;
    }
}
