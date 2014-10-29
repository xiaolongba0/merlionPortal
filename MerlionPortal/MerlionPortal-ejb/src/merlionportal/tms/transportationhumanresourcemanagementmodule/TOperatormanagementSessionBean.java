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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class TOperatormanagementSessionBean {

    @PersistenceContext
    EntityManager em;

    // Opertator
    private TransportationOperator operator;
    private Integer operatorId;
    private ArrayList<OperatorSchedule> operatorScheduleList;

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

    public Integer addNewOperator(String operatorName, String operatorGender, Date birthday, String operatorType, String operatorStatus, Integer companyId) {
        System.out.println("[INSIDE EJB]================================Add New Operator");

        operator = new TransportationOperator();

        operator.setOperatorType(operatorType);
        operator.setOperatorStatus(operatorStatus);
        operator.setIsAvailable(Boolean.TRUE);
        operator.setBirthday(birthday);
        operator.setGender(operatorGender);
        operator.setOperatorName(operatorName);
//        operator.setAssetSchedulescheduleId();

        operatorScheduleList = new ArrayList<OperatorSchedule>();
        operator.setOperatorScheduleList(operatorScheduleList);

        System.out.println("==========Operator Type=========== :" + operatorType);
        System.out.println("==========Operator Status========= :" + operatorStatus);
        System.out.println("==========Operator Birthday======= :" + birthday);
        System.out.println("==========Operator Gender========= :" + operatorGender);
        System.out.println("==========Operator Name=========== :" + operatorName);
        System.out.println("==========Operator is Availble==== :" + "yes");
        System.out.println("==========Operator Id============= :" + operator.getOperatorId());

        em.persist(operator);
        em.flush();

        System.out.println("[EJB]================================Successfully Added a New Location");

        return operator.getOperatorId();

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
        Query query = em.createNamedQuery("TransportationOperator.findByoperatorId").setParameter("operatorId", operatorId);

        List<TransportationOperator> allMyOperatorss = query.getResultList();

        TransportationOperator tOperator = (TransportationOperator) query.getSingleResult();
        if (tOperator != null) {
            OperatorSchedule schedule = new OperatorSchedule();
            schedule.setStartDate(startDate);
            schedule.setEndDate(endDate);
            schedule.setTransportationOperatoroperatorId(tOperator);

            em.merge(tOperator);
            em.persist(schedule);
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
        Query query = em.createNamedQuery("OperatorSchedule.findByOperatorScheduleId").setParameter("scheduleId", operatorScheduleId);
        OperatorSchedule oSchedule = (OperatorSchedule) query.getSingleResult();
        System.out.println("Delete OperatorSchedule ================= : " + oSchedule);
        if (oSchedule == null) {
            return false;
        }
        TransportationOperator tOperator = oSchedule.getTransportationOperatoroperatorId();
        tOperator.getOperatorScheduleList().remove(oSchedule);
        em.merge(tOperator);
        em.remove(oSchedule);
        em.flush();

        System.out.println("END OF DELETE OPERATOR SCHEDULE FUNCTION IN SESSION BEAN");
        return true;
    }

    public boolean editOSchedule(Date startDate, Date endDate, Integer operatorScheduleId) {

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
