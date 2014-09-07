package merlionportal.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "MerlionPortalFilter", urlPatterns = {"/*"})
public class MerlionPortalFilter implements Filter {

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
        boolean redirect = false;
        String newPage = "";

        //Switch to HTTPS
        if (httpRequest.getServerPort() != 8181) {
            httpResponse.sendRedirect(httpResponse.encodeRedirectURL("https://" + request.getServerName() + ":8181" + requestURI));
        } else {
//        if (requestURI != null && requestURI.length() > 0) {
//        }
            if (redirect) {
//            httpServletResponse.sendRedirect(newPage);
            } else {
                chain.doFilter(request, response);
            }
        }
    }
}
