/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.config;
import oims.support.util.FileIo;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException; 
/**
 *
 * @author ezouyyi
 */
public class ConfigManager {
    final String configFullPaht_ = "c:\\OIMS\\config";
           
    public Boolean loadConfig(){return false;};
    public Boolean systemConfiged(){return false;};
    public void    CreateNewConfigFile(File configFile)
    {
        if(configFile == null)
        {
            // trigger manual configure mode
        }
        else
        {
            // trigger auto configure mode
        }
    }
    
    
}
