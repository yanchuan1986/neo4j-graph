package com.dx.graph.neo4j.util;

import org.neo4j.driver.internal.value.ListValue;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.internal.value.PathValue;
import org.neo4j.driver.internal.value.RelationshipValue;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;

import java.util.*;

/**
 * Created by yanchuan on 2018/7/16.
 */
public class GraphFormatUtil {

    /**
     * 将neo4j结果转换为D3.js要求的格式，用于前端实现图可视化
     */
    public static Map<String, Object> toD3Format(List<Record> list) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> links = new ArrayList<>();
        List<Relationship> rels = new ArrayList<Relationship>();
        Map<Long, Long> ids = new HashMap<Long, Long>();
        long i = 0;
        for (Record record : list) {
            for (Value value : record.values()) {
                if (value instanceof NodeValue) {
                    Node node = value.asNode();
                    if (ids.get(node.id()) == null) {
                        ids.put(node.id(), i);
                        nodes.add(nodeMap(node));
                        i++;
                    }
                }
                else if (value instanceof RelationshipValue) {
                    Relationship rel = value.asRelationship();
                    if(!isSameRel(rels,rel.startNodeId(), rel.endNodeId())) {
                        rels.add(rel);
                    }
                }
                else if (value instanceof ListValue) {
                    for (Value relValue : value.values()) {
                        if (relValue instanceof RelationshipValue) {
                            Relationship rel = relValue.asRelationship();
                            if(!isSameRel(rels,rel.startNodeId(), rel.endNodeId())) {
                                rels.add(rel);
                            }
                        }
                    }
                }
                else if (value instanceof PathValue) {
                    for (Node node : value.asPath().nodes())
                        if (ids.get(node.id()) == null) {
                            ids.put(node.id(), i);
                            nodes.add(nodeMap(node));
                            i++;
                        }
                    for (Relationship rel : value.asPath().relationships()) {
                        if (!isSameRel(rels,rel.startNodeId(), rel.endNodeId())) {
                            rels.add(rel);
                        }
                    }
                }
            }
        }
        // 节点之间建立关系
        for (Relationship relationship : rels) {
            if (ids.containsKey(relationship.startNodeId()) && ids.containsKey(relationship.endNodeId())) {
                links.add(linksMap(relationship, ids));
            }
        }
        if (links.size() > 0) {
            result.put("links", links);
        }
        if (nodes.size() > 0) {
            result.put("nodes", nodes);
        }

        return result;
    }

    /**
     * 解析node生成map格式数据
     */
    private static Map<String, Object> nodeMap(Node node) {
        Map<String, Object> result = new HashMap<String, Object>();
        node.labels().iterator().next();
        result.put("lable", node.labels().iterator().next());
        result.put("id", node.id());
        for (String key : node.keys()) {
            if (!"id".equals(key)) {
                result.put(key, node.asMap().get(key));
            }
        }
        return result;
    }

    /**
     * 解析关系实体生成map格式数据
     */
    private static Map<String, Object> linksMap(Relationship relation,Map<Long, Long> ids) {
        Map<String, Object> links = new HashMap<String, Object>();
        long source = ids.getOrDefault(relation.startNodeId(), -1l);
        long target = ids.getOrDefault(relation.endNodeId(), -1l);
        String type = relation.type();
        links.put("source", source);
        links.put("target", target);
        links.put("type", type);
        for (String key : relation.keys()) {
            links.put(key, relation.asMap().get(key));
        }
        return links;
    }

    /**
     * 判断是否为重复的关系
     */
    private static boolean isSameRel(List<Relationship> rels, long source, long target ) {
        for (Relationship rel : rels){
            if (rel.startNodeId() == source && rel.endNodeId() == target) {
                return true;
            }
        }
        return false;
    }

}
