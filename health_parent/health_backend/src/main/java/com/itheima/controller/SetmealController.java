package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @program: Itcast_health
 * @ClassName: SetmealController
 * @description: 体检套餐管理
 * @author: KyleSun
 **/
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    /**
     * @Description: //TODO 图片上传到云服务器
     * @Param: [multipartFile]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("imgFile") MultipartFile multipartFile) {

        // 获取图片的原始文件名 xxx.jpg ,并通过原始文件名获取后缀: .jpg
        String originalFilename = multipartFile.getOriginalFilename();
        // 比如: xxx.jpg 获取最后一个点的索引 3
        int lastIndex = originalFilename.lastIndexOf('.');
        // 截取方法: [a,b)->"左闭右开",因此从索引 3 开始截取到结束: .jpg
        String suffix = originalFilename.substring(lastIndex);
        // 获得上传至服务器的文件名 fileName,uuid文件名保证唯一性
        String fileName = UUID.randomUUID().toString() + suffix;

        try {
            // 将文件上传至七牛云服务器
            QiNiuUtils.uploadFile(multipartFile.getBytes(), fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        /*
            此处需要返回我们上传的文件名fileName,这里的fileName为Result中的data,
            返回前端页面后,通过response.data获取文件名
            fileName 文件名的用处:
                1. 返回前端用于拼接地址,从而获取 imageUrl => 图片的链接,实现预览功能
                2. 返回数据库用于保存
         */
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
    }


    @Reference
    private SetmealService setmealService;


    /**
     * @Description: //TODO 新增套餐
     * @Param: [setmeal, checkgroupIds]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }
}
