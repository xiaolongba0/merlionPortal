package merlionportal.managedbean.ci;

import entity.SystemUser;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.utility.MD5Generator;
import merlionportal.utility.Postman;
import org.primefaces.context.RequestContext;

@Named(value = "resetPasswordBean")
@ViewScoped
public class ResetPasswordManagedBean {

    private String email;

    @EJB
    private UserAccountManagementSessionBean uamsb;
    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private UserAccountManagementSessionBean uamb;
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

    public ResetPasswordManagedBean() {
    }

    @PostConstruct
    public void init() {

    }

    public void requestPasswordChange() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        System.out.println("==================1============");

        SystemUser user = uamsb.getUserByEmail(email);
        if (user != null) {
            System.out.println("==============2================");

            Random random = new Random();
            String newPassword = MD5Generator.hash(Integer.toString(random.nextInt(5000) + 1000)).substring(0, 15);
            System.out.println("===============3===============");

            if (user.getLocked()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Your account is locked", "Fail to update passwor"));
                System.out.println("==============4================");

            } else {
                //Set Password to user's password and save to database.
                if (uamsb.updateUserPassword(user.getSystemUserId(), MD5Generator.hash(newPassword))) {
                    System.out.println("============5==================");

                    //Send Email
                    String sender = "merlionportal@nus.edu.sg";
                    String[] recipients = {email};
                    String subject = "Password Reset";
                    System.out.println("===============6===============");

                    String content = "<h2>Hi user,</h2><p>You have request a password reset for your account at " + email + ". Please kindly use your new password below to login to your account.<br/><br/>"
                            + "Password: <strong>" + newPassword + "</strong><br/><br/>"
                            + " Thank you. <br/><br/>Best Regards,<br/>Administrator, Merlion Portal";
                    if (Postman.sendMail(sender, recipients, subject, content)) {
                        System.out.println("==============7================");

                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reset Password Success!", "Please check your email for your new password"));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Reset Password Failed!", "Unknown Error"));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Failed to update password", "Unknown Error"));
                }
            }
        } else {
            System.out.println("================8==============");
            //Not user found
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Email not found", "Please check if your email is entered correctly"));
        }
        systemLogSB.recordSystemLog(uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId")).getSystemUserId(), "CI User request to reset password");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
