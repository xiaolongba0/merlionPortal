/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.managedbean.tms;

import javax.inject.Named;
import entity.SystemUser;
import entity.Node;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;

/**
 *
 * @author Yuanbo
 */
@Named(value = "nodeManagerBean")
@RequestScoped
public class NodeManagerBean {

    @EJB
    private TAssetmanagementSessionBean tassetManagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;
    private SystemUser loginedUser;
    private Integer companyId;
    private Integer nodeId;
    private Integer newNodeId;
    private String nodeName;
    private String nodeType;

    /**
     * Creates a new instance of NodeManagerBean
     */
    public NodeManagerBean() {
    }

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            loginedUser = uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
            companyId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId");
            if (loginedUser != null) {
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

    public void createNewNode(ActionEvent node) {

        try {
            System.out.println("[INSIDE WAR FILE]===========================Create New Node");
            newNodeId = tassetManagementSessionBean.addNewNode(nodeName, nodeType);
            if (newNodeId > -1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Node Added!", ""));
                clearAllFields();
                System.out.println("[WAR FILE]===========================Create New Node");
                systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "TMS created new node");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please Select Type!", ""));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearAllFields() {

        nodeName = null;
        nodeType = null;

    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getNewNodeId() {
        return newNodeId;
    }

    public void setNewNodeId(Integer newNodeId) {
        this.newNodeId = newNodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

}
