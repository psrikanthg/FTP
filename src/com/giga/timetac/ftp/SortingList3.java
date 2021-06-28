package com.giga.timetac.ftp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SortingList3 {

    public static void main(String args[]){

        List<String> country = Arrays.asList("Russia", "India", "China", "Japan", "", "Ghana" );
        List<String> country1 = Arrays.asList("Russia", "inida", "China",  "Japan", "", "ghana");


        System.out.println(country);
        country.sort(String.CASE_INSENSITIVE_ORDER);
        System.out.println(country);

        System.out.println(country1);
        //sorts list in ascending order(null, Capital Letter and Small letter)
        country1.sort(Comparator.naturalOrder());

        System.out.println(country1);

    }
}
