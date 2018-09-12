package com.dx.graph.neo4j.service;

import com.dx.graph.neo4j.controller.SearchParam;
import com.dx.graph.neo4j.dao.GraphDao;
import com.dx.graph.neo4j.util.GraphFormatUtil;
import com.dx.graph.neo4j.util.StringUtils;
import com.dx.graph.neo4j.web.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanchuan on 2018/7/14.
 */
@Slf4j
@Service
public class SearchService {
    @Autowired
    GraphDao graphDao;

    /**
     * 获取节点相邻的节点信息
     * @param param
     * @return
     */
    public Object getAdjacentNode(SearchParam.AdjacentNode param) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("MATCH (from_n:%s{name:'%s'}) ",param.getLabelName(), param.getPropertyName()));
        String relType = "r";
        if (StringUtils.isNotEmpty(param.getRelation())) {
            relType = String.format("r:%s",param.getRelation());
        }
        if (param.getPoint() == null) {
            buffer.append(String.format("- [%s] - (to_n) ",relType));
        }
        else if (param.getPoint() == SearchParam.Rel_Point_Forward) {
            buffer.append(String.format("- [%s] -> (to_n) ",relType));
        }
        else if (param.getPoint() == SearchParam.Rel_Point_Inversion) {
            buffer.append(String.format("<- [%s] - (to_n) ",relType));

        }
        else {
            buffer.append(String.format("- [%s] - (to_n) ",relType));
        }
        buffer.append("RETURN from_n,r,to_n");
        List<Record> records  = graphDao.execute(buffer.toString());
        graphDao.close();
        return ResultUtil.getSuccessResult(GraphFormatUtil.toD3Format(records),param);
    }

    /**
     * 按路径查询节点
     * @param param
     * @return
     * @throws Exception
     */
    public Object getNodeRoute(SearchParam.GetNodeRoute param) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("MATCH (from_n:%s{name:'%s'}) ",param.getLabelName(), param.getPropertyName()));
        String start = "";
        String end = "";
        if (param.getStart() != null) {
            start = param.getStart().toString();
        }
        if (param.getEnd() != null) {
            end = param.getEnd().toString();
        }
        buffer.append(String.format("- [r:%s*%s..%s] -> (to_n) RETURN from_n,r,to_n",param.getRelation(),start,end));
        String searchStr = buffer.toString();
        List<Record> records  = graphDao.execute(searchStr);
        graphDao.close();
        return ResultUtil.getSuccessResult(GraphFormatUtil.toD3Format(records),param);
    }

    /**
     * 获取所有lable
     * @return
     * @throws Exception
     */
    public Object getAllLabels(SearchParam.GetAllLabels param) throws Exception {
        String searchStr = "CALL db.labels";
        List<Record> records = graphDao.execute(searchStr);
        List<String> list = new ArrayList<String>();
        for (Record record:records) {
            String label = record.get("label","");
            if(StringUtils.isNotEmpty(label)) {
                list.add(label);
            }

        }
        graphDao.close();
        return ResultUtil.getSuccessResult(list, param);
    }

    /**
     * 获取所有属性
     * @return
     * @throws Exception
     */
    public Object getAllPros(SearchParam.GetAllPros param) throws Exception {
        String searchStr = "call db.propertyKeys";
        List<Record> records = graphDao.execute(searchStr);
        List<String> list = new ArrayList<String>();
        for (Record record:records) {
            String label = record.get("propertyKey","");
            if(StringUtils.isNotEmpty(label)) {
                list.add(label);
            }

        }
        graphDao.close();
        return ResultUtil.getSuccessResult(list, param);
    }

    /**
     * 获取节点关系类型
     * @param param
     * @return
     * @throws Exception
     */
    public Object getRelTypes(SearchParam.GetRelTypes param) throws Exception {
        String searchStr = "CALL db.schema";
        List<Record> records = graphDao.execute(searchStr);
        List<String> relTypes = new ArrayList<String>();
        for (Record record : records)
        {
            long id = getNodeId(record, param.getLabelName());
            Iterable<Value> iterable= record.get("relationships").values();
            for(Value value : iterable) {
                Relationship rel = value.asRelationship();
                if(rel.startNodeId() == id || rel.endNodeId() == id) {
                    if(!relTypes.contains(rel.type())) {
                        relTypes.add(rel.type());
                    }
                }
            }
        }
        graphDao.close();
        return ResultUtil.getSuccessResult(relTypes, param);
    }

    /**
     * 查询两个节点之间最短路径
     * @param param
     * @return
     * @throws Exception
     */
    public Object getShortestPath(SearchParam.GetShortestPath param) throws Exception {
        String searchStr = String.format("MATCH p=shortestPath((from_n:%s{name:'%s'})-[*]-(to_n:%s{name:'%s'})) RETURN p",
                param.getStartLabelName(), param.getStartProName(), param.getEndLabelName(),param.getEndProName());
        List<Record> records = graphDao.execute(searchStr);
        graphDao.close();
        return ResultUtil.getSuccessResult(GraphFormatUtil.toD3Format(records), param);
    }

    /**
     * 根据节点label获取id
     * @param record
     * @param label
     * @return
     */
    private static long getNodeId(Record record,String label) {
        Iterable<Value>  iterable = record.get("nodes").values();
        for (Value value:iterable) {
            if (value.asNode().hasLabel(label)) {
                return value.asNode().id();
            }
        }
        return 0;
    }

    /**
     * 根据节点属性按条件查询节点信息
     * @param param
     * @return
     * @throws Exception
     */
    public Object getNodeByCondition(SearchParam.GetNodeByCondition param) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("MATCH (from_n:%s) -[r]- (to_n) ",param.getLabelName()));
        if (param.getConditions() != null) {
            buffer.append("WHERE ");
            for (String condition : param.getConditions()) {
                List<String> conditions = StringUtils.splitStr(condition,",");
                if (conditions.size() == 3) {
                    buffer.append(String.format("from_n.%s %s ", conditions.get(0), conditions.get(1)));
                }
                else if (conditions.size() == 4) {
                    buffer.append(String.format(" %s from_n.%s %s ", conditions.get(0), conditions.get(1), conditions.get(2)));
                }
                //判断值是否为数值型
                String conditionValue = conditions.get(conditions.size()-1);
                if (StringUtils.isNumeric(conditionValue)) {
                    buffer.append(conditionValue);
                }
                else {
                    buffer.append(String.format("'%s'",conditionValue));
                }

            }
        }
        buffer.append(" RETURN from_n,r");
        List<Record> records = graphDao.execute(buffer.toString());
        graphDao.close();
        return ResultUtil.getSuccessResult(GraphFormatUtil.toD3Format(records), param);
    }

    /**
     * 根据节点标签名查询节点子图，为防止全图搜索引起性能问题，需要加上limit
     * @param param
     * @return
     * @throws Exception
     */
    public Object queryNodes(SearchParam.QueryNodes param) throws Exception {
        String cypherSql = String.format("MATCH (from_n:%s) - [r] - (to_n) RETURN from_n,r,to_n LIMIT %s",param.getLabelName(),param.getLimit());
        List<Record> records = graphDao.execute(cypherSql);
        graphDao.close();
        return ResultUtil.getSuccessResult(GraphFormatUtil.toD3Format(records), param);

    }

}

