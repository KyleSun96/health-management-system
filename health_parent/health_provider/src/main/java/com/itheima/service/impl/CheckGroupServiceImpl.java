package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: Itcast_health
 * @ClassName: CheckGroupServiceImpl
 * @description: 检查组服务
 * @author: KyleSun
 **/
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * @Description: //TODO 新增检查组
     * @Param: [checkGroup, checkitemIds]
     * @return: void
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {

        // 插入检查组数据
        checkGroupDao.add(checkGroup);
        Integer checkGroupId = checkGroup.getId();

        // 设置检查组和检查项的多对多的关联关系
        setRelOfGroupAndItem(checkGroupId, checkitemIds);
    }


    /**
     * @Description: //TODO 设置检查组和检查项的多对多的关联关系
     * @Param: [checkGroupId, checkitemIds]
     * @return: void
     */
    public void setRelOfGroupAndItem(Integer checkGroupId, Integer[] checkitemIds) {

        if (checkitemIds != null && checkitemIds.length > 0) {
            Map<String, Integer> map = new HashMap<>();
            for (Integer checkitemId : checkitemIds) {
                // key的值一定要和SQLxml文件的值一一对应: "checkGroupId" => #{checkGroupId}
                map.put("checkGroupId", checkGroupId);
                map.put("checkitemId", checkitemId);
                checkGroupDao.setRelOfGroupAndItem(map);
            }
        }
    }


    /**
     * @Description: //TODO 分页查询
     * @Param: [queryPageBean]
     * @return: com.itheima.entity.PageResult
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);

        // Page<CheckGroup> 根据条件查询检查组,<CheckGroup>表示封装为检查组
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckGroup> rows = page.getResult();
        return new PageResult(total, rows);
    }


    /**
     * @Description: //TODO 根据ID查询检查组
     * @Param: [checkgroupId]
     * @return: com.itheima.pojo.CheckGroup
     */
    @Override
    public CheckGroup findById(Integer checkgroupId) {
        return checkGroupDao.findById(checkgroupId);
    }


    /**
     * @Description: //TODO 根据检查组ID查询,检查组关联多少检查项ID
     * @Param: [checkgroupId]
     * @return: java.util.List<java.lang.Integer>
     */
    @Override
    public List<Integer> findRelOfGroupAndItem(Integer checkgroupId) {
        return checkGroupDao.findRelOfGroupAndItem(checkgroupId);
    }


    /**
     * @Description: //TODO 编辑检查组信息,同时关联检查项
     * @Param: [checkGroup, checkitemIds]
     * @return: void
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {

        // 修改检查组基本信息 t_checkgroup
        checkGroupDao.edit(checkGroup);

        // 清理当前检查组与检查项的关联关系 t_checkgroup_checkitem
        checkGroupDao.deleteRelation(checkGroup.getId());

        // 重新建立当前检查组和检查项的关联关系
        setRelOfGroupAndItem(checkGroup.getId(), checkitemIds);

    }


    /**
     * @Description: //TODO 查询所有检查组
     * @Param: []
     * @return: java.util.List<com.itheima.pojo.CheckGroup>
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }


    /**
     * @Description: //TODO 根据检查组Id删除该检查组
     * @Param: [checkgroupId]
     * @return: void
     */
    @Override
    public void deleteById(Integer checkgroupId) {
        // 删除检查组前,需要查询该检查项是否关联到套餐中,若已经关联则不能删除
        long count = checkGroupDao.findSetmealCountById(checkgroupId);
        if (count > 0) {
            // 套餐中含有该检查组,无法删除
            throw new RuntimeException();
        } else {
            // 若与套餐无关联,先删除当前检查组与检查项的关联关系,再执行删除
            checkGroupDao.deleteRelation(checkgroupId);
            checkGroupDao.deleteById(checkgroupId);
        }
    }


}
