package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
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
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private CheckItemDao checkItemDao;
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    // 从属性文件中获取要生成的HTML对应的目录
    private String outPutPath;


    /**
     * @Description: //TODO 新增套餐,同时需要关联检查组
     * @Param: [setmeal, checkgroupIds]
     * @return: void
     */
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

        // 当添加套餐后,需要重新生成当前方法所需的所有静态页面: 套餐列表页面 + 套餐详情页面
        this.generateMobileStaticHtml();
    }


    /**
     * @Description: //TODO 生成当前方法所需的静态页面
     * @Param: []
     * @return: void
     */
    private void generateMobileStaticHtml() {
        // 重新生成静态页面前,准备数据
        List<Setmeal> list = setmealDao.findAll();

        // 生成套餐列表静态页面
        generateMobileSetmealListHtml(list);

        // 生成套餐详情静态页面
        generateMobileSetmealDetailHtml(list);
    }


    /**
     * @Description: //TODO 生成套餐列表静态页面
     * @Param: [list]
     * @return: void
     */
    private void generateMobileSetmealListHtml(List<Setmeal> list) {
        Map<String, Object> map = new HashMap<>();
        //为模板提供数据,用于生成静态页面,key必须和接收数据页面的名称相一致
        map.put("setmealList", list);
        this.generteHtml("mobile_setmeal.ftl", "setmeal_list.html", map);
    }


    /**
     * @Description: //TODO 生成套餐详情静态页面
     * @Param: [list]
     * @return: void
     */
    private void generateMobileSetmealDetailHtml(List<Setmeal> list) {
        for (Setmeal setmeal : list) {
            Map<String, Object> map = new HashMap<>();
            // 这里不能直接返回 setmeal ,这里的数据应经过查询后再返回,拿到其中的检查组和检查项信信息
            map.put("setmeal", this.findById(setmeal.getId()));
            this.generteHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_" + setmeal.getId() + ".html", map);
        }
    }


    /**
     * @Description: //TODO 通用方法: 用于生成静态页面
     * @Param: [templateName, htmlPageName, map]
     * @return: void
     */
    public void generteHtml(String templateName, String htmlPageName, Map<String, Object> map) {
        // 获得配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        // 构造输出流
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            // 写入时,默认采用系统本身的字符集,因此可能出现 字符串乱码 问题
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutPath + "/" + htmlPageName)), "UTF-8"));
            // 输出文件
            template.process(map, out);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
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
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<Setmeal> rows = page.getResult();

        return new PageResult(total, rows);
    }


    /**
     * @Description: //TODO 查询所有套餐
     * @Param: []
     * @return: java.util.List<com.itheima.pojo.Setmeal>
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }


    /**
     * @Description: //TODO 根据套餐的ID查询详细信息(包括套餐基本信息,套餐包含的检查组信息,检查组包含的检查项信息)
     * @Param: [id]
     * @return: com.itheima.pojo.Setmeal
     */
    @Override
    public Setmeal findById(int id) {
        Setmeal setmeal = setmealDao.findById(id);
        int[] ids = new int[]{id};
        /*
            此处的套餐id只有一个,此时的 for(int setmealId : ids) 循环可以省略
            但是当id有多个时,就会像下面的检查组的id一样进行循环
        */
        for (int setmealId : ids) {
            List<CheckGroup> checkGroups = checkGroupDao.listGroupsBySetmealId(setmealId);
            for (CheckGroup checkGroup : checkGroups) {
                List<CheckItem> checkItems = checkItemDao.listItemsBycheckGroupId(checkGroup.getId());
                checkGroup.setCheckItems(checkItems);
            }
            setmeal.setCheckGroups(checkGroups);
        }
        return setmeal;
    }


    /**
     * @Description: //TODO 设置套餐和检查项的多对多的关联关系
     * @Param: [setmealId, checkgroupIds]
     * @return: void
     */
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
