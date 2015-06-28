/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import oims.UI.UiManager;

/**
 *
 * @author ezouyyi
 */
public class OimsWindowsListener  implements WindowListener{
    private final UiManager itsUiManager_;
    private final Page.PAGE_TYPE itsPageType_;
    private final Page itsPage_;
    public OimsWindowsListener(UiManager uiM, Page page, Page.PAGE_TYPE type)
    {
        itsUiManager_ = uiM;
        itsPageType_ = type;
        itsPage_ = page;
    }
    
    @Override
    public void windowOpened(WindowEvent we) {
        }

    @Override
    public void windowClosing(WindowEvent we) {
        itsUiManager_.onPageClosed(itsPage_, itsPageType_);
    }

    @Override
    public void windowClosed(WindowEvent we) {
        
    }

    @Override
    public void windowIconified(WindowEvent we) {
        }

    @Override
    public void windowDeiconified(WindowEvent we) {
        }

    @Override
    public void windowActivated(WindowEvent we) {
        }

    @Override
    public void windowDeactivated(WindowEvent we) {
        }

    
}
