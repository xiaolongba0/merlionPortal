package merlionportal.managedbean.ci;

import entity.SystemUser;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.utility.MD5Generator;
import merlionportal.utility.Postman;
import org.primefaces.context.RequestContext;

@Named(value = "resetPasswordBean")
@RequestScoped
public class ResetPasswordBean {

    private String email;

    @EJB
    private UserAccountManagementSessionBean uamsb;
//    1. check that email exists in database
//2. get the user from the database via email call ejb bean to get user
//3. generate new password
//4. set new password for the user -> ejb method
//5. send email
//
//2a. cannot find user
//3. prompt user for error
//
//2b. user account is locked
//3. prompt user for error

    public ResetPasswordBean() {
    }

    @PostConstruct
    public void init() {

    }

    public void requestPasswordChange() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        SystemUser user = uamsb.getUserByEmail(email);
        if (user != null) {
            Random random = new Random();
            String newPassword = MD5Generator.hash(Integer.toString(random.nextInt(5000) + 1000)).substring(0, 15);

            if (user.getLocked()) {
                requestContext.execute("fail('Your account is locked. Fail to update password')");
            } else {
                //Set Password to user's password and save to database.
                if (uamsb.updateUserPassword(user.getSystemUserId(), newPassword)) {
                    //Send Email
                    String sender = "merlionportal@nus.edu.sg";
                    String[] recipients = {email};
                    String subject = "Password Reset";

                    String content = "<h2>Hi user,</h2><p>You have request a password reset for your account at " + email + ". Please kindly use your new password below to login to your account.<br/><br/>"
                            + "Password; <strong>" + newPassword + "</strong><br/><br/>"
                            + " Thank you. <br/><br/>Best Regards,<br/>Administrator, Merlion Portal";
                    if (Postman.sendMail(sender, recipients, subject, content)) {
                        requestContext.execute("success()");
                    } else {
                        requestContext.execute("fail('Unknown Error')");
                    }
                } else {
                    requestContext.execute("fail('Fail to update password')");
                }
            }
        } else {
            //Not user found
            requestContext.execute("fail('Email not found')");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
