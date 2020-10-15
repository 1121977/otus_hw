package ru.otus;
import java.util.Comparator;

import static java.util.Collections.*;

public class App
{
    public static void main( String[] args )
    {
        DIYarrayList<Integer> diYarrayListInteger = new DIYarrayList<>();
        DIYarrayList<Integer> diYarrayListInteger2 = new DIYarrayList<>();
        addAll(diYarrayListInteger, 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24);
        addAll(diYarrayListInteger2, 0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2);
        copy(diYarrayListInteger2,diYarrayListInteger);
        DIYarrayList<String> diYarrayListString, diYarrayListString2;
        diYarrayListString = new DIYarrayList<>();
        diYarrayListString2 = new DIYarrayList<>();
        addAll(diYarrayListString,"one","two","three","four","five","one","two","three","four","five","one","two","three","four","five","one","two","three","four","five","six");
        addAll(diYarrayListString2,"a","b","c","d","f","a","b","c","d","f","a","b","c","d","f","a","b","c","d","f","six");
        copy(diYarrayListString2,diYarrayListString);
        System.out.println(diYarrayListInteger.toString());
        System.out.println(diYarrayListString.toString());
        System.out.println("diYarrayListInteger2 = "  + diYarrayListInteger2.toString());
        addAll(diYarrayListString2,"a","b","c","d","f","a","b","c","d","f","a","b","c","d","f","a","b","c","d","f","six");
        System.out.println("diYarrayListString2 = " + diYarrayListString2.toString());
        sort(diYarrayListString2, (a,b)->{
            if (a!=null&&b!=null)
                return a.compareTo(b);
            else
                return a!=null?-1:1;
        });
        System.out.println("diYarrayListString2 (after sort()) = " + diYarrayListString2.toString());
        addAll(diYarrayListInteger, 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21);
        System.out.println("diYarrayListInteger (after addAll()) = "+diYarrayListInteger.toString());
        sort(diYarrayListInteger, (a,b)->{
            if (a!=null&&b!=null)
                return a.compareTo(b);
            else
                return a!=null?-1:1;
        });
        System.out.println("diYarrayListInteger (after sort()) = "+diYarrayListInteger.toString());
    }
}
