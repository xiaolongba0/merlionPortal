package merlionportal.filters;

import java.io.IOException;
import javax.faces.context.FacesContext;
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
import merlionportal.utility.FilterBean;

@WebFilter(filterName = "MerlionPortalFilter", urlPatterns = {"/*"})
public class MerlionPortalFilter implements Filter {

    @Inject
    private FilterBean filterbean;

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

                if (requestURI.contains("/oes/")) {
                    //check if session is valid
                    if (userId != null) {
                        //Check company package                        
                        //1PL has access
                        if (filterbean.retrieveCompanyPac(companyId) == 1 || filterbean.retrieveCompanyPac(companyId) == 2) {
                            
                            
                        }
                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }

                }
                if (requestURI.contains("/crms/")) {
                    //check if session is valid
                    if (userId != null) {
                        //All company has access

                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }

                }
                if (requestURI.contains("/mrp/")) {
                    //check if session is valid
                    if (userId != null) {
                        //1PL has access
                        if (filterbean.retrieveCompanyPac(companyId) == 1 || filterbean.retrieveCompanyPac(companyId) == 2) {

                        }

                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }

                }
                if (requestURI.contains("/wms/")) {
                    //check if session is valid
                    if (userId != null) {
                            //Session is valid
                        //Check company package
                        //IPL and 3/4/5PL has access
                        if (filterbean.retrieveCompanyPac(companyId) == 1 || filterbean.retrieveCompanyPac(companyId) == 2 || filterbean.retrieveCompanyPac(companyId) == 4) {

                        }

                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }
                }
                if (requestURI.contains("/tms/")) {

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
                if (requestURI.contains("/grns/")) {

                    //check if session is valid
                    if (userId != null) {
                            //Session is valid
                        //Check company package
                        //2PL and 3/4/5PL has access
                        if (filterbean.retrieveCompanyPac(companyId) == 3 || filterbean.retrieveCompanyPac(companyId) == 4) {

                        }
                    } else {
                            //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath +"/index.xhtml";

                    }

                }
                if (requestURI.contains("/admin/")) {

                    //check if session is valid
                    if (userId != null) {
                        //Session is valid
                        //All company has access
                        //check if it is user user or company admin or normal user
                        if (filterbean.retrieveSystemUser(userId).getUserType()!= null) {
                            if (filterbean.retrieveSystemUser(userId).getUserType().equals("superuser")) {
                                //is super user
                            }
                        }
                        else if (filterbean.isSystemAdmin(userId)) {
                            //is system admin

                        } else {
                            //normal user, does not have right to go admin
                            System.out.println("==========here=====3=========");
                            redirect = true;
                            newPage = ctxtPath+"/user/dashboard.xhtml";
                        }
                    } else {
                        //session is not valid
                        //direct to login page
                        redirect = true;
                        newPage = ctxtPath + "/index.xhtml";

                    }

                }
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
