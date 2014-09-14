/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oes;

import entity.Quotation;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author mac
 */
@Named(value = "quotationManagedBean")
@RequestScoped
public class QuotationManagedBean {
    @EJB
    private QuotationManagerSessionBean quotationMB;
    
    private Quotation quotation;

    public QuotationManagedBean() {
    }
    
}
