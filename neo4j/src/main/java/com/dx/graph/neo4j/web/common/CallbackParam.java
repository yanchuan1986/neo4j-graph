package com.dx.graph.neo4j.web.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zhulei on 2017/5/18.
 */
@Setter
@Getter
public class CallbackParam implements Serializable {

    private static final long serialVersionUID = 4064118805074330001L;
    private String callback;
    private String scriptWrapping;

}
