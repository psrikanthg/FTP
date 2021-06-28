package com.giga.timetac.ftp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ForEachLoop {

    public static void main(String args[]){

      

        List<Student> studentLIst = new ArrayList<>();
      
             
        Student s1 = new Student();
        s1.setName("Pamangundla Sreekanth");
        s1.setJob("Sr Software Engineer");
        s1.setDoj(new Date());

        studentLIst.add(s1);

        Student s2 = new Student();
        s2.setName("Tapas Ranjan");
        s2.setJob("Associate Engineer");
        s2.setDoj(new Date());

        studentLIst.add(s2);

        studentLIst.forEach(sdt -> System.out.println(sdt.getName()+"  "+sdt.getJob()+" "+sdt.getDoj()));
          //End of the program --changed by sreekanth

    }
}
