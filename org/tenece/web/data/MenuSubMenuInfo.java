/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tenece.web.data;

import com.google.common.collect.Multimap;
import java.util.List;

/**
 *
 * @author kenneth
 */
public class MenuSubMenuInfo {
    
      private Multimap<String, MenuData> subMenuMap;
      private List<MenuData> menuList;
    
    public Multimap<String, MenuData> getSubMenuMap() {
        return subMenuMap;
    }

    public void setSubMenuMap(Multimap<String, MenuData> subMenuMap) {
        this.subMenuMap = subMenuMap;
    }
     public List<MenuData> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuData> menuList) {
        this.menuList = menuList;
    }
}
