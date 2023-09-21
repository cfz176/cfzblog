package cfz;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cfz.CFZAdminApplication;
import com.cfz.entity.ArticleTag;
import com.cfz.entity.vo.MenuVo;
import com.cfz.mapper.ArticleTagMapper;
import com.cfz.service.ArticleTagService;
import com.cfz.service.MenuService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest(classes = CFZAdminApplication.class)
public class TestDemo {

    @Autowired
    MenuService menuService;

    @Test
    public void test() {
        }

    }

