/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
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
public class EmployeeTable extends Db_table{
    static public String getDerivedTableName()
    {
        return "EmployeeTable";
    }
    public EmployeeTable(DataBaseManager dbm)
    {
        super("EmployeeTable", dbm, Table_Type.TABLE_TYPE_MIRROR);
        super.registerColumn("gender", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR10,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("deptId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("positionId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("enrollDate", Db_publicColumnAttribute.ATTRIBUTE_NAME.DATE,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("birthDate", Db_publicColumnAttribute.ATTRIBUTE_NAME.DATE,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("password", Db_publicColumnAttribute.ATTRIBUTE_NAME.TEXT,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("valid", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("contact", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.TRUE,  Boolean.FALSE, null);
        super.registerColumn("picurl", Db_publicColumnAttribute.ATTRIBUTE_NAME.TEXT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("NationalId", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("name", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("EmployeeId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    
    public SqlResultInfo newEntry(Employee employee)
    {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        Date date = new Date(); 
        try {
            date = timeFormat.parse(employee.getBirthDay());
        } catch (ParseException ex) {
            Logger.getLogger(EmployeeTable.class.getName()).log(Level.SEVERE, null, ex);
            result.setErrInfo(ex.toString());
            return result;
        }
        return this.newEntry(employee.getName(), employee.getNationId(), 
                employee.getUrl(), employee.getContact(), date,
                employee.getPositionId(), employee.getDepId(), employee.getGender());
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
    
    public SqlResultInfo queryEmployeeGeneralInfo(String employeeId, String employeeName)
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
    
    public Boolean checkPassword(String id, String pw)
    {
        Boolean value = false;
        TableEntry select = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("EmployeeId", "select");
        valueHolder.put("gender", "select");
        valueHolder.put("valid", "1");
        valueHolder.put("contact", "select");
        valueHolder.put("name", "select");
        
        TableEntry eq = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        valueHoldereq.put("EmployeeId", id);
        valueHoldereq.put("password", pw);
        
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
                Logger.getLogger(EmployeeTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return value;
    }
    static public String getPrimaryKeyColNameInCh(){return EnToCh("EmployeeId");} 
    static public String getPrimaryKeyColNameInEng(){return "EmployeeId";}
    static public String getEmployeeNameColNameInCh(){return EnToCh("name");} 
    static public String getEmployeeNameColNameInEng(){return "name";}
}
