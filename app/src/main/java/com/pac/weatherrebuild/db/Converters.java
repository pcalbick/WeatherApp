package com.pac.weatherrebuild.db;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class Converters {
    //Integer List Converters
    @TypeConverter
    public List<Integer> fromStringValueToIntegerList(String list){
        List<Integer> finalList = new ArrayList<>();
        int a = 0;
        for(int i=0; i<list.length(); i++){
            if(list.charAt(i) == ','){
                finalList.add(Integer.parseInt(list.substring(a,i)));
                a = i+1;
            }
        }
        finalList.add(a,list.length()-1);
        return finalList;
    }

    @TypeConverter
    public String fromIntegerListToString(List<Integer> list){
        StringBuilder builder = new StringBuilder();
        for(Integer i : list){
            builder.append(i);
            builder.append(",");
        }
        return builder.toString();
    }

    //String List Converters
    @TypeConverter
    public List<String> fromStringToStringList(String list){
        List<String> finalList = new ArrayList<>();
        int a = 0;
        for(int i=0; i<list.length(); i++){
            if(list.charAt(i) == ','){
                finalList.add(list.substring(a,i));
                a = i+i;
            }
        }
        finalList.add(list.substring(a,list.length()-1));
        return finalList;
    }

    @TypeConverter
    public String fromStringListToString(List<String> list){
        StringBuilder builder = new StringBuilder();
        for(String s : list){
            builder.append(s);
            builder.append(",");
        }
        return builder.toString();
    }
}
