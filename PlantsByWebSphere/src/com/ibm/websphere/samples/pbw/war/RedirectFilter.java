package com.ibm.websphere.samples.pbw.war;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
//* add the following
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletResponse;
//* add the following 
import javax.servlet.http.HttpServletResponse ;
//import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.*;

/**
 * Servlet Filter implementation class RedirectFilter
 */

@WebFilter(filterName="RedirectFilter", 
         servletNames={"com.ibm.websphere.samples.pbw.war.ImageServlet", "FacesServlet"},
          urlPatterns="*" ) 
 
 public class RedirectFilter implements Filter {

	// Read the Kubernetes service environment variables for the Image Service and 
	// construct the Image Service service endpoint 
	String httpPre = "http://" ;
	String colon = ":" ;
	String pbwisIp = System.getenv("PBWIS_SERVICE_PORT_9081_TCP_ADDR") ;
	String pbwisPort = System.getenv("PBWIS_SERVICE_SERVICE_PORT") ;
	
	//comment and uncomment below for local testing
	
	String serviceEndpoint = httpPre+pbwisIp+colon+pbwisPort;
	// String serviceEndpoint = "http://localhost:9081" ;
	 
@Override
 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    
	if(response.isCommitted() || (serviceEndpoint == null)) {
         //can't do anything as the response has already been committed
         chain.doFilter(request, response);
         return;
     }
     HttpServletResponse resp = (HttpServletResponse) response;
     HttpServletRequest req = (HttpServletRequest) request;
     if("ln=images".equals(req.getQueryString())) {
         String path = req.getServletPath();
         if(path.endsWith(".jsf")) {
             String resource = path.substring(path.lastIndexOf('/'), path.lastIndexOf('.'));
           //  resp.sendRedirect(serviceEndpoint + "/images/resources" + resource);
             resp.sendRedirect(serviceEndpoint + "/resources/images" + resource);
             return;
         }
     }
     if(req.getServletPath().endsWith("/ImageServlet")) {
     //    resp.sendRedirect(serviceEndpoint + "/images/product/inventory/" + req.getParameterMap().get("inventoryID")[0]);
         resp.sendRedirect(serviceEndpoint + "/product/inventory/" + req.getParameterMap().get("inventoryID")[0]);
         return;
     }
     chain.doFilter(request, response);
 }
 
public void init(FilterConfig fConfig) throws ServletException {
	// TODO Auto-generated method stub
}
 
public void destroy() {
	// TODO Auto-generated method stub
}
}
