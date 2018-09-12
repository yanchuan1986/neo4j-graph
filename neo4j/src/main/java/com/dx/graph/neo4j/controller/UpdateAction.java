package com.dx.graph.neo4j.controller;

import com.dx.graph.neo4j.service.UpdateService;
import com.dx.graph.neo4j.web.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by yanchuan on 2018/7/24.
 */
@Slf4j
@RestController
@RequestMapping("/graph/update")
public class UpdateAction {

    @Autowired
    UpdateService updateService;

    @RequestMapping("/updatePro")
    public Object updatePro(@Valid UpdateParam.UpdatePro param, BindingResult bindResult) {
        try{
            if (bindResult.hasErrors()) {
                return ResultUtil.getValidFailedResult(bindResult, param);
            }
            return updateService.updateProperty(param);
        }
        catch (Exception ex) {
            log.error(String.format("修改属性值 updatePro参数：%s", param), ex);
            return ResultUtil.getFailedResult(ex.getMessage(), param);
        }
    }

}
