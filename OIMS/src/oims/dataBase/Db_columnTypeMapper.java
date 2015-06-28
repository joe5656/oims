/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase;

import oims.support.util.Db_publicColumnAttribute;

/**
 *
 * @author ezouyyi
 */
public interface Db_columnTypeMapper {
    public String MapAttribute(Db_publicColumnAttribute.ATTRIBUTE_NAME name);
}
