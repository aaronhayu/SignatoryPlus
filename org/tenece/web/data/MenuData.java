/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tenece.web.data;

import com.google.common.collect.Multimap;

/**
 *
 * @author kenneth
 */
public class MenuData implements Comparable<MenuData> {
    
    private String menuTitle;
    private String divClass;
    private String uRL;
    private String submenutitle;
    private String target;
    private int orderCode;
    private int menuId;
    private int id;
    private int menuInRoleId;
    private String roleName;
    private int roleId;
    
    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }
    public String getDivClass() {
        return divClass;
    }

    public void setDivClass(String divClass) {
        this.divClass = divClass;
    }
    public String getURL() {
        return uRL;
    }

    public void setURL(String uRL) {
        this.uRL = uRL;
    }
     public String getSubmenutitle() {
        return submenutitle;
    }

    public void setSubmenutitle(String submenutitle) {
        this.submenutitle = submenutitle;
    }
    
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
   
     public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }
      public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
       public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
        public int getMenuInRoleId() {
        return menuInRoleId;
    }

    public void setMenuInRoleId(int menuInRoleId) {
        this.menuInRoleId = menuInRoleId;
    }
         public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    
             public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    @Override
    public int compareTo(MenuData o) {

        if(this.orderCode > o.orderCode){
            return 1;
        }
        return 0;
    }

}
