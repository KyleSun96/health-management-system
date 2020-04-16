package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @program: Itcast_health
 * @ClassName: ClearImgJob
 * @description: 定时任务组件: 自定义job,实现定时清理垃圾图片
 * @author: KyleSun
 **/
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    /**
     * @Description: //TODO 实现定时清理垃圾图片
     * @Param: []
     * @return: void
     */
    public void clearImg() {
        // sdiff: 返回差值set集合,比如 sdiff(a,b) => 集合a减去子集合b
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (sdiff != null && sdiff.size() != 0) {
            for (String fileName : sdiff) {
                // 删除云端的垃圾图片
                QiNiuUtils.deleteFileFromQiNiu(fileName);
            }
            // 删除Redis中的所有的缓存图片
            Jedis resource = jedisPool.getResource();
            resource.del(RedisConstant.SETMEAL_PIC_RESOURCES);
            resource.del(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
            resource.close();
            // 删除缓存控制台提示
            System.out.println("我好了,你们呢?");
        }
    }
}
