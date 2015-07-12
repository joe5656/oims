/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages.ProductPage;

import oims.support.util.SqlDataTable;

/**
 *
 * @author freda
 */
public interface ProductPickerTx {
    public void ProductDataSelected(SqlDataTable rx, Integer identity);    
}
