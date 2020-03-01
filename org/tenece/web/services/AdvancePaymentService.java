/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tenece.web.services;

import java.util.ArrayList;
import java.util.List;
import org.tenece.ap.data.dao.AdvancePaymentDAO;
import org.tenece.web.data.AdvanceCollection;

/**
 *
 * @author root
 */
public class AdvancePaymentService extends BaseService {
    private AdvancePaymentDAO advancePaymentDAO;

    /* ------------------ AdvanceCollection ------------------ */
    public boolean updateAdvancePayment(AdvanceCollection item, int mode){
        try{
            if(this.MODE_INSERT == mode){
                getAdvancePaymentDAO().createNewAdvancePayment(item);
            }else if(this.MODE_UPDATE == mode){
                getAdvancePaymentDAO().updateAdvancePayment(item);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public List<AdvanceCollection> findAllAdvancePayment(long employeeId){
        try{
            return getAdvancePaymentDAO().getAllAdvancePayment(employeeId);
        }catch(Exception e){
            return new ArrayList<AdvanceCollection>();
        }
    }

    public List<AdvanceCollection> findAllAdvancePayment(long employeeId, String criteria, String searchText){
        try{
            return getAdvancePaymentDAO().getAllAdvancePayment(employeeId, criteria, searchText);
        }catch(Exception e){
            return new ArrayList<AdvanceCollection>();
        }
    }

    public AdvanceCollection findAdvancePaymentById(int id){
        try{
            return getAdvancePaymentDAO().getAdvancePaymentById(id);
        }catch(Exception e){
            return null;
        }
    }

    public AdvanceCollection findAdvancePaymentClosedById(long id){
        try{
            return getAdvancePaymentDAO().getAdvancePaymentClosedById(id);
        }catch(Exception e){
            return null;
        }
    }

    public Long checkNumberOfAdvanceForEmployee(long employeeId){
        return getAdvancePaymentDAO().checkNumberOfAdvanceForEmployee(employeeId);
    }

    public boolean updateCloseAdvancePayment(AdvanceCollection item, int mode){
        try{
            if(this.MODE_INSERT == mode){
                getAdvancePaymentDAO().createNewClosedAdvancePayment(item);
            }else if(this.MODE_UPDATE == mode){
                //getAdvancePaymentDAO().updateAdvancePayment(item);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean deleteAdvancePayment(int id){
        try{
            AdvanceCollection item = new AdvanceCollection();
            item.setId(id);
            getAdvancePaymentDAO().deleteAdvancePayment(item);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public boolean deleteAdvancePayment(List<Integer> ids){
        try{
            getAdvancePaymentDAO().deleteAdvancePayment(ids);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * @return the advancePaymentDAO
     */
    public AdvancePaymentDAO getAdvancePaymentDAO() {
        return advancePaymentDAO;
    }

    /**
     * @param advancePaymentDAO the advancePaymentDAO to set
     */
    public void setAdvancePaymentDAO(AdvancePaymentDAO advancePaymentDAO) {
        this.advancePaymentDAO = advancePaymentDAO;
    }

}
