package merlionportal.listener;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.mrp.productcatalogmodule.ProductSessionBean;
import merlionportal.utility.MD5Generator;

public class MerlionPortalContextListener implements ServletContextListener {

    @EJB
    UserAccountManagementSessionBean uamb;
    @EJB
    private ProductSessionBean productSessionBean;

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
        //create products

        productSessionBean.addNewProduct("Bicycle", "Bicycle", "Household Products", "Manufacturing", "US Dollar (USD)", 55.0, 12345);
       /* productSessionBean.addNewProduct("Water Bottle", "Water Bottle", "Household Products", "Manufacturing", "US Dollar (USD)", 10.0, 12345);
        productSessionBean.addNewProduct("Red Car", "Red Car", "Automotive", "Manufacturing", "US Dollar (USD)", 65555.0, 12345);
        productSessionBean.addNewProduct("Fabric Bag", "Fabric Bag", "Retail and Apparel", "Non-anufacturing", "US Dollar (USD)", 30.0, 12345);
        productSessionBean.addNewProduct("Phone", "Iphone", "Electronics", "Manufacturing", "US Dollar (USD)", 900.0, 12345);
        productSessionBean.addNewProduct("Pencil", "Pencil", "Household Products", "Manufacturing", "US Dollar (USD)", 5.0, 12345);
        productSessionBean.addNewComponent("Wheel", "Bicycle wheel", 2, "US Dollar (USD)", 30.0, 2, 333, "John", "65656565", "john@gmail.com", 12345, 1);
        productSessionBean.addNewComponent("Handlebars", "Bicycle handlebars", 1, "US Dollar (USD)", 15.0, 2, 111, "Cindy", "658555555", "cidy@gmail.com", 12345, 1);
        productSessionBean.addNewComponent("Pedal", "Bicycle pedal", 2, "US Dollar (USD)", 5.0, 2, 333, "John", "65656565", "john@gmail.com", 12345, 1);
        productSessionBean.addNewComponent("Frame", "Bicycle frame", 1, "US Dollar (USD)", 50.0, 2, 333, "David", "65656569", "david@gmail.com", 12345, 1);
        productSessionBean.addNewComponent("Saddle", "Bicycle saddle", 1, "US Dollar (USD)", 50.0, 2, 333, "David", "65656569", "david@gmail.com", 12345, 1);   */
   }

    private void startTMS() {

    }

    private void startWMS() {

    }

    private void startGRNS() {

    }

}
