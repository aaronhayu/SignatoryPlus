/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tenece.web.data;

import java.util.Date;
import org.tenece.web.common.NumberToWords;

/**
 *
 * @author root
 * id bigint auto_increment NOT NULL,
	advance_to varchar(200) NOT NULL,
	application_date date NOT NULL,
	
	id, advance_to, application_date, form_number, emp_number, purpose, amount, current_advance_temp_amount, current_advance_perm_amount, active
 */
public class AdvanceCollection extends BaseData{
    private long id;
    private String advanceTo;
    private Date applicationDate;
    private String formNumber;
    private long employeeId;
    private String purpose;
    private float amount;
    private float currentAdvance_Temp_Amount;
    private float currentAdvance_Permanent_Amount;
    private String status;

    private long closeId;
    private float totalExpenses;
    private float lessExpenses;
    
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the advanceTo
     */
    public String getAdvanceTo() {
        return advanceTo;
    }

    /**
     * @param advanceTo the advanceTo to set
     */
    public void setAdvanceTo(String advanceTo) {
        this.advanceTo = advanceTo;
    }

    /**
     * @return the applicationDate
     */
    public Date getApplicationDate() {
        return applicationDate;
    }

    /**
     * @param applicationDate the applicationDate to set
     */
    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    /**
     * @return the formNumber
     */
    public String getFormNumber() {
        return formNumber;
    }

    /**
     * @param formNumber the formNumber to set
     */
    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    /**
     * @return the employeeId
     */
    public long getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the purpose
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * @return the amount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * @return the currentAdvance_Temp_Amount
     */
    public float getCurrentAdvance_Temp_Amount() {
        return currentAdvance_Temp_Amount;
    }

    /**
     * @param currentAdvance_Temp_Amount the currentAdvance_Temp_Amount to set
     */
    public void setCurrentAdvance_Temp_Amount(float currentAdvance_Temp_Amount) {
        this.currentAdvance_Temp_Amount = currentAdvance_Temp_Amount;
    }

    /**
     * @return the currentAdvance_Permanent_Amount
     */
    public float getCurrentAdvance_Permanent_Amount() {
        return currentAdvance_Permanent_Amount;
    }

    /**
     * @param currentAdvance_Permanent_Amount the currentAdvance_Permanent_Amount to set
     */
    public void setCurrentAdvance_Permanent_Amount(float currentAdvance_Permanent_Amount) {
        this.currentAdvance_Permanent_Amount = currentAdvance_Permanent_Amount;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the totalExpenses
     */
    public float getTotalExpenses() {
        return totalExpenses;
    }

    /**
     * @param totalExpenses the totalExpenses to set
     */
    public void setTotalExpenses(float totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    /**
     * @return the lessExpenses
     */
    public float getLessExpenses() {
        return lessExpenses;
    }

    /**
     * @param lessExpenses the lessExpenses to set
     */
    public void setLessExpenses(float lessExpenses) {
        this.lessExpenses = lessExpenses;
    }

    public String getAmountInWords(){
        return NumberToWords.convertToMoney(amount);
    }

    /**
     * @return the closeId
     */
    public long getCloseId() {
        return closeId;
    }

    /**
     * @param closeId the closeId to set
     */
    public void setCloseId(long closeId) {
        this.closeId = closeId;
    }

}
