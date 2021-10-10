
DNA Gen Detector.

You can use it as a stand alone lib. No container is needed (glassfish/Tomcat).<br>
(Should add the jar 'JettyServerLib-1.0-SNAPSHOT-jar-with-dependencies.jar' to project libraries. <br>[found under 'DNAGenDetectorServer/GenDetectorServer/lib/'])

Here some use case of the project.

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
    

 
 
