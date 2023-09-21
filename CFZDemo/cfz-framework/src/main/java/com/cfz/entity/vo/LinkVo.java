package com.cfz.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 友链(Link)表实体类Vo
 *
 * @author makejava
 * @since 2023-02-04 21:07:38
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkVo {

    private Long id;

    private String name;

    private String logo;

    private String status;

    private String description;
    //网站地址
    private String address;

}

