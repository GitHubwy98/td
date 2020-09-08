package com.wudi.td.entity.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class MenuVo {

    private Long menuId;

    private String menuName;

    private Long parentId;

    private Integer orderNum;

    private String url;

    private String icon;

    List<MenuVo> children;
}
