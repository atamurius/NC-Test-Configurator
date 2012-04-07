package com.netcracker.tc.tests.examples.search;

import com.netcracker.sova.annotated.anns.Param;
import com.netcracker.tc.tests.examples.order.ServiceOrderScenario.TaskStatus;

public class SearchCondition 
{
	public enum Condition { EQUALS, NOT_EQUALS, CONTAINS, STARTS_WITH, ENDS_WITH };
	
	@Param("Delay for Attamps, sec") 
	public int delay = 5;
	
	@Param("Timeout, sec") 
	public int timeout = 120;
	
	public TaskStatus taskStatus;
}
