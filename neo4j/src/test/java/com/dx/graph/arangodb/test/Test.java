package com.dx.graph.arangodb.test;

import com.alibaba.fastjson.JSONObject;
import net.minidev.json.JSONArray;

/**
 * Created by yanchuan on 2018/9/4.
 */
public class Test {
    public static void main(String[] args) {
        Son son = new Son();
        System.out.println(son.name);
        son.showName();
        String str = "[\n" +
                "    \"person-mail\",\n" +

                "]";
        com.alibaba.fastjson.JSONArray json = JSONObject.parseArray(str);
        json.forEach(t -> t.toString().concat(","));
        System.out.println(String.join(",", json.toJavaList(String.class)));
    }
}
