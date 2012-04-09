package com.netcracker.tc.tests.examples.task;

import com.netcracker.sova.annotated.anns.Param;
import com.netcracker.sova.annotated.anns.Scenarios;
import com.netcracker.sova.types.pub.Label;

@Scenarios("Task")
public abstract class AbstractTask 
{
	public enum TaskAction { REPORT_JEOPARDY, COMPLETE, SUSPEND }
	
	public enum YesNo { YES, NO }
	
	public enum DesignerType { CUSTOM, REPEATABLE, STANDARD, TWEAKED }
	
	public enum Automation { AUTOMATIC, MANUAL }
	
	public enum MeshHubSpoken { FULL_MESH, HUB, SPOKEN }
	
	public enum RouteProtocol { BGP__, OSPF__, RIP__, STATIC, NONE }
	
	public enum RelativeDate { PAST, CURRENT, FUTURE }
	
	public enum EFRateUnit { KBPS, MBPS, GBPS }
	
	public enum AccessType {
		@Label("ADSL Off-Net") ADSL_OFF_NET, 
		@Label("ADSL Off-Net (M10i)") ADSL_OFF_NET_M10I, 
		@Label("ADSL On-Net") ADSL_ON_NET, 
		@Label("ADSL On-Net (M10i)") ADSL_ON_NET_M10I, 
		@Label("ADSL2+ On-Net (M10i)") ADSL2_PLUS_ON_NET_M10I;
	}
	
	public enum ConnectivityType {
		@Label("ATM On-Net") ATM_ON_NET, RE__SINGLE }
	
	public enum RemedyContactRole { ATM__AND_EDGE__MONITORING, BC__ADSL__OPERATIONS, BC__CORE }
	
	public enum SupportGroup { AB__46020_ALARMS, AB__INTERNETWORKING, AB__OPERATIONS, AB__OPERATIONS_CORE }
	
	@Param TaskAction taskAction;
	
	public abstract void execute();
	
	protected void executeAction() 
	{
		// execute action
		switch (taskAction) {
		case REPORT_JEOPARDY: 
			System.out.println(TaskAction.REPORT_JEOPARDY);
			break;
		case COMPLETE: 
            System.out.println(TaskAction.COMPLETE);
			break;
		case SUSPEND:
            System.out.println(TaskAction.SUSPEND);
			break;
		default: 
		    throw new IllegalArgumentException("Task Action "+taskAction+" is not appropriate");
		}		
	}
}
