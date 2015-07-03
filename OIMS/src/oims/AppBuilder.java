/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims;

import java.util.logging.LogManager;
import oims.UI.UiManager;
import oims.dataBase.DataBaseManager;
import oims.employeeManager.EmployeeManager;
import oims.loggingManagement.LoggingManager;
import oims.rawMaterialManagement.RawMaterialManager;
import oims.stackManagement.StackManager;
import oims.systemManagement.SystemManager;
import oims.ticketSystem.TicketManager;
import oims.warehouseManagemnet.WareHouseManager;

/**
 *
 * @author freda
 */
public class AppBuilder {
    SystemManager   itsSystemManager_;
    DataBaseManager itsDataBaseManager_;
    WareHouseManager itsWareHouseManager_;
    TicketManager    itsTicketManager_;
    UiManager        itsUiManager_;
    LoggingManager   itsLogManager_;
    EmployeeManager  itsEmployeeManger_;
    StackManager     itsStackManager_;
    RawMaterialManager itsRawMManager_;
    
    public AppBuilder()
    {
        itsSystemManager_ = new SystemManager();
        itsDataBaseManager_ = new DataBaseManager();
        itsUiManager_ = new UiManager(itsDataBaseManager_);
        itsUiManager_.showStartingPage();
        itsEmployeeManger_ = new EmployeeManager(itsDataBaseManager_);
        itsStackManager_   = new StackManager();
        itsWareHouseManager_ = new WareHouseManager(itsDataBaseManager_,itsStackManager_);
        itsRawMManager_      = new RawMaterialManager(itsDataBaseManager_);
        //itsTicketManager_ = new TicketManager(itsSystemManager_, itsDataBaseManager_);
        //itsWareHouseManager_ = new WareHouseManager(itsSystemManager_, itsDataBaseManager_);
        //itsLogManager_ = new LoggingManager(itsDataBaseManager_);
        itsSystemManager_.registerClient(SystemManager.clientType.DATABASE_MANAGER,itsDataBaseManager_);
        itsSystemManager_.registerClient(SystemManager.clientType.UI_MANAGER, itsUiManager_);
        itsSystemManager_.registerClient(SystemManager.clientType.EMPLOYEE_MANAGER, itsEmployeeManger_);
        //itsSystemManager_.registerClient(SystemManager.clientType.TICKET_MANAGER, itsTicketManager_);
        itsSystemManager_.registerClient(SystemManager.clientType.WAREHOUSE_MANAGER,itsWareHouseManager_);
        itsSystemManager_.registerClient(SystemManager.clientType.WAREHOUSE_MANAGER,itsRawMManager_);
    }
    public void run()
    {System.out.print("sys started");
        itsSystemManager_.run();
        System.out.print("system reached the last line");
        //System.exit(0);
    };
}
