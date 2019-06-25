package com.revolut.api.resources;

import com.google.inject.Guice;
import com.revolut.exception.ExceptionsHandler;
import com.revolut.platform.AppModule;
import com.revolut.platform.ObjectMapperContextResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;

public class AppResourcesConfig extends ResourceConfig {

    @Inject
    public AppResourcesConfig(ServiceLocator serviceLocator) {
        packages(AppResourcesConfig.class.getPackage().getName());

        register(ObjectMapperContextResolver.class);
        register(ExceptionsHandler.class);

        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(Guice.createInjector(new AppModule()));
    }


}
