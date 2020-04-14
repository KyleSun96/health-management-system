package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.SetmealDao;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: Itcast_health
 * @ClassName: SetmealServiceImpl
 * @description: 体检套餐服务
 * @author: KyleSun
 **/
@Service(interfaceClass = SetmealService.class)
@Transactional()
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    // 新增套餐,同时需要关联检查组
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 新增套餐_1_插入套餐数据
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        // 新增套餐_2_设置套餐和检查项的多对多的关联关系
        this.setRelOfMealAndGroup(setmealId, checkgroupIds);
    }


    // 设置套餐和检查项的多对多的关联关系
    public void setRelOfMealAndGroup(Integer setmealId, Integer[] checkgroupIds) {

        if (checkgroupIds != null && checkgroupIds.length > 0) {
            Map<String, Integer> map = new HashMap<>();
            for (Integer checkgroupId : checkgroupIds) {
                map.put("setmealId", setmealId);
                map.put("checkgroupId", checkgroupId);
                setmealDao.setRelOfMealAndGroup(map);
            }
        }
    }
}
