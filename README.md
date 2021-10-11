
DNA Gen Detector.

Initiate DNA file by lexicographically sorted (up to 2000 characters) gen file offset list. (O(nlog(n)) complexity)<br>
It gives a O(log(n)) complexity for checking existence of a gen string.

You can use this project's jar (found under 'dist' folder) as a library in a stand alone application. No container is needed (glassfish/Tomcat).<br>
(Should add to your project the jar 'JettyServerLib-1.0-SNAPSHOT-jar-with-dependencies.jar'. <br>[found under 'lib' folder])

Here some use case of the library.

    GenDetector genDetector = GenDetector.getInstance();
    File file = new File("DNA_file_path");
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

After successfully init, we can call some http GET requests to check if a string of some gene exists on the file.<br>
(GEN must starts with 'AAAAAAAAAAA' prefix to be searched on the file.)<br>
The url is:<br>

    http://((<ip>:8084)|<domain name>)/genes/find/<GEN>
    
After finish with the GenDetector, you can call GenDetector.getInstance().cleanup() for stopping the server from listenning.
    

 
 
