/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.nube.util.logging;

/**
 *
 * @author incognito
 */
public class NubeLogger {

    public static void error(String message) {
        System.out.println("error: " + message);
    }

    public static void debug(String message) {
        System.out.println("debug: " + message);
    }

    public static void warning(String message) {
        System.out.println("warning: " + message);

    }

    public static void info(String message) {
        System.out.println("info: " + message);
    }
}
