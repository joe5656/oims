/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.employeeManager.Employee;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;

/**
 *
 * @author ezouyyi
 */
public class SellTicketDetailTable extends Db_table{
    static public String getDerivedTableName()
    {
        return "SellTicketTable";
    }
    public SellTicketDetailTable(DataBaseManager dbm)
    {
        super("SellTicketTable", dbm, Table_Type.TABLE_TYPE_2_LEVEL);
        super.registerColumn("resvfield3", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("resvfield2", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("resvfield1", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("productPrice", Db_publicColumnAttribute.ATTRIBUTE_NAME.DOUBLE,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("memberName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("memberId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("orderTime", Db_publicColumnAttribute.ATTRIBUTE_NAME.TIME,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("quantity", Db_publicColumnAttribute.ATTRIBUTE_NAME.DOUBLE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("productName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("ticketId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("id", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo newEntry(String name, String NationalId, String picurl, String contact,
                            Date birthDate, Integer posi,
                            Integer deptId, String gender)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        if(!"male".equals(gender) && !"female".equals(gender))
        {
            result.setErrInfo("员工信息不合法，性别请填写“男/女”");
            return result;
        }
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String enroll = timeFormat.format(new Date(System.currentTimeMillis()));
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("enrollDate", enroll);
        valueHolder.put("gender", gender);
        valueHolder.put("deptId", deptId.toString());
        valueHolder.put("positionId", posi.toString());
        valueHolder.put("birthDate", timeFormat.format(birthDate));
        valueHolder.put("valid", "1");
        valueHolder.put("contact", contact);
        valueHolder.put("password", "123456");
        valueHolder.put("picurl", picurl);
        valueHolder.put("NationalId", NationalId);
        valueHolder.put("name", name);
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        else
        {
            result.setErrInfo("数据库连接请求失败，位置: EmployeeTable.newEntry");
        }
        
        return result;        
    }
    
    public SqlResultInfo update(String id, String name, String NationalId, String picurl, String contact,
                            Integer posi,Integer deptId, Boolean valid, String pw)
    {
         SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        
       
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        if(deptId!=null)valueHolder.put("deptId", deptId.toString());
        if(posi!=null)valueHolder.put("positionId", posi.toString());
        if(valid!=null)valueHolder.put("valid", valid==true?"1":"0");
        if(contact!=null)valueHolder.put("contact", contact);
        if(pw!=null)valueHolder.put("password", pw);
        if(picurl!=null)valueHolder.put("picurl", picurl);
        if(NationalId!=null)valueHolder.put("NationalId", NationalId);
        if(name!=null)valueHolder.put("name", name);
        
        
        // where
        TableEntry wh = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        valueHoldereq.put("EmployeeId", id);
        
        if(entryToBeInsert.fillInEntryValues(valueHolder) && wh.fillInEntryValues(valueHoldereq))
        {
            result = super.update(entryToBeInsert, wh, null, null);
        }
        else
        {
            result.setErrInfo("数据库连接请求失败，位置: EmployeeTable.newEntry");
        }
        
        return result;  
    }
    
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "EmployeeTable":
            {
                return "员工信息表";
            }
            case "gender":
            {
                return "性别";
            }       
            case "deptId":
            {
                return "部门ID";
            }       
            case "positionId":
            {
                return "职位ID";
            }   
            case "enrollDate":
            {
                return "入职日期";
            }   
            case "birthDate":
            {
                return "出生日期";
            }    
            case "password":
            {
                return "密码";
            }     
            case "valid":
            {
                return "是否在职";
            }        
            case "contact":
            {
                return "联系电话";
            }      
            case "picurl":
            {
                return "照片";
            }       
            case "NationalId":
            {
                return "身份证号码";
            }   
            case "name":
            {
                return "名字";
            }         
            case "EmployeeId":
            {
                return "员工号码";
            }   
            default:
            {
                return "错误";
            }
        }
    }
    @Override
    public void translateColumnName(Vector col)
    {
        for(int i = 0; i<col.size();i++)
        {
            col.setElementAt(EnToCh((String)col.elementAt(i)), i);
        }
    }
    
    public SqlResultInfo queryEmployeeGeneralInfo(String employeeId, String employeeName, Boolean acitveFlag)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        TableEntry select = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("EmployeeId", "select");
        valueHolder.put("gender", "select");
        valueHolder.put("valid", "1");
        valueHolder.put("contact", "select");
        valueHolder.put("name", "select");
        
        TableEntry eq = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        if(employeeId != null)valueHoldereq.put("EmployeeId", employeeId);
        if(employeeName != null)valueHoldereq.put("name", employeeName);
        if(acitveFlag != null)valueHoldereq.put("valid", acitveFlag?"1":"0");
        
        if(select.fillInEntryValues(valueHolder)
                && eq.fillInEntryValues(valueHoldereq))
        {
            result = super.select(select, eq, null, null);
        }
        else
        {
            result.setErrInfo("数据库连接请求失败，位置: EmployeeTable.newEntry");
        }
        
        return result;        
    }
    
    public void serializeEmployee(Employee e, String employeeId)
    {
        TableEntry entryToBeQuery = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("enrollDate", "select");
        valueHolder.put("gender", "select");
        valueHolder.put("deptId", "select");
        valueHolder.put("positionId", "select");
        valueHolder.put("birthDate", "select");
        valueHolder.put("valid", "select");
        valueHolder.put("contact", "select");
        valueHolder.put("password", "select");
        valueHolder.put("picurl", "select");
        valueHolder.put("NationalId", "select");
        valueHolder.put("name", "select");
        valueHolder.put("EmployeeId", "select");    
        
        // where
        TableEntry where = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        valueHoldereq.put("EmployeeId", employeeId);
        
        if(entryToBeQuery.fillInEntryValues(valueHolder) && where.fillInEntryValues(valueHoldereq))
        {
            SqlResultInfo result = super.select(entryToBeQuery, where, null, null);
            if(result.isSucceed())
            {
                ResultSet rs = result.getResultSet();
                try {
                    if(rs.first())
                    {
                        e.setId(rs.getInt("EmployeeId"));
                        e.setName(rs.getString("name"));
                        e.setNationId(rs.getString("NationalId"));
                        e.setUrl(rs.getString("picurl"));
                        e.setPassword(rs.getString("password"));
                        e.setContact(rs.getString("contact"));
                        e.setBirthDay(rs.getString("birthDate"));
                        e.setPositionId(rs.getInt("positionId"));
                        e.setDepId(rs.getInt("deptId"));
                        e.setGender(rs.getString("gender"));
                        e.setEnrollDate(rs.getString("enrollDate"));
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(SellTicketDetailTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public Boolean checkPassword(String id, String pw)
    {
        Boolean value = false;
        
        TableEntry select = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("EmployeeId", "select");
        valueHolder.put("gender", "select");
        valueHolder.put("contact", "select");
        valueHolder.put("name", "select");
        
        TableEntry eq = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        valueHoldereq.put("EmployeeId", id);
        valueHoldereq.put("password", pw);
        valueHoldereq.put("valid", "1");
        
        if(select.fillInEntryValues(valueHolder)
                && eq.fillInEntryValues(valueHoldereq))
        {
            SqlResultInfo result = super.select(select, eq, null, null);
            try {
                if(result.isSucceed() && result.getResultSet().first())
                {
                    value = true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(SellTicketDetailTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(value == false)
        {
            // no employee entry in system at all? login with admin
            TableEntry sel = generateTableEntry();
            Map<String, String> selHolder = Maps.newHashMap();
            selHolder.put("EmployeeId", "select");
            TableEntry where = generateTableEntry();
            Map<String, String> selHoldereq = Maps.newHashMap();
            selHoldereq.put("valid", "1");
            
            
            if(sel.fillInEntryValues(selHolder) && where.fillInEntryValues(valueHoldereq))
            {
                SqlResultInfo result = super.select(select, where, null, null);
                try {
                    if(result.isSucceed() && !result.getResultSet().first())
                    {
                        value = ("admin".equals(id)) && ("admin".equals(pw));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(SellTicketDetailTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        return value;
    }
    static public String getPrimaryKeyColNameInCh(){return EnToCh("EmployeeId");} 
    static public String getPrimaryKeyColNameInEng(){return "EmployeeId";}
    static public String getEmployeeNameColNameInCh(){return EnToCh("name");} 
    static public String getEmployeeNameColNameInEng(){return "name";}
}
