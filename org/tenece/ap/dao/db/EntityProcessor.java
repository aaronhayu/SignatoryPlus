/*
 * (c) Copyright 2008 Tenece Professional Services.
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
 * even if Strategiex has been advised of the possibility of such damages.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

/**
 * 
 */
package org.tenece.ap.dao.db;

import java.sql.Connection;
import java.util.Properties;

import org.strategiex.connection.ConnectionPool;
import org.tenece.web.common.ConfigReader;
import org.tenece.web.filter.ApplicationFilter;

/**
 * @author amachree
 *
 */
public class EntityProcessor {
	private static EntityProcessor proc = null;
	private ConnectionPool connectionPool = null;
	private EntityProcessor(){
		init();
	}
	
	private void init(){
		//get config
		//ConfigReader config = ConfigReader.getInstance();
		Properties dbConfig = ConfigReader.getDatabaseConfig();
		//make database connection
                System.out.println("**********" + dbConfig.getProperty(ApplicationFilter.APPLICATION_DATABASE_URL) + "**********");
		try{
			connectionPool = ConnectionPool.getInstance(
					dbConfig.getProperty(ApplicationFilter.APPLICATION_DATABASE_DRIVER),
					dbConfig.getProperty(ApplicationFilter.APPLICATION_DATABASE_URL),
					dbConfig.getProperty(ApplicationFilter.APPLICATION_DATABASE_USERNAME),
					dbConfig.getProperty(ApplicationFilter.APPLICATION_DATABASE_PASSWORD),
					Integer.parseInt("10"),
					Integer.parseInt("2"),
					Integer.parseInt("1"),
					dbConfig.getProperty("select * from dual"));
		}catch(Exception e){
                    e.printStackTrace();
                    connectionPool = null;
		}
		//store instance in object for later reference
	}
	
	public static EntityProcessor getInstance(){
		if(proc == null){
			proc = new EntityProcessor();
		}
		return proc;
	}
	public Connection acquireConnection() throws Exception{
            if(connectionPool == null){
                throw new Exception("Unable to instantiate connection. check system log for more information.");
            }
            return connectionPool.getConnection();
	}
	
	private ConnectionPool getConnectionPool() {
            return connectionPool;
	}

	private void setConnection(ConnectionPool connectionPool) {
            this.connectionPool = connectionPool;
	}

}
