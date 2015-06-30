/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.employeeManager;

/**
 *
 * @author ezouyyi
 */
public class Employee {
    private Integer id_;
    private String  name_;
    private Integer depId_;
    private String  enrollDate_;
    private String  birthDate_;
    private String  nationalId_;
    private String  contact_;
    private Boolean valid_;
    private String  url_;
    private String  password_;
    private String  gender_;
    private Integer  positionId_;
    
    public Employee()
    {
        id_ = 0;
        valid_ = Boolean.TRUE;
        name_ = "Doe John";
        depId_ = 1;
        birthDate_ = "1900-1-1";
        nationalId_ = "12345";
        contact_ = "0000000";
        gender_ = "male";
        positionId_ = 0;
        url_ = "NA";
    }
    
    public Integer getId(){return id_;}
    public String  getName(){return name_;}
    public Integer  getDepId(){return depId_;}
    public String  getEnrollDate(){return enrollDate_;}
    public String  getBirthDay(){return birthDate_;}
    public String  getNationId(){return nationalId_;}
    public String  getContact(){return contact_;}
    public Boolean  isValid(){return valid_;}
    public String  getUrl(){return url_;}
    public String  getPassword(){return password_;}
    public String  getGender(){return gender_;}
    public Integer  getPositionId(){return positionId_;}
    
    public void  setId(Integer id){  id_ = id;}
    public void  setName(String name){  name_ = name;}
    public void  setDepId(Integer id){  depId_ = id;}
    public void  setEnrollDate(String date){  enrollDate_ = date;}
    public void  setBirthDay(String date){  birthDate_ = date;}
    public void  setNationId(String id){  nationalId_ = id;}
    public void  setContact(String id){  contact_ = id;}
    public void  isValid(Boolean valid){  valid_ = valid;}
    public void  setUrl(String url){  url_ = url;}
    public void  setPassword(String pw){  password_ = pw;}
    public void  setGender(String gender){gender_ = ("男".equals(gender) || "male".equals(gender))? "male":"female";}
    public void  setPositionId(Integer id){  positionId_ = id;}
}
