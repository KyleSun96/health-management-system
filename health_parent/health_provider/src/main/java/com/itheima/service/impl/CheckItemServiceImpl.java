package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: Itcast_health
 * @ClassName: CheckItemServiceImpl
 * @description: 检查项服务
 * @author: KyleSun
 **/
//用dubbo的service接口,用于暴露服务
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * @Description: //TODO 新增检查项
     * @Param: [checkItem]
     * @return: void
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }


    /**
     * @Description: //TODO 检查项分页查询
     * @Param: [queryPageBean]
     * @return: com.itheima.entity.PageResult
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        // 基于mybatis框架提供的分页查询助手插件
        PageHelper.startPage(currentPage, pageSize);
        /*
            获得由分页助手封装好的的Page对象,再从中获取 需要在页面展示的数据,
            最后将这些数据封装为我们所需要的PageResult对象
         */
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        return new PageResult(total, rows);
    }


    /**
     * @Description: //TODO 删除检查项
     * @Param: [id]
     * @return: void
     */
    @Override
    public void deleteById(Integer id) {
        // 删除检查项前,需要查询该检查项是否关联到检查组中,若已经关联则不能删除
        long count = checkItemDao.findGroupCountById(id);
        if (count > 0) {
            throw new RuntimeException();
        } else {
            // 若无关联,执行删除
            checkItemDao.deleteById(id);
        }
    }


    /**
     * @Description: //TODO 编辑检查项
     * @Param: [checkItem]
     * @return: void
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }


    /**
     * @Description: //TODO 根据检查项Id查询数据
     * @Param: [id]
     * @return: com.itheima.pojo.CheckItem
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }


    /**
     * @Description: //TODO 查询所有检查项
     * @Param: []
     * @return: java.util.List<com.itheima.pojo.CheckItem>
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
