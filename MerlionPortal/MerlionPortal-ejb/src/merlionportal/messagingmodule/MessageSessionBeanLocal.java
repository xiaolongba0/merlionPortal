package merlionportal.messagingmodule;

import javax.ejb.Local;

@Local
public interface MessageSessionBeanLocal {
    public void createMessage(String title, String re, String message);
}
