/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import gendetecor.GenDetector;
import java.io.File;

/**
 *
 * @author Nuriel
 */
public class Test {

    public static void main(String[] args) {
        GenDetector genDetector = GenDetector.getInstance();
        File file = new File("C:\\Users\\nuriel\\Documents\\NetBeansProjects\\GenDetectorServer\\genTest.txt");
        if (genDetector.init(8084, file)) {
            System.out.println("GenDetector initiated successfully.");
            try {
                if (genDetector.isDebug()) {
                    genDetector.printList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("GenDetector init failed.");
        }
    }
}
