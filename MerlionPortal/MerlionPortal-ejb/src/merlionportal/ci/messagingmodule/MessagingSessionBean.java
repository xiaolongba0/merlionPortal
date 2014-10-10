package merlionportal.ci.messagingmodule;

import entity.Message;
import entity.SystemUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class MessagingSessionBean {

    @PersistenceContext
    EntityManager em;
    Query q;

    public int findUser(String email) {
        q = em.createNamedQuery("SystemUser.findByEmailAddress").setParameter("emailAddress", email);
        if (q.getResultList().isEmpty()) {
            return -1;
        } else {
            SystemUser su = (SystemUser) q.getResultList().get(0);
            return su.getSystemUserId().intValue();
        }
    }
    
    public String findEmail(int id){
        SystemUser su = em.find(SystemUser.class, id);
        if(su != null){
            return su.getEmailAddress();
        }else{
            return "";
        }
    }

    //Return message id upon successful creation.
    public int createMessage(int senderId, int receiverId, String title, String body, int status) {
        int response = -1;
        try{
        Message msg = new Message();
        msg.setMessageTitle(title);
        msg.setMessageBody(body);
        msg.setSender(senderId);
        msg.setReceiver(receiverId);
        msg.setSentTime(new Date());
        msg.setStatus(status);
        msg.setMessageType("mail");
        em.persist(msg);
        em.flush();
        em.refresh(msg);
        response = msg.getMessageId();
        }catch(Exception e){
            e.printStackTrace();
        }
        return response;
    }

    public List<Message> getInbox(int customerId) {
        List<Message> inbox;
        q = em.createNamedQuery("Message.findByReceiver").setParameter("receiver", customerId);
        if (q.getResultList().isEmpty()) {
            inbox = new ArrayList<Message>();
        } else {
            inbox = q.getResultList();
        }
        return inbox;
    }
    
    public Message getMail(int mailid){
        return em.find(Message.class, mailid);
    }
}
