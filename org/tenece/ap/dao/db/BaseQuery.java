/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tenece.ap.dao.db;

/**
 *
 * @author root
 */
public abstract class BaseQuery{
    protected String TEST_QUERY = "select 'mssql' ";

    public  String ORDER_BY_1 = " order by 1";
    public  String ORDER_BY_2 = " order by 2";
    public  String ORDER_BY_3 = " order by 3";
    public  String ORDER_BY_4 = " order by 4";
    public  String ORDER_BY_5 = " order by 5";

    public  String COUNTRY_SELECT = "select code, description from country order by 1 ";
    public  String RELATIONSHIP_SELECT = "select code, description from relationship order by 1";

}
