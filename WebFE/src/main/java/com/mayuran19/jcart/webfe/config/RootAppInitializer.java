package com.mayuran19.jcart.webfe.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by mayuran on 16/7/16.
 * This class will replace the web.xml file. All the configurations in web.xml can be configured
 * in this class as per servlet API 3.0
 */
public class RootAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        /*
        All the common configuration for the whole application will be loaded by the RootAppConfig
        class.
         */
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootAppConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        /*
        Add a DispatcherServlet for "/*" context
         */
        AnnotationConfigWebApplicationContext servicesContext = new AnnotationConfigWebApplicationContext();
        servicesContext.setParent(rootContext);
        servicesContext.register(WebFEServletConfig.class);
        ServletRegistration.Dynamic servicesDispatchServlet = servletContext.addServlet("services", new DispatcherServlet(servicesContext));
        servicesDispatchServlet.setLoadOnStartup(1);
        servicesDispatchServlet.addMapping("/*");
    }
}
