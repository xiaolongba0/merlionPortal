package merlionportal.managedbean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import merlionportal.messagingmodule.MessageSessionBean;

@ManagedBean
@RequestScoped
public class CreateMessageFormBean {

    private String title;
    private String recipient;
    private String message;
    
    @EJB
    MessageSessionBean messageSessionBean;

    public CreateMessageFormBean() {
    }

    @PostConstruct
    public void init() {
    }

    public void sendMessage() {
        messageSessionBean.createMessage(title,recipient,message);
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
