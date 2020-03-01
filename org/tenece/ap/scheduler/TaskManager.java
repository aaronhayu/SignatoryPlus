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

package org.tenece.ap.scheduler;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

/**
 *
 * @author amachree
 */
public class TaskManager {
    private static TaskManager taskMgr = null;

    /**
     * @return the taskMgr
     */
    public static TaskManager getTaskMgr() {
        return taskMgr;
    }

    /**
     * @param aTaskMgr the taskMgr to set
     */
    public static void setTaskMgr(TaskManager aTaskMgr) {
        taskMgr = aTaskMgr;
    }
    private SchedulerFactory scheduleFactory = null;
    private Scheduler scheduler = null;

    private TaskManager() throws SchedulerException{
        init();
    }
    private void init() throws SchedulerException{
        setScheduleFactory(new org.quartz.impl.StdSchedulerFactory());
        setScheduler(getScheduleFactory().getScheduler());
        getScheduler().start();
    }

    public static TaskManager getInstance() throws SchedulerException{
        //confirm if object is null
        if(getTaskMgr() == null){
                setTaskMgr(new TaskManager());
        }
        //return the instantiated instance
        return getTaskMgr();
    }

    /**
     * @return the scheduleFactory
     */
    public SchedulerFactory getScheduleFactory() {
        return scheduleFactory;
    }

    /**
     * @param scheduleFactory the scheduleFactory to set
     */
    public void setScheduleFactory(SchedulerFactory scheduleFactory) {
        this.scheduleFactory = scheduleFactory;
    }

    /**
     * @return the scheduler
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * @param scheduler the scheduler to set
     */
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
