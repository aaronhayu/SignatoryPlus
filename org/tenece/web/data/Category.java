/*
 * (c) Copyright 2010 The Tenece Professional Services.
 *
 * Created on 6 February 2009, 09:57
 *
 * ============================================================
 * Project Info:  http://tenece.com
 * Project Lead:  Aaron Osikhena (aaron.osikhena@tenece.com);
 * ============================================================
 *
 *
 * Licensed under the Tenece Professional Services;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.tenece.com/
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
package org.tenece.web.data;

/**
 * Tenece Professional Services Limited
 * @author Kenneth
 */
public class Category {
    private String key;
    private String description;
    private String currencyCode;
    private float transactionLimt;
    private String formattedtxnLimt;

     /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
 /**
     * @param key the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
     /**
     * @return the description
     */
    public String getCurrencyCode() {
        return currencyCode;
    }
 /**
     * @param key the description to set
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    /**
     * @param key the description to set
     */
    public void setTransactionLimt(float transactionLimt) {
        this.transactionLimt = transactionLimt;
    }
     /**
     * @return the description
     */
    public float getTransactionLimt() {
        return transactionLimt;
    }
   
 /**
     * @param key the description to set
     */
    public void setFormattedtxnLimt(String formattedtxnLimt) {
        this.formattedtxnLimt = formattedtxnLimt;
    }
     /**
     * @return the description
     */
    public String getFormattedtxnLimt() {
        return formattedtxnLimt;
    }
   
}
