/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.MYSQL;
import oims.dataBase.Db_columnTypeMapper;
import oims.support.util.Db_publicColumnAttribute;

/**
 *
 * @author ezouyyi
 */
public class Db_columnTypeMapper_MYSQL implements Db_columnTypeMapper{
    
    String attributeMap_[];
    
    @Override
    public String MapAttribute(Db_publicColumnAttribute.ATTRIBUTE_NAME name)
    {
        return attributeMap_[name.ordinal()];
    };
    
    Db_columnTypeMapper_MYSQL()
    {
        attributeMap_ = new String[Db_publicColumnAttribute.ATTRIBUTE_NAME.NUMBER_OF_ATTRIBUTE_NAME.ordinal()];
        
        // should always include a SPACE at the end of the rh string
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT.ordinal()] = "BIT "; 
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.SMALL_INTEGER.ordinal()] = "SMALLINT ZEROFILL ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER.ordinal()] = "INTEGER ZEROFILL ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.DOUBLE.ordinal()] = "DOUBLE ZEROFILL ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT.ordinal()] = "FLOAT ZEROFILL ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.DATE.ordinal()] = "DATE ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.TIME.ordinal()] = "TIME ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.TIMESTAMP.ordinal()] = "TIMESTAMP ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.DATATIME.ordinal()] = "DATATIME ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.YEAR.ordinal()] = "YEAR ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR10.ordinal()] = "VARCHAR(10) ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60.ordinal()] = "VARCHAR(60) ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.TEXT.ordinal()] = "TEXT ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.LONG_TEXT.ordinal()] = "LONG_TEXT ";
        attributeMap_[Db_publicColumnAttribute.ATTRIBUTE_NAME.ENUM.ordinal()] = "ENUM ";
    }
}
