package com.cfz.job;

import com.cfz.constants.SystemConstants;
import com.cfz.entity.Article;
import com.cfz.service.ArticleService;
import com.cfz.utils.RedisCache;
import com.cfz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisCache redisCache;

    @Scheduled(cron = "0/55 * * * * ?")
    public void testJob() {
        Map<String, Integer> cacheMap = redisCache.getCacheMap(SystemConstants.ARTICLE_REDIS_KEY);
        List<Article> articles = cacheMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());

        if (!ObjectUtils.isEmpty(SecurityUtils.getUserId())) {
            //定时任务
            articleService.updateBatchById(articles);
        }
    }

}
