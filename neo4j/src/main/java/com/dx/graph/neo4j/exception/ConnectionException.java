package com.dx.graph.neo4j.exception;

/**
 * Created by yanchuan on 2018/7/18.
 */
public class ConnectionException extends RuntimeException {
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
