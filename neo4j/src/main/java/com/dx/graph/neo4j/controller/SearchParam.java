package com.dx.graph.neo4j.controller;

import com.dx.graph.neo4j.web.common.CallbackParam;
import com.dx.graph.neo4j.web.common.PageParam;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by yanchuan on 2018/07/13.
 */
public class SearchParam {

    public static final int Rel_Point_Forward = 0;    //节点正向关系查询
    public static final int Rel_Point_Inversion = 1;  //节点反向关系查询
    public static final int Rel_Point_Bothway = 2;  //节点双向关系查询

    @Data
    public static class AdjacentNode extends CallbackParam {

        @NotNull(message = "节点label名称不能为空")
        private String labelName;   //节点label名称

        @NotNull(message = "节点属性name不能为空")
        private String propertyName;             //节点属性name

        @NotNull(message = "关系不能为空")
        private String relation;         //图节点关系

        //private String condition;     //查询属性条件

        private Integer point;            //节点指向

    }

    @Data
    public static class QueryNodes extends CallbackParam {

        @NotNull(message = "节点label名称不能为空")
        private String labelName;   //节点label名称

        @Range(min = 1, message = "查询节点个数值必须大于1")
        private Integer limit;  //查询节点个数限制（防止全图搜索）
    }

    @Data
    public static class GetAllLabels extends PageParam {

    }

    @Data
    public static class GetAllPros extends PageParam {

    }

    @Data
    public static class GetRelTypes extends CallbackParam {

        @NotNull(message = "节点label名称不能为空")
        private String labelName;   //节点label名称

    }

    @Data
    public static class GetNodeRoute extends CallbackParam {
        private String labelName;   //节点label名称

        @NotNull(message = "节点属性name不能为空")
        private String propertyName;             //查询路径的根节点名称

        @NotNull(message = "关系不能为空")
        private String relation;         //图节点关系

        private Integer start;           //查询路径开始值
        private Integer end;             //查询路径结束值

    }

    @Data
    public static class GetShortestPath extends CallbackParam {

        @NotNull(message = "起始节点label名称不能为空")
        private String startLabelName;  //起始节点label名称

        @NotNull(message = "结束节点label名称不能为空")
        private String endLabelName;    //结束节点label名称

        @NotNull(message = "起始节点属性name不能为空")
        private String startProName;             //查询路径的根节点名称

        @NotNull(message = "结束节点属性name不能为空")
        private String endProName;             //查询路径的根节点名称
    }

    @Data
    public static class GetNodeByCondition extends CallbackParam {
        @NotNull(message = "节点label名称不能为空")
        private String labelName;   //节点label名称

        private List<String> conditions; //条件，按逗号分隔如: [and|or] age > 10 表示为: [and|or],age,>,10,where后面的运算符设为空

    }

}
