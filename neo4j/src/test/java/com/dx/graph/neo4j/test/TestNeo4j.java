package com.dx.graph.neo4j.test;

import com.dx.graph.neo4j.util.StringUtils;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.internal.value.RelationshipValue;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

public class TestNeo4j {


    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver( "bolt://111.231.62.228:7687", AuthTokens.basic( "neo4j", "123456" ) );
        Session session = driver.session();
        batchUpdate(session);
        session.close();
//        session.run( "CREATE (a:Person {name: {name}, title: {title}})",
//                parameters( "name", "Arthur001", "title", "King001" ) );
//
//        StatementResult result = session.run( "MATCH (a:Person) WHERE a.name = {name} " +
//                        "RETURN a.name AS name, a.title AS title",
//                parameters( "name", "Arthur001" ) );
        //MATCH (tom:Person{name:{name}})-[:ACTED_IN]->(m)<-[:DIRECTED]-(d) RETURN tom,m,d LIMIT 10
//        StatementResult result = session.run( "MATCH (tom:Person{name:{name}})-[:ACTED_IN]->(m)<-[:DIRECTED]-(d) RETURN tom,m,d LIMIT 10",
//                parameters("name","Tom Hanks"));
        //MATCH (n:people) -[r] -(m) WHERE n.age >18 and m.age>20 RETURN n,
        String str = ",11,22,33";
        StringUtils.isEmpty("");
        StringUtils.splitStr(str,",");
        //执行代码
        StatementResult result = session.run( "call db.propertyKeys");
        String label = "Movie";
        List<String> relTypes = new ArrayList<String>();
        List<Record> list1 = result.list();
        for (Record record : list1)
        {
            long id = getNodeId(record,label);
            Iterable<Value> iterable= record.get("relationships").values();
            for(Value value : iterable) {
                Relationship rel = value.asRelationship();
                if(rel.startNodeId() == id || rel.endNodeId() == id){
                    if(!relTypes.contains(rel.type())) {
                        relTypes.add(rel.type());
                    }

                }
            }
            System.out.println("types: "+ relTypes);
            //System.out.println( record.get( "m" ).get("name").asString() + " " + record.get( "a" ).get("name").asString() );
        }
//        search("MATCH (tom:Person{name:'Tom Hanks'})-[:ACTED_IN]->(m)<-[:DIRECTED]-(d) RETURN tom,m,d LIMIT 10",session);
        session.close();
        driver.close();
    }

    private static long getNodeId(Record record,String label) {
        Iterable<Value>  iterable = record.get("nodes").values();
        for(Value value:iterable) {
            if (value.asNode().hasLabel(label)) {
                return value.asNode().id();
            }
        }
        return 0;
    }

    public static void search(final String cypher,Session session){
        List<Record> list;
        session.writeTransaction(new TransactionWork<Object>() {
            @Override
            public Object execute(Transaction tx) {
                return searchNode(tx,cypher);
            }
        });
    }

    private static StatementResult batch(final Transaction tx,String cypher) {
        return tx.run(cypher);
    }
    private static StatementResult unwind(final Transaction tx,String cypher) {
        return tx.run(cypher);
    }
    public static void batchUpdate(Session session) {
        String batchSql = "{:param batch: [{name:'张三',properties:{age:50}},{name:'小白',properties:{age:50}}]}";
        String updateSql= "UNWIND {batch} as row\n" +
                "MERGE (n:people {name:row.name})\n" +
                "SET n.age = row.properties.age";
        session.writeTransaction( tx -> batch( tx, batchSql));
        session.writeTransaction( tx -> unwind( tx,updateSql));
    }

    private static List<Record> searchNode(Transaction tx,String cypher) {
        return tx.run(cypher).list();
    }

}
