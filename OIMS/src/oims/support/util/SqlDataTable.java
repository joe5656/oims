/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;

import com.google.common.collect.Lists;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ezouyyi
 */
public class SqlDataTable {
    private String     tableName_;
    private Vector     tableHead_;
    private Vector     data_;
    private List<Integer> selectedRows_;
    
    public SqlDataTable(String tableName)
    {
       tableName_ = tableName==null?"noName":tableName;
       data_ = new Vector();
       selectedRows_ = Lists.newArrayList();
       tableHead_ = new Vector();
    }
    
    public SqlDataTable()
    {
       tableName_ = "noName";
       data_ = new Vector();
       selectedRows_ = Lists.newArrayList();
       tableHead_ = new Vector();
    }
    
    public SqlDataTable(ResultSet rs, String tableName)
    {
        tableName_ = tableName;
        data_ = new Vector();
        selectedRows_ = Lists.newArrayList();
        tableHead_ = new Vector();
        try {
            if(rs != null && rs.first())
            {
                ResultSetMetaData metaData = rs.getMetaData();
                Integer loop = metaData.getColumnCount();
                for(int i = 1; i <= loop; i++)
                {
                    tableHead_.add(metaData.getColumnName(i));
                }
                
                rs.first();
                do
                {
                    Vector row = new Vector();
                    for(int i = 1; i <= loop; i++)
                    {
                        row.add(rs.getString(i));
                    }
                    data_.add(row);
                }while(rs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(SqlDataTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Vector getColumnNames(){return this.tableHead_;}
    public Vector getData(){return this.data_;}
    public void   setRowSelected(Integer rowNum)
    {
        if(rowNum >= 0 && rowNum < this.data_.size())
        {
            this.selectedRows_.add(rowNum);
        }
    }
    public void     setRowUnselected(Integer rowNum)
    {
        this.selectedRows_.remove(rowNum);
    }
    public Vector getSelectedRows()
    {
        Vector result = new Vector();
        for(int i:this.selectedRows_)
        {
            result.add(data_.get(i));
        }
        return result;
    }
    public void setHeader(Vector header){tableHead_ = header;}
    public void addRow(Vector data)
    {
        if(this.data_ != null)
        {
            this.data_.add(data);
        }
    }
}
