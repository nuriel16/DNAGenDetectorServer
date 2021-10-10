/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.nio.file.Files;
import jettyserver.JettyServerNew;

/**
 *
 * @author nuriel
 */
public class Server {

    JettyServerNew server;
    int port;
    private final static String SERVLET_PATH = "genes";

    public Server(int port) {
        this.port = port;
    }

    public boolean startServer() {
        boolean success;
        try {
            server = new JettyServerNew("", port);
            server.init();
            server.addServlet(1, GenEntryPoint.class, SERVLET_PATH);
            success = server.startServer(); // listen on http://((<ip>:<port>)|<domain name>)/genes/* on GenEntryPoint class.
            if (success) {
                System.out.println(server.getInfo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public void stopServer() {
        if (server != null) {
            server.cleanup();
        }
    }
}
