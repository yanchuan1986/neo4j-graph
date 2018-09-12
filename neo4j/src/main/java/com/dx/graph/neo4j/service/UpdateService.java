package com.dx.graph.neo4j.service;

import com.dx.graph.neo4j.controller.UpdateParam;
import com.dx.graph.neo4j.dao.GraphDao;
import com.dx.graph.neo4j.util.GraphFormatUtil;
import com.dx.graph.neo4j.util.StringUtils;
import com.dx.graph.neo4j.web.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.v1.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by yanchuan on 2018/7/24.
 */
@Slf4j
@Service
public class UpdateService {

    @Autowired
    GraphDao graphDao;
    /**
     * 修改属性值
     */
    public Object updateProperty(UpdateParam.UpdatePro param) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("MATCH (n) WHERE id(n)=%s ", param.getId()));
        int i = 0;
        for (Map.Entry<String,String> entry : param.getPros().entrySet()) {
            if (i == 0) {
                buffer.append(String.format("set n.%s = ",entry.getKey()));
            }
            else {
                buffer.append(String.format(", n.%s = ",entry.getKey()));
            }
            if (StringUtils.isNumeric(entry.getValue())) {
                buffer.append(entry.getValue());
            }
            else {
                buffer.append(String.format("'%s'",entry.getValue()));
            }
            i++;
        }
        buffer.append(" RETURN n");
        List<Record> records  = graphDao.execute(buffer.toString());
        graphDao.close();
        return ResultUtil.getSuccessResult(GraphFormatUtil.toD3Format(records),param);
    }
}
