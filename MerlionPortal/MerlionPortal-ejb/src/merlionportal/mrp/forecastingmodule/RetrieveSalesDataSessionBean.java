/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.mrp.forecastingmodule;

import entity.Product;
import entity.ProductOrderLineItem;
import java.text.SimpleDateFormat;
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
 * @author manliqi
 */
@Stateless
@LocalBean
public class RetrieveSalesDataSessionBean {

    @PersistenceContext
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public List<String> retrieveDateList(Integer productId) {
        Query q = em.createNamedQuery("Product.findByProductId");
        q.setParameter("productId", productId);
        Product product = (Product) q.getSingleResult();

        List<ProductOrderLineItem> lineItems = product.getProductOrderLineItemList();
        ProductOrderLineItem lineItem;

//        Get Today's date
        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
//        get first day of this month
        Date month0 = calendar.getTime();

        calendar.add(Calendar.MONTH, -24); // to get two years ago

//        Set two years ago
        Date month24 = calendar.getTime();

//        Set date for each month for comparison
        calendar.add(Calendar.MONTH, +1);
        Date month23 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month22 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month21 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month20 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month19 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month18 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month17 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month16 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month15 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month14 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month13 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month12 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month11 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month10 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month9 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month8 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month7 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month6 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month5 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month4 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month3 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month2 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month1 = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateList = new ArrayList<>();
        dateList.add(df.format(month24));
        dateList.add(df.format(month23));
        dateList.add(df.format(month22));
        dateList.add(df.format(month21));
        dateList.add(df.format(month20));
        dateList.add(df.format(month19));
        dateList.add(df.format(month18));
        dateList.add(df.format(month17));
        dateList.add(df.format(month16));
        dateList.add(df.format(month15));
        dateList.add(df.format(month14));
        dateList.add(df.format(month13));
        dateList.add(df.format(month12));
        dateList.add(df.format(month11));
        dateList.add(df.format(month10));
        dateList.add(df.format(month9));
        dateList.add(df.format(month8));
        dateList.add(df.format(month7));
        dateList.add(df.format(month6));
        dateList.add(df.format(month5));
        dateList.add(df.format(month4));
        dateList.add(df.format(month3));
        dateList.add(df.format(month2));
        dateList.add(df.format(month1));
        
        return dateList;
        
    }

