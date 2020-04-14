package com.itheima.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * @program: Itcast_health
 * @ClassName: QiNiuTest
 * @description: 七牛云
 * @author: KyleSun
 * @create: 15:28 2020/4/13
 **/
public class QiNiuTest {


    // 使用七牛云提供SDK将本地图片上传到七牛云服务器
    @Test
    public void uploadPic() {

//构造一个带指定 Zone 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "YozUon5xZ0yHsOH_Zer4F229Pihnle-e1B5Pyh2Q";
        String secretKey = "CkU_QpyzALq7WTanXMnweqKCi8cJfhec1ob9A8s7";
        String bucket = "health-management-system";
// String localFilePath = "/home/qiniu/test.png"; 一般不用绝对路径,因为在生产中,单元测试无法运行
        String localFilePath = this.getClass().getResource("/friend_404.gif").getPath();
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "404q.gif";

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
//解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }


    // 使用七牛云提供SDK将七牛云服务器中的图片删除
    @Test
    public void deletePic(){
//构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        String accessKey = "YozUon5xZ0yHsOH_Zer4F229Pihnle-e1B5Pyh2Q";
        String secretKey = "CkU_QpyzALq7WTanXMnweqKCi8cJfhec1ob9A8s7";
        String bucket = "health-management-system";
        String key = "404q.gif";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
