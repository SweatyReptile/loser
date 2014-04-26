package com.sweatyreptile.losergame.sensors;

public interface CountingSensorListener {

	public void contactAdded(int totalContacts);
	public void contactRemoved(int totalContacts);
	
}
