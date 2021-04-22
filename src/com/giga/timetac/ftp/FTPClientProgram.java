package com.giga.timetac.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;


/**
 * A program that demonstrates how to upload files from local computer
 * to a remote FTP server using Apache Commons Net API.
 * @author Sreekanth P
 */
public class FTPClientProgram {



    public static void main(String[] args) throws Exception{

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("logfile.txt", true)));

        FTPClient ftpClient = new FTPClient();
        try {

            out.println("---------------------******************************-------------------------------------------------------");
            out.println("Batch Started @ " + new Date());
             File file = new File("C:/FTP");
             URL[] urls = new URL[]{file.toURI().toURL()};
             ClassLoader loader = new URLClassLoader(urls);  
            ResourceBundle resourceBundle = ResourceBundle.getBundle("SFTPProperties_en", Locale.getDefault(), loader);
            String hostName = resourceBundle.getString("hostname");
            String port = resourceBundle.getString("port");
            String userName =  resourceBundle.getString("username");
            String password = resourceBundle.getString("password");
            String srcDir =  resourceBundle.getString("inputFolder");
            String destDir = resourceBundle.getString("destinationFolder");
            String fileNameStartsWith =  resourceBundle.getString("fileNameStartsWith");
            System.out.println("Host Name: "+hostName+" Port: "+port);
            System.out.println("UserName: "+userName+" Password: "+password);
            System.out.println("Source Directory 1234: "+srcDir+" fileNameStartsWith: "+fileNameStartsWith);

            ftpClient.connect(hostName, Integer.valueOf(port));
            ftpClient.login(userName, password);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            System.out.println(" File Copying Started");
            String outPutMsg =copyToSTP(srcDir, destDir, fileNameStartsWith, ftpClient);
            System.out.println(" File Copying Ended");
            out.println(outPutMsg);
            out.println("Batch Ended.@ " + new Date());
            out.println("-------------------------------******************************-------------------------------------------------------");

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            out.println("Exception occured::");
            out.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }

                if(out != null)
                    out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static String copyToSTP(String srcDir, String destDir, String fileNameStartsWith, FTPClient ftpClient){

        try {
            StringBuffer sb = new StringBuffer();
            Calendar midnightToday = new GregorianCalendar();
            midnightToday.set(Calendar.HOUR_OF_DAY, 0);
            midnightToday.set(Calendar.MINUTE, 0);

            midnightToday.set(Calendar.SECOND, 0);
            midnightToday.set(Calendar.MILLISECOND, 0);

            File directory = new File(srcDir);
            File[] files = directory.listFiles();

            for (File file : files) {
                Date lastMod = new Date(file.lastModified());
                if (file.getName().startsWith(fileNameStartsWith)
                        && lastMod.compareTo(midnightToday.getTime()) > 0) {
                    System.out.println("today file created is:: "+file.getName());
                    if(sendTOFTPServer(file, ftpClient))
                         sb.append("File >> " + file.getName() + " Copied Successfully.");
                    else
                        sb.append("File >> " + file.getName() + " Copied Failed.");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }

    public static boolean sendTOFTPServer(File file, FTPClient ftpClient){
        boolean msg = false;
        try {
            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = file;

            String firstRemoteFile = file.getName();
            InputStream inputStream = new FileInputStream(firstLocalFile);

            System.out.println("Start uploading file");
            boolean sucesOrFailure = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (sucesOrFailure) {
                System.out.println("The file is uploaded successfully.");
                msg =true;
            }else {
               msg = false;
            }

            inputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return msg;
    }

}
