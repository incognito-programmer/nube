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
import org.reflections.Reflections;

/**
 * Engine that initiates the whole cycle
 *
 * @author incognito
 */
public class NubeServer {

    private static String packageDir = "";

    public String getPackageDir() {
        return packageDir;
    }

    public void setPackageDir(String packageDir) {
        this.packageDir = packageDir;
    }

    public static void start() throws IllegalArgumentException {
        Reflections reflections = new Reflections(packageDir);
        Set<Class<?>> annotated
                = reflections.getTypesAnnotatedWith(MicroService.class);

        if (annotated != null && annotated.size() > 1) {
//we are working ona multi service environment

        }

        List<Object> serviceObjects = new ArrayList<Object>();

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
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(NubeServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(NubeServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                Object object = (Object) s.newInstance();
                Server a = new Server(null, path, port, object);
                try {
                    a.start();
                } catch (Exception ex) {
                    Logger.getLogger(NubeServer.class.getName()).log(Level.SEVERE, null, ex);
                }

                serviceObjects.add(object);
                break;
            } catch (InstantiationException ex) {
                Logger.getLogger(NubeServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(NubeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //get ready to start server
    }
}
