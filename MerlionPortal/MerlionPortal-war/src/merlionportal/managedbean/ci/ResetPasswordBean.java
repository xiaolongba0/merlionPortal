package merlionportal.managedbean.ci;

import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import merlionportal.utility.MD5Generator;
import merlionportal.utility.Postman;
import org.primefaces.context.RequestContext;

@Named(value = "resetPasswordBean")
@ManagedBean
@RequestScoped
public class ResetPasswordBean {

    private String email;

    public ResetPasswordBean() {
    }

    @PostConstruct
    public void init() {

    }

    public void requestPasswordChange() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        //GENERATE NEW PASSWORD AND SAVE TO DATABASE
        Random random = new Random();
        String newPassword = MD5Generator.hash(Integer.toString(random.nextInt(5000) + 1000)).substring(0, 15);
        String sender = "merlionportal@nus.edu.sg";
        String[] recipients = {email};
        String subject = "Password Reset";

        String content = "<h2>Hi user,</h2><p>You have request a password reset for your account at " + email + ". Please kindly use your new password below to login to your account.<br/><br/>"
                + "Password; <strong>" + newPassword + "</strong><br/><br/>"
                + " Thank you. <br/><br/>Best Regards,<br/>Administrator, Merlion Portal";
        if (Postman.sendMail(sender, recipients, subject, content)) {
            requestContext.execute("success()");
        } else {
            requestContext.execute("fail()");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
