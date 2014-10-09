/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import entity.Contract;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.crms.contractmanagementmodule.ContractManagementSessionBean;
import merlionportal.utility.DTOContract;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author manliqi
 */
@Named(value = "viewContractManagedBean")
@ViewScoped
public class ViewContractDetailManagedBean {

    /**
     * Creates a new instance of ViewContractManagedBean
     */
    @EJB
    ContractManagementSessionBean contractManagementSB;

    @EJB
    UserAccountManagementSessionBean userAccountSB;

    private Integer companyId;
    private Integer userId;

    //session passed attibute
    private Contract selectedContract;
    private DTOContract dtoContract;
    private String partyACompanyName;
    private String partyBCompanyName;

    private String reason;
    private String status;
    private List<DTOContract> contractToPrint;

    public ViewContractDetailManagedBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            userId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
            companyId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
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

        selectedContract = (Contract) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedContract");
        if (selectedContract != null) {
            partyACompanyName = userAccountSB.getCompany(selectedContract.getPartyA()).getName();
            partyBCompanyName = userAccountSB.getCompany(selectedContract.getPartyB()).getName();
            dtoContract = new DTOContract();
            this.mapContractClassToDTO();
            contractToPrint = new ArrayList<>();
            contractToPrint.add(dtoContract);
        }
        this.statusText(selectedContract.getStatus());

    }

    private void mapContractClassToDTO() {

        dtoContract.setContractId(selectedContract.getContractId());

        dtoContract.setPartyA(selectedContract.getPartyA());
        dtoContract.setPartyB(selectedContract.getPartyB());
        dtoContract.setPartyAName(partyACompanyName);
        dtoContract.setPartyBName(partyBCompanyName);

        dtoContract.setConditionText("N.A.");
        if (selectedContract.getConditionText() != null) {
            dtoContract.setConditionText(selectedContract.getConditionText());
        }
        dtoContract.setStatus(selectedContract.getStatus());
        dtoContract.setStartDate(selectedContract.getStartDate());
        dtoContract.setEndDate(selectedContract.getEndDate());
        dtoContract.setCreatedDate(selectedContract.getCreatedDate());
        dtoContract.setPrice(selectedContract.getPrice());
        dtoContract.setAgreedQuantity(selectedContract.getServiceQuotation().getQuantityPerMonth().toString());
        

        dtoContract.setOrigin("N.A.");
        if (selectedContract.getOrigin() != null) {
            dtoContract.setOrigin(selectedContract.getOrigin());
        }

        dtoContract.setDestination("N.A.");
        if (selectedContract.getDestination() != null) {
            dtoContract.setDestination(selectedContract.getDestination());
        }

        dtoContract.setWarehouseId("N.A.");
        if (selectedContract.getWarehouseId() != null) {
            dtoContract.setWarehouseId(selectedContract.getWarehouseId().toString());
        }

        dtoContract.setStorageZoneId("N.A.");
        if (selectedContract.getStorageZoneId() != null) {
            dtoContract.setStorageZoneId(selectedContract.getStorageZoneId().toString());
        }

        dtoContract.setStorageBinId("N.A.");
        if (selectedContract.getStorageBinId() != null) {
            dtoContract.setStorageBinId(selectedContract.getStorageBinId().toString());
        }
        dtoContract.setContactPersonName(selectedContract.getContactPersonName());
        dtoContract.setContactPersonNumber(selectedContract.getContactPersonNumber());
        dtoContract.setServiceType(selectedContract.getServiceType());

    }

    public void requestToModifyContract() {
        int result = contractManagementSB.requestToModify(selectedContract.getContractId(), reason);
        if (result == 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Request to modify contract is sent!", "Please wait for the other party to respond"));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oops", "Something went wrong!"));

        }
    }

    private void statusText(int passedStatus) {
        if (passedStatus == 1) {
            status = "Initial contract";
        }
        if (passedStatus == 2) {
            status = "Request to modify";
        }
        if (passedStatus == 3) {
            status = "Waiting for review";
        }
        if (passedStatus == 4) {
            status = "Waiting To be Signed";
        }
        if (passedStatus == 5) {
            status = "Valid";
        }
    }

    public void acceptContract() {
        int result = contractManagementSB.acceptContract(selectedContract.getContractId());
        if (result == 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contract Accepted!", "Please download and sign contract, and upload contract afterwards"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oops", "Something went wrong!"));
        }
    }

    public void previewPDFContract() {

        ServletOutputStream outputStream = null;
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(contractToPrint);
            HashMap parameters = new HashMap();
            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreports/servicecontract.jasper");
            HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            outputStream = response.getOutputStream();
            JasperPrint japerPrint = JasperFillManager.fillReport(reportStream, parameters, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(japerPrint, outputStream);

        } catch (IOException ex) {
            Logger.getLogger(ViewContractDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(ViewContractDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ViewContractDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public void downloadPDFContract(){
        ServletOutputStream outputStream = null;
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(contractToPrint);
            HashMap parameters = new HashMap();
            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreports/servicecontract.jasper");
            HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment; filename=servicecontract"+selectedContract.getContractId()+".pdf");
            outputStream = response.getOutputStream();
            JasperPrint japerPrint = JasperFillManager.fillReport(reportStream, parameters, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(japerPrint, outputStream);

        } catch (IOException ex) {
            Logger.getLogger(ViewContractDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(ViewContractDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ViewContractDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    public Contract getSelectedContract() {
        return selectedContract;
    }

    public void setSelectedContract(Contract selectedContract) {
        this.selectedContract = selectedContract;
    }

    public String getPartyACompanyName() {
        return partyACompanyName;
    }

    public void setPartyACompanyName(String partyACompanyName) {
        this.partyACompanyName = partyACompanyName;
    }

    public String getPartyBCompanyName() {
        return partyBCompanyName;
    }

    public void setPartyBCompanyName(String partyBCompanyName) {
        this.partyBCompanyName = partyBCompanyName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DTOContract getDtoContract() {
        return dtoContract;
    }

    public void setDtoContract(DTOContract dtoContract) {
        this.dtoContract = dtoContract;
    }

    public List<DTOContract> getContractToPrint() {
        return contractToPrint;
    }

    public void setContractToPrint(List<DTOContract> contractToPrint) {
        this.contractToPrint = contractToPrint;
    }

}
