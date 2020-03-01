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

package org.tenece.web.data;

/**
 * This is the Data object to handle Department records.
 * It is also tied to DepartmentDAO for all Database Operation
 * @author jeffry.amachree
 */
public class ReportTemplate {
    private String code;
    private String control;
    private String controlValue;
    private String procedures;
    private  String custom1;
    private String captions;
    /** Creates a new instance of Department */
    public ReportTemplate() {
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the control
     */
    public String getControl() {
        return control;
    }

    /**
     * @param control the control to set
     */
    public void setControl(String control) {
        this.control = control;
    }

    /**
     * @return the controlValue
     */
    public String getControlValue() {
        return controlValue;
    }

    /**
     * @param controlValue the controlValue to set
     */
    public void setControlValue(String controlValue) {
        this.controlValue = controlValue;
    }

    /**
     * @return the procedures
     */
    public String getProcedures() {
        return procedures;
    }

    /**
     * @param procedures the procedures to set
     */
    public void setProcedures(String procedures) {
        this.procedures = procedures;
    }

    /**
     * @return the custom1
     */
    public String getCustom1() {
        return custom1;
    }

    /**
     * @param custom1 the custom1 to set
     */
    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    /**
     * @return the captions
     */
    public String getCaptions() {
        return captions;
    }

    /**
     * @param captions the captions to set
     */
    public void setCaptions(String captions) {
        this.captions = captions;
    }

    
}
