/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oes;

import entity.Product;
import entity.Quotation;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author mac
 */
@Named(value = "displayProductManagedBean")
@RequestScoped
public class DisplayProductManagedBean {

    @EJB
    private QuotationManagerSessionBean quotationMB;
    private List<Product> products;
    private String[] selectedProducts;
    private Quotation newRequest;
    private int companyId = 1;
    private List<Product> filteredProducts;
    private List<String> types;

    public DisplayProductManagedBean() {
    }

    public List<Product> getProducts() {
        this.products = quotationMB.displayAllProducts(companyId);
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String[] getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(String[] selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public Quotation getNewRequest() {
        return newRequest;
    }

    public void setNewRequest(Quotation newRequest) {
        this.newRequest = newRequest;
    }

    public void submitRequest() {
        System.out.println("Start Submit Request");
        if (selectedProducts == null) {
            System.out.println("WRONG!!!!!!!! DIDNT SAVE THE VALUE!!!");
        } else {
            newRequest = quotationMB.requestForQuotation(companyId, 1);
            System.out.println("Sting is empty array");
            System.out.println("selected product 1==================" + selectedProducts[0]);
            System.out.println("Quotation*****************" + newRequest.getQuotationId());
            for (int i = 0; i < selectedProducts.length; i++) {
                int productId = Integer.parseInt(selectedProducts[i]);
                quotationMB.createLineItem(companyId, productId, newRequest);
            }
        }
    }

    public List<Product> getFilteredProducts() {
        return filteredProducts;
    }

    public void setFilteredProducts(List<Product> filteredProducts) {
        this.filteredProducts = filteredProducts;
    }

    public List<String> getTypes() {
        types=quotationMB.getAllTypes();
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
    
    
}
