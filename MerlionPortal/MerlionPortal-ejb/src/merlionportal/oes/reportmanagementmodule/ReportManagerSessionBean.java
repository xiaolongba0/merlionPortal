package merlionportal.oes.reportmanagementmodule;

import entity.ProductOrder;
import entity.Quotation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class ReportManagerSessionBean {

    public ReportManagerSessionBean() {
    }

    @PersistenceContext(unitName = "MerlionPortal-ejbPU")
    private EntityManager em;

    public List<ProductOrder> findAllProduct(int companyId, Date start, Date end) {
        Query q = em.createQuery("SELECT p FROM ProductOrder p WHERE p.companyId =:companyId").setParameter("companyId", companyId);
        Boolean result;
        Boolean validity = false;
        Date orderDate;
        List<ProductOrder> orderList = new ArrayList();

        for (Object o : q.getResultList()) {
            ProductOrder order = (ProductOrder) o;
            validity = this.checkIfValidOrder(order);
            orderDate = order.getCreatedDate();
            result = this.compareDate(start, orderDate, end);
            if (result && validity) {
                orderList.add(order);
            }
        }
        return orderList;
    }

    public List<ProductOrder> findAllProduct(int companyId, Date start, Date end, int cId) {
        Query q = em.createQuery("SELECT p FROM ProductOrder p WHERE p.companyId =:companyId AND p.creatorId =:customerId").setParameter("companyId", companyId);
        q.setParameter("customerId", cId);
        Boolean result;
        Boolean validity = false;
        Date orderDate;
        List<ProductOrder> orderList = new ArrayList();

        for (Object o : q.getResultList()) {
            ProductOrder order = (ProductOrder) o;
            validity = this.checkIfValidOrder(order);
            orderDate = order.getCreatedDate();
            result = this.compareDate(start, orderDate, end);
            if (result && validity) {
                orderList.add(order);
            }
        }
        return orderList;
    }

    public Set<Integer> findAllUniqueCustomerId(int companyId) {

        Set<Integer> result;
        List<Integer> nonUniqueList;
        nonUniqueList = this.findAllCustomerAccountId(companyId);
        result = this.findMyUniqueCustomer(nonUniqueList);
        for (Object o : result) {
            System.out.println("Unique customer Id ********************"+o);
        }
        return result;
    }

    public Double getTotalValueOfCustomerOfCurrentMonth(int companyId, Date current, int cId) {
        Double result = 0.0;
        List<ProductOrder> allMyOrders;
        allMyOrders = this.allCurrentMonOrdersOrCustomer(companyId, current, cId);
        result = this.getTotalValueOfCustomer(allMyOrders);
        System.out.println("SessionBean get Total Value of Customer of current Month +================= " + result);
        return result;
    }

    public Double getTotalValueOfCustomerOfAll(int companyId, int cId) {
        Double result = 0.0;
        List<ProductOrder> allMyOrders;
        allMyOrders = this.findAllProductOfOneCustomer(companyId, cId);
        result = this.getTotalValueOfCustomer(allMyOrders);
                System.out.println("SessionBean get Total Value of Customer of current year +================= " + result);

        return result;
    }

    public Double getTotalValueOfMonth(List<ProductOrder> orderList, int month, int year) {
        Double result = 0.0;
        Date date1;
        Calendar cal = Calendar.getInstance();
        if (orderList != null) {
            for (Object o : orderList) {
                ProductOrder myOrder = (ProductOrder) o;
                date1 = myOrder.getCreatedDate();
                cal.setTime(date1);
                int orderMonth = cal.get(Calendar.MONTH);
                orderMonth++;
                int orderYear = cal.get(Calendar.YEAR);
                if (orderMonth == month && orderYear == year) {
                    result += myOrder.getPrice();
                }
            }
        }
        return result;

    }

    public Double getTotalValueofDay(List<ProductOrder> orderList, long day, int month, int year) {
        Double result = 0.0;
        Date date1;
        Calendar cal = Calendar.getInstance();
        if (orderList != null) {
            for (Object o : orderList) {
                ProductOrder myOrder = (ProductOrder) o;
                date1 = myOrder.getCreatedDate();
                cal.setTime(date1);
                int orderDay = cal.get(Calendar.DAY_OF_MONTH);
                System.out.println("this is gettotalCalueofDay " + orderDay);
                int orderMonth = cal.get(Calendar.MONTH);
                orderMonth++;
                int orderYear = cal.get(Calendar.YEAR);
                if (orderDay == day && orderMonth == month && orderYear == year) {
                    result += myOrder.getPrice();
                }
            }
        }
        return result;

    }

    public long retrieveTotalDay(Date start, Date end) {
        long diffDay = 0;
        long days = 0;
        diffDay = end.getTime() - start.getTime();
        days = diffDay / (24 * 60 * 60 * 1000);
        return days;
    }

    public int retrieveTotalMonth(Date start, Date end) {
        int diffMonth = 0;
        if (start != null && end != null) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(start);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(end);
            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH) + 1;
        }
        return diffMonth;

    }

    // private functions
    private Boolean checkIfValidOrder(ProductOrder order) {
        Boolean result = false;
        int status = order.getStatus();
        if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {
            result = true;
        }
        return result;
    }

    private Boolean compareDate(Date d1, Date d2, Date d3) {
        Boolean result = true;
        if (d2.before(d1) || d2.after(d3)) {
            result = false;
        }
        return result;
    }

    private Boolean compareMonth(Date d1, Date d2) {
        Boolean result = true;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        int cuM = cal1.get(Calendar.MONTH);
        int oM = cal2.get(Calendar.MONTH);
        if (cuM != oM) {
            result = false;
        }
        return result;
    }

    private List<ProductOrder> allCurrentMonOrdersOrCustomer(int companyId, Date current, int cId) {
        Query q = em.createQuery("SELECT p FROM ProductOrder p WHERE p.companyId =:companyId AND p.creatorId =:customerId").setParameter("companyId", companyId);
        q.setParameter("customerId", cId);
        Boolean result;
        Boolean validity = false;
        Date orderDate;
        List<ProductOrder> orderList = new ArrayList();

        for (Object o : q.getResultList()) {
            ProductOrder order = (ProductOrder) o;
            validity = this.checkIfValidOrder(order);
            orderDate = order.getCreatedDate();
            result = this.compareMonth(current, orderDate);
            if (result && validity) {
                orderList.add(order);
            }
        }
        return orderList;
    }

    private List<ProductOrder> findAllProductOfOneCustomer(int companyId, int cId) {
        Query q = em.createQuery("SELECT p FROM ProductOrder p WHERE p.companyId =:companyId AND p.creatorId =:customerId").setParameter("companyId", companyId);
        q.setParameter("customerId", cId);
        Boolean validity = false;
        List<ProductOrder> orderList = new ArrayList();

        for (Object o : q.getResultList()) {
            ProductOrder order = (ProductOrder) o;
            validity = this.checkIfValidOrder(order);
            if (validity) {
                orderList.add(order);
            }
        }
        return orderList;
    }

    private List<Integer> findAllCustomerAccountId(int companyId) {
        List<Integer> result = new ArrayList();
        Query q = em.createQuery("SELECT q FROM Quotation q WHERE q.company = :company").setParameter("company", companyId);
        for (Object o : q.getResultList()) {
            Quotation myq = (Quotation) o;
            result.add((myq.getCustomerId()));
        }
        return result;
    }

    private Set<Integer> findMyUniqueCustomer(List<Integer> nonUniqueList) {
        Set< Integer> uniqueList = new HashSet(nonUniqueList);
        return uniqueList;
    }

    private Double getTotalValueOfCustomer(List<ProductOrder> orderList) {
        Double result = 0.0;
        for (Object o : orderList) {
            ProductOrder myorder = (ProductOrder) o;
            result += myorder.getPrice();
        }
        return result;
    }
}
