package com.dx.graph.neo4j.dao;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yanchuan on 2018/7/18.
 */
@Slf4j
@Repository
public class GraphDao {

    @Autowired
    BoltDriver driver;

    public List<Record> execute(String cypherStr) throws Exception {
        log.info("cypher sql: {}",cypherStr);
        Session session = driver.newSession();
        StatementResult result = session.writeTransaction(tx -> run(tx,cypherStr));
        List<Record> list = new ArrayList<>();
        if (result != null) {
            list = result.list();
        }
        return list;
    }


    public void close() {
        driver.close();
    }

    private StatementResult run(final Transaction tx,String cypherStr) {
        return tx.run(cypherStr);
    }
}
