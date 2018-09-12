package com.dx.graph.arangodb.test;

import lombok.Data;

/**
 * Created by yanchuan on 2018/9/4.
 */
@Data
public class Relations {
    private String hive_table_path;
    private String edge_name;
    private String name;
    private String from_node;
    private String to_node;
}
