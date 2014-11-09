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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;

/**
 *
 * @author Yuanbo
 */
@Named(value = "nodeViewEditManagedBean")
@ViewScoped
public class NodeViewEditManagedBean {

    @EJB
    private TAssetmanagementSessionBean tassetmanagementSessionBean;
    @EJB
    private UserAccountManagementSessionBean uamb;
    @EJB
    private SystemLogSessionBean systemLogSB;

    private SystemUser loginedUser;
    private List<Node> nodes;
    private Integer nodeId;
    private String nodeName;
    private String nodeType;
    private Integer companyId;

    @PostConstruct
    public void init() {
        boolean redirect = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userId")) {
            setLoginedUser(uamb.getUser((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId")));
            setCompanyId((Integer) (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyId"));
            if (getLoginedUser() != null) {
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

    public List<Node> getNodes() {
        System.out.println("===============================[In Managed Bean - getNode!]");
        nodes = tassetmanagementSessionBean.viewTheNodes();

        // for checking
        for (Object obj : nodes) {
            System.out.println(obj);
        }
        return nodes;
    }

    public void deleteNode(Node node) {
        try {
            nodeId = node.getNodeId();
            System.out.println("[In WAR FILE - Delete Node Function] Node ID========== :" + nodeId);
            tassetmanagementSessionBean.deleteNode(nodeId);
            systemLogSB.recordSystemLog(loginedUser.getSystemUserId(), "TMS deleted node");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public SystemUser getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(SystemUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

}
