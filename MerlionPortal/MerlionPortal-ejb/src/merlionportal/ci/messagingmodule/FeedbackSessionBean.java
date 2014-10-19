package merlionportal.ci.messagingmodule;

import entity.FeedBack;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

@Stateless
@LocalBean
public class FeedbackSessionBean {

    public int createFeedback(int creatorId, String content){
        FeedBack fb = new FeedBack();
        return -1;
    }
    
    public int addLike(int id,int threadType){
        return -1;
    }
    
    public int createComment(int creatorId, String content, int feedbackId){
        return -1;
    }
    
}
