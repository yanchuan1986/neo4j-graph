package com.dx.graph.arangodb.test;

import lombok.Data;


/**
 * Created by yanchuan on 2018/8/31.
 */

@Data
public class Label {
    private String _id;
    private String labelName;
    private String nodeName;
    private String conditions;
    private String color;
    private Integer size;
    private Integer modifyUserId;
    private String modifyTime;

}
