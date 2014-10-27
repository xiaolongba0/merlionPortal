/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.crms;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.crms.contractmanagementmodule.ContractManagementSessionBean;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author manliqi
 */
@Named(value = "uploadSignedContractManagedBean")
@ViewScoped
public class UploadSignedContractManagedBean {

    /**
     * Creates a new instance of UploadSignedContractManagedBean
     */
    @EJB
    ContractManagementSessionBean contractManagementSB;
    @EJB
    SystemLogSessionBean logSB;

    private Integer contractId;

    private Integer userId;
    private Integer companyId;

    public UploadSignedContractManagedBean() {
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
        contractId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contractId");

    }

    public void upload(FileUploadEvent event) {
        System.out.println("Upload start");
        if (event.getFile() != null) {
            InputStream inputStream = null;
            try {
                inputStream = event.getFile().getInputstream();
                byte[] bFile = new byte[inputStream.available()];
                inputStream.read(bFile);
                int result = contractManagementSB.saveSignedContract(contractId, bFile);
                if (result == 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded."));
                    logSB.recordSystemLog(userId, "uploaded a signed contract");

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Snap!", "Something went very wrong"));
                }
            } catch (IOException ex) {
                Logger.getLogger(UploadSignedContractManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(UploadSignedContractManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
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

}
