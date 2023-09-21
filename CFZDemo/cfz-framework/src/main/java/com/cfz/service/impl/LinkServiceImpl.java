package com.cfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cfz.ResponseResult;
import com.cfz.constants.SystemConstants;
import com.cfz.entity.Link;
import com.cfz.entity.vo.LinkVo;
import com.cfz.entity.vo.PageVo;
import com.cfz.mapper.LinkMapper;
import com.cfz.service.LinkService;
import com.cfz.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-02-04 21:07:40
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过对
        LambdaQueryWrapper<Link> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(lambdaQueryWrapper);
        //转换vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(linkVos);
    }

    /**
     * 获取友链列表
     *
     * @param pageNum
     * @param pageSize
     * @param linkVo
     * @return
     */
    @Override
    public ResponseResult<LinkVo> listLink(Integer pageNum, Integer pageSize, LinkVo linkVo) {
        LambdaQueryWrapper<Link> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //判断 友链名 是否为空
        if (!ObjectUtils.isEmpty(linkVo.getName())) {
            lambdaQueryWrapper.like(Link::getName, linkVo.getName());
        }
        //判断 友链状态是否为空
        if (!ObjectUtils.isEmpty(linkVo.getStatus())) {
            lambdaQueryWrapper.like(Link::getStatus, linkVo.getStatus());
        }

        //分页查询
        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, lambdaQueryWrapper);

        //转换vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(page.getRecords(), LinkVo.class);
        return ResponseResult.okResult(new PageVo(linkVos, page.getTotal()));
    }
}

