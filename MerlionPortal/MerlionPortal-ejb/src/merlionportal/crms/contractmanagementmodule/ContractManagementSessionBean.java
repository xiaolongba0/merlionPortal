/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.crms.contractmanagementmodule;

import entity.Company;
import entity.Contract;
import entity.ServicePO;
import entity.ServiceQuotation;
import entity.SignedContract;
import entity.SystemUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author manliqi
 */
@Stateless
@LocalBean
public class ContractManagementSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

//    contract status
//    1. Initial contract
//    2. Request to modify
//    3. Waiting for review
//    4. Waiting To be Signed
//    5. Valid
    public int createTransportationServiceContract(Integer quotationId, String condition, Integer creatorId) {

        Contract contract = new Contract();

        contract.setStatus(1);
        contract.setCreatedDate(new Date());
        SystemUser user = em.find(SystemUser.class, creatorId);
        String name = user.getFirstName() + "  " + user.getLastName();

        ServiceQuotation quotation = em.find(ServiceQuotation.class, quotationId);
        if (quotation != null) {
            contract.setStartDate(quotation.getStartDate());
            contract.setEndDate(quotation.getEndDate());
            contract.setPrice(quotation.getPrice() - quotation.getDiscountRate() * quotation.getPrice() / 100);
            contract.setOrigin(quotation.getOrigin());
            contract.setDestination(quotation.getDestination());
            contract.setServiceType(quotation.getServiceType());
            contract.setPartyA(quotation.getReceiverCompanyId());
            contract.setPartyB(quotation.getSenderCompanyId());
            contract.setContactPersonId(creatorId);
            contract.setContactPersonName(name);
            contract.setContactPersonNumber(user.getContactNumber());
            contract.setServiceQuotation(quotation);
            contract.setConditionText(condition);

            List<ServicePO> servicePOList = new ArrayList<>();
            contract.setServicePOList(servicePOList);

            quotation.getContractList().add(contract);
            em.persist(contract);
            em.merge(quotation);
            em.flush();

            return contract.getContractId();
        } else {
            return -1;
        }

    }

    public int createWarehouseServiceContract(Integer quotationId, String condition, Integer creatorId) {
        Contract contract = new Contract();

        contract.setStatus(1);
        contract.setCreatedDate(new Date());
        SystemUser user = em.find(SystemUser.class, creatorId);
        String name = user.getFirstName() + "  " + user.getLastName();

        ServiceQuotation quotation = em.find(ServiceQuotation.class, quotationId);
        if (quotation != null) {
            contract.setStartDate(quotation.getStartDate());
            contract.setEndDate(quotation.getEndDate());
            contract.setPrice(quotation.getPrice() - quotation.getDiscountRate() * quotation.getPrice() / 100);
            contract.setServiceType(quotation.getServiceType());
            contract.setPartyA(quotation.getReceiverCompanyId());
            contract.setPartyB(quotation.getSenderCompanyId());
            contract.setContactPersonId(creatorId);
            contract.setContactPersonName(name);
            contract.setContactPersonNumber(user.getContactNumber());
            contract.setServiceQuotation(quotation);
            contract.setConditionText(condition);

            List<ServicePO> servicePOList = new ArrayList<>();
            contract.setServicePOList(servicePOList);

            quotation.getContractList().add(contract);
            em.persist(contract);
            em.merge(quotation);
            em.flush();

            return contract.getContractId();
        } else {
            return -1;
        }
    }

    public Contract searchAValidContractToRenew(Integer contractId, Integer myCompanyId) {
        Contract contract = em.find(Contract.class, contractId);
        if (contractId != null) {
            if (contract.getStatus() == 5 && (int) contract.getPartyA() == myCompanyId) {
                return contract;
            }
        }
        return null;
    }

    public ServiceQuotation searchAValidQuotation(Integer quotationId, Integer myCompanyId) {
        ServiceQuotation quotation = em.find(ServiceQuotation.class, quotationId);
        if (quotation != null) {
            if (quotation.getStatus() == 3 && (int) quotation.getReceiverCompanyId() == myCompanyId) {
                return quotation;
            }
        }
        return null;
    }

    public int requestToModify(Integer contractId, String reason) {
        Contract contract = em.find(Contract.class, contractId);
        if (contract != null) {
            contract.setStatus(2);
            contract.setReasonModification(reason);

            em.merge(contract);
            em.flush();

            return 1;
        } else {
            return -1;
        }

    }

    public int modifyContract(Integer contractId, String conditionText) {
        Contract contract = em.find(Contract.class, contractId);
        if (contract != null) {
            contract.setStatus(3);
            contract.setCreatedDate(new Date());
            contract.setConditionText(conditionText);

            em.merge(contract);
            em.flush();

            return 1;
        } else {
            return -1;
        }
    }

    public int acceptContract(Integer contractId) {
        Contract contract = em.find(Contract.class, contractId);
        if (contract != null) {
            contract.setStatus(4);
            contract.setCreatedDate(new Date());

            em.merge(contract);
            em.flush();

            return 1;
        } else {
            return -1;
        }
    }

    public int renewContract(Integer contractId, Date startDate, Date endDate, Integer creatorId) {
        Contract contract = em.find(Contract.class, contractId);

        SystemUser user = em.find(SystemUser.class, creatorId);
        String name = user.getFirstName() + "  " + user.getLastName();

        if (contract != null) {
            Contract renewContract = new Contract();
            renewContract.setPartyA(contract.getPartyA());
            renewContract.setPartyB(contract.getPartyB());
            renewContract.setConditionText(contract.getConditionText());
            renewContract.setOrigin(contract.getOrigin());
            renewContract.setDestination(contract.getDestination());
            renewContract.setPrice(contract.getPrice());
            renewContract.setServiceQuotation(contract.getServiceQuotation());
            renewContract.setServiceType(contract.getServiceType());
            renewContract.setWarehouseId(contract.getWarehouseId());
            renewContract.setStorageZoneId(contract.getStorageZoneId());
            renewContract.setStorageBinId(contract.getStorageBinId());

            renewContract.setCreatedDate(new Date());
            renewContract.setStartDate(startDate);
            renewContract.setEndDate(endDate);

            renewContract.setContactPersonId(creatorId);
            renewContract.setContactPersonName(name);
            renewContract.setContactPersonNumber(user.getContactNumber());

            ServiceQuotation quotation = em.find(ServiceQuotation.class, contract.getServiceQuotation().getQuotationId());
            quotation.getContractList().add(renewContract);

            List<ServicePO> servicePOList = new ArrayList<>();
            renewContract.setServicePOList(servicePOList);

            em.persist(renewContract);
            em.merge(quotation);
            em.merge(contract);
            em.flush();

            return 1;
        } else {
            return -1;
        }
    }

    public List<Contract> viewSentContracts(Integer myCompanyId) {
        Query q = em.createNamedQuery("Contract.findByPartyA").setParameter("partyA", myCompanyId);
        return (List<Contract>) q.getResultList();
    }

    public List<Contract> viewReceivedContracts(Integer myCompanyId) {
        Query q = em.createNamedQuery("Contract.findByPartyB").setParameter("partyB", myCompanyId);
        return (List<Contract>) q.getResultList();
    }

    public String retrieveCompanyName(Integer companyId) {
        Company company = em.find(Company.class, companyId);
        return company.getName();
    }

    public int saveSignedContract(Integer contractId, byte[] pdfFileByteArray) {
        Contract contract = em.find(Contract.class, contractId);
        SignedContract signedContract = new SignedContract();
        signedContract.setSignedContractId(contractId);
        signedContract.setUploadedDate(new Date());
        signedContract.setPdf(pdfFileByteArray);
        signedContract.setContract(contract);

        contract.setSignedContract(signedContract);
        contract.setStatus(5);
        em.persist(signedContract);
        em.merge(contract);
        em.flush();

        return 1;
    }

    public boolean hasDuplicateContract(Integer quotationId) {
        ServiceQuotation quotation = em.find(ServiceQuotation.class, quotationId);
        if (quotation.getContractList().isEmpty() || quotation.getContractList() == null) {
            //No duplicate contract for this quotation
            return false;
        } else {
            return true;
        }
    }

}
