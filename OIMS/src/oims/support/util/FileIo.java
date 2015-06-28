/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
/**
 *
 * @author ezouyyi
 */
public class FileIo {
    public static Boolean exportXmlFile(String dir, String fileName, Document doc)
    {
        File dstFile = new File(dir);
        if(!dir.endsWith("\\"))
        {
            dir = dir.concat("\\");
        }
        try
        {
            if(!dstFile.exists() && !dstFile.isDirectory())
            {
                dstFile.mkdirs();
            }
            
            File fullPath = new File(dir.concat(fileName));
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fullPath);
            transformer.transform(source, result);
        }catch(Exception e)
        {
            System.out.println(e);
        }
        return true;
    }
    
    public static Boolean exportFile(String dir, String fileName, byte[] fileContext)
    {
        File dstFile = new File(dir);
        if(!dir.endsWith("\\"))
        {
            dir.concat("\\");
        }
        try
        {
            if(!dstFile.exists() && !dstFile.isDirectory())
            {
                dstFile.mkdir();
            }
            
            File fullPath = new File(dir.concat(fileName));
            
            FileOutputStream output = new FileOutputStream(fullPath);
            output.write(fileContext);
            output.close();
            
        }catch(Exception e)
        {
            return false;
        }
        return true;
    }
}
