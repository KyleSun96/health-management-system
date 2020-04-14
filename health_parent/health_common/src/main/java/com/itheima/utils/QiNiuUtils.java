package com.itheima.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * @program: Itcast_health
 * @ClassName: QiNiuUtils
 * @description: 七牛云工具类抽取
 **/
public class QiNiuUtils {

    /**
     * 生成上传凭证，然后准备上传
     */
    private static String accessKey = "YozUon5xZ0yHsOH_Zer4F229Pihnle-e1B5Pyh2Q";
    private static String secretKey = "CkU_QpyzALq7WTanXMnweqKCi8cJfhec1ob9A8s7";
    private static String bucket = "health-management-system";

    /**
     * @Description: //TODO 将本地文件上传到七牛云服务器 -- 文件
     * @Param: [filePath, fileName]
     * @return: void
     */
    public static void uploadFile(String filePath, String fileName) {
        //构造一个带指定 Zone 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(filePath, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }


    /**
     * @Description: //TODO 将本地文件上传到七牛云服务器 -- byte数组
     * @Param: [bytes, fileName]
     * @return: void
     */
    public static void uploadFile(byte[] bytes, String fileName) {
        //构造一个带指定 Zone 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(bytes, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * @Description: //TODO 删除七牛云服务器中的文件
     * @Param: [fileName]
     * @return: void
     */
    public static void deleteFileFromQiNiu(String fileName) {
        //构造一个带指定 Zone 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, fileName);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}

