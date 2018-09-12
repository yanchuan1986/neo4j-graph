package com.dx.graph.neo4j.controller;

import com.dx.graph.neo4j.service.SearchService;
import com.dx.graph.neo4j.util.StringUtils;
import com.dx.graph.neo4j.web.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by yanchuan on 2018/07/13.
 */
@Slf4j
@RestController
@RequestMapping("/graph/search")
public class SearchAction {

    @Autowired
    SearchService searchService;

    @RequestMapping("/getAllLabels")
    public Object getAllLables(@Valid SearchParam.GetAllLabels param) {
        try {
            return searchService.getAllLabels(param);
        }
        catch (Exception ex) {
            log.error(String.format("查询所有label getAllLabel参数：%s", param), ex);
            return ResultUtil.getFailedResult(ex.getMessage(), param);
        }
    }

    @RequestMapping("/getAllPros")
    public Object getAllPros(@Valid SearchParam.GetAllPros param) {
        try {
            return searchService.getAllPros(param);
        }
        catch (Exception ex) {
            log.error(String.format("查询所有属性 getAllPros参数：%s", param), ex);
            return ResultUtil.getFailedResult(ex.getMessage(), param);
        }
    }

    @RequestMapping("/getRelTypes")
    public Object getRelTypes(@Valid SearchParam.GetRelTypes param, BindingResult bindResult) {
        try {
            if (bindResult.hasErrors()) {
                return ResultUtil.getValidFailedResult(bindResult, param);
            }
            return searchService.getRelTypes(param);
        }
        catch (Exception ex) {
            log.error(String.format("根据节点名称查询关系类型 getRelTypes参数：%s", param), ex);
            return ResultUtil.getFailedResult(ex.getMessage(), param);
        }
    }

    @RequestMapping("/adjacent")
    public Object getAdjacentNode(@Valid SearchParam.AdjacentNode param, BindingResult bindResult) {
        try {
            if (bindResult.hasErrors()) {
                return ResultUtil.getValidFailedResult(bindResult, param);
            }
            if (StringUtils.isEmpty(param.getLabelName())) {
                return ResultUtil.getFailedResult("节点label名称不能为空", param);
            }
            return searchService.getAdjacentNode(param);
        }
        catch (Exception ex) {
            log.error(String.format("查询节点相邻节点 getAdjacentNode参数：%s", param), ex);
            return ResultUtil.getFailedResult(ex.getMessage(), param);
        }
    }

    @RequestMapping("/nodeRoute")
    public Object getNodeRoute(@Valid SearchParam.GetNodeRoute param, BindingResult bindResult) {
        try {
            if (bindResult.hasErrors()) {
                return ResultUtil.getValidFailedResult(bindResult, param);
            }
            return searchService.getNodeRoute(param);
        }
        catch (Exception ex) {
            log.error(String.format("按路径查询节点 getNodeRoute参数：%s", param), ex);
            return ResultUtil.getFailedResult(ex.getMessage(), param);
        }
    }

    @RequestMapping("/shortestPath")
    public Object getShortestPath(@Valid SearchParam.GetShortestPath param, BindingResult bindResult) {
        try {
            if (bindResult.hasErrors()) {
                return ResultUtil.getValidFailedResult(bindResult, param);
            }
            return searchService.getShortestPath(param);
        }
        catch (Exception ex) {
            log.error(String.format("查询两个节点之间最短路径 getShortestPath参数：%s", param), ex);
            return ResultUtil.getFailedResult(ex.getMessage(), param);
        }
    }

    @RequestMapping("/queryByCond")
    public Object getNodeByCondition(@Valid SearchParam.GetNodeByCondition param, BindingResult bindResult) {
        try {
            if (bindResult.hasErrors()) {
                return ResultUtil.getValidFailedResult(bindResult, param);
            }
            return searchService.getNodeByCondition(param);
        }
        catch (Exception ex) {
            log.error(String.format("按条件查询图节点信息 getNodeByCondition参数：%s", param), ex);
            return ResultUtil.getFailedResult(ex.getMessage(), param);
        }
    }

    @RequestMapping("/queryNodes")
    public Object queryNodes(@Valid SearchParam.QueryNodes param, BindingResult bindResult) {
        try {
            if (bindResult.hasErrors()) {
                return ResultUtil.getValidFailedResult(bindResult, param);
            }
            return searchService.queryNodes(param);
        }
        catch (Exception ex) {
            log.error(String.format("根据节点标签名查询节点子图 queryNodes参数：%s", param), ex);
            return ResultUtil.getFailedResult(ex.getMessage(), param);
        }
    }
}
