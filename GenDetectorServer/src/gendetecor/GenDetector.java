/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gendetecor;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import services.Server;

/**
 *
 * @author Nuriel
 */
public class GenDetector {

    private static GenDetector instance;
    /**
     * DNA DNAFile.
     */
    File DNAFile;

    /**
     * Lexicographically sorted (up to 2000 characters) gen file offset list.
     * Gives a log(n) complexity for checking existence of a gen string.
     */
    List<Integer> genFileOffsetList = new ArrayList<>();
    FileInitializer fileInitializer;

    /**
     * Jetty rest server wrapper. listen to http requests.
     */
    Server server;

    private boolean debug;
    public static final String GEN_PREFIX = "AAAAAAAAAAA";

    private GenDetector() {
        fileInitializer = new FileInitializer();
    }

    public static GenDetector getInstance() {
        if (instance == null) {
            instance = new GenDetector();
        }
        return instance;
    }

    public boolean init(int serverPort, File file) {
        System.out.println("GenDetector init starts.");
        this.DNAFile = file;
        if (fileInitializer.init()) {
            server = new Server(serverPort);
            if (server.startServer()) {
                return true;
            } else {
                System.out.println("GenDetector init: Failed to start server.");
            }
        }
        return false;
    }

    public void cleanup() {
        if (server != null) {
            server.stopServer();
        }
    }

    public boolean genExists(String gen) throws Exception {
        if (genFileOffsetList == null) {
            return false;
        }
        int location = binarySearch(genFileOffsetList, 0, genFileOffsetList.size(), gen);
        return location > -1;
    }

    int binarySearch(List<Integer> list, int l, int r, String gen) throws Exception {
        if (r >= l) {
            int mid = l + (r - l) / 2;
            int fileIdx = list.get(mid);
            int compare = gen.compareTo(getFileGen(fileIdx, gen.length()));
            if (compare == 0) {
                return mid;
            }
            if (compare < 0) {
                return binarySearch(list, l, mid - 1, gen);
            }
            return binarySearch(list, mid + 1, r, gen);
        }
        return -1;
    }

    private String getFileGen(int fileIdx, int length) throws Exception {
        byte[] fileGen = new byte[length];
        RandomAccessFile raf = new RandomAccessFile(DNAFile, "r");
        raf.seek(fileIdx - 1);
        if (raf.read(fileGen) > -1) {
            return new String(fileGen);
        }
        return "";
    }

    /**
     * Print the gen location list.
     *
     * @throws Exception
     */
    public void printList() throws Exception {
        for (int i = 0; i < genFileOffsetList.size(); i++) {
            int fileIdx = genFileOffsetList.get(i);
            System.out.printf("%d - %d - %s\n", i, fileIdx, getFileGen(fileIdx, 10));
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public class FileInitializer {

        public boolean init() {
            boolean result = true;
            if (DNAFile == null || !DNAFile.exists()) {
                System.out.println("FileInitializer init: DNAFile is null or not exists.");
                result = false;
            } else {
                try {
                    fillGenFileOffsetList();
                } catch (Exception e) {
                    e.printStackTrace();
                    result = false;
                }
            }
            return result;
        }

        private void fillGenFileOffsetList() throws Exception {
            Calendar lastLog = Calendar.getInstance();
            FileInputStream fin = new FileInputStream(DNAFile);
            byte[] data = new byte[11];
            fin.read(data);
            int idx = 11;
            checkGenPrefix(data, idx);
            int b;
            while ((b = fin.read()) != -1) {
                System.arraycopy(data, 1, data, 0, 10);
                data[10] = (byte) b;
                checkGenPrefix(data, ++idx);
                Calendar thirtySecAgo = Calendar.getInstance();
                thirtySecAgo.add(Calendar.SECOND, -30);//log every 30 seconds.
                if (thirtySecAgo.after(lastLog)) {
                    System.out.println("FileInitializer: Still fill gen file offset list.");
                    lastLog = Calendar.getInstance();
                }
            }
            System.out.println("FileInitializer: Finished to fill genFileOffsetList.");
        }

        private void checkGenPrefix(byte[] data, int idx) throws Exception {
            String str = new String(data);
            if (GEN_PREFIX.equals(str)) {
                RandomAccessFile raf = new RandomAccessFile(DNAFile, "r");
                raf.seek(idx);
                byte[] genBytes = new byte[2000];
                if (raf.read(genBytes) > 0) {
                    String genStr = new String(genBytes);
                    insertionSort(genStr, idx + 1);
                }
            }
        }

        void insertionSort(String gen, int fileIdx) throws Exception {
            int loc;
            if (genFileOffsetList.isEmpty()) {
                loc = 0;
            } else {
                loc = insertBinarySearch(genFileOffsetList, gen, 0, genFileOffsetList.size());
                if (loc >= genFileOffsetList.size()) {
                    genFileOffsetList.add(fileIdx);
                    return;
                }
            }
            genFileOffsetList.add(loc, fileIdx);
        }

        int insertBinarySearch(List<Integer> list, String gen, int low, int high) throws Exception {
            int compare, fileIdx;
            if (high <= low) {
                fileIdx = list.get(Math.min(low, list.size() - 1));
                compare = gen.compareTo(getFileGen(fileIdx, gen.length()));
                return (compare > 0) ? (low + 1) : low;
            }
            int mid = (low + high) / 2;
            fileIdx = list.get(mid);
            compare = gen.compareTo(getFileGen(fileIdx, gen.length()));
            if (compare == 0) {
                return mid + 1;
            }
            if (compare > 0) {
                return insertBinarySearch(list, gen, mid + 1, high);
            }
            return insertBinarySearch(list, gen, low, mid - 1);
        }
    }
}
