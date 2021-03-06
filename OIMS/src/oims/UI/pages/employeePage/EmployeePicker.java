/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages.employeePage;

import javax.swing.ListSelectionModel;
import oims.support.util.SqlDataTable;
import oims.support.util.UneditableTableModule;

/**
 *
 * @author freda
 */
public class EmployeePicker extends javax.swing.JFrame {
    private SqlDataTable      itsSqlDTable_;
    private EmployeePickerTx  pickerTx_;
    private final Integer     callerIdentifier_;
    /**
     * Creates new form EmployeePicker
     */
    public EmployeePicker(SqlDataTable table, EmployeePickerTx pickerTx, Integer identifier) {
        initComponents();
        callerIdentifier_ = identifier;
        if(table!=null)
        {
            this.jTable1.setModel(new UneditableTableModule(table.getData(),table.getColumnNames()));
            itsSqlDTable_ = table;
            if(pickerTx != null)
            {
                this.selectB.setEnabled(Boolean.TRUE);
                pickerTx_ = pickerTx;
            }
            else
            {
                this.selectB.setEnabled(Boolean.FALSE);
            }
            jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        else
        {
            jTable1.setVisible(Boolean.FALSE);
            this.selectB.setEnabled(Boolean.FALSE);
        }
        
    }                                      

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        selectB = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("员工信息"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        selectB.setText("选择");
        selectB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(selectB)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(selectB)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectBActionPerformed
        if(this.jTable1.getSelectedRows().length == 0)
        {return;}
        for(Integer row:this.jTable1.getSelectedRows())
        {
            this.itsSqlDTable_.setRowSelected(row);
        }
        this.pickerTx_.employeeDataSelected(itsSqlDTable_,this.callerIdentifier_);
        this.dispose();
    }//GEN-LAST:event_selectBActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton selectB;
    // End of variables declaration//GEN-END:variables
}
