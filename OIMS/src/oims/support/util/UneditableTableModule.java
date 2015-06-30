/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;

import java.util.Vector;

/**
 *
 * @author freda
 */
public class UneditableTableModule extends javax.swing.table.DefaultTableModel{
    public UneditableTableModule(Vector data, Vector col)
    {
        super(data, col);
    }
    @Override
    public boolean isCellEditable(int row, int column) { 
        return false; 
    } 
}
