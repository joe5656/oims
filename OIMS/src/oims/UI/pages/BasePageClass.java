/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages;

import javax.swing.JFrame;
import oims.UI.UiManager;

/**
 *
 * @author ezouyyi
 */
public class BasePageClass extends JFrame implements Page{

    protected final Page.PAGE_TYPE pageType_;
    protected final UiManager itsUiManager_;    
    
    public BasePageClass(UiManager uiM, Page.PAGE_TYPE type)
    {
        itsUiManager_ = uiM;
        pageType_ = type;
        this.addWindowListener(new OimsWindowsListener(itsUiManager_, this, pageType_));
    }

    @Override
    public void go() {
        this.setVisible(true);}

    @Override
    public void close() {
        this.setVisible(false);}
}
