package com.dx.graph.arangodb.test;

import com.arangodb.*;
import com.arangodb.entity.*;
import com.arangodb.model.AqlQueryOptions;
import com.arangodb.model.CollectionCreateOptions;
import com.arangodb.model.CollectionsReadOptions;
import com.arangodb.model.DocumentCreateOptions;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import com.dx.graph.neo4j.util.StringUtils;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.junit.Test;

import java.util.*;
import java.util.Iterator;
/**
 * Created by yanchuan on 2018/7/28.
 */
public class TestCreateDB {
    //连接和mongo差不多
//    ArangoDB arangoDB = new ArangoDB.Builder().host("111.231.62.228", 8529).user("root").password("123456").build();
    ArangoDB arangoDB = new ArangoDB.Builder().host("192.168.42.167", 8529).user("root").password("test").build();

    //数据库
    ArangoDatabase db = arangoDB.db("graphdb");
    //集合
    ArangoCollection coll =db.collection("actsIn");

    public void insertDocument() {
        ArangoCollection coll =db.collection("test");
        BaseDocument document = new BaseDocument();
//       BaseDocument document = new BaseDocument();
        //document.setKey("yanc");
//        document.addAttribute("_from","actors/Carrie1");
//        document.addAttribute("_to","movies/TheMatrixReloaded1");
        //document.addAttribute("roles","[\"Trinity\"]");

        document.addAttribute("time","1988");
        Map<String,Object> map = new HashMap();
        map.put("_from","aa/55");
        map.put("_to","bb/66");
        map.put("time",1986);
//        Map<String,Object> map1 = new HashMap();
//        map1.put("_from","aa/33");
//        map1.put("_to","bb/44");
//        map1.put("time",1988);
        List list = new ArrayList<Map<String,Object>>();
        list.add(map);
//        list.add(map1);
        coll.insertDocuments(list);
    }
    public void createDB(){
        arangoDB.createDatabase("graph_db");
    }
    public void createCollection(){
        CollectionEntity myArangoCollection = db.createCollection("test",new CollectionCreateOptions().type(CollectionType.EDGES));

    }
    public void createGraph(){
        //arangoDB.createDatabase("AQLTest");
        CollectionEntity col = arangoDB.db("AQLTest").createCollection("graph",new CollectionCreateOptions().type(CollectionType.EDGES));
    }
    public void queryDocument(){
        BaseDocument myDocument = db.getDocument("label/2630134",
                BaseDocument.class);

        System.out.println("attribute=" + myDocument);
    }
    public void queryDocument_1(){
        Label myDocument = db.getDocument("label/2630134", Label.class);
        Label myDocument1 = db.collection("label").getDocument("2630134",
                Label.class);
        System.out.println("attribute=" + myDocument);
    }
    public  <T> Iterable<T> findAll(){
        Class entityClass = GraphDefine.class;
        final String query = "FOR entity IN @@col RETURN entity";
        final Map<String, Object> bindVars = new MapBuilder().put("@col", "graph_define_2018").get();
//        ArangoCursor cursor = db.query(query, bindVars == null ? null : bindVars, null, entityClass);
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return query(query, bindVars, null, entityClass);
            }
        };
    }

    public <T> ArangoCursor<T> query(
            final String query,
            final Map<String, Object> bindVars,
            final AqlQueryOptions options,
            final Class<T> entityClass)  {
        return db.query(query, bindVars == null ? null : bindVars, null, entityClass);
    }

    public void createGraph1(){
        // Edge definitions of the graph
        List<EdgeDefinition> edgeDefinitions = new ArrayList<EdgeDefinition>();

     // We start with one edge definition:
        EdgeDefinition edgeDefHasWritten = new EdgeDefinition();

     // Define the edge collection...
        edgeDefHasWritten.collection("HasWritten2");

     // ... and the vertex collection(s) where an edge starts...
        List<String> from = new ArrayList<String>();
        from.add("Person");
        edgeDefHasWritten.from("people//xf");

// ... and ends.
        List<String> to = new ArrayList<String>();
        to.add("xf");
        edgeDefHasWritten.to("people//xm");

// add the edge definition to the list
        edgeDefinitions.add(edgeDefHasWritten);

// We do not need any orphan collections, so this is just an empty list
        List<String> orphanCollections = new ArrayList<String>();

// Create the graph:
        GraphEntity graphAcademical =  arangoDB.db("AQLTest").createGraph("cccc", edgeDefinitions);
    }
    public void createGraph2(){
        List<EdgeDefinition> edgeDefinitions = new ArrayList<EdgeDefinition>();
        ArangoCollection edge= db.collection("actsIn");
        db.getCollections().forEach(
                entity -> {
                    if(entity.getType().toString()=="EDGES"){
//                        edgeDefinitions.add((EdgeDefinition)entity);
                    }
                });
//        db.createGraph("test",db.getCollections());
    }

    public void close(){
        arangoDB.shutdown();
    }

    public void queryVelocyPack(){
        VPackSlice myDocument = arangoDB.db("AQLTest").collection("test").getDocument("yanc",
                VPackSlice.class);
        System.out.println("Key: " + myDocument.get("id").getAsInt());
        System.out.println("Attribute a: " + myDocument.get("name").getAsString());
        System.out.println("Attribute b: " + myDocument.get("tag").getAsString());
        System.out.println("Attribute b: " + myDocument.get("pro1").getAsString());
    }

    public void updateDoc() {
        BaseDocument myDocument = arangoDB.db("AQLTest").collection("test").getDocument("2087359",
                BaseDocument.class);
        myDocument.addAttribute("pro1","bbb");
        DocumentUpdateEntity docment = arangoDB.db("AQLTest").collection("test").updateDocument("2087359", myDocument);
        System.out.println(docment.getId());

    }
    public void queryGraph() {
        try {
            //String query = "for e,v,p in ANY @name actsIn COLLECT WITH COUNT INTO length return {length}";
            String query = "FOR v,e,p IN OUTBOUND @name actsIn OPTIONS {bfs: true, uniqueVertices: 'global'} RETURN p";
            Map<String, Object> bindVars = new MapBuilder().put("name", "persons/TomH").get();
            ArangoCursor<Object> cursor = arangoDB.db("AQLTest").query(query, bindVars, null,
                    Object.class);

            List<Map<String,Object>> list = new ArrayList();
            cursor.forEachRemaining(aDocument -> {
//                list.add(aDocument);

                if(aDocument instanceof Map)
                {
                    System.out.println("map");

                }
                if(aDocument instanceof String)
                {
                    System.out.println("string");

                }
                if(aDocument instanceof Long)
                {
                    System.out.println("string");

                }
                if (aDocument instanceof Map) {
                    list.add((Map<String, Object>) aDocument);
                }
                System.out.println("document: "+aDocument.toString());
//                for(Map.Entry<String,Object> entry: aDocument.getProperties().entrySet()){
//                 System.out.println("===="+entry.getKey());
//                    System.out.println(entry.getValue());
//                }
        });
            List<String> ids = new ArrayList<>();
            List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
            for(Map<String,Object> map : list){
                Map<String,Object> e = (Map<String,Object>)map.get("e");
                Map<String,String> v = (Map<String,String>)map.get("v");
                if (!ids.contains(e.get("_id"))) {
                    lists.add(e);
                }


            }
            System.out.println("size=="+lists.size());
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
    }
    public void queryByAQL() {

        try {
            String queryNode = "for person in persons limit 10 return {person}";
            ArangoCursor<BaseDocument> cursor = arangoDB.db("AQLTest").query(queryNode, null, null,
                    BaseDocument.class);
            List<Map<String,Object>> list = new ArrayList();
            cursor.forEachRemaining(aDocument -> {
                list.add(aDocument.getProperties());
            });
            List<String> ids = new ArrayList<>();
            List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
//            for(Map<String,Object> map : list){
//                Map<String,Object> e = (Map<String,Object>)map.get("e");
//                Map<String,String> v = (Map<String,String>)map.get("v");
//                if (!ids.contains(e.get("_id"))) {
//                    lists.add(e);
//                }
//            }
            System.out.println("size=="+lists.size());
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
    }

    public void dropCollect(String colName) {
        db.collection(colName).drop();
    }
    public void updateColNamde(){
        db.collection("mail_test_2018").rename("mail");
        db.collection("person_2018").rename("person");
        db.collection("person_card_2018").rename("person_card");
        db.collection("person_mail_2018").rename("person_mail");
    }
    public static void main(String[] args) {
        TestCreateDB test = new TestCreateDB();
        try{
//            final Iterable<GraphDefine> all = test.findAll();
//            for(GraphDefine graph:all){
//                System.out.println("result=="+graph);
//            }
//            while(all.iterator().hasNext()){
//                Label label = all.iterator().next();
//                System.out.println("result=="+label.getLabelName());
//
//            }
//        test.dropCollect("card_finance_2018");
//        test.dropCollect("persons");
        //test.createGraph1();
        //test.dropCollect();
//        test.createGraph();
        //test.insertDocument();

//        test.createDB();
        //test.insertDocument();
//        test.insertDocument();
        //test.queryDocument();
            //test.createGraph2();
            //test.updateDoc();
//            test.queryVelocyPack();
//            test.queryByAQL();
            test.queryDocument_1();
            //test.queryDocument();

//            test.updateColNamde();
        }
        finally {
            test.close();
        }
    }

}
