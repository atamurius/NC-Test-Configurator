package com.netcracker.tc.tests.examples.order;

import java.util.Random;

import com.netcracker.sova.annotated.anns.Output;
import com.netcracker.sova.annotated.anns.Param;
import com.netcracker.sova.annotated.anns.Params;
import com.netcracker.sova.annotated.anns.Scenario;
import com.netcracker.sova.annotated.anns.Scenarios;
import com.netcracker.sova.types.anns.Label;
import com.netcracker.sova.types.ref.Ref;
import com.netcracker.tc.tests.examples.search.SearchCondition;

@Scenarios("ServiceOrder")
public class ServiceOrderScenario
{
    public enum OrderType
    {
        ADD_ORDER, DELETE_ORDER, CHANGE_ORDER, @Label("In-Flight Cancel")
        CANCEL
    }

    public enum TaskStatus
    {
        READY, ACTIVE, COMPLETED, SKIPPED
    }

    public static class ServiceOrderInfo
    {
        @Param OrderType orderType = OrderType.ADD_ORDER;
        
        @Param int esd_productId_ = 13084;
        
        @Param String customerServiceName = "TEST_SERVICE";
        
        @Param String esd_customerLocationId_ = "0003";
        
        @Param String esd_customerId_ = "10003";
        
        @Param String customerShortName = "TSTCST";
        
        @Param String customerName = "TEST Customer";
        
        @Param String productName = "Product Name Test";
    }

    @Scenario
    @Output("SO Number")
    public long create(@Params ServiceOrderInfo serviceOrderInfo)
    {
        System.out.println(serviceOrderInfo.orderType);
        System.out.println(serviceOrderInfo.esd_productId_);
        System.out.println(serviceOrderInfo.customerServiceName);
        System.out.println(serviceOrderInfo.esd_customerLocationId_);
        System.out.println(serviceOrderInfo.esd_customerId_);
        System.out.println(serviceOrderInfo.customerShortName);
        System.out.println(serviceOrderInfo.customerName);
        System.out.println(serviceOrderInfo.productName);
        
        return new Random().nextLong();
    }

    @Scenario
    @Output(value="Task ID", type="task:id")
    public String findTask(
            @Params SearchCondition searchCondition,
            @Ref @Param("SO Number") long soNumber)
    {
        // do all staff
        return "" + Math.random();
    }

}
