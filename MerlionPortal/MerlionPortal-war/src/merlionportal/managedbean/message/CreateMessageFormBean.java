package merlionportal.managedbean.message;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

@Named(value = "messageFormBean")
@RequestScoped
public class CreateMessageFormBean {

    private String title;
    private String recipient;
    private String message;

    public CreateMessageFormBean() {
    }

    @PostConstruct
    public void init() {
    }

    public void sendMessage() {

    }

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    //</editor-fold>
}
