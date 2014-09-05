package merlionportal.messagingmodule;

import entity.Message;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MessageSessionBean implements MessageSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    
    @Override
    public void createMessage(String title, String re, String message) {
        Message m = new Message();
        em.persist(m);
    }

}
