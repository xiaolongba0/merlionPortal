/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.oes.reportmanagementmodule;

import entity.ProductOrder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mac
 */
@Stateless
@LocalBean
public class MonthlyReportManagerSessionBean {

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public MonthlyReportManagerSessionBean() {
    }

    public List<ProductOrder> findAllProduct(int companyId, int currenttMonth) {
        Query q = em.createQuery("SELECT p FROM ProductOrder p WHERE p.companyId =:companyId").setParameter("companyId", companyId);
        Boolean result;
        Boolean validity = false;
        Date orderDate;
        List<ProductOrder> orderList = new ArrayList();

        for (Object o : q.getResultList()) {
            ProductOrder order = (ProductOrder) o;
            validity = this.checkIfValidOrder(order);
            orderDate = order.getCreatedDate();
            result = this.compareDate(currenttMonth, orderDate);
            if (result && validity) {
                orderList.add(order);
            }
        }
        return orderList;
    }

    public Double getTotalValueofDay(List<ProductOrder> orderList, int day) {
        Double result = 0.0;
        Date date1;
        Calendar cal = Calendar.getInstance();
        if (orderList != null) {
            for (Object o : orderList) {
                ProductOrder myOrder = (ProductOrder) o;
                date1 = myOrder.getCreatedDate();
                cal.setTime(date1);
                int orderDay = cal.get(Calendar.DAY_OF_MONTH);
                if (orderDay == day) {
                    result += myOrder.getPrice();
                }
            }
        }
        return result;

    }

    public int getTotalNumberOfDays(int month, int year) {
        int result = 0;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            result = 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            result = 30;
        } else if (month == 2 && year % 4 == 0) {
            result = 29;
        } else {
            result = 28;
        }
        return result;
    }

    private Boolean checkIfValidOrder(ProductOrder order) {
        Boolean result = false;
        int status = order.getStatus();
        if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {
            result = true;
        }
        return result;
    }

    private Boolean compareDate(int d1, Date d2) {
        Boolean result = true;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d2);
        int orderMonth = cal1.get(Calendar.MONTH);
        if (d1 != orderMonth) {
            result = false;
        }
        return result;
    }
}
