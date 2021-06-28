package com.giga.timetac.ftp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortedList4Ex {

    public static void main(String args[]){
        List<Integer> list = Arrays.asList(10, 1, -20, 40, 5, -23, 0);

        Collections.sort(list, Collections.reverseOrder());
        String str ="strssss                              ";
        System.out.println(list+" "+str.length());



    }
}
