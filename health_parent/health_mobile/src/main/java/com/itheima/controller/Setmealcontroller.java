package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: Itcast_health
 * @ClassName: Setmealcontroller
 * @description: 移动端套餐管理
 * @author: KyleSun
 **/

@RestController
@RequestMapping("/setmeal")
public class Setmealcontroller {

    @Reference
    private SetmealService setmealService;


    /**
     * @Description: //TODO 查询所有套餐
     * @Param: []
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/getAllSetmeal.do")
    public Result getAllSetmeal() {
        try {
            List<Setmeal> setmeals = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, setmeals);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }


    /**
     * @Description: //TODO 根据套餐的ID查询详细信息(包括套餐基本信息,套餐包含的检查组信息,检查组包含的检查项信息)
     * @Param: [id]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/findById.do")
    public Result findById(int id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
