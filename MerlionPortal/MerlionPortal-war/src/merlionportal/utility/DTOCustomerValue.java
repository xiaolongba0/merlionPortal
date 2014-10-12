/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package merlionportal.utility;

/**
 *
 * @author mac
 */
public class DTOCustomerValue implements Comparable<DTOCustomerValue> {
    private int customerId;
    private Double customerCurrentValue;
    
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Double getCustomerCurrentValue() {
        return customerCurrentValue;
    }

    public void setCustomerCurrentValue(Double customerCurrentValue) {
        this.customerCurrentValue = customerCurrentValue;
    }
    
    public int compareTo(DTOCustomerValue compareValue){
        Double compareQuatity =((DTOCustomerValue) compareValue).getCustomerCurrentValue();
        return compareQuatity.compareTo(this.customerCurrentValue);
    }
}
