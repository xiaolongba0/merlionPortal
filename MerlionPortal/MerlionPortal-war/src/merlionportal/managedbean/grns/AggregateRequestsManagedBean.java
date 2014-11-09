/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.grns;

import entity.Contract;
import entity.ServicePO;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.grns.reverseauctionmodule.ReverseAuctionManagerSessionBean;

/**
 *
 * @author mac
 */
@Named(value = "aggregateRequestsManagedBean")
@ViewScoped
public class AggregateRequestsManagedBean {

    @EJB
    private ReverseAuctionManagerSessionBean reverseSB;

    private Integer userId;
    private Integer companyId;
    private List<ServicePO> transportionList;
    private List<Contract> warehouseList;
    private List<ServicePO> selectedTransList;
    private List<Contract> selectedWareList;
    private List<ServicePO> filteredTlist;
    private List<Contract> filteredWlist;
    private Date startDate;
    private Date endDate;
    private List<String> allOrig;
    private List<String> allDest;

    @PostConstruct
    public void init() {
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
        transportionList = reverseSB.getAllService(companyId, "Transportation", 5);
        if (transportionList == null) {
            System.out.println("--------------NULLLLL");
        }
        warehouseList = reverseSB.getAllContract(companyId, "Warehouse", 5);

    }

    public AggregateRequestsManagedBean() {
    }

    public String aggregateRequests() {

        return "aggretable";
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public List<ServicePO> getTransportionList() {

        return transportionList;
    }

    public void setTransportionList(List<ServicePO> transportionList) {
        this.transportionList = transportionList;
    }

    public List<Contract> getWarehouseList() {
        return warehouseList;
    }

    public void setWarehouseList(List<Contract> warehouseList) {
        this.warehouseList = warehouseList;
    }

    public List<ServicePO> getSelectedTransList() {
        return selectedTransList;
    }

    public void setSelectedTransList(List<ServicePO> selectedTransList) {
        this.selectedTransList = selectedTransList;
    }

    public List<ServicePO> getFilteredTlist() {
        return filteredTlist;
    }

    public void setFilteredTlist(List<ServicePO> filteredTlist) {
        this.filteredTlist = filteredTlist;
    }

    public List<Contract> getSelectedWareList() {
        return selectedWareList;
    }

    public void setSelectedWareList(List<Contract> selectedWareList) {
        this.selectedWareList = selectedWareList;
    }

    public List<Contract> getFilteredWlist() {
        return filteredWlist;
    }

    public void setFilteredWlist(List<Contract> filteredWlist) {
        this.filteredWlist = filteredWlist;
    }

    public String viewCompanyName(Integer compId) {
        if (compId == null) {
            return "";
        }
        return reverseSB.getCompanyName(compId);
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

    public String origion(ServicePO mypo1) {
        if (mypo1 == null) {
            return "";
        }
        return reverseSB.getORegion(mypo1);
    }

    public String destination(ServicePO mypo1) {
        if (mypo1 == null) {
            return "";
        }
        return reverseSB.getDRegion(mypo1);
    }

    public List<String> getAllOrig() {
        if (transportionList != null) {
            allOrig = reverseSB.getAllOrigionRegion(transportionList);
        }
        return allOrig;
    }

    public void setAllOrig(List<String> allOrig) {
        this.allOrig = allOrig;
    }

    public List<String> getAllDest() {
        if (transportionList != null) {
            allDest = reverseSB.getAllDestinationRegion(transportionList);
        }

        return allDest;
    }

    public void setAllDest(List<String> allDest) {
        this.allDest = allDest;
    }

    public void checkTAggregatable() {
        if (selectedTransList == null || selectedTransList.isEmpty()) {
            System.out.println("NULLLLLLLLL=============================");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select requests to aggregate"));
        }
        Boolean result = reverseSB.checkTAggregatability(selectedTransList);
        if (!result) {
            System.out.println("NOT NUL BUT FAULSE=============================");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Cannot Aggregate Please re-select ."));
        }
        if (result) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Infor!", "Can aggregate!."));

        }
    }

    public void checkWAggregatable() {
        if (selectedWareList == null || selectedWareList.isEmpty()) {
            System.out.println("NULLLLLLLLL=============================");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select requests to aggregate"));
        }
        Boolean result = reverseSB.checkWAggregatability(selectedWareList);
        if (!result) {
            System.out.println("bot NULLLLLLLLL_________");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Cannot Aggregate Please re-select ."));
        }
        if (result) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Infor!", "Can aggregate!."));

        }
    }

    public String postTRequest() {
        if (selectedTransList == null || selectedTransList.isEmpty()) {
            System.out.println("NULLLLLLLLL=============================");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select requests to aggregate"));
            return "aggregaterequests.xhtml";
        }
        Boolean result = reverseSB.checkTAggregatability(selectedTransList);
        if (!result) {
            System.out.println("NOT NUL BUT FAULSE=============================");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Cannot Aggregate Please re-select ."));
            return "aggregaterequests.xhtml";
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("serviceType", 1);
        sessionMap.put("selectedT", selectedTransList);
        return "post.xhtml?faces-redirect=true";
    }

    public String postWReqeust() {

        if (selectedWareList == null || selectedWareList.isEmpty()) {
            System.out.println("NULLLLLLLLL=============================");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select requests to aggregate"));
            return "aggregaterequests.xhtml";
        }
        Boolean result = reverseSB.checkWAggregatability(selectedWareList);
        if (!result) {
            System.out.println("bot NULLLLLLLLL_________");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Cannot Aggregate Please re-select ."));
            return "aggregaterequests.xhtml";
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("serviceType", 2);
        sessionMap.put("selectedW", selectedWareList);
        return "post.xhtml?faces-redirect=true";

    }

}
