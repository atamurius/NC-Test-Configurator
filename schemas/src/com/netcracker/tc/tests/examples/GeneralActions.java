package com.netcracker.tc.tests.examples;

import com.netcracker.sova.annotated.anns.Scenario;
import com.netcracker.sova.annotated.anns.Scenarios;

@Scenarios("General Actions")
public class GeneralActions 
{
	@Scenario("Login")
	public void login()
	{
		// do login		
	}
	
	@Scenario("Close Report") //this is Stub and should be removed in future
	public void closeReport()
	{
		// close report
	}
	
	@Scenario("Open Report") //this is Stub and should be removed in future
	public void openReport()
	{
		// open report
	}		
}
