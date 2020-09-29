package com.pac.weatherrebuild.model;

import java.util.ArrayList;
import java.util.List;

public class Values {

    private List<Integer> values = new ArrayList<>();
    private String type;
    private String timeLayout;
    private String name;

    public void addName(String n) { name = n; }

    public void addValue(int temp) { values.add(temp); }

    public void addType(String t) { type = t; }

    public void addTime(String time) { timeLayout = time; }

    public String getType() { return type; }

    public String getTime() { return timeLayout; }

    public List<Integer> getValues() { return values; }

    public int getValue(int i) { return values.get(i); }

    public String getName() { return name; }
}
