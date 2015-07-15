/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI;

import com.google.common.collect.Maps;
import java.util.Map;
import oims.UI.pages.Page;
import oims.UI.pages.ProductPage.ProductPicker;
import oims.UI.pages.ProductPage.ProductPickerTx;
import oims.UI.pages.ProductPage.Ui_ProductPage;
import oims.UI.pages.employeePage.EmployeePageTx;
import oims.UI.pages.employeePage.Ui_employeeManagerment;
import oims.dataBase.DataBaseManager;
import oims.systemManagement.Client;
import oims.systemManagement.SystemManager;
import oims.UI.pages.initPage.Ui_initPage;
import oims.UI.pages.mainMenu.Ui_mainMenuPage;
import oims.UI.pages.StackPage.Ui_StackMangement;
import oims.UI.pages.employeePage.EmployeePageRx;
import oims.UI.pages.employeePage.EmployeePicker;
import oims.UI.pages.employeePage.EmployeePickerTx;
import oims.UI.pages.rawMaterialPage.RawMaterialPicker;
import oims.UI.pages.rawMaterialPage.RawMaterialPickerTx;
import oims.UI.pages.rawMaterialPage.Ui_rawMaterialPage;
import oims.UI.pages.reciptPage.DetailReciptPicker;
import oims.UI.pages.reciptPage.DetailReciptPickerTx;
import oims.UI.pages.reciptPage.ProductReciptPicker;
import oims.UI.pages.reciptPage.ProductReciptPickerTx;
import oims.UI.pages.reciptPage.Ui_reciptPage;
import oims.UI.pages.storeManagementPage.Ui_storeManagePage;
import oims.UI.pages.warehouseManagerment.Ui_WarehousePage;
import oims.UI.pages.warehouseManagerment.WarehouseListPage;
import oims.UI.pages.warehouseManagerment.WarehousePageTx;
import oims.UI.pages.warehouseManagerment.WarehousePickerTx;
import oims.dataBase.Interfaces.dbStatus.DataBaseRx_dbStatus;
import oims.dataBase.Interfaces.dbStatus.DataBaseTx_dbStatus;
import oims.dataBase.tables.DetailReciptTable;
import oims.employeeManager.EmployeeManager;
import oims.productManagement.ProductManager;
import oims.rawMaterialManagement.RawMaterialManager;
import oims.reciptManagement.ReciptManager;
import oims.storeManagement.StoreManager;
import oims.support.util.SqlDataTable;
/**
 *
 * @author ezouyyi
 */
public class UiManager   implements oims.systemManagement.Client,UiManagerRx, DataBaseTx_dbStatus{
    
    private SystemManager   itsSysManager_;
    private DataBaseManager itsDbm_;
    private Map<UiManagerRx.PageType, Page> itsPages_;
    
    public UiManager(DataBaseManager dbm)
    {
        itsPages_ = Maps.newHashMap();
        itsDbm_ = dbm;
    }
    
