/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.oes;

import entity.ProductOrder;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.oes.ordermanagement.CommonFunctionSessionBean;
import merlionportal.oes.reportmanagementmodule.ReportManagerSessionBean;

/**
 *
 * @author LI XIAMENG
 */
@Named(value = "reportManagedBean")
@ViewScoped
public class ReportManagedBean {

    @EJB
    private SystemLogSessionBean systemLogSB;
    @EJB
    private CommonFunctionSessionBean commonMB;
    @EJB
    private ReportManagerSessionBean reportMB;

    private Integer companyId;
    private Integer userId;
    private Date startDate;
    private Date endDate;
    private List<ProductOrder> productOrderList;
    private Integer customerId;
    private Date firstStartDate;
    private Date firstEndDate;
    private Date secondStartDate;
    private Date secondEndDate;
    private List<ProductOrder> secondOrderList;

    public ReportManagedBean() {
    }

    @PostConstruct
    public void init() {
        systemLogSB.recordSystemLog(userId, "OES View Report. ");
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");

            if (userId != null) {
                redirect = false;
            }
        }
        if (redirect) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String allValidOrders() {
        if (startDate == null || endDate == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", "Please select starting date and end date"));
            return null;

        } else {
            System.out.println(startDate + " " + endDate);
            productOrderList = reportMB.findAllProduct(companyId, startDate, endDate);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            sessionMap.put("StartDate", startDate);
            sessionMap.put("EndDate", endDate);
            sessionMap.put("OrderList", productOrderList);
        }
        return "reportdisplaysalessum.xhtml?faces-redirect=true";
    }

    public String allCustomerOrders() {
        if (firstStartDate == null || secondStartDate == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", "Please specify Starting date"));
            return null;

        } else if (firstEndDate == null || secondEndDate == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", "Please specify End date"));
            return null;

        } else if (customerId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", "Please specify Customer Id "));
            return null;

        } else {
            System.out.println(startDate + " " + endDate + " " + customerId);
            productOrderList = reportMB.findAllProduct(companyId, firstStartDate, firstEndDate, customerId);
            secondOrderList = reportMB.findAllProduct(companyId, secondStartDate, secondEndDate, customerId);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            sessionMap.put("FirstOrderList", productOrderList);
            sessionMap.put("SecondOrderList", secondOrderList);
            sessionMap.put("firstStartDate", firstStartDate);
            sessionMap.put("firstEndDate", firstEndDate);
            sessionMap.put("secondStartDate", secondStartDate);
            sessionMap.put("secondEndDate", secondEndDate);
            sessionMap.put("reportCustomer", customerId);
        }
        return "customeranalysisdetail.xhtml?faces-redirect=true";
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<ProductOrder> getProductOrderList() {
        return productOrderList;
    }

    public void setProductOrderList(List<ProductOrder> productOrderList) {
        this.productOrderList = productOrderList;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getFirstStartDate() {
        return firstStartDate;
    }

    public void setFirstStartDate(Date firstStartDate) {
        this.firstStartDate = firstStartDate;
    }

    public Date getFirstEndDate() {
        return firstEndDate;
    }

    public void setFirstEndDate(Date firstEndDate) {
        this.firstEndDate = firstEndDate;
    }

    public Date getSecondStartDate() {
        return secondStartDate;
    }

    public void setSecondStartDate(Date secondStartDate) {
        this.secondStartDate = secondStartDate;
    }

    public Date getSecondEndDate() {
        return secondEndDate;
    }

    public void setSecondEndDate(Date secondEndDate) {
        this.secondEndDate = secondEndDate;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

}
