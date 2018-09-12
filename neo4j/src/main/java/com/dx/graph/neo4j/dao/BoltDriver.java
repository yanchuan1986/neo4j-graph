package com.dx.graph.neo4j.dao;

import com.dx.graph.neo4j.exception.ConnectionException;
import lombok.extern.slf4j.Slf4j;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;

/**
 * Created by yanchuan on 2018/7/18.
 */
@Slf4j
@Repository
public class BoltDriver {

    @Value("${neo4j.uri}")
    private String uri;

    @Value("${neo4j.username}")
    private String userName;

    @Value("${neo4j.password}")
    private String password;

    private Driver boltDriver;

    private Session session;

    private void checkDriverInitialized() {
        Driver driver = boltDriver;
        if (driver == null) {
            initializeDriver();
        }
    }

    private void initializeDriver() {
        boltDriver = GraphDatabase.driver(uri, AuthTokens.basic( userName,password));
    }

    public Session newSession() {
        checkDriverInitialized();
        try {
            session = boltDriver.session();
        } catch (ClientException ce) {
            throw new ConnectionException(
                    "Error connecting to graph database using Bolt: " + ce.code() + ", " + ce.getMessage(), ce);
        } catch (Exception e) {
            throw new ConnectionException("Error connecting to graph database using Bolt", e);
        }
        return session;
    }

    public void close() {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PreDestroy
    public void closeDriver(){
        log.info("Shutting down Bolt driver");
        boltDriver.close();
    }
}
