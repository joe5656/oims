/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.ticketSystem;

import oims.dataBase.tables.TicketTable;
import static oims.ticketSystem.Ticket.TicketType;
import oims.ticketSystem.Tickets.WareHouseCiTicket;
import oims.ticketSystem.Tickets.WareHouseCoTicket;

/**
 *
 * @author ezouyyi
 */
public class TicketFactory {
    TicketTable itsTable_;
    
    public TicketFactory(TicketTable table)
    {
        itsTable_ =  table;
    }
    
    public Ticket genTicket(TicketType tt)
    {
        switch(tt)
        {
            case WAREHOUSETICKET_CI:
            {
                return new WareHouseCiTicket(itsTable_);
            }
            case WAREHOUSETICKET_CO:
            {
                return new WareHouseCoTicket(itsTable_);
            }
        }
        return null;
    }
}