    public void showStartingPage()
    {
        Ui_initPage page = new Ui_initPage(this);
        page.go();
        itsPages_.put(PageType.INIT_PAGE, page);
    }
    
    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status)
    {
        switch(status)
        {
            case SYS_INIT:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE,"UI");
                break;
            }
            case SYS_REGISTER:
            {
                if(itsDbm_ != null)
                {
                    itsDbm_.registerListener(this);
                }
                break;
            }
            case SYS_CONFIG:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE,"UI");
                break;
            }
            case SYS_ACTIVE:
            {
                Ui_mainMenuPage page = new Ui_mainMenuPage(this, itsDbm_.islocalDbConnected(),itsDbm_.isRemoteDbConnected());
                itsPages_.put(PageType.MAIN_PAGE, page);
                if(itsPages_.containsKey(PageType.INIT_PAGE))
                {
                    Page initPage = itsPages_.get(PageType.INIT_PAGE);
                    initPage.close();
                }
                page.go();
                itsSysManager_.statusChangeCompleted(Boolean.TRUE,"UI");
                break;
            }
            default:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE,"UI");
                break;
            }
        }
        return Boolean.TRUE;
    }
    
    @Override
    public void setSystemManager(SystemManager sysManager)
    {
        itsSysManager_ = sysManager;
    }
    
    public Client requestManager(SystemManager.clientType managerType)
    {
        return this.itsSysManager_.getClient(SystemManager.clientType.DATABASE_MANAGER);
    }

    @Override
    public Page getPage(PageType pt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showPage(Integer pageId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void onPageClosed(Page page, Page.PAGE_TYPE type)
    {
        switch(type)
        {
            case SUB_PAGE:
            {
                page.close();
                if(itsPages_.containsKey(PageType.MAIN_PAGE))
                {
                    itsPages_.get(PageType.MAIN_PAGE).go();
                }
                break;
            }
            case MAIN_PAGE:
            {
                System.exit(0);
                break;
            }
        }
    }
    
    public void createAndShowPage(PageType type, Page curPage)
    {
        Page pageWanted = null;
        switch(type)
        {
            case MAIN_PAGE:
            {
                if(itsPages_.containsKey(PageType.MAIN_PAGE))
                {
                    pageWanted = itsPages_.get(PageType.MAIN_PAGE);
                }
                else
                {
                    pageWanted = new Ui_mainMenuPage(this, itsDbm_.islocalDbConnected(),itsDbm_.isRemoteDbConnected());
                    itsPages_.put(PageType.MAIN_PAGE, pageWanted);
                }
                break;
            }
            case STACK_PAGE:
            {
                if(itsPages_.containsKey(PageType.STACK_PAGE))
                {
                    pageWanted = itsPages_.get(PageType.STACK_PAGE);
                }
                else
                {
                    pageWanted = new Ui_StackMangement(this);
                    itsPages_.put(PageType.STACK_PAGE, pageWanted);
                }
                break;
            }
            case WAREHOUSE_PAGE:
            {
                if(itsPages_.containsKey(PageType.WAREHOUSE_PAGE))
                {
                    pageWanted = itsPages_.get(PageType.WAREHOUSE_PAGE);
                }
                else
                {
                    WarehousePageTx tempWM = (WarehousePageTx)itsSysManager_.getClient(SystemManager.clientType.WAREHOUSE_MANAGER);
                    EmployeeManager em = (EmployeeManager)itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                    if(tempWM == null){break;}
                    pageWanted = new Ui_WarehousePage(this,tempWM,em);
                    itsPages_.put(PageType.WAREHOUSE_PAGE, pageWanted);
                }
                break;
            }
            case EMPLOYEE_PAGE:
            {
                if(itsPages_.containsKey(PageType.EMPLOYEE_PAGE))
                {
                    pageWanted = itsPages_.get(PageType.EMPLOYEE_PAGE);
                }
                else
                {
                    EmployeePageTx tempEM = (EmployeePageTx)itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                    if(tempEM == null){break;}
                    pageWanted = new Ui_employeeManagerment(this, tempEM);
                    itsPages_.put(PageType.EMPLOYEE_PAGE, pageWanted);
                }
                break;
            }
            case RAWMATERIAL_PAGE:
            {
                if(itsPages_.containsKey(PageType.RAWMATERIAL_PAGE))
                {
                    pageWanted = itsPages_.get(PageType.RAWMATERIAL_PAGE);
                }
                else
                {
                    RawMaterialManager tempRM = (RawMaterialManager)itsSysManager_.getClient(SystemManager.clientType.RAWMATERIAL_MANAGER);
                    if(tempRM == null){break;}
                    pageWanted = new Ui_rawMaterialPage(this, tempRM);
                    itsPages_.put(PageType.RAWMATERIAL_PAGE, pageWanted);
                }
                break;
            }
            case STORE_PAGE:
            {
                if(itsPages_.containsKey(PageType.STORE_PAGE))
                {
                    pageWanted = itsPages_.get(PageType.STORE_PAGE);
                }
                else
                {
                    StoreManager tempRM = (StoreManager)itsSysManager_.getClient(SystemManager.clientType.STORE_MANAGER);
                    EmployeeManager tempEM = (EmployeeManager)itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                    if(tempRM == null || tempEM == null){break;}
                    pageWanted = new Ui_storeManagePage(this,tempRM,tempEM);
                    itsPages_.put(PageType.STORE_PAGE, pageWanted);
                }
                break;
            }
            case PRODUCT_PAGE:
            {
                if(itsPages_.containsKey(PageType.PRODUCT_PAGE))
                {
                    pageWanted = itsPages_.get(PageType.PRODUCT_PAGE);
                }
                else
                {
                    ProductManager tempPM = (ProductManager)itsSysManager_.getClient(SystemManager.clientType.PRODUCT_MANAGER);
                    if(tempPM == null){break;}
                    pageWanted = new Ui_ProductPage(this,tempPM);
                    itsPages_.put(PageType.PRODUCT_PAGE, pageWanted);
                }
                break;
            }
            case RECIPT_PAGE:
            {
                if(itsPages_.containsKey(PageType.RECIPT_PAGE))
                {
                    pageWanted = itsPages_.get(PageType.RECIPT_PAGE);
                }
                else
                {
                    ReciptManager tempRM = (ReciptManager)itsSysManager_.getClient(SystemManager.clientType.RECIPT_MANAGER);
                    ProductManager tempPM = (ProductManager)itsSysManager_.getClient(SystemManager.clientType.PRODUCT_MANAGER);
                    if(tempPM == null || tempRM == null){break;}
                    pageWanted = new Ui_reciptPage(this,tempRM,tempPM);
                    itsPages_.put(PageType.RECIPT_PAGE, pageWanted);
                }
                break;
            }
            default:
            {
                break;
            }
        }
        
        if(pageWanted != null)
        {
            curPage.close();
            pageWanted.go();
        }
    }

    @Override
    public void remoteDatabaseDies() 
    {
        if(itsPages_.containsKey(PageType.MAIN_PAGE))
        {
            Ui_mainMenuPage page = (Ui_mainMenuPage)itsPages_.get(PageType.MAIN_PAGE);
            page.toggleDbStatus(Boolean.FALSE, Boolean.FALSE);
        }
    }

    @Override
    public void LocalDatabaseDies() 
    {
        if(itsPages_.containsKey(PageType.MAIN_PAGE))
        {
            Ui_mainMenuPage page = (Ui_mainMenuPage)itsPages_.get(PageType.MAIN_PAGE);
            page.toggleDbStatus(Boolean.TRUE, Boolean.FALSE);
        }
    }

    @Override
    public void remoteDataBaesAlive() 
    {
        if(itsPages_.containsKey(PageType.MAIN_PAGE))
        {
            Ui_mainMenuPage page = (Ui_mainMenuPage)itsPages_.get(PageType.MAIN_PAGE);
            page.toggleDbStatus(Boolean.FALSE, Boolean.TRUE);
        }
    }

    @Override
    public void LocalDatabaseAlive() 
    {
        if(itsPages_.containsKey(PageType.MAIN_PAGE))
        {
            Ui_mainMenuPage page = (Ui_mainMenuPage)itsPages_.get(PageType.MAIN_PAGE);
            page.toggleDbStatus(Boolean.TRUE, Boolean.TRUE);
        }
    }

    @Override
    public void SetRx(DataBaseRx_dbStatus rx) 
    {
        if(itsPages_.containsKey(PageType.MAIN_PAGE))
        {
            Ui_mainMenuPage page = (Ui_mainMenuPage)itsPages_.get(PageType.MAIN_PAGE);
            page.toggleDbStatus(Boolean.TRUE, Boolean.FALSE);
        }
    }

    @Override
    public void showEmployeePicker(SqlDataTable table, EmployeePickerTx tx, Integer identity) 
    {
        EmployeePicker page = new EmployeePicker(table, tx, identity);
        page.setVisible(true);
    }

    @Override
    public void showWarehousePicker(SqlDataTable table, WarehousePickerTx tx) 
    {
        WarehouseListPage page = new WarehouseListPage(table, tx);
        page.setVisible(true);
    }

    @Override
    public void showRawMaterialPicker(SqlDataTable table, RawMaterialPickerTx tx) 
    {
        RawMaterialPicker page = new RawMaterialPicker(table, tx);
        page.setVisible(true);
    }
    
    @Override
    public void showProductPicker(SqlDataTable table, ProductPickerTx tx, Integer identity) 
    {
        ProductPicker page = new ProductPicker(table, tx,identity);
        page.setVisible(true);
    }

    @Override
    public void showDetailReciptPicker(SqlDataTable table, DetailReciptPickerTx tx, Integer identity) {
        DetailReciptPicker page = new DetailReciptPicker(table, tx, identity);
        page.setVisible(true);
    }

    @Override
    public void showrProductReciptPicker(SqlDataTable table, ProductReciptPickerTx tx, Integer identity) {
        ProductReciptPicker page = new ProductReciptPicker(table, tx, identity);
        page.setVisible(true);
    }
}
