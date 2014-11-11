package merlionportal.filters;

import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import merlionportal.ci.administrationmodule.SystemAccessRightSessionBean;
import merlionportal.utility.FilterBean;

@WebFilter(filterName = "MerlionPortalFilter", urlPatterns = {"/*"})
public class MerlionPortalFilter implements Filter {

    @Inject
    private FilterBean filterbean;
    @EJB
    SystemAccessRightSessionBean systemAccessRightsb;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        String ctxtPath = httpRequest.getContextPath();
        boolean redirect = false;
        String newPage = "";

        //Switch to HTTPS
        if (httpRequest.getServerPort() != 8181) {
            httpResponse.sendRedirect(httpResponse.encodeRedirectURL("https://" + request.getServerName() + ":8181" + requestURI));
        } else {
            if (requestURI != null && requestURI.length() > 0) {
                HttpSession session = httpRequest.getSession(false);
                Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
                Integer companyId = (session != null) ? (Integer) session.getAttribute("companyId") : null;

//***********************************
//***             OES             ***
//***                             ***
//***********************************
                if (requestURI.contains("/oes/")) {
                    //check if session is valid
                    if (userId != null) {
                        //Check company package 
                        if (filterbean.retrieveSystemUser(userId).getUserType() != null) {
                            if (filterbean.retrieveSystemUser(userId).getUserType().equals("superuser")) {
                                //is super user can proceed
                            }
                        } else {
                            //1PL has access
                            if (filterbean.retrieveCompanyPac(companyId) == 1 || filterbean.retrieveCompanyPac(companyId) == 2) {
                                //check user access right
                                if (systemAccessRightsb.canUseOES(userId)) {
                                    if (requestURI.contains("/oes/createso.xhtml")) {
                                        if (systemAccessRightsb.checkOESGenerateSO(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/displayallproducts.xhtml")) {
                                        if (systemAccessRightsb.checkOESGenerateSO(userId) || systemAccessRightsb.checkOESGeneratePO(userId) || systemAccessRightsb.checkOESGenerateQuotation(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/displayallquotations.xhtml")) {
                                        if (systemAccessRightsb.checkOESGenerateSO(userId) || systemAccessRightsb.checkOESGeneratePO(userId) || systemAccessRightsb.checkOESGenerateQuotation(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/displayallso.xhtml")) {
                                        if (systemAccessRightsb.checkOESGenerateSO(userId) || systemAccessRightsb.checkOESGeneratePO(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/displayrequestinfor.xhtml")) {
                                        if (systemAccessRightsb.checkOESGeneratePO(userId) || systemAccessRightsb.checkOESGenerateQuotation(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/editsavedorder.xhtml")) {
                                        if (systemAccessRightsb.checkOESGeneratePO(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/orderdetail.xhtml")) {
                                        if (systemAccessRightsb.checkOESGeneratePO(userId) || systemAccessRightsb.checkOESGenerateSO(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/pogeneration.xhtml")) {
                                        if (systemAccessRightsb.checkOESGeneratePO(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/requestforreturn.xhtml")) {
                                        if (systemAccessRightsb.checkOESGenerateSO(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/retrievesavedorder.xhtml")) {
                                        if (systemAccessRightsb.checkOESGeneratePO(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/viewallpo.xhtml")) {
                                        if (systemAccessRightsb.checkOESGeneratePO(userId) || systemAccessRightsb.checkOESGenerateSO(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/viewallrequests.xhtml")) {
                                        if (systemAccessRightsb.checkOESGeneratePO(userId) || systemAccessRightsb.checkOESGenerateSO(userId) || systemAccessRightsb.checkOESGenerateQuotation(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/viewquotationinfor.xhtml")) {
                                        if (systemAccessRightsb.checkOESGeneratePO(userId) || systemAccessRightsb.checkOESGenerateSO(userId) || systemAccessRightsb.checkOESGenerateQuotation(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/oes/generatequotation.xhtml")) {
                                        if (systemAccessRightsb.checkOESGenerateQuotation(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/oes/oesindexpage.xhtml";
                                        }
                                    }

                                } else {
                                    //User cannot access OES
                                    redirect = true;
                                    //System admin
                                    if (filterbean.isSystemAdmin(userId)) {
                                        newPage = ctxtPath + "/admin/dashboard.xhtml";

                                    } else {
                                        //Normal user
                                        newPage = ctxtPath + "/user/dashboard.xhtml";
                                    }
                                }

                            } else {
                                //User cannot access OES as not included in company package
                                redirect = true;
                                //System admin
                                if (filterbean.isSystemAdmin(userId)) {
                                    newPage = ctxtPath + "/admin/dashboard.xhtml";

                                } else {
                                    //Normal user
                                    newPage = ctxtPath + "/user/dashboard.xhtml";
                                }
                            }
                        }

                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }

                }

//***********************************
//***            CRMS             ***
//***                             ***
//***********************************                
                if (requestURI.contains("/crms/")) {
                    //check if session is valid
                    if (userId != null) {
                        if (filterbean.retrieveSystemUser(userId).getUserType() != null) {
                            if (filterbean.retrieveSystemUser(userId).getUserType().equals("superuser")) {
                                //is super user can proceed
                            }
                        } else {
                            //All company has access
                        }

                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }

                }

//***********************************
//***             MRP             ***
//***                             ***
//***********************************
                if (requestURI.contains("/mrp/")) {
                    //check if session is valid
                    if (userId != null) {
                        //1PL has access
                        if (filterbean.retrieveSystemUser(userId).getUserType() != null) {
                            if (filterbean.retrieveSystemUser(userId).getUserType().equals("superuser")) {
                                //is super user can proceed
                            }
                        } else {
                            if (filterbean.retrieveCompanyPac(companyId) == 1 || filterbean.retrieveCompanyPac(companyId) == 2) {
                                //check if user can user mrp
                                if (systemAccessRightsb.canUseMRP(userId)) {
                                    //Check user right for diff pages
                                    if (requestURI.contains("/mrp/addnewcomponent.xhtml")) {
                                        if (systemAccessRightsb.checkMRPManageProduct(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/mrp/mrp.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/mrp/addnewproduct.xhtml")) {
                                        if (systemAccessRightsb.checkMRPManageProduct(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/mrp/mrp.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/mrp/viewallproducts.xhtml")) {
                                        if (systemAccessRightsb.checkMRPManageProduct(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/mrp/mrp.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/mrp/viewbom.xhtml")) {
                                        if (systemAccessRightsb.checkMRPManageProduct(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/mrp/mrp.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/mrp/forecast.xhtml")) {
                                        if (systemAccessRightsb.checkMRPUseForecast(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/mrp/mrp.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/mrp/forecastgetperiodicity.xhtml")) {
                                        if (systemAccessRightsb.checkMRPUseForecast(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/mrp/mrp.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/mrp/forecastviewhistory.xhtml")) {
                                        if (systemAccessRightsb.checkMRPUseForecast(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/mrp/mrp.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/mrp/forecastresult.xhtml")) {
                                        if (systemAccessRightsb.checkMRPUseForecast(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/mrp/mrp.xhtml";
                                        }
                                    }

                                } else {

                                    //User Cannot access MRP System
                                    redirect = true;
                                    //System admin
                                    if (filterbean.isSystemAdmin(userId)) {
                                        newPage = ctxtPath + "/admin/dashboard.xhtml";

                                    } else {
                                        //Normal user
                                        newPage = ctxtPath + "/user/dashboard.xhtml";
                                    }
                                }
                            } else {
                                //User Cannot access MRP System as not included in package
                                redirect = true;
                                //System admin
                                if (filterbean.isSystemAdmin(userId)) {
                                    newPage = ctxtPath + "/admin/dashboard.xhtml";

                                } else {
                                    //Normal user
                                    newPage = ctxtPath + "/user/dashboard.xhtml";
                                }
                            }
                        }
                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }

                }

//***********************************
//***             WMS             ***
//***                             ***
//***********************************
                if (requestURI.contains("/wms/")) {
                    //check if session is valid
                    if (userId != null) {
                        //Session is valid
                        if (filterbean.retrieveSystemUser(userId).getUserType() != null) {
                            if (filterbean.retrieveSystemUser(userId).getUserType().equals("superuser")) {
                                //is super user can proceed
                            }
                        } else {
                            //Check company package
                            //IPL and 3/4/5PL has access
                            if (filterbean.retrieveCompanyPac(companyId) == 1 || filterbean.retrieveCompanyPac(companyId) == 2 || filterbean.retrieveCompanyPac(companyId) == 4) {
                                //check if user can user mrp
                                if (systemAccessRightsb.canUseWMS(userId)) {
                                    if (requestURI.contains("/wms/addnewwarehouse.xhtml")) {
                                        if (systemAccessRightsb.checkWMSManageWarehouse(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/wms/wmsindex.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/wms/addstoragetype.xhtml")) {
                                        if (systemAccessRightsb.checkWMSManageWarehouse(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/wms/wmsindex.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/wms/addstoragebin.xhtml")) {
                                        if (systemAccessRightsb.checkWMSManageWarehouse(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/wms/wmsindex.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/wms/viewallwarehouse.xhtml")) {
                                        if (systemAccessRightsb.checkWMSManageWarehouse(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/wms/wmsindex.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/wms/viewstoragebin.xhtml")) {
                                        if (systemAccessRightsb.checkWMSManageWarehouse(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/wms/wmsindex.xhtml";
                                        }
                                    }
                                    if (requestURI.contains("/wms/viewstoragetype.xhtml")) {
                                        if (systemAccessRightsb.checkWMSManageWarehouse(userId)) {
                                        } else {
                                            //User cannot access this page
                                            redirect = true;
                                            newPage = ctxtPath + "/wms/wmsindex.xhtml";
                                        }
                                    }
                                } else {
                                    //User Cannot use WMS
                                    redirect = true;
                                    //System admin
                                    if (filterbean.isSystemAdmin(userId)) {
                                        newPage = ctxtPath + "/admin/dashboard.xhtml";

                                    } else {
                                        //Normal user
                                        newPage = ctxtPath + "/user/dashboard.xhtml";
                                    }
                                }
                            } else {
                                //User Cannot use WMS as not included in company package
                                redirect = true;
                                //System admin
                                if (filterbean.isSystemAdmin(userId)) {
                                    newPage = ctxtPath + "/admin/dashboard.xhtml";

                                } else {
                                    //Normal user
                                    newPage = ctxtPath + "/user/dashboard.xhtml";
                                }
                            }
                        }

                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }
                }

//***********************************
//***             TMS             ***
//***                             ***
//***********************************
                if (requestURI.contains("/tms/")) {

                    //check if session is valid
                    if (userId != null) {
                        //Session is valid
                        if (filterbean.retrieveSystemUser(userId).getUserType() != null) {
                            if (filterbean.retrieveSystemUser(userId).getUserType().equals("superuser")) {
                                //is super user can proceed
                            }
                        } else {
                            //All company has access
                        }

                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }

                }

//***********************************
//***            GRNS             ***
//***                             ***
//***********************************               
                if (requestURI.contains("/grns/")) {

                    //check if session is valid
                    if (userId != null) {
                        //Session is valid
                        if (filterbean.retrieveSystemUser(userId).getUserType() != null) {
                            if (filterbean.retrieveSystemUser(userId).getUserType().equals("superuser")) {
                                //is super user can proceed
                            }
                        } else {
                            //Check company package
                            //2PL and 3/4/5PL has access
                        }
                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }

                }
//***********************************
//***           ADMIN             ***
//***                             ***
//***********************************                
                if (requestURI.contains("/admin/")) {

                    //check if session is valid
                    if (userId != null) {
                        //Session is valid
                        //All company has access
                        //check if it is user user or company admin or normal user
                        if (filterbean.retrieveSystemUser(userId).getUserType() != null) {
                            if (filterbean.retrieveSystemUser(userId).getUserType().equals("superuser")) {
                                //is super user
                            }
                        } else if (filterbean.isSystemAdmin(userId)) {
                            //is system admin
                            //System Admin cannot view all subscribers
                            if (requestURI.contains("/admin/viewallsubscribers.xhtml")) {
                                redirect = true;
                                newPage = ctxtPath + "/admin/dashboard.xhtml";
                            }

                        } else {
                            //normal user, does not have right to go admin
                            System.out.println("==========here=====3=========");
                            redirect = true;
                            newPage = ctxtPath + "/user/dashboard.xhtml";
                        }
                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }

                }

//***********************************
//***            USER             ***
//***                             ***
//***********************************               
                if (requestURI.contains("/user/")) {
                    //check if session is valid
                    if (userId != null) {
                        //Session is valid
                        //All company has access

                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }

                }

                if (redirect) {
                    httpResponse.sendRedirect(newPage);
                } else {
                    chain.doFilter(request, response);
                }
            }
        }
    }
}
