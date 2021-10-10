
DNA Gen Detector.
 
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

After successfully init, we can call some http GET requests to check if a string of some gene exists on the file.<BR>
(GEN must starts with 'AAAAAAAAAAA' prefix to be searched on the file.)<BR>
The url is:<BR>

    http://((<ip>:8084)|<domain name>)/genes/find/<GEN>
