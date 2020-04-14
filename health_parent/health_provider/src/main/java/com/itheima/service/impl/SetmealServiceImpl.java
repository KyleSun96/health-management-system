package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private JedisPool jedisPool;


    // 新增套餐,同时需要关联检查组
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 新增套餐_1_插入套餐数据
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        // 新增套餐_2_设置套餐和检查项的多对多的关联关系
        this.setRelOfMealAndGroup(setmealId, checkgroupIds);

        // 将图片名称保存到Redis set集合中,Redis有五种数据类型: string list hash set zset
        String fileName = setmeal.getImg();
        Jedis resource = jedisPool.getResource();
        resource.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);
        // 用完必须要归还资源!不归还线上很快就满了
        resource.close();

    }


    // 分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<Setmeal> rows = page.getResult();

        return new PageResult(total, rows);
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
