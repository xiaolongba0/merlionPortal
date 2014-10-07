package merlionportal.oes.reportmanagementmodule;

import entity.ProductOrder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    private Boolean compareDate(Date d1, Date d2, Date d3) {
        Boolean result = true;
        if (d2.before(d1) || d2.after(d3)) {
            result = false;
        }
        return result;
    }

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

    public Double getTotalValueOfMonth(List<ProductOrder> orderList, int month, int year) {
        System.out.println("getTotalValueOfMonth "+month+" "+year);
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

    public int retrieveTotalMonth(Date start, Date end) {
        int diffMonth=0;
        if(start!=null&&end!=null){
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)+1;
        }
        return diffMonth;

    }

    private Boolean checkIfValidOrder(ProductOrder order) {
        Boolean result = false;
        int status = order.getStatus();
        if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {
            result = true;
        }
        return result;
    }

}
