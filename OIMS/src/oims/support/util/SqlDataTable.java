/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author ezouyyi
 */
public class SqlDataTable {
    private String     tableName_;
    private Vector     tableHead_;
    private Vector     data_;
    
    public SqlDataTable(ResultSet rs, String tableName) throws SQLException
    {
        tableName_ = tableName;
        data_ = new Vector();
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
    }
    public Vector getColumnNames(){return this.tableHead_;}
    public Vector getData(){return this.data_;}
}
