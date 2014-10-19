package merlionportal.managedbean.ci;

import entity.SystemUser;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;

@Named(value = "feedbackBean")
@ViewScoped
public class FeedbackManagedBean {

    @EJB
    private SystemLogSessionBean systemLogSB;

    @EJB
    private UserAccountManagementSessionBean uamsb;

    private Integer companyId;
    private Integer userId;
    private SystemUser loginedUser;

    @PostConstruct
    public void init() {
        userId = null;
        loginedUser = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            if (userId > 0) {
                loginedUser = uamsb.getUser(userId);
            }
        }
    }

    public void postFeedback() {

    }

    public void replyFeedback() {

    }

    public void likeFeedback() {

    }

    public SystemLogSessionBean getSystemLogSB() {
        return systemLogSB;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setSystemLogSB(SystemLogSessionBean systemLogSB) {
        this.systemLogSB = systemLogSB;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }
}
