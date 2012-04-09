package com.netcracker.tc.tests.examples.task;

import com.netcracker.sova.annotated.anns.Param;
import com.netcracker.sova.annotated.anns.Scenario;
import com.netcracker.sova.types.pub.Enums;

public class FillFacilityInfoTask extends AbstractTask 
{
	@Param("Utilize QinQ") YesNo utilizeQInQ;
	
	@Param("Use QinQ") YesNo useQInQ;
	
	@Param int accessFacilityNumber;
	
	@Param int accessCircuitNumber;
	
	@Param int accessCircuitOrderNumber;
	
	@Param("1B Order Number") String oneB;
	
	@Param("1B/1BL/1FL Number") String oneBL;
	
	@Param int busLineNumber;
	
	@Param RemedyContactRole remedyContactRole;
	
	@Param SupportGroup supportGroup;

	@Override
	@Scenario("Process [Fill facility] task")
	public void execute() 
	{
	    System.out.println(Enums.toString(utilizeQInQ));
	    System.out.println(accessFacilityNumber);
	    System.out.println(accessCircuitNumber);
	    System.out.println(accessCircuitOrderNumber);
	    System.out.println(oneB);
	    System.out.println(oneBL);
	    System.out.println(busLineNumber);
	    System.out.println(remedyContactRole);
	    System.out.println(supportGroup);		
	    System.out.println(Enums.toString(useQInQ));
	    
	    executeAction();
	}

}
