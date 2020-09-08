package com.wudi.td.util;

import com.wudi.td.entity.primary.SysMenu;
import com.wudi.td.entity.vo.MenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 转换菜单返回格式
 */
public class MenuUtil {

    public static List<MenuVo> previousPage(List<SysMenu> menus) {
        List<MenuVo> menuVos = new LinkedList<>();
        menus.forEach(menu -> {
            if (menu.getParentId().equals(0L)) {
                MenuVo menuVo = new MenuVo();
                BeanUtils.copyProperties(menu, menuVo);
                menuVos.add(menuVo);
            }
        });
        menuVos.forEach(menuVo -> {
            List<MenuVo> menuVoList = getChild(menuVo.getMenuId(), menus);
            if (!CollectionUtils.isEmpty(menuVoList)) {
                Collections.sort(menuVoList, new Comparator<MenuVo>() {
                    @Override
                    public int compare(MenuVo o1, MenuVo o2) {
                        int i = o1.getOrderNum() - o2.getOrderNum();
                        if (i == 0) {
                            return o1.getOrderNum() - o2.getOrderNum();
                        }
                        return i;
                    }
                });
            }
            menuVo.setChildren(menuVoList);
        });
        return menuVos;
    }
    /**
     * 返回子菜单
     * @param menuId 一级菜单id
     * @param menus 所有菜单
     * @return
     */
    public static List<MenuVo> getChild(Long menuId, List<SysMenu> menus) {
        List<MenuVo> menuVoList = new LinkedList<>();
        menus.forEach(menu->{
            if (menu.getParentId().equals(menuId)){
                MenuVo menuVo = new MenuVo();
                BeanUtils.copyProperties(menu,menuVo);
                menuVoList.add(menuVo);
            }
        });
        if (!CollectionUtils.isEmpty(menuVoList)){
            menuVoList.forEach(menuVo ->{
                menuVo.setChildren(getChild(menuVo.getMenuId(),menus));
            });
            return menuVoList;
        }
        return null;
    }
}
