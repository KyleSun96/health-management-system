package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: Itcast_health
 * @ClassName: CheckItemController
 * @description: 检查项管理
 **/

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference // dubbo包内
    private CheckItemService checkItemService;

    /*
        @RestController中的 @ResponseBody:
            将后端方法返回前端的java对象 转换为json对象
        @RequestBody:
            将前端获取的json对象 转换为java对象
    */

    /**
     * @Description: //TODO 新增检查项
     * @Param: [checkItem]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            // 服务调用失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }


    /**
     * @Description: //TODO 检查项分页查询
     * @Param: [queryPageBean]
     * @return: com.itheima.entity.PageResult
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkItemService.findPage(queryPageBean);
    }


    /**
     * @Description: //TODO 删除检查项
     * @Param: [id]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/delete.do")
    public Result delete(Integer id) { // id绑定在URL参数中,因此不需要加 @RequestBody
        try {
            checkItemService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            // 服务调用失败
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }


    /**
     * @Description: //TODO 编辑检查项
     * @Param: [checkItem]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            // 服务调用失败
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }


    /**
     * @Description: //TODO 编辑检查项弹窗 根据检查项Id数据回显
     * @Param: [id]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/findById.do")
    public Result findById(Integer id) { // id绑定在URL参数中,因此不需要加 @RequestBody
        try {
            CheckItem checkItem = checkItemService.findById(id);
            // 返回查询到的java对象,由@ResponseBody转换为json对象
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            // 服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }



    /**
     * @Description: //TODO 查询所有检查项
     * @Param: []
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/findAll.do")
    public Result findAll() {
        try {
            List<CheckItem> checkItemList = checkItemService.findAll();
            // 返回查询到的java对象,由@ResponseBody转换为json对象
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            // 服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
