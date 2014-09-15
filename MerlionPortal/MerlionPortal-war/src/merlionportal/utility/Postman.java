/**
 * Postman
 *
 * @author Wen Xin
 */
package merlionportal.utility;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Postman {

    public static boolean sendMail(String from, String recipients[], String subject, String message) {
        boolean success = false;
        try {
            Message msg = null;
            Session session = null;
            Properties props = new Properties();
            InternetAddress sender = null;
            InternetAddress[] destination = null;
            props.put("mail.smtp.host", "mailc.nus.edu.sg");
            props.put("mail.smtp.port", "25");
            session = Session.getDefaultInstance(props);
            msg = new MimeMessage(session);
            sender = new InternetAddress(from);
            msg.setFrom(sender);
            destination = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                destination[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, destination);
            msg.setSubject(subject);
            msg.setContent(message, "text/html");
            Transport.send(msg);
            success = true; 
        } catch (Exception ex) {
            ex.printStackTrace();
            success = false;
        }
        return success;
    }
}
