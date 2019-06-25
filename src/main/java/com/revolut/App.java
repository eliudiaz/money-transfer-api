package com.revolut;

import com.revolut.api.resources.AppResourcesConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletProperties;

public class App {

    public static void main(String[] args) throws Exception {
        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        final Server server = new Server(8080);
        server.setHandler(context);

        final ServletHolder apiServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/api/v1/*");
        apiServlet.setInitOrder(0);
        apiServlet.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, AppResourcesConfig.class.getCanonicalName());


        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

}
