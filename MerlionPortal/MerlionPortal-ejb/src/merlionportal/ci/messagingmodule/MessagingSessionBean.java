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

    public String findEmail(int id) {
        SystemUser su = em.find(SystemUser.class, id);
        if (su != null) {
            return su.getEmailAddress();
        } else {
            return "";
        }
    }

    //Return message id upon successful creation.
    public int createMessage(int senderId, int receiverId, String title, String body, int status) {
        int response = -1;
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public List<Message> getInbox(int customerId) {
        List<Message> inbox;
        q = em.createQuery("SELECT m FROM Message m WHERE m.receiver = :receiver AND m.status != :status ORDER BY m.sentTime DESC").setParameter("receiver", customerId).setParameter("status", 9992);
        if (q.getResultList().isEmpty()) {
            inbox = new ArrayList<Message>();
        } else {
            inbox = q.getResultList();
        }
        return inbox;
    }
     public List<Message> getOutbox(int customerId) {
        List<Message> inbox;
        q = em.createQuery("SELECT m FROM Message m WHERE m.sender = :sender AND m.status != :status ORDER BY m.sentTime DESC").setParameter("sender", customerId).setParameter("status", 9992);
        if (q.getResultList().isEmpty()) {
            inbox = new ArrayList<Message>();
        } else {
            inbox = q.getResultList();
        }
        return inbox;
    }


    public Message getMail(int mailid) {
        return em.find(Message.class, mailid);
    }

    public void changeMailStatus(int mailid, int status) {
        Message m = em.find(Message.class, mailid);
        if (m != null) {
            m.setStatus(status);
            em.merge(m);
            em.flush();
            em.refresh(m);
        }
    }

    public int findCompany(int userid) {
        SystemUser su = em.find(SystemUser.class, userid);
        if (su != null) {
            return su.getCompanycompanyId().getCompanyId();
        } else {
            return -1;
        }
    }
    public int getNumberOfUnreadMessages(Integer userId){
        Query q = em.createNamedQuery("Message.findByReceiver").setParameter("receiver", userId);
        int count = 0;
        for(Message m: (List<Message>)q.getResultList()){
            if(m.getStatus()==9990){
                count++;
            }
        }
        return count;
    }
}
