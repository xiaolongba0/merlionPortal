package merlionportal.managedbean.ci;

import entity.Comment;
import entity.FeedBack;
import entity.SystemUser;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.ci.messagingmodule.FeedbackSessionBean;
import org.primefaces.context.RequestContext;

@Named(value = "feedbackBean")
@ViewScoped
public class FeedbackManagedBean {

    @EJB
    private SystemLogSessionBean systemLogSB;

    @EJB
    private UserAccountManagementSessionBean uamsb;

    @EJB
    private FeedbackSessionBean fsb;

    private Integer companyId;
    private Integer userId;
    private SystemUser loginedUser;
    private List<FeedBack> feedbackList;
    private ExternalContext ec;

    //Form
    private String feedBackTitle;
    private String feedBackContent;
    private String commentContent;

    //Types
    private int TYPE_FEEDBACK = 100;
    private int TYPE_COMMENT = 200;

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        userId = null;
        loginedUser = null;
        if (ec.getSessionMap().containsKey("userId")) {
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            if (userId > 0) {
                loginedUser = uamsb.getUser(userId);
            }
        }
        onLoad();
    }

    public void onLoad() {
        this.setFeedbackList(fsb.getFeedBacks());
    }

    public void postFeedback() {
        if (fsb.createFeedback(userId, feedBackTitle, feedBackContent) > -1) {
            RequestContext.getCurrentInstance().execute("postCreateFeedback()");
        }
        onLoad();
    }

    public void postComment() {
        int id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fbid"));
        if (userId == null) {
            RequestContext.getCurrentInstance().execute("noUserFound()");
        } else {
            String commentMsg = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("commentInput-" + Integer.toString(id));
            if (fsb.createComment(userId, commentMsg, id) > -1) {
                RequestContext.getCurrentInstance().execute("commentCreated()");
                onLoad();
            }
        }
    }

    public void likeFeedback() {
        int id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fbid"));
        if (userId == null) {
            RequestContext.getCurrentInstance().execute("noUserFound()");
        } else {
            if (fsb.addLike(id, TYPE_FEEDBACK) > -1) {
                FeedBack fb = fsb.getFeedBack(id);
                RequestContext.getCurrentInstance().execute("feedbackLiked('" + fb.getTitle() + "')");
                onLoad();
            }
        }
    }

    public void likeComment() {
        int id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("commentid"));
        if (userId == null) {
            RequestContext.getCurrentInstance().execute("noUserFound()");
        } else {
            if (fsb.addLike(id, TYPE_COMMENT) > -1) {
                RequestContext.getCurrentInstance().execute("commentLiked()");
                onLoad();
            }
        }
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

    public int getTYPE_FEEDBACK() {
        return TYPE_FEEDBACK;
    }

    public void setTYPE_FEEDBACK(int TYPE_FEEDBACK) {
        this.TYPE_FEEDBACK = TYPE_FEEDBACK;
    }

    public int getTYPE_COMMENT() {
        return TYPE_COMMENT;
    }

    public void setTYPE_COMMENT(int TYPE_COMMENT) {
        this.TYPE_COMMENT = TYPE_COMMENT;
    }

    public List<FeedBack> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<FeedBack> feedbackList) {
        this.feedbackList = feedbackList;
    }

    public String getFeedBackTitle() {
        return feedBackTitle;
    }

    public void setFeedBackTitle(String feedBackTitle) {
        this.feedBackTitle = feedBackTitle;
    }

    public String getFeedBackContent() {
        return feedBackContent;
    }

    public void setFeedBackContent(String feedBackContent) {
        this.feedBackContent = feedBackContent;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