    public List<Integer> retrieveQuantityList(Integer productId) {
        Query q = em.createNamedQuery("Product.findByProductId");
        q.setParameter("productId", productId);
        Product product = (Product) q.getSingleResult();


        List<ProductOrderLineItem> lineItems = product.getProductOrderLineItemList();
        ProductOrderLineItem lineItem;
        
//        Get Today's date
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
//        get first day of this month
        Date month0 = calendar.getTime();
        calendar.add(Calendar.MONTH, -24); // to get two years ago

//        Set two years ago
        Date month24 = calendar.getTime();
//        Set date for each month for comparison
        calendar.add(Calendar.MONTH, +1);
        Date month23 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month22 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month21 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month20 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month19 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month18 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month17 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month16 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month15 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month14 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month13 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month12 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month11 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month10 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month9 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month8 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month7 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month6 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month5 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month4 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month3 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month2 = calendar.getTime();
        calendar.add(Calendar.MONTH, +1);
        Date month1 = calendar.getTime();

//       QUANTITY FOR EACH MONTH
        Integer quantity1 = 0;
        Integer quantity2 = 0;
        Integer quantity3 = 0;
        Integer quantity4 = 0;
        Integer quantity5 = 0;
        Integer quantity6 = 0;
        Integer quantity7 = 0;
        Integer quantity8 = 0;
        Integer quantity9 = 0;
        Integer quantity10 = 0;
        Integer quantity11 = 0;
        Integer quantity12 = 0;
        Integer quantity13 = 0;
        Integer quantity14 = 0;
        Integer quantity15 = 0;
        Integer quantity16 = 0;
        Integer quantity17 = 0;
        Integer quantity18 = 0;
        Integer quantity19 = 0;
        Integer quantity20 = 0;
        Integer quantity21 = 0;
        Integer quantity22 = 0;
        Integer quantity23 = 0;
        Integer quantity24 = 0;

//        line item for this product check the date is within two years span
        for (Object o : lineItems) {
            lineItem = (ProductOrderLineItem) o;
            if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month0) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month1)) {
                quantity1 = quantity1 + lineItem.getQuantity();
            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month1) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month2)) {
                quantity2 = quantity2 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month2) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month3)) {
                quantity3 = quantity3 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month3) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month4)) {
                quantity4 = quantity4 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month4) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month5)) {
                quantity5 = quantity5 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month5) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month6)) {
                quantity6 = quantity6 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month6) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month7)) {
                quantity7 = quantity7 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month7) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month8)) {
                quantity8 = quantity8 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month8) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month9)) {
                quantity9 = quantity9 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month9) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month10)) {
                quantity10 = quantity10 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month10) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month11)) {
                quantity11 = quantity11 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month11) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month12)) {
                quantity12 = quantity12 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month12) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month13)) {
                quantity13 = quantity13 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month13) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month14)) {
                quantity14 = quantity14 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month14) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month15)) {
                quantity15 = quantity15 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month15) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month16)) {
                quantity16 = quantity16 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month16) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month17)) {
                quantity17 = quantity17 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month17) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month18)) {
                quantity18 = quantity18 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month18) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month19)) {
                quantity19 = quantity19 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month19) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month20)) {
                quantity20 = quantity20 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month20) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month21)) {
                quantity21 = quantity21 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month21) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month22)) {
                quantity22 = quantity22 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month22) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month23)) {
                quantity23 = quantity23 + lineItem.getQuantity();

            } else if (lineItem.getProductOrderproductPOId().getCreatedDate().before(month23) && lineItem.getProductOrderproductPOId().getCreatedDate().after(month24)) {
                quantity24 = quantity24 + lineItem.getQuantity();

            } else {
//                Do nothing
            }
        }
        List<Integer> quantityList = new ArrayList<>();
//later check if quantitiy is empty, if it is, dun add in the quantityList, check in managedbean the size of quantityList
        if(quantity24 != 0){
            quantityList.add(quantity24);
        }
        if(quantity23 != 0){
            quantityList.add(quantity23);
        }
        if(quantity22 != 0){
            quantityList.add(quantity22);
        }
        if(quantity21 != 0){
            quantityList.add(quantity21);
        }
        if(quantity20 != 0){
            quantityList.add(quantity20);
        }
        if(quantity19 != 0){
            quantityList.add(quantity19);
        }
        if(quantity18 != 0){
            quantityList.add(quantity18);
        }
        if(quantity17 != 0){
            quantityList.add(quantity17);
        }
        if(quantity16 != 0){
            quantityList.add(quantity16);
        }
        if(quantity15 != 0){
            quantityList.add(quantity15);
        }
        if(quantity14 != 0){
            quantityList.add(quantity14);
        }
        if(quantity13 != 0){
            quantityList.add(quantity13);
        }
        if(quantity12 != 0){
            quantityList.add(quantity12);
        }
        if(quantity11 != 0){
            quantityList.add(quantity11);
        }
        if(quantity10 != 0){
            quantityList.add(quantity10);
        }
        if(quantity9 != 0){
            quantityList.add(quantity9);
        }
        if(quantity8 != 0){
            quantityList.add(quantity8);
        }
        if(quantity7 != 0){
            quantityList.add(quantity7);
        }
        if(quantity6 != 0){
            quantityList.add(quantity6);
        }
        if(quantity5 != 0){
            quantityList.add(quantity5);
        }
        if(quantity4 != 0){
            quantityList.add(quantity4);
        }
        if(quantity3 != 0){
            quantityList.add(quantity3);
        }
        if(quantity2 != 0){
            quantityList.add(quantity2);
        }
        if(quantity1 != 0){
            quantityList.add(quantity1);
        }

        return quantityList;
    }
}
