/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages.reciptPage;

import oims.support.util.SqlDataTable;

/**
 *
 * @author ezouyyi
 */
public interface DetailReciptPickerTx {
    public void DetailReciptDataSelected(SqlDataTable dTable, Integer identity);
}
