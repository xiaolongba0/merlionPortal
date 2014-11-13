/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.tms.transportationhumanresourcemanagementmodule;

import javax.ejb.Stateless;
import entity.TransportationOperator;
import entity.OperatorSchedule;
import entity.SystemUser;
import entity.Company;
import entity.AssetSchedule;
import entity.UserRole;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import merlionportal.ci.administrationmodule.UserAccountManagementSessionBean;

/**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class TOperatormanagementSessionBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    private UserAccountManagementSessionBean uamsb;

    // Opertator
    private TransportationOperator operator;
    private Integer operatorId;
    private ArrayList<OperatorSchedule> operatorScheduleList;
    private ArrayList<AssetSchedule> assetScheduleList;

// Operator Schedule
    private OperatorSchedule operatorSchedule;
    private Integer operatorScheduleId;

    private Integer companyId;
//    

    public List<TransportationOperator> viewMyOperator(Integer companyId) {
        List<TransportationOperator> allMyOperator = new ArrayList<>();
        System.out.println("In viewMyOperator, company ID ============================= : " + companyId);
        System.out.println("ehhh===================== : " + companyId);

        Query query = em.createNamedQuery("TransportationOperator.findByCompanyId").setParameter("companyId", companyId);
        for (Object o : query.getResultList()) {
            operator = (TransportationOperator) o;
            allMyOperator.add(operator);
        }

        return allMyOperator;
    }
//
//    public List<TransportationOperator> viewMyDriver(Integer companyId) {
//        List<TransportationOperator> allMyOperator = new ArrayList<>();
//        List<TransportationOperator> tempList = new ArrayList();
//        System.out.println("In viewMyOperator, company ID ============================= : " + companyId);
//        System.out.println("ehhh===================== : " + companyId);
//
//        Query query = em.createNamedQuery("TransportationOperator.findByCompanyId").setParameter("companyId", companyId);
//        tempList = query.getResultList();
//        for (TransportationOperator o : tempList) {
//            if (o.getIsAvailable()) {
//                if (operator.getOperatorType().equals("Driver")) {
//                    allMyOperator.add(operator);
//                }
//            }
//        }
//
//        return allMyOperator;
//    }

    public List<TransportationOperator> viewMyAvailableOperator(Integer companyId) {
        List<TransportationOperator> allMyOperator = new ArrayList<>();
        List<TransportationOperator> tempList = new ArrayList();
        System.out.println("In viewMyOperator, company ID ============================= : " + companyId);
        System.out.println("ehhh===================== : " + companyId);

        Query query = em.createNamedQuery("TransportationOperator.findByCompanyId").setParameter("companyId", companyId);
        tempList = query.getResultList();
        for (TransportationOperator o : tempList) {
            if (o.getIsAvailable()) {
                allMyOperator.add(o);
            }
        }
        return allMyOperator;
    }

    public Integer countMyAvailableOperator(Integer companyId) {
        Integer count = 0;
        List<TransportationOperator> tempList = new ArrayList();
        System.out.println("In viewMyOperator, company ID ============================= : " + companyId);
        System.out.println("ehhh===================== : " + companyId);

        Query query = em.createNamedQuery("TransportationOperator.findByCompanyId").setParameter("companyId", companyId);
        tempList = query.getResultList();
        for (TransportationOperator o : tempList) {
            if (o.getIsAvailable() && o.getOperatorType().equals("Operator")) {
                count++;
            }
        }
        return count;
    }

    public Integer countMyNotAvailableOperator(Integer companyId) {
        Integer count = 0;
        List<TransportationOperator> tempList = new ArrayList();
        System.out.println("In viewMyOperator, company ID ============================= : " + companyId);
        System.out.println("ehhh===================== : " + companyId);

        Query query = em.createNamedQuery("TransportationOperator.findByCompanyId").setParameter("companyId", companyId);
        tempList = query.getResultList();
        for (TransportationOperator o : tempList) {
            if (!o.getIsAvailable() && o.getOperatorType().equals("Operator")) {
                count++;
            }
        }
        return count;
    }

    public Integer countMyAvailableDriver(Integer companyId) {
        Integer count = 0;
        List<TransportationOperator> tempList = new ArrayList();
        System.out.println("In viewMyOperator, company ID ============================= : " + companyId);
        System.out.println("ehhh===================== : " + companyId);

        Query query = em.createNamedQuery("TransportationOperator.findByCompanyId").setParameter("companyId", companyId);
        tempList = query.getResultList();
        for (TransportationOperator o : tempList) {
            if (o.getIsAvailable() && o.getOperatorType().equals("Driver")) {
                count++;
            }
        }
        return count;
    }

    public Integer countMyNotAvailableDriver(Integer companyId) {
        Integer count = 0;
        List<TransportationOperator> tempList = new ArrayList();
        System.out.println("In viewMyOperator, company ID ============================= : " + companyId);
        System.out.println("ehhh===================== : " + companyId);

        Query query = em.createNamedQuery("TransportationOperator.findByCompanyId").setParameter("companyId", companyId);
        tempList = query.getResultList();
        for (TransportationOperator o : tempList) {
            if (!o.getIsAvailable() && o.getOperatorType().equals("Driver")) {
                count++;
            }
        }
        return count;
    }

    public List<TransportationOperator> viewMyNotAvailableOperator(Integer companyId) {
        List<TransportationOperator> allMyOperator = new ArrayList<>();
        List<TransportationOperator> tempList = new ArrayList();
        System.out.println("In viewMyOperator, company ID ============================= : " + companyId);
        System.out.println("ehhh===================== : " + companyId);

        Query query = em.createNamedQuery("TransportationOperator.findByCompanyId").setParameter("companyId", companyId);
        tempList = query.getResultList();
        for (TransportationOperator o : tempList) {
            if (!o.getIsAvailable()) {
                allMyOperator.add(o);
            }
        }
        return allMyOperator;
    }

    public Boolean deleteOperator(Integer operatorId) {

        Query query = em.createNamedQuery("TransportationOperator.findByOperatorId").setParameter("operatorId", operatorId);
        List<TransportationOperator> allMyOperator = query.getResultList();

        for (TransportationOperator o : allMyOperator) {
            if (Objects.equals(o.getOperatorId(), operatorId)) {
                em.remove(o);
                em.flush();
                return true;
            }
        }
        return false;
    }

    public Integer addNewOperator(Integer operatorId, String operatorName, String operatorLastName, String operatorGender, Date birthday, String operatorType, String operatorStatus, Integer companyId, String emailAddress, String contactNumber, String password, Integer roleId) {
        System.out.println("[INSIDE EJB]================================Add New Operator");
////      public int createSystemUser(Integer operatorId, Integer companyId, List<Integer> roles, String firstName, String lastName, String emailAddress, String password, String postalAddress,
//        String contactNumber, String salution, Integer credit)   
        String salution = new String();
        if (operatorGender.equals("Male")) {
            salution = "Mr.";
        } else {
            salution = "Ms.";
        }
        Company tempcompany = em.find(Company.class, companyId);
        String postalAddress = tempcompany.getAddress();

        List<Integer> roles = new ArrayList();
        roles.add(roleId);

// MANLI COME CHECK!!!!!!!!!!!     
//        What are the roles and operatorId;
        Integer userId = uamsb.createSystemUser(1, companyId, roles, operatorName, operatorLastName, emailAddress, password, postalAddress, contactNumber, salution, 1);
        System.out.println("==========Created System User====== :" + userId);
        TransportationOperator operatorr = new TransportationOperator();
        String tempName = operatorName + operatorLastName;

        operatorr.setOperatorType(operatorType);
        operatorr.setOperatorStatus(operatorStatus);
        operatorr.setIsAvailable(Boolean.TRUE);
        operatorr.setBirthday(birthday);
        operatorr.setGender(operatorGender);
        operatorr.setOperatorName(tempName);
        operatorr.setCompanyId(companyId);

        operatorScheduleList = new ArrayList<OperatorSchedule>();
        operatorr.setOperatorScheduleList(operatorScheduleList);

        System.out.println("==========Operator Type=========== :" + operatorType);
        System.out.println("==========Operator Status========= :" + operatorStatus);
        System.out.println("==========Operator Birthday======= :" + birthday);
        System.out.println("==========Operator Gender========= :" + operatorGender);
        System.out.println("==========Operator Name=========== :" + operatorName);
        System.out.println("==========Operator Company======== :" + companyId);
        System.out.println("==========Operator is Availble==== :" + "yes");

        em.persist(operatorr);
        em.flush();
        System.out.println("==========Operator Id============= :" + operatorr.getOperatorId());
        System.out.println("[EJB]================================Successfully Added a New Operator");

        return operatorr.getOperatorId();

    }

    public Integer editOperator(String operatorStatus, Boolean isAvailable) {

        System.out.println("[EJB]================================edit Operator");
        Query query = em.createNamedQuery("TransportationOperator.findByCompanyId").setParameter("companyId", companyId);
        List<TransportationOperator> allMyOpertors = query.getResultList();

        TransportationOperator operatorr = new TransportationOperator();
        for (TransportationOperator l : allMyOpertors) {
            if (Objects.equals(l.getOperatorId(), operatorId)) {
                operatorr = l;
            }
        }

        operatorr.setOperatorStatus(operatorStatus);
        operatorr.setIsAvailable(isAvailable);

        em.merge(operatorr);
        em.flush();

        System.out.println("[EJB]================================Successfully EDITED location");
        return operatorr.getOperatorId();

    }

    public boolean addOschedule(Date startDate, Date endDate, Integer operatorId) {

        System.out.println("[INSIDE EJB]================================Add OperatorSchedule");
        System.out.println("Print out the operatorId before query: " + operatorId);
        Query query = em.createNamedQuery("TransportationOperator.findByOperatorId").setParameter("operatorId", operatorId);

        TransportationOperator tOperator = (TransportationOperator) query.getSingleResult();
        if (tOperator != null) {
            OperatorSchedule schedule = new OperatorSchedule();
            schedule.setStartDate(startDate);
            schedule.setEndDate(endDate);
            schedule.setTransportationOperatoroperatorId(tOperator);
            tOperator.setIsAvailable(Boolean.FALSE);

            em.persist(schedule);
            em.persist(tOperator);
            em.flush();

            return true;
        } else {
            return false;
        }

    }

    public List<OperatorSchedule> viewtOscheduleforOperator(Integer operatorId) {

        System.out.println("In viewOperatorScheduleforTOperator, tOperatorId ============================= : " + operatorId);
        TransportationOperator operatorTemp = null;

        if (operatorId != null) {
            operatorTemp = em.find(TransportationOperator.class, operatorId);
            System.out.println("In view my viewOschedule, finding operator" + operatorTemp);
        }
        return operatorTemp.getOperatorScheduleList();

    }

    public Boolean deleteOschedule(Integer operatorScheduleId) {

        System.out.println("The selected operator schedule ID  " + operatorScheduleId);;
        OperatorSchedule oSchedule = em.find(OperatorSchedule.class, operatorScheduleId);
        System.out.println("Delete OperatorSchedule ================= : " + oSchedule);
        if (oSchedule == null) {
            return false;
        }
        TransportationOperator tOperator = oSchedule.getTransportationOperatoroperatorId();
        tOperator.getOperatorScheduleList().remove(oSchedule);
        tOperator.setIsAvailable(Boolean.TRUE);
        em.merge(tOperator);
        em.remove(oSchedule);
        em.flush();

        System.out.println("END OF DELETE OPERATOR SCHEDULE FUNCTION IN SESSION BEAN");
        return true;
    }

    public Boolean editOSchedule(Date startDate, Date endDate, Integer operatorScheduleId) {

        OperatorSchedule oSchedule = new OperatorSchedule();
        Query query = em.createNamedQuery("OperatorSchedule.findByOperatorScheduleId").setParameter("operatorScheduleId", operatorScheduleId);
        oSchedule = (OperatorSchedule) query.getSingleResult();
        if (oSchedule != null) {
            oSchedule.setStartDate(startDate);
            oSchedule.setEndDate(endDate);

            em.merge(oSchedule);
            em.flush();

            return true;
        } else {
            return false;
        }

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
