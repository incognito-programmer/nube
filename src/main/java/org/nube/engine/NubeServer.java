/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nube.engine;

import core.nube.util.logging.NubeLogger;
import org.nube.core.annotations.MicroService;
import org.nube.core.api.Server;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nube.core.api.ServiceObjectDef;
import org.reflections.Reflections;

/**
 * Engine that initiates the whole cycle
 *
 * @author incognito
 */
public class NubeServer {

    private  String packageDir = "";

    public String getPackageDir() {
        return packageDir;
    }

    public void setPackageDir(String packageDir) {
        this.packageDir = packageDir;
    }

    public  void start() throws IllegalArgumentException {
        Reflections reflections = new Reflections(packageDir);
        Set<Class<?>> annotated
                = reflections.getTypesAnnotatedWith(MicroService.class);

        if (annotated != null && annotated.size() > 1) {
//we are working ona multi service environment
        }

        List<ServiceObjectDef> serviceObjects = new ArrayList<>();
        for (Class s : annotated) {
            try {
                Annotation annotation = s.getAnnotation(MicroService.class);
                String path = "";
                Integer port = 0;
                try {
                    try {
                        path = (String) annotation.annotationType().getMethod("path").invoke(annotation);
                        port = (int) annotation.annotationType().getMethod("port").invoke(annotation);
                        NubeLogger.debug("Instatiating server with path: " + path + " and port: " + port);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(NubeServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(NubeServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                Object object = (Object) s.newInstance();
                ServiceObjectDef serviceDef= new ServiceObjectDef();
                serviceDef.setPath(path);
                serviceDef.setService(object);
                serviceObjects.add(serviceDef);
                
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(NubeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //get ready to start server
        Server a = new Server(serviceObjects);
        try {
            a.start();
        } catch (Exception ex) {
            Logger.getLogger(NubeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
