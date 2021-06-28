package com.giga.timetac.ftp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text.NEW_LINE;

public class FileCreation {

    public static void main(String args[]){
    try{

        /*LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String fileName = "GCT"+now.format(formatter)+".txt";
        System.out.println("FIle Name is::: "+fileName);
        File file = new File("C:/Micro ID Sdn. Bhd - Copy123/"+fileName);
        if (file.createNewFile()) {
            System.out.println("New File is created!");
        } else {
            System.out.println("File already exists.");
        }*/

        Path path1 = Paths.get("E:/srikanth");
        //SRW_210616_121500
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd_HHmm00");
        String fileName  ="SRW_"+now.format(formatter);
        System.out.println(" File Name is:: "+fileName);
        /*001210615081515 00PLD00016
        001210615081810 00PLD00021
        000210615081855 000KC20001
        000210615082754 000KC18006
        000210615083650 000KC15002
        000210615083655 PL00000006*/
        String checkFlag = "001210610013655";


        if(checkFlag !=null && checkFlag.startsWith("000")){
            System.out.println("Orginal String:: " + checkFlag.replaceFirst("000", "001"));
            System.out.println("String after replacing 'fox' with 'dog': " + checkFlag.replaceFirst("000", "001"));
        }

        if(checkFlag !=null && checkFlag.startsWith("001")){
            System.out.println("Orginal String:: " + checkFlag.replaceFirst("000", "001"));
            System.out.println("String after replacing  " + checkFlag.replaceFirst("001", "002"));
        }
        File directory = new File("C:/Micro ID Sdn. Bhd/FTP TCMS");
        File[] files = directory.listFiles();
        Calendar midnightToday = new GregorianCalendar();
        midnightToday.set(Calendar.HOUR_OF_DAY, 0);
        midnightToday.set(Calendar.MINUTE, 0);

        midnightToday.set(Calendar.SECOND, 0);
        midnightToday.set(Calendar.MILLISECOND, 0);

        for (File file : files) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            //&& lastMod.compareTo(midnightToday.getTime()) > 0
            Date lastMod = new Date(file.lastModified());
            if (file.getName().startsWith("TCMS") && lastMod.compareTo(midnightToday.getTime()) > 0) {
                String st = "";
                while ((st = br.readLine()) != null){

                    String staffAtt[] = st.split(" ");
                   // System.out.println(staffAtt.length);

                    String staffId="";
                    String finalString =null;
                    String checkInOutFlag ="";
                    if(staffAtt!=null && staffAtt[0].length() > 0 && staffAtt[0] !=null && staffAtt[0].startsWith("000")){
                        checkInOutFlag =  staffAtt[0].replaceFirst("000", "001");
                    }

                    if(staffAtt!=null && staffAtt[0].length() > 0 && staffAtt[0] !=null && staffAtt[0].startsWith("001")){
                        checkInOutFlag = staffAtt[0].replaceFirst("001", "002");
                    }
                    if(staffAtt !=null && staffAtt.length >1 && staffAtt[1] !=null && staffAtt[1].length() > 0) {
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

                        finalString = checkInOutFlag + " " + staffId + NEW_LINE;
                        System.out.println("finalString "+finalString);

                    }else {
                        System.out.println("finalString "+staffAtt[0]);
                    }
                }

            }

            br.close();
        }

           /// deleteInputFile("C:/Micro ID Sdn. Bhd/FTP TCMS", "TCMS");
            if (Files.notExists(path1)) {
                Files.createDirectories(path1);
            }

        } catch(FileNotFoundException  ff){
            ff.getMessage();


    } catch(IOException e) {
        e.printStackTrace();
      }

    }

    public static void deleteInputFile(String srcDir, String fileNameStartsWith){

        try {

            File directory = new File(srcDir);
            File[] files = directory.listFiles();

            Calendar midnightToday = new GregorianCalendar();
            midnightToday.set(Calendar.HOUR_OF_DAY, 0);
            midnightToday.set(Calendar.MINUTE, 0);

            midnightToday.set(Calendar.SECOND, 0);
            midnightToday.set(Calendar.MILLISECOND, 0);
           // Thread.sleep(100000);
            for (File file : files) {

                Date lastMod = new Date(file.lastModified());
                if(fileNameStartsWith!=null && file.getName().contains(fileNameStartsWith) && lastMod.compareTo(midnightToday.getTime()) > 0){
                  Files.delete(Paths.get(srcDir+"/"+file.getName()));
                    System.out.println("Deleted Successfully::: ");
                }else {

                }

            }

       /* }  catch (IOException io) {
            io.printStackTrace();

        }*/ } catch (Exception e) {
            e.printStackTrace();


        }


    }
}
