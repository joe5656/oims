/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.reciptManagement;

/**
 *
 * @author ezouyyi
 */
public class ProductRecipt {
    private final String  productName_;
    private Integer standardNum_;
    private String  mainReciptName_;
    private String  topReciptName_;
    private String  fillingReciptName_;
    private Double  topFactor_;
    private Double  mainFactor_;
    private Double  fillingFactor_;
    private Boolean mainByCk_;
    private Boolean topByCk_;
    private Boolean fillByCk_;
    
    public ProductRecipt(String name)
    {
        productName_ = name;
        standardNum_ = 0;
    }
    
    public void setProductRecipt(Integer sNum, String mr, String tr, String fr, 
            Double mf, Double tf, Double ff, Boolean mCk, Boolean tCk, Boolean fCk)
    {
        standardNum_ = sNum;
        mainReciptName_ = mr;
        topReciptName_ = tr;
        fillingReciptName_ = fr;
        topFactor_ = tf;
        mainFactor_ = mf;
        fillingFactor_ = ff;
        mainByCk_ = mCk;
        topByCk_ = tCk;
        fillByCk_ = fCk;
    }
    
    public String getMainReciptName(){return mainReciptName_;}
    public String getTopReciptName(){return topReciptName_;}
    public String getFillReciptName(){return fillingReciptName_;}
    public Double getMainFactor(){return mainFactor_;}
    public Double getTopFactor(){return topFactor_;}
    public Double getFillFactor(){return fillingFactor_;}
    public Integer getStandNum(){return standardNum_;}
}
