package merlionportal.listener;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import merlionportal.ci.administrationmodule.UserAccountManagementBean;
import merlionportal.utility.MD5Generator;

public class MerlionPortalContextListener implements ServletContextListener {

    @EJB
    UserAccountManagementBean uamb;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        startCI();
        startOES();
        startCRMS();
        startMRP();
        startTMS();
        startWMS();
        startGRNS();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    private void startCI() {
        if (uamb.checkSuperUser()) {
            int companyid = uamb.registerNewCompany("Merlion Portal", "NUS Computing Drive 1", "6516 6731", "Tan Wee Kek", "tanwk@comp.nus.edu.sg", "Merlion Portal", 0);
            uamb.createSuperUser(MD5Generator.hash("pass"), companyid);
        }
    }

    private void startOES() {

    }

    private void startCRMS() {

    }

    private void startMRP() {

    }

    private void startTMS() {

    }

    private void startWMS() {

    }

    private void startGRNS() {

    }

}
