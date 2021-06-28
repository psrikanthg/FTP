package com.giga.timetac.ftp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortingList {

    public static void main(String args[]){

        List<String> slist = Arrays.asList("Tanu", "Kamal", "Suman", "Lucky","Bunty", "Amit" );

        List<String> sortedList = slist.stream().sorted().collect(Collectors.toList());
        sortedList.forEach(System.out::println);


        List<String>  slist1 = Arrays.asList("78", "a", "m", "b", "z", "c", "12", "i", "1");

        List<String> sortedList1 = slist1.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        sortedList1.forEach(System.out::println);



    }
}
