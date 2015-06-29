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
    private String[][] data_;
    
    public SqlDataTable(ResultSet rs, String tableName) throws SQLException
    {
        tableName_ = tableName;
        if(rs != null && rs.first())
        {
            ResultSetMetaData metaData = rs.getMetaData();
            Integer loop = metaData.getColumnCount();
            rs.first();
            for(int i = 1; i <= loop; i++)
            {
                tableHead_.add(metaData.getColumnName(i));
                int row = 1;
                do
                {
                    data_[row][i] = rs.getString(row);
                    row++;
                }while(rs.next());
            }
        }
    }
    
    
}
