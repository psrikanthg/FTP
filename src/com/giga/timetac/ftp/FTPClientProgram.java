package com.giga.timetac.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.attribute.PosixFilePermission;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;

import static com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text.NEW_LINE;

/**
 * A program that demonstrates how to upload files from local computer
 * to a remote FTP server using Apache Commons Net API.
 * @author Sreekanth P
 */
public class FTPClientProgram {

    static PrintWriter out = null;


    public static void main(String[] args) throws Exception{

        out = new PrintWriter(new BufferedWriter(new FileWriter("logfile.txt", true)));

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
            String dirNameToCreate =  resourceBundle.getString("dirNameToCreate");
            String fileNameToCreate =  resourceBundle.getString("fileNameToCreate");

            System.out.println("Host Name : "+hostName+" Port : "+port);
            System.out.println("UserName : "+userName+" Password : "+password);
            System.out.println("Source Directory : "+srcDir);
            System.out.println("FileNameStartsWith : "+fileNameStartsWith);
            System.out.println("DirNameToCreate : "+dirNameToCreate);
            System.out.println("FileNameToCreate : "+fileNameToCreate);

            ftpClient.connect(hostName, Integer.valueOf(port));
            ftpClient.login(userName, password);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            System.out.println(" File Copying Started");
            String outPutMsg =copyToSTP(srcDir, destDir, fileNameStartsWith, dirNameToCreate, fileNameToCreate, ftpClient);
            System.out.println(" File Copying Ended");
            deleteInputFile(srcDir, fileNameStartsWith);
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

    public static void deleteInputFile(String srcDir, String fileNameStartsWith){
        out.println(" ****** starting deleteInputFile  *********");
        try {

            File directory = new File(srcDir);
            File[] files = directory.listFiles();
            System.out.println("  deleteInputFile:::  ");
            for (File file : files) {
                out.print(file.getName());

                if(fileNameStartsWith!=null && file.getName().contains(fileNameStartsWith)){
                    Files.delete(Paths.get(srcDir+"/"+file.getName()));
                    out.print(" been deleted Successfully::");
                }else {
                    out.print(" not found with "+fileNameStartsWith+"\n");
                }

            }

        }  catch (IOException io) {
                io.printStackTrace();
                out.println(" ****** IOException  deleteInputFile  *********"+io.getMessage());
                System.out.println(" IOException:::  "+io.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                out.println(" ****** Exception  deleteInputFile  *********"+e.getMessage());
            System.out.println(" Exception:::  "+e.getMessage());

            }
        System.out.println(" deleteInputFile:::  ");
        out.println(" ****** end deleteInputFile  *********");

    }

    public static String copyToSTP(String srcDir, String destDir, String fileNameStartsWith, String dirToCreate, String fileNameToCreate, FTPClient ftpClient){

        StringBuffer sb = new StringBuffer();
        try {
            out.println(" ****** starting copyToSTP  *********");
           // File tempFile = new File("C:/Micro ID Sdn. Bhd - Copy/music.txt");
            //SRW_210616_121500  ddMMyyyy
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd_HHmmss");
            String fileName  =fileNameToCreate+now.format(formatter);
            // Path path1 = Paths.get("E:/Micro ID Sdn. Bhd - Copy1");
            Path path1 = Paths.get(dirToCreate);

            Calendar midnightToday = new GregorianCalendar();
            midnightToday.set(Calendar.HOUR_OF_DAY, 0);
            midnightToday.set(Calendar.MINUTE, 0);

            midnightToday.set(Calendar.SECOND, 0);
            midnightToday.set(Calendar.MILLISECOND, 0);
            if(Files.notExists(path1)){
                try {
                    Files.createDirectories(path1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // using PosixFilePermission to set file permissions
                Set<PosixFilePermission> perms = new HashSet<>();
                perms.add( PosixFilePermission.OWNER_READ );
                perms.add( PosixFilePermission.OWNER_WRITE );
                perms.add( PosixFilePermission.GROUP_READ );
                perms.add( PosixFilePermission.GROUP_WRITE );
                perms.add( PosixFilePermission.OTHERS_READ );
                perms.add( PosixFilePermission.OTHERS_WRITE );

                    //Files.setPosixFilePermissions(Paths.get("E:/MicroID"), perms );



            }
            if(Files.exists(path1)) {
                System.out.println("Directory exists:: ");
            }else {
                System.out.println("Directory Not Exists:: ");

            }


            File directory = new File(srcDir);
            File[] files = directory.listFiles();
            int totalLines =0;
            for (File file : files) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                Date lastMod = new Date(file.lastModified());
                if (file.getName().startsWith(fileNameStartsWith) && lastMod.compareTo(midnightToday.getTime()) > 0) {
                    String st ="";
                    while ((st = br.readLine()) != null){
                        if(st.contains(" ")) {
                            String[] staffAtt = st.split(" ");
                            if (staffAtt.length == 0 || staffAtt.length == 2){
                                totalLines++;
                            }
                        }
                    }
                }
                br.close();
            }


            System.out.println("Total Lines are::--->>> "+totalLines);

            System.out.println("New Satement Added;; ");


           for (File file : files) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                 //&& lastMod.compareTo(midnightToday.getTime()) > 0
                Date lastMod = new Date(file.lastModified());
                if (file.getName().startsWith(fileNameStartsWith) && lastMod.compareTo(midnightToday.getTime()) > 0) {
                    String st ="";

                   // Path path = Paths.get("E:/Micro ID Sdn. Bhd - Copy1/"+file.getName()+".txt");
                    Path path = Paths.get(dirToCreate+"/"+fileName+".txt");
                    System.out.println("file created is:: "+path.getFileName());
                    Files.write(path, "".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    long numberOfLines =0;
                    while ((st = br.readLine()) != null){
                        if(st.contains(" ")) {

                            String[] staffAtt = st.split(" ");
                          //System.out.print("starff:: "+staffAtt.length);
                            //in case Array is empty
                            String staffId = "";
                            String finalString = null;
                            String checkInOutFlag = "";
                            if (staffAtt.length == 0 || staffAtt.length == 2){
                                if (staffAtt != null && staffAtt[0].length() > 0 && staffAtt[0] != null && staffAtt[0].startsWith("000")) {
                                    checkInOutFlag = staffAtt[0].replaceFirst("000", "001");
                                } else  if (staffAtt != null && staffAtt[0].length() > 0 && staffAtt[0] != null && staffAtt[0].startsWith("001")) {
                                checkInOutFlag = staffAtt[0].replaceFirst("001", "002");
                                } else {
                                    checkInOutFlag = staffAtt[0];
                                }

                            if (staffAtt != null && staffAtt.length > 1 && staffAtt[1] != null && staffAtt[1].length() > 0) {
                                // System.out.println(staffAtt[0]+" Staff ID: "+staffAtt[1]);
                                if (staffAtt[1].trim().length() == 5)
                                    staffId = "00000" + staffAtt[1].trim();
                                else if (staffAtt[1].trim().length() == 6)
                                    staffId = "0000" + staffAtt[1].trim();
                                else if (staffAtt[1].trim().length() == 7)
                                    staffId = "000" + staffAtt[1].trim();
                                else if (staffAtt[1].trim().length() == 8)
                                    staffId = "00" + staffAtt[1].trim();
                                else if (staffAtt[1].trim().length() == 9)
                                    staffId += "0" + staffAtt[1].trim();
                                else
                                    staffId = staffAtt[1].trim();
                                numberOfLines++;
                                finalString = checkInOutFlag + " " + staffId;
                                if(totalLines >=2){
                                    finalString+=NEW_LINE;
                                }
                                totalLines--;
                                Files.write(path, (finalString).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                            } else {
                                System.out.println(" staffAtt[1]  "+staffAtt[1] );
                                finalString = staffAtt[0] + NEW_LINE;
                                Files.write(path, finalString.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                            }
                          }
                        }else {

                            if(!st.equals("") && !st.isEmpty()){
                                System.out.println(" Striss ->>>>"+st);
                                Files.write(path, st.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                            }

                        }
                     }

                }
               br.close();
            }
            return sendModifiedFile(fileNameToCreate, dirToCreate, ftpClient);
        }  catch (FileNotFoundException e) {
            e.printStackTrace();
            out.println(" ****** Exception FileNotFoundException copyToSTP  *********"+e.getMessage());

        } catch (IOException io) {
            io.printStackTrace();
            out.println(" ****** IOException  copyToSTP  *********"+io.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            out.println(" ****** Exception  copyToSTP  *********"+e.getMessage());
            return e.getLocalizedMessage();
        }

        out.println(" ****** End of  copyToSTP  *********");
        return sb.toString();
    }

    public static String sendModifiedFile(String  fileNameToCreate, String dirToCreate, FTPClient ftpClient){

        StringBuffer sb = null;
        try {
            out.println(" ****** Start of  sendModifiedFile  *********");
            System.out.println(" sendModifiedFile -->>>  ");
            File directory = new File(dirToCreate);
            File[] files = directory.listFiles();
            Calendar midnightToday = new GregorianCalendar();
            midnightToday.set(Calendar.HOUR_OF_DAY, 0);
            midnightToday.set(Calendar.MINUTE, 0);

            midnightToday.set(Calendar.SECOND, 0);
            midnightToday.set(Calendar.MILLISECOND, 0);
            sb = new StringBuffer();
            for(File file: files){
                System.out.println("dirToCreate File Name is:: "+file.getName());
                Date lastMod = new Date(file.lastModified());
                if (file.getName().startsWith(fileNameToCreate)  && lastMod.compareTo(midnightToday.getTime()) > 0) {
                    if(sendTOFTPServer(file, ftpClient))
                        sb.append("File >> " + file.getName() + " Copied Successfully.\n");
                    else
                        sb.append("File >> " + file.getName() + " Copied Failed. \n");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println(" ****** Exception of  sendModifiedFile  *********");
        }

        return sb.toString();

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
            out.println(" ****** Exception of  sendTOFTPServer  *********");
        }

        return msg;
    }

}
