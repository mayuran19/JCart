package com.mayuran19.jcart.webbe.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by mayuran on 18/7/16.
 */
public class WebBEAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        /*
        All the common configuration for the whole application will be loaded by the RootAppConfig
        class.
         */
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(WebBEAppConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        /*
        Add a DispatcherServlet for "/*" context
         */
        AnnotationConfigWebApplicationContext servicesContext = new AnnotationConfigWebApplicationContext();
        servicesContext.setParent(rootContext);
        servicesContext.register(WebBEServletConfig.class);
        ServletRegistration.Dynamic servicesDispatchServlet = servletContext.addServlet("webbe", new DispatcherServlet(servicesContext));
        servicesDispatchServlet.setLoadOnStartup(1);
        servicesDispatchServlet.addMapping("/");
    }
}
