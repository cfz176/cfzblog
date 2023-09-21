package com.cfz;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cfz.entity.ArticleTag;
import com.cfz.mapper.ArticleTagMapper;
import com.cfz.service.ArticleTagService;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;

@SpringBootTest(classes = CFZBlogApplication.class)
public class OSSTest {

    private ArticleTagMapper articleTagMapper;
    private ArticleTagService articleTagService;

    private String accessKey;
    private String secretKey;
    private String bucket;

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Test
    public void test() {
        LambdaUpdateWrapper<ArticleTag> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ArticleTag::getArticleId,14);
        articleTagService.remove(lambdaUpdateWrapper);
    }

    @Test
    public void OSSTest(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        cfg.resumableUploadMaxConcurrentTaskCount = 2;  // 设置分片上传并发，1：采用同步上传；大于1：采用并发上传
        //...其他参数参考类注释

        //...生成上传凭证，然后准备上传
//        String accessKey = "a_OaSnzAFpmvgupELkN_Efxe6cFrcpIgIRx-D9-8";
//        String secretKey = "M-wkpdqKLlMihkNG1KOWyR5IDw-dwcX55jQTF7JQ";
//        String bucket = "cfz-blog";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//        String localFilePath = "/home/qiniu/test.mp4";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        String localTempDir = Paths.get(System.getenv("java.io.tmpdir"), bucket).toString();
        try {
            InputStream inputStream = new FileInputStream("C:\\Users\\cfz17\\Pictures\\Feedback\\{0A0398A6-57EF-4170-9D49-30A9FD2016F0}\\Capture001.png");
            //设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            try {
                Response response = uploadManager.put(inputStream, key, upToken,null,null);
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
