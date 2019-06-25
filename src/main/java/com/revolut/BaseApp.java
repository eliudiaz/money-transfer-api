package com.revolut;

import com.revolut.api.resources.AppResourcesConfig;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletProperties;

@Builder
@Getter
@Setter
@Slf4j
public class BaseApp {

    private String contextPath;
    private int port;
    private String basePath;
    private Server server;

    public void launch(boolean launchNewThread) throws Exception {
        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(contextPath);

        server = new Server(port);
        server.setHandler(context);

        final ServletHolder apiServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, basePath);
        apiServlet.setInitOrder(0);
        apiServlet.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, AppResourcesConfig.class.getCanonicalName());
        if (launchNewThread) {
            final Thread newProcess = new Thread(() -> {
                try {
                    BaseApp.this.start();
                } catch (Exception ex) {
                    log.error("Error launching in new thread >> ", ex);
                }
            });
            newProcess.start();
        } else {
            this.start();
        }
    }

    protected void start() throws Exception {
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

    public void stop() throws Exception {
        server.stop();
    }
}
