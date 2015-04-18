/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nube.core.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import core.nube.util.logging.NubeLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author incognito
 */
public class Server {

    List<ServiceObjectDef> services = new ArrayList<>();
    //microservices name
    private String path = "";

    private Integer port = 0;

    public Server(List<ServiceObjectDef> services) {
        this.services = services;
    }

    public void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8181), 0);
        for (ServiceObjectDef input : services) {
            NubeLogger.debug("registring service: "+input.getClass().getName() + " on path: "+input.getPath());
            server.createContext(input.getPath(), new MyHandler(input.getService()));
        }
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler {

        private final Object player;

        public MyHandler(Object player) {
            this.player = player;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "hola ";
            String fullRequest = t.getRequestURI().toASCIIString();
            String fileAccess = "";
            //access html resources. 
            if (fullRequest.contains(".")) {
                fileAccess = fullRequest.substring(fullRequest.lastIndexOf(".") + 1, fullRequest.length());
            }
            if (null != fileAccess && fileAccess.length() > 0) {
                processViews(fullRequest, t);
            } else {
                processData(t, response);
            }
        }

        private void processData(HttpExchange t, String response) throws IOException, SecurityException {
            System.out.println("Request method is " + t.getRequestMethod());
            System.out.println("Get Request body" + t.getRequestURI());

            String requestBody = t.getRequestURI().getQuery();
            System.out.println(requestBody);

           /**
            
            Figure out how to name url params
            **/
            
            
           // String response = player.getData(name) + " time : " + System.currentTimeMillis();
            for (Method s : player.getClass().getMethods()) {

                if (s.getName().startsWith("getNube")) {

                    String requests[] = requestBody.split("&");
                    List<String> params = new ArrayList();

                    for (String requestInput : requests) {
                        params.add(requestInput.substring(requestInput.lastIndexOf("=") + 1, requestInput.length()));
                    }

                    try {
                        System.out.println("About to invoke" + s.getName());
                        System.out.println("About to invoke with " + params.size());
                        System.out.println("About to invoke with " + params);
                        response = (String) s.invoke(player, params.toArray());
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private void processViews(String fullRequest, HttpExchange t) {
            try {
                String fileName = fullRequest.substring(fullRequest.lastIndexOf("/") + 1, fullRequest.length());
                File f = new File("C:\\Nube\\html\\" + fileName);

                byte[] arBytes = new byte[(int) f.length()];
                FileInputStream is = new FileInputStream(f);
                is.read(arBytes);

                t.sendResponseHeaders(200, arBytes.length);
                OutputStream os = t.getResponseBody();
                os.write(arBytes);
                os.close();
            } catch (Exception e) {
                NubeLogger.error("Problem with file " + e.toString());
                e.printStackTrace();
            }
            //we have a file let's server it
        }
    }

}
