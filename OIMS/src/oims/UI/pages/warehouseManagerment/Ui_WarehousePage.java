/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages.warehouseManagerment;

import java.util.Vector;
import oims.UI.UiManager;
import oims.UI.pages.BasePageClass;
import oims.UI.pages.Page;
import oims.UI.pages.employeePage.EmployeePageRx;
import oims.UI.pages.employeePage.EmployeePickerTx;
import oims.dataBase.tables.EmployeeTable;
import oims.dataBase.tables.WareHouseTable;
import oims.employeeManager.EmployeeManager;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;

/**
 *
 * @author ezouyyi
 */
public class Ui_WarehousePage extends BasePageClass implements WarehousePageRx,WarehousePickerTx,EmployeePickerTx{
    private WarehousePageTx   itsWarehousePageTx_;
    private SqlDataTable      tempTable_;
    private String            tempKey_;
    private SqlDataTable      itsEmloyeeTmpDTable_;
    private String            tempEmployeeKey_;
    private EmployeeManager   itsEmployeeM_;
    /**
     * Creates new form Ui_WarehouseManagerment
     */
    public Ui_WarehousePage(UiManager uiM,WarehousePageTx itsWarehousePageTx,EmployeeManager em) {
        super(uiM,Page.PAGE_TYPE.SUB_PAGE);
        itsWarehousePageTx_ = itsWarehousePageTx;
        itsWarehousePageTx_.setRx(this);
        itsEmployeeM_ = em;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        newWarehouseB = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        createStatus = new javax.swing.JLabel();
        createPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        warehouseName = new javax.swing.JTextField();
        warehouseContact = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        warehouseAddr = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        createCancelB = new javax.swing.JButton();
        createB = new javax.swing.JButton();
        keeperLable = new javax.swing.JLabel();
        selEmployeeB = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        delB = new javax.swing.JButton();
        delCancelB = new javax.swing.JButton();
        DelSelB = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        delStatusLable = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(750, 720));
        setPreferredSize(new java.awt.Dimension(750, 720));
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("新建仓库"));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("操作"));

        jButton1.setText("查询所有仓库列表");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        newWarehouseB.setText("新建仓库");
        newWarehouseB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newWarehouseBActionPerformed(evt);
            }
        });

        jLabel1.setText("状态：");

        createStatus.setText("等待创建仓库");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(newWarehouseB)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(createStatus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(createStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(newWarehouseB))
                .addContainerGap())
        );

        createPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("仓库信息"));
        createPanel.setEnabled(false);

        jLabel3.setText("仓库名称");

        jLabel4.setText("仓库地址");

        warehouseName.setEnabled(false);

        warehouseContact.setEnabled(false);

        jLabel5.setText("仓库电话");

        warehouseAddr.setEnabled(false);

        jLabel6.setText("仓库管理员");

        createCancelB.setText("取消");
        createCancelB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createCancelBActionPerformed(evt);
            }
        });

        createB.setText("创建");
        createB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createBActionPerformed(evt);
            }
        });

        keeperLable.setText("未制定管理员");

        selEmployeeB.setText("选择管理员");
        selEmployeeB.setEnabled(false);
        selEmployeeB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selEmployeeBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout createPanelLayout = new javax.swing.GroupLayout(createPanel);
        createPanel.setLayout(createPanelLayout);
        createPanelLayout.setHorizontalGroup(
            createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, createPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(createCancelB)
                .addGap(34, 34, 34)
                .addComponent(createB)
                .addGap(13, 13, 13))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, createPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(warehouseName, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(warehouseAddr))
                .addGap(67, 67, 67)
                .addGroup(createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, createPanelLayout.createSequentialGroup()
                        .addComponent(keeperLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selEmployeeB))
                    .addComponent(warehouseContact, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        createPanelLayout.setVerticalGroup(
            createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(warehouseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(warehouseContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(warehouseAddr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(keeperLable)
                    .addComponent(selEmployeeB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(createB)
                    .addComponent(createCancelB))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(createPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("删除仓库"));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("信息"));
        jPanel5.setEnabled(false);

        jLabel7.setText("删除");

        delB.setText("确认删除");
        delB.setEnabled(false);
        delB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delBActionPerformed(evt);
            }
        });

        delCancelB.setText("取消");
        delCancelB.setEnabled(false);
        delCancelB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delCancelBActionPerformed(evt);
            }
        });

        DelSelB.setText("选择要删除的仓库");
        DelSelB.setEnabled(false);
        DelSelB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DelSelBActionPerformed(evt);
            }
        });

        jLabel2.setText("仓库：未选择仓库");

        jLabel10.setText("仓库地址：未选择仓库");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(DelSelB)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(delCancelB)
                        .addGap(18, 18, 18)
                        .addComponent(delB)
                        .addGap(42, 42, 42))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(DelSelB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel2)
                            .addComponent(jLabel10)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(delCancelB)
                            .addComponent(delB))))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("操作"));

        jLabel8.setText("状态：");

        delStatusLable.setText("删除操作未激活 点击删除仓库按钮激活");

        jButton6.setText("删除仓库");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(delStatusLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6)
                .addGap(43, 43, 43))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(delStatusLable)
                    .addComponent(jButton6))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       this.showWarehousePicker(null);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void newWarehouseBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWarehouseBActionPerformed
        this.toggleCreateArea(Boolean.TRUE);
    }//GEN-LAST:event_newWarehouseBActionPerformed

    private void createCancelBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createCancelBActionPerformed
        this.toggleCreateArea(Boolean.FALSE);
        this.createStatus.setText("等待创建仓库");
        clearCreateArea();
    }//GEN-LAST:event_createCancelBActionPerformed

    private void createBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createBActionPerformed
        String keeper = this.tempEmployeeKey_==null?"":this.tempEmployeeKey_;
        String addr   = this.warehouseAddr.getText() ;
        String contact= this.warehouseContact.getText() ;
        String name   = this.warehouseName.getText() ;
        if("".equals(name) || "".equals(addr) || "".equals(contact) || "".equals(keeper))
        {
            this.createStatus.setText("信息不完整");
            return;
        }
        SqlResultInfo rs = this.itsWarehousePageTx_.newWareHouse(name, Integer.parseInt(keeper), addr, contact);
        if(rs.isSucceed())
        {
            this.toggleCreateArea(Boolean.FALSE);
            this.clearCreateArea();
            this.createStatus.setText("创建成功，可以继续创建或返回");
            this.tempEmployeeKey_= null;
            itsEmloyeeTmpDTable_ = null;
        }
        else
        {
            this.createStatus.setText(rs.getErrInfo());
        }
    }//GEN-LAST:event_createBActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        toggleDelArea(Boolean.TRUE);
        this.delStatusLable.setText("已激活删除，尚未开始删除");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void delCancelBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delCancelBActionPerformed
        
        toggleDelArea(Boolean.FALSE);
        
    }//GEN-LAST:event_delCancelBActionPerformed

    private void delBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delBActionPerformed
        if(this.tempTable_ != null && this.tempKey_!=null)
        {
            SqlResultInfo result = this.itsWarehousePageTx_.deleteWareHouse(Integer.parseInt(tempKey_));
            if(result.isSucceed())
            {
                toggleDelArea(Boolean.FALSE);
                this.delStatusLable.setText("删除成功 点击删除仓库按钮继续删除其它仓库");
            }
            else
            {
                toggleDelArea(Boolean.FALSE);
                this.delStatusLable.setText("删除失败 "+result.getErrInfo()+" 点击删除仓库按钮继续删除其它仓库");
            }
        }
        else
        {
            toggleDelArea(Boolean.FALSE);
            this.delStatusLable.setText("删除失败 点击删除仓库按钮继续删除其它仓库");
        }
        this.tempKey_ = null;
        this.tempTable_ = null;
    }//GEN-LAST:event_delBActionPerformed

    private void DelSelBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DelSelBActionPerformed
        this.showWarehousePicker(this);
    }//GEN-LAST:event_DelSelBActionPerformed

    private void selEmployeeBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selEmployeeBActionPerformed
       itsEmployeeM_.needEmployeePicker(this,0);
    }//GEN-LAST:event_selEmployeeBActionPerformed
    
    private void toggleDelArea(Boolean on)
    {
        this.delCancelB.setEnabled(on);
        this.DelSelB.setEnabled(on);
        if(on == Boolean.FALSE)
        {
            this.delStatusLable.setText("删除操作未激活 点击删除仓库按钮激活");
            this.jLabel2.setText("仓库：未选择仓库");
            this.jLabel10.setText("仓库地址：未选择仓库");
            this.delB.setEnabled(on);
        }
    }
    private void toggleCreateArea(Boolean on)
    {
        this.selEmployeeB.setEnabled(on);
        this.createPanel.setEnabled(on);
        this.warehouseName.setEnabled(on);
        this.keeperLable.setEnabled(on);
        this.warehouseAddr.setEnabled(on);
        this.createCancelB.setEnabled(on);
        this.warehouseContact.setEnabled(on);
        this.createB.setEnabled(on);
        this.tempEmployeeKey_=null;
    }
    
    private void clearCreateArea()
    {
        this.keeperLable.setText("未指定管理员");
        this.warehouseAddr.setText(null);
        this.warehouseContact.setText(null);
        this.warehouseName.setText(null);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DelSelB;
    private javax.swing.JButton createB;
    private javax.swing.JButton createCancelB;
    private javax.swing.JPanel createPanel;
    private javax.swing.JLabel createStatus;
    private javax.swing.JButton delB;
    private javax.swing.JButton delCancelB;
    private javax.swing.JLabel delStatusLable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel keeperLable;
    private javax.swing.JButton newWarehouseB;
    private javax.swing.JButton selEmployeeB;
    private javax.swing.JTextField warehouseAddr;
    private javax.swing.JTextField warehouseContact;
    private javax.swing.JTextField warehouseName;
    // End of variables declaration//GEN-END:variables

    @Override
    public void DataSelected(SqlDataTable dTable, Integer id) {
        this.delB.setEnabled(Boolean.TRUE);
        this.tempTable_ = dTable;
        Vector head = dTable.getColumnNames();
        Integer addrDataIndex = head.indexOf(WareHouseTable.getAddreessColNameInCh());
        Integer nameDataIndex = head.indexOf(WareHouseTable.getWarehouseNameColNameInCh());
        Integer primaryKeyIndex = head.indexOf(WareHouseTable.getPrimaryKeyColNameInCh());
        if(addrDataIndex != -1 && nameDataIndex != -1 && primaryKeyIndex != -1)
        {
            Vector data = (Vector)dTable.getSelectedRows().get(0);
            this.jLabel2.setText((String)data.get(nameDataIndex));
            this.jLabel10.setText("仓库地址： "+(String)data.get(addrDataIndex));
            this.tempKey_ = (String)data.get(primaryKeyIndex);
        }
    }

    
    private void showWarehousePicker(WarehousePickerTx rx) {
        WarehouseListPage page = new WarehouseListPage(this.itsWarehousePageTx_.queryAllWarehouseInfo(), rx,0);
        page.setVisible(Boolean.TRUE);
    }

    @Override
    public void employeeDataSelected(SqlDataTable dTable, Integer id) {
        this.itsEmloyeeTmpDTable_ = dTable;
        Vector head = dTable.getColumnNames();
        Integer nameDataIndex = head.indexOf(EmployeeTable.getEmployeeNameColNameInEng());
        Integer primaryKeyIndex = head.indexOf(EmployeeTable.getPrimaryKeyColNameInEng());
        if(nameDataIndex != -1 && primaryKeyIndex != -1)
        {
            Vector data = (Vector)dTable.getSelectedRows().get(0);
            this.keeperLable.setText((String)data.get(nameDataIndex));
            this.tempEmployeeKey_ = (String)data.get(primaryKeyIndex);
        }
    }
}
