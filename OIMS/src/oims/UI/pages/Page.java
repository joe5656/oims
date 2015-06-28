/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages;

import oims.UI.UiManager;

/**
 *
 * @author ezouyyi
 */
public interface Page {
    public enum PAGE_TYPE
    {
        MAIN_PAGE,
        SUB_PAGE
    }
    public void go();
    public void close();
}
