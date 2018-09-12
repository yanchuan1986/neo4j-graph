package com.dx.graph.neo4j.web.common;

/**
 * Created by wuling on 2017/5/22.
 */
public interface ErrorMessage {

    String INTERNAL_SERVER = "系统出现异常，请稍后重试。";

    String NAME_EXIST = "名称已存在";

    String PERMISSION_DENIED = "对不起，您没有删除权限。";

}
