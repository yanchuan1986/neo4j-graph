package com.dx.graph.neo4j.controller;

import com.dx.graph.neo4j.web.common.CallbackParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by yanchuan on 2018/7/24.
 */
public class UpdateParam {

    @Data
    public static class UpdatePro extends CallbackParam {

        @NotNull(message = "节点id不能为空")
        private String id;   //节点id

        @NotNull(message = "属性键值对不能为空")
        private Map<String,String> pros;  //属性key与value
    }
}
