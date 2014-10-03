/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.crms.accountmanagementmodule;

import entity.Company;
import entity.ServiceCatalog;
import entity.ServiceQuotation;
import java.util.ArrayList;
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
public class ServiceCatalogSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    public int createServiceCatalog(Integer companyId, String serviceName, String description, boolean view, int type, double price) {
        ServiceCatalog serviceCatalog = new ServiceCatalog();
        serviceCatalog.setCompanyId(companyId);
        
        Query q = em.createNamedQuery("Company.findByCompanyId");
        q.setParameter("companyId", companyId);
        Company company = (Company)q.getSingleResult();
        serviceCatalog.setCompanyName(company.getName());
        serviceCatalog.setServiceName(serviceName);
        serviceCatalog.setServiceDescription(description);
        serviceCatalog.setPublicView(view);
        serviceCatalog.setServiceType(type);
        serviceCatalog.setPricePerTEU(price);
        
        serviceCatalog.setVoid1(false);

        List<ServiceQuotation> quotations = new ArrayList<>();
        serviceCatalog.setServiceQuotationList(quotations);

        em.persist(serviceCatalog);
        em.flush();

        return serviceCatalog.getServiceCatalogId();
    }

    public int updateServiceCatalog(Integer serviceCatalogId, String serviceName, String description, boolean view, int type, double price) {
        ServiceCatalog serviceCatalog = em.find(ServiceCatalog.class, serviceCatalogId);
        serviceCatalog.setServiceName(serviceName);
        serviceCatalog.setServiceDescription(description);
        serviceCatalog.setPublicView(view);
        serviceCatalog.setPricePerTEU(price);
        serviceCatalog.setServiceType(type);

        em.merge(serviceCatalog);
        em.flush();
        em.refresh(serviceCatalog);

        return serviceCatalog.getServiceCatalogId();

    }

    //return 1 if successfully remove and 2 if there are quotations attached to it but can be marked as void
    public int deleteServiceCatalog(Integer serviceCatalogId) {
        ServiceCatalog serviceCatalog = em.find(ServiceCatalog.class, serviceCatalogId);
        if (!serviceCatalog.getServiceQuotationList().isEmpty()) {
            System.out.println("ServiceCatalog cannot be deleted as quotation is not empty");
            serviceCatalog.setVoid1(true);
            em.merge(serviceCatalog);
            em.flush();
            return 2;
        } else {
            em.remove(serviceCatalog);
            em.flush();
            return 1;
        }
    }
    
    public List<ServiceCatalog> getAllPublicServices(){
        Query q = em.createQuery("SELECT s FROM ServiceCatalog s WHERE s.void1 = false AND s.publicView = true");
        return (List<ServiceCatalog>) q.getResultList();
    }

}
