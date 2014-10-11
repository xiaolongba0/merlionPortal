package merlionportal.managedbean.message;

import entity.Message;
import entity.SystemUser;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.messagingmodule.MessagingSessionBean;
import merlionportal.utility.MessagingStatus;
import org.primefaces.context.RequestContext;

@Named(value = "messagerBean")
@ViewScoped
public class MessageManagedBean {

    private List<Message> inbox;
    private String receipentEmail;
    private SystemUser loginedUser;
    private String subject;
    private String content;

    @EJB
    UserAccountManagementSessionBean uamb;

    @EJB
    private MessagingSessionBean msb;

    public MessageManagedBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        Integer userId;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            loginedUser = uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            if (loginedUser != null) {
                redirect = false;

                //Set Inbox
                inbox = msb.getInbox(userId);

            }
        }
        if (redirect) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void verifyEmail() {
        String email = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("emailAddress");
        int userid = msb.findUser(email);
        String functionCall = "verifyEmail(" + userid + ")";
        RequestContext.getCurrentInstance().execute(functionCall);
    }

    public void readMail() {
        int mailid = -1;
        try {
            mailid = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectmsgid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mailid > -1) {
            Message m = msb.getMail(mailid);
            if (m != null) {
                String s = m.getMessageBody().replace("\n", " ");
                String functionCall = "postReadMail('" + m.getMessageTitle() + "','" + s + "','" + msb.findEmail(m.getSender()) + "')";

                RequestContext.getCurrentInstance().execute(functionCall);
            }
        }
    }

    public void sendEmail() {
        int receipentid = -1;
        int success = -1;
        try {
            receipentid = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("receipentid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (receipentid > -1) {
            String subject = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("subject");
            String content = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("content");
            success = msb.createMessage(loginedUser.getSystemUserId(), receipentid, subject, content, MessagingStatus.MAIL_UNREAD);
        }
        String functionCall = "postEmail(" + success + ")";
        RequestContext.getCurrentInstance().execute(functionCall);
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<Message> getInbox() {
        return inbox;
    }

    public void setInbox(List<Message> inbox) {
        this.inbox = inbox;
    }

    public String getReceipentEmail() {
        return receipentEmail;
    }

    public void setReceipentEmail(String receipentEmail) {
        this.receipentEmail = receipentEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }
//</editor-fold>
}
