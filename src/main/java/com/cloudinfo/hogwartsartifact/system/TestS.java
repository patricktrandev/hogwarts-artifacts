package com.cloudinfo.hogwartsartifact.system;

import java.util.HashSet;
import java.util.Set;

public class TestS {
    public static void main(String[] args) {
        Set<String> set=new HashSet<>();
        set.add("ADMIN");
        set.add("USER");
        if (set.contains("ADMIN")){
            set.remove("ADMIN");
        }
        StringBuilder s=new StringBuilder();
        for(String r:set){
            s.append(r).append("_");
        }
        System.out.println(s);
    }
}
