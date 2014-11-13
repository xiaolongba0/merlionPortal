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
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;
import merlionportal.ci.loggingmodule.SystemLogSessionBean;
import merlionportal.tms.transportationmanagementmodule.TAssetmanagementSessionBean;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;

/**
 *
 * @author Yuanbo
 */
@Named(value = "nodeViewEditManagedBean")
@ViewScoped
public class NodeViewEditManagedBean implements Serializable {

    private MindmapNode root;

    private MindmapNode selectedNode;

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

    public NodeViewEditManagedBean() {

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
        String initialNodeName = tassetmanagementSessionBean.retrieveInitialNodeFromId(1);
//        Node initialNode = tassetmanagementSessionBean.retrieveNodeFromId(1);
        root = new DefaultMindmapNode(initialNodeName, 1, "eb6161", false);
        List<Integer> nodeIdList = tassetmanagementSessionBean.retrieveListOfNodeIdFromANode(1);
        List<String> nodeNameList = tassetmanagementSessionBean.retrieveListOfNodeFromANode(1);
        for (int i = 0; i < nodeNameList.size(); i++) {
            MindmapNode temp = new DefaultMindmapNode(nodeNameList.get(i), nodeIdList.get(i), "eb6161", true);
            root.addNode(temp);
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

    public void onNodeSelect(SelectEvent event) {
        MindmapNode node = (MindmapNode) event.getObject();
        int nodeid = (int) node.getData();
        int parentId = (int) node.getParent().getData();
        System.out.println("Selected Node Id :" + nodeid);
        //populate if not already loaded
//        Node initialNode = tassetmanagementSessionBean.retrieveNodeFromId(1);
        List<Integer> nodeIdList = tassetmanagementSessionBean.retrieveListOfNodeIdFromANode(nodeid);
        List<String> nodeNameList = tassetmanagementSessionBean.retrieveListOfNodeFromANode(nodeid);
        for (int i = 0; i < nodeNameList.size(); i++) {

            MindmapNode temp = new DefaultMindmapNode(nodeNameList.get(i), nodeIdList.get(i), "eb6161", true);
            System.out.println("i :" + i);
            if (nodeIdList.get(i) != parentId) {
                node.addNode(temp);
            }
        }
    }

    public void onNodeDblselect(SelectEvent event) {
        this.selectedNode = (MindmapNode) event.getObject();
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

    public MindmapNode getRoot() {
        return root;
    }

    public void setRoot(MindmapNode root) {
        this.root = root;
    }

    public MindmapNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(MindmapNode selectedNode) {
        this.selectedNode = selectedNode;
    }

}
