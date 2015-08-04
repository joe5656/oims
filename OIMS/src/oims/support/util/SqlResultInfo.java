/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author freda
 */
public class SqlResultInfo {
    private Boolean executeResult_;
    private String  errInfo_;
    private Integer index_;
    private ResultSet rs_;
    
    public SqlResultInfo(Boolean result)
    {
        executeResult_ = result;
        if(result == Boolean.TRUE)
        {
            errInfo_ = "executing succeeded!";
        }
        else
        {
            errInfo_ = "information not set!";
        }
        index_ = 0;
    }
    
    public Boolean isRsEmpty()
    {
        Boolean result = false;
        try {
            if(this.rs_ != null && rs_.first())
            {
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SqlResultInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    public void    setSucceed()
    {
        executeResult_ = Boolean.TRUE;
        errInfo_ = "executing succeeded!";
    }
    public Boolean isSucceed(){return this.executeResult_;}
    public void    setErrInfo(String err){this.errInfo_ = err;this.executeResult_=Boolean.FALSE;}
    public void    setErrInfo(SQLException e){this.errInfo_ = e.toString();this.executeResult_=Boolean.FALSE;}
    public void    setIndex(Integer ind){this.index_ = ind;}
    public Integer getIndex(){return this.index_;}
    public String  getErrInfo(){return this.errInfo_;}
    public void    setResultSet(ResultSet rs){this.rs_ = rs;}
    public ResultSet getResultSet(){return this.rs_;}
}
