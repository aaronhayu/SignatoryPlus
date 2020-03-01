
/*
 * (c) Copyright 2008 Tenece Professional Services.
 *
 * Created on 27 May 2009, 13:28
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
package org.tenece.ap.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.tenece.web.common.FormatUtility;
import org.tenece.web.data.Transaction;
import org.tenece.web.data.TransactionType;
import org.tenece.web.data.Users;
import org.tenece.web.services.EmployeeService;
import org.tenece.web.services.TransactionService;
import org.tenece.web.services.UserService;

/**
 *
 * @author jeffry.amachree
 */
public class InboxController extends BaseController {

    private UserService userService = new UserService();
    private TransactionService transactionService = new TransactionService();
    /** Creates a new instance of DepartmentController */
    public InboxController() {
    }

    public ModelAndView viewInbox(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView view = new ModelAndView("inbox_view");
        //get user detail from session
        Users user = this.getUserPrincipal(request);

    
        //instantiate list for results
        List<Transaction> list = new ArrayList<Transaction>();

        //get search criteria
        String searchKey = request.getParameter("cbSearch");
        String searchValue = request.getParameter("txtSearch");

       // check if user requested for a search result
        
        if(searchKey == null || searchValue == null){
        //use the id to get all transactions for the user
        list = getTransactionService().findInboxTransactions(user.getId());
        
        }else{
        list = getTransactionService().findInboxTransactions(user.getId(), searchKey, searchValue);
        }
         
       // add to view
        view.addObject("result", list);
        //return view to UI - (inbox-view)
        return view;
    }

    public ModelAndView viewInboxDetail(HttpServletRequest request, HttpServletResponse response) {
        
        ModelAndView view = new ModelAndView("error");
        //get user detail from session
        Users user = this.getUserPrincipal(request);
        //use the id to get all transactions for the user
        
        String strTrnx = request.getParameter("idx");
        //check if id is a number
        if(!FormatUtility.isNumber(strTrnx)){
        view = new ModelAndView("error");
        return view;
        }
        Transaction trnx = transactionService.findTransactionById(Long.parseLong(strTrnx));
        TransactionType transactionType = transactionService.findTransactionTypeById(trnx.getTransactionType());
        
        if(transactionType.getParentId().equals("USER_CREATION")){
        return getUserDetailViewForCreation(trnx);
        }
        else if(transactionType.getParentId().equals("BULK_USER_CREATION")){
        return getBulkUserDetailView(trnx);
        }
        else if(transactionType.getParentId().equals("USER_DELETION")){
        return getUserDetailViewForDeletion(trnx);
        }
        view = new ModelAndView("error");
        view.addObject("message", "Unable to process transaction, contact system administrator for support.");
        
         
        return null;
    }
    private ModelAndView getUserDetailViewForCreation(Transaction transaction){
        ModelAndView view = new ModelAndView("transaction/users_edit");
       
       
        Users user = getUserService().findUserByTransaction(transaction.getId());
        user.setTransactionId(transaction.getId());
        view.addObject("roleList", getUserService().findRoles());
        view.addObject("user", user);
        
        return view;
    }
    private ModelAndView getUserDetailViewForDeletion(Transaction transaction){
        ModelAndView view = new ModelAndView("transaction/users_delete");
       
       
        Users user = getUserService().findUserByTransaction(transaction.getId());
        user.setTransactionId(transaction.getId());
        view.addObject("roleList", getUserService().findRoles());
        view.addObject("user", user);
        
        return view;
    }
   private ModelAndView getBulkUserDetailView(Transaction transaction){
        ModelAndView view = new ModelAndView("transaction/user_upload_view");
        System.out.println("getUserDetailView");
       
        List<Users> userlist = getUserService().findBulkUserByTransaction(transaction.getId());
        view.addObject("result", userlist);
        view.addObject("transactionId", transaction.getId());
        
        return view;
    }
    /**
     * @return the transactionService
     */
    public TransactionService getTransactionService() {
        return transactionService;
    }

    /**
     * @param transactionService the transactionService to set
     */
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
  /**
     * @return the userService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
