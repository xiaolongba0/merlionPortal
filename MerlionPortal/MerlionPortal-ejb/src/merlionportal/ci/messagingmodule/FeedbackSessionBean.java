package merlionportal.ci.messagingmodule;

import entity.Comment;
import entity.FeedBack;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class FeedbackSessionBean {

    @PersistenceContext
    EntityManager em;

    public int createFeedback(int creatorId, String title, String content) {
        FeedBack fb = new FeedBack();
        fb.setTitle(title);
        fb.setContent(content);
        fb.setPoster(creatorId);
        fb.setCreateDate(new Date());
        fb.setLikes(0);

        try {
            em.persist(fb);
            em.flush();
            em.refresh(fb);
            return fb.getFeedbackId();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int addLike(int id, int threadType) {

        //Feedback
        if (threadType == 100) {
            FeedBack fb = em.find(FeedBack.class, id);
            if (fb != null) {
                int likes = (fb.getLikes() == null) ? 0 : fb.getLikes();
                likes += 1;
                fb.setLikes(likes);
                em.merge(fb);
                em.flush();
                em.refresh(fb);
                return fb.getFeedbackId();
            }

        } //Comments
        else if (threadType == 200) {
            Comment c = em.find(Comment.class, id);
            if (c != null) {
                int likes = (c.getLikes() == null) ? 0 : c.getLikes();
                likes += 1;
                c.setLikes(likes);
                em.merge(c);
                em.flush();
                em.refresh(c);
                return c.getCommentId();
            }
        }
        return -1;
    }

    public int createComment(int creatorId, String content, int feedbackId) {
        FeedBack fb = em.find(FeedBack.class, feedbackId);
        if (fb != null) {
            Comment c = new Comment();
            c.setCommentBody(content);
            c.setLikes(0);
            c.setPoster(creatorId);
            c.setCreateDate(new Date());
            c.setFeedback(fb);
            em.persist(c);
            em.flush();
            em.refresh(c);
            return c.getCommentId();
        }
        return -1;
    }

    public List<FeedBack> getFeedBacks() {
        List<FeedBack> fbList = em.createNamedQuery("FeedBack.findAll").getResultList();
        List<FeedBack> resList = new ArrayList<>();
        for (FeedBack fb : fbList) {
            em.refresh(fb);
            resList.add(fb);
        }

        return resList;
    }

    public FeedBack getFeedBack(int id) {
        return em.find(FeedBack.class, id);
    }

    public Comment getComment(int id) {
        return em.find(Comment.class, id);
    }
}
