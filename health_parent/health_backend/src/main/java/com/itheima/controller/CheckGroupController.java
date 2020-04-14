package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: Itcast_health
 * @ClassName: CheckGroupController
 * @description: 检查组管理
 **/
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;


    /**
     * @Description: //TODO 新增检查组
     * @Param: [checkGroup, checkitemIds]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


    /**
     * @Description: //TODO 分页查询
     * @Param: [queryPageBean]
     * @return: com.itheima.entity.PageResult
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkGroupService.findPage(queryPageBean);
    }


    /**
     * @Description: //TODO 根据ID查询检查组
     * @Param: [id]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/findById.do")
    public Result findById(Integer checkgroupId) {
        try {
            CheckGroup checkGroup = checkGroupService.findById(checkgroupId);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }


    /**
     * @Description: //TODO 根据检查组ID查询,检查组关联多少检查项ID,以数组形式返回
     * @Param: [checkgroupId]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/findRelOfGroupAndItem.do")
    public Result findRelOfGroupAndItem(Integer checkgroupId) {
        try {
            // 将检查组关联多少检查项ID 以数组形式返回
            List<Integer> checkitemIds = checkGroupService.findRelOfGroupAndItem(checkgroupId);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }


    /**
     * @Description: //TODO 编辑检查组
     * @Param: [checkGroup, checkitemIds]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.edit(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }


    /**
     * @Description: //TODO 查询所有检查组
     * @Param: []
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/findAll.do")
    public Result findAll() {
        try {
            List<CheckGroup> groupList = checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, groupList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

}
