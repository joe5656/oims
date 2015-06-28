/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims;
import com.google.common.collect.Maps;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import  oims.config.ConfigManager;
import  oims.dataBase.*;
import  oims.dataBase.MYSQL.*;
/**
 *
 * @author ezouyyi
 */
public class OIMS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AppBuilder builder = new AppBuilder();
        builder.run();
    }
    
}
