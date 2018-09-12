package com.dx.graph.arangodb.test;

import lombok.Data;

import java.util.List;

/**
 * Created by yanchuan on 2018/9/4.
 */
@Data
public class Node {
    private String hive_table_path;
    private String node_name;
    private List<String> fields;
    private String name;
}
