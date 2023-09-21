package com.cfz.utils;

import com.cfz.entity.Article;
import com.cfz.entity.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

//    private BeanCopyUtils() {
//
//    }

    public static <V> V copyBean(Object list, Class<V> cla) {
        //创建目标对象
        V result = null;
        try {
            result = cla.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(list, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回结果
        return result;
    }

    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> cla) {
        return list.stream()
                .map(v -> copyBean(v, cla))
                .collect(Collectors.toList());
    }

}
