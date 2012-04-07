package com.netcracker.tc.tests.examples.task;

import com.netcracker.sova.annotated.anns.Param;
import com.netcracker.sova.annotated.anns.Scenario;
import com.netcracker.sova.types.anns.Str;
import com.netcracker.sova.types.ref.Ref;


public class ReviewAddSOTask extends AbstractTask {
	
	@Param("Off-Net") YesNo offNet;
	
	@Param AccessType accessType;
	
	@Param String connectivityType;
	
	@Param MeshHubSpoken siteType = MeshHubSpoken.FULL_MESH;
	
	@Param MeshHubSpoken VPN_Type = MeshHubSpoken.FULL_MESH;
	
	@Param String targetVrf_Name;
	
	@Param String normalSpeed;
	
	@Param int customerSolution;
	
	@Param DesignerType designType = DesignerType.STANDARD;
	
	@Param Automation activationAutomation = Automation.AUTOMATIC;
	
	@Param Automation designAutomation = Automation.AUTOMATIC;
	
	@Param String streetNumber = "11";
	
	@Param("Street Name/PO Box") String streetName = "Mira str.";
	
	@Param("City/Municipality") String city = "Toronto";
	
	@Param("State/Province") String state = "ON";
	
	@Param("Country") String country = "Canada";
	
	@Param(value="LAN IP Address",description="Must be in format DDD.DDD.DDD.DDD") 
	@Str(pattern="\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")
	String ip;
	
	@Param RouteProtocol secondaryRoutingProtocol = RouteProtocol.BGP__;
	
	@Param RelativeDate estimatedDueDate = RelativeDate.FUTURE;
	
	@Param int AF1_percentage = 100;
	
	@Param int AF2_percentage = 0;
	
	@Param int AF3_percentage = 0;
	
	@Param EFRateUnit EF_rateUnit = EFRateUnit.KBPS;
	
	@Param int EF_rate = 0;
	
	@Ref("task:id") @Param String taskId_;
	
	@Override
	@Scenario("Process [Review Add SO] task")	
	public void execute() 
	{
	    System.out.println(offNet.toString());
	    System.out.println(accessType.toString());
	    System.out.println(connectivityType.toString());
	    System.out.println(siteType.toString());
	    System.out.println(VPN_Type.toString());
	    System.out.println(normalSpeed);
	    System.out.println(designType.toString());
	    System.out.println(activationAutomation.toString());
	    System.out.println(designAutomation.toString());
	    System.out.println(streetNumber);
	    System.out.println(streetName);
	    System.out.println(city);
	    System.out.println(state);
	    System.out.println(country);
	    System.out.println(secondaryRoutingProtocol.toString());
	    System.out.println(AF1_percentage);
	    System.out.println(AF2_percentage);
	    System.out.println(AF3_percentage);
	    System.out.println(EF_rateUnit.toString());
	    System.out.println(Integer.toString(EF_rate));
	    
	    executeAction();
	}
}
