
/*
 * (c) Copyright 2008 Tenece Professional Services.
 *
 * Created on 24 May 2009, 22:11
 *
 * ============================================================
 * Project Info:  http://www.tenece.com
 * Project Lead:  Amachree Jeffry (info@tenece.com);
 * ============================================================
 *
 *
 * Licensed under the Tenece Professional Services;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.tenece.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Strategiex. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "as is," without a warranty of any
 * kind. All express or implied conditions, representations and
 * warranties, including any implied warranty of merchantability,
 * fitness for a particular purpose or non-infringement, are hereby
 * excluded.
 * Tenece and its licensors shall not be liable for any damages
 * suffered by licensee as a result of using, modifying or
 * distributing the software or its derivatives. In no event will Strategiex
 * or its licensors be liable for any lost revenue, profit or data, or
 * for direct, indirect, special, consequential, incidental or
 * punitive damages, however caused and regardless of the theory of
 * liability, arising out of the use of or inability to use software,
 * even if Tenece Professional Services has been advised of the possibility of such damages.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package org.tenece.ap.dao.db;

/**
 *
 * @author jeffry.amachree
 */
public class DatabaseQueryLoader {

    private static QueryHolder queryHandler = null;
    

    public static QueryHolder getQueryAssignedConstant(){
        if(queryHandler == null){
            System.out.println("Database Query Loader....");
            java.util.Properties database = org.tenece.web.common.ConfigReader.getDatabaseConfig();
            String dbType = database.getProperty(org.tenece.web.filter.ApplicationFilter.APPLICATION_DBTYPE);

            System.out.println("Database Type...." + dbType);

            queryHandler = new QueryHolder();
            
            //System.out.println("Database Test:" + queryHandler.TEST_QUERY);
        }
        return queryHandler;
    }

}
