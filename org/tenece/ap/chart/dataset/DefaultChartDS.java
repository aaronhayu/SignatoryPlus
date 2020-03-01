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

package org.tenece.ap.chart.dataset;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.tenece.web.data.Chart;

/**
 * Tenece Professional Services Limited
 * @author amachree
 */
public class DefaultChartDS implements DatasetProducer, Serializable {
    private List<Chart> chartList;
    private String specialKey;
    private float defaultValue;

    @Override
    public Object produceDataset(Map param) throws DatasetProduceException {

        //use this key to get value
        String key = "de.laures.cewolf.DatasetProducer.id";

        Object mainDS = null;

        //check if key exist
        if(param.containsKey(key)){
            //check if key value is null or is valid
            if(param.get(key) != null){
                //check if it is pie chart
                if(String.valueOf(param.get(key)).startsWith("pie")){
                    mainDS = getPieDataSet(chartList);
                }else if(String.valueOf(param.get(key)).startsWith("bar")
                        || String.valueOf(param.get(key)).startsWith("category")){
                    mainDS = getCategoryDataSet(chartList);
                }else if(String.valueOf(param.get(key)).startsWith("xy")){
                    mainDS = getXYSeriesDataSet(chartList);
                }else if(String.valueOf(param.get(key)).startsWith("thermometer") ||
                        String.valueOf(param.get(key)).startsWith("dial")
                        || String.valueOf(param.get(key)).startsWith("compass")){
                    mainDS = getThermometerDataSet(defaultValue);
                }
            }
        }
        return mainDS;
        
    }

    private DefaultPieDataset getPieDataSet(List<Chart> chartList){
        DefaultPieDataset ds = new DefaultPieDataset();
        //check if it is null or size is zero.... return empty data set
        if(chartList == null || chartList.size() == 0){
            return ds;
        }
        //loop thru the arrayList to set values
        for(Chart chart : chartList){
            ds.setValue(chart.getKey(), chart.getdValue());
        }
        return ds;
    }

    private TimeSeriesCollection getTimeSeriesDataSet(List<Chart> chartList){
        TimeSeriesCollection ds = new TimeSeriesCollection();
        //check if it is null or size is zero.... return empty data set
        if(chartList == null || chartList.size() == 0){
            return ds;
        }

        TimeSeries pop = new TimeSeries(this.specialKey);
        //loop thru the arrayList to set values
        for(Chart chart : chartList){
            //pop.add(RegularTimePeriod.createInstance(Day.class, chart.getDate(), TimeZone.getDefault()), chart.getiValue());

        }
        ds.addSeries(pop);
        return ds;
    }

    private DefaultValueDataset getThermometerDataSet(float value){
        DefaultValueDataset ds = new DefaultValueDataset();

        ds.setValue(value);

        return ds;
    }

    /**
     * This will be used to create bar chart....
     * // row keys...
        String series1 = "First";
        String series2 = "Second";
        String series3 = "Third";

        // column keys...
        String category1 = "Category 1";
        String category2 = "Category 2";
        String category3 = "Category 3";
        String category4 = "Category 4";
        String category5 = "Category 5";
     * 
     * dataset.addValue(1.0, series1, category1);
        dataset.addValue(4.0, series1, category2);
        dataset.addValue(3.0, series1, category3);
        dataset.addValue(5.0, series1, category4);
        dataset.addValue(5.0, series1, category5);

        dataset.addValue(5.0, series2, category1);
        dataset.addValue(7.0, series2, category2);
        dataset.addValue(6.0, series2, category3);
        dataset.addValue(8.0, series2, category4);
        dataset.addValue(4.0, series2, category5);
     * @param chartList
     * @return
     */
    private DefaultCategoryDataset getCategoryDataSet(List<Chart> chartList){
        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //check if it is null or size is zero.... return empty data set
        if(chartList == null || chartList.size() == 0){
            return dataset;
        }
        //loop thru the arrayList to set values
        for(Chart chart : chartList){
            dataset.addValue(chart.getdValue(), chart.getSeriesName(), chart.getCategoryName());
        }
        return dataset;
    }

    /**
     * An XYDataset can create area, line, and step XY charts
     * 
     * @param chartList
     * @return
     */
    private XYDataset getXYSeriesDataSet(List<Chart> chartList){
        XYSeries localXYSeries = new XYSeries(getSpecialKey());

        XYSeriesCollection ds = null;
        if(chartList == null || chartList.size() == 0){
            return new XYSeriesCollection();
        }
        //loop thru the arrayList to set values
        for(Chart chart : chartList){
            localXYSeries.add(chart.getiValue(), chart.getdValue());
        }
        return new XYSeriesCollection(localXYSeries);
    }


    @Override
    public boolean hasExpired(Map arg0, Date arg1) {
        return false;
    }

    @Override
    public String getProducerId() {
        return "ResourceEdge";
    }

    /**
     * @return the chartList
     */
    public List<Chart> getChartList() {
        return chartList;
    }

    /**
     * @param chartList the chartList to set
     */
    public void setChartList(List<Chart> chartList) {
        this.chartList = chartList;
    }

    /**
     * @return the specialKey
     */
    public String getSpecialKey() {
        return specialKey;
    }

    /**
     * @param specialKey the specialKey to set
     */
    public void setSpecialKey(String specialKey) {
        this.specialKey = specialKey;
    }

    /**
     * @return the defaultValue
     */
    public float getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }

}
