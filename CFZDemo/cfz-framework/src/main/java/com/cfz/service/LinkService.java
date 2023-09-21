package com.cfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cfz.ResponseResult;
import com.cfz.entity.Link;
import com.cfz.entity.vo.LinkVo;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-02-04 21:07:39
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult<LinkVo> listLink(Integer pageNum, Integer pageSize, LinkVo linkVo);
}

