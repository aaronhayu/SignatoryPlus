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

package org.tenece.ap.controller;

import de.laures.cewolf.jfree.ThermometerPlot;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.plot.dial.DialPointer.Pin;
import org.springframework.web.servlet.ModelAndView;
import org.tenece.ap.chart.dataset.DefaultChartDS;
import org.tenece.ap.chart.dataset.PieChartDS;
import org.tenece.ap.chart.dataset.REString;
import org.tenece.web.common.ConfigReader;
import org.tenece.web.common.DateUtility;
import org.tenece.web.data.Chart;
import org.tenece.web.services.DashBoardService;

/**
 * Tenece Professional Services Limited
 * @author amachree
 */
public class DashBoardController extends BaseController {

    private DashBoardService dashBoardService;

    public ModelAndView viewDashBoard(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("dashboard/dashboard_view");
        //pie info
        List<Chart> list = new ArrayList<Chart>();
        Chart ch1 = new Chart();
        ch1.setKey("Development"); ch1.setdValue(20);
        list.add(ch1);

        Chart ch2 = new Chart();
        ch2.setKey("Operations"); ch2.setdValue(19);
        list.add(ch2);

        Chart ch3 = new Chart();
        ch3.setKey("HR"); ch3.setdValue(59);
        list.add(ch3);

        DefaultChartDS ds = new DefaultChartDS();
        ds.setChartList(list);
        view.addObject("pieData", ds);

        //to be used for thermometer, dial and meter
        DefaultChartDS ds1 = new DefaultChartDS();
        ds1.setDefaultValue(95);
        view.addObject("dialData", ds1);

        //category dataset
        List<Chart> _list = new ArrayList<Chart>();
        Chart _ch1 = new Chart();
        _ch1.setCategoryName("2008");
        _ch1.setSeriesName("Development"); _ch1.setdValue(20);
        _list.add(_ch1);
//        Chart _ch11 = new Chart();
//        _ch11.setCategoryName("2008");
//        _ch11.setSeriesName("Operations"); _ch11.setdValue(20);
//        _list.add(_ch11);

        Chart _ch2 = new Chart();
        _ch2.setCategoryName("2009");
        _ch2.setSeriesName("Operations"); _ch2.setdValue(19);
        _list.add(_ch2);

        Chart _ch3 = new Chart();
        _ch3.setCategoryName("2009");
        _ch3.setSeriesName("HR"); _ch3.setdValue(59);
        _list.add(_ch3);

        DefaultChartDS ds2 = new DefaultChartDS();
        ds2.setChartList(_list);
        view.addObject("categoryData", ds2);
        return view;
    }

    public ModelAndView viewEmployeeCountChart(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("dashboard/piechart");
        view.addObject("chartType", "pie3d");
        //pie info
        view.addObject("title", "Signature Based on Status");
        List<Chart> list = getDashBoardService().getEmployeeCount();

        DefaultChartDS ds = new DefaultChartDS();
        ds.setChartList(list);
        view.addObject("pieData", ds);

        return view;
    }

    public ModelAndView viewMaxSignatureMeterChart(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView("dashboard/dialchart");
        String maxNumber = getDashBoardService().getMaximumSignatureNumber();

        view.addObject("title", "Last Signature Number: "+ maxNumber);
        view.addObject("unitText", "Signature Number");
        //to be used for thermometer, dial and meter
        DefaultChartDS ds1 = new DefaultChartDS();
        String maxSignNumber = getDashBoardService().getMaximumSignatureNumber();
        float maxSignNo = Float.parseFloat(maxSignNumber.substring(2, maxSignNumber.length()-1));      
        ds1.setDefaultValue(maxSignNo);
        view.addObject("dialData", ds1);

        return view;
    }

    /**
     * @return the dashBoardService
     */
    public DashBoardService getDashBoardService() {
        return dashBoardService;
    }

    /**
     * @param dashBoardService the dashBoardService to set
     */
    public void setDashBoardService(DashBoardService dashBoardService) {
        this.dashBoardService = dashBoardService;
    }
}
