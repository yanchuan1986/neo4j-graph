package com.dx.graph.arangodb.test;

import lombok.Data;

import java.util.List;

/**
 * Created by yanchuan on 2018/9/4.
 */
@Data
public class GraphDefine {
    List<Node> nodes;
    List<Relations> relations;
}
