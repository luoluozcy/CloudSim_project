package org.cloudbus.cloudsim.core;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.traffic.*;
public class TrafficSimEvent extends SimEvent {
    private  float resource[][];
    private  Map<List<TrafficVm>,float[]> vmResRequired;
    private  Map<List<TrafficHost>,float[]> overloadHost;
    private   List<TrafficVm> vmList;
    private   List<TrafficHost> hostList;
    
    
    public TrafficSimEvent() {
    	super();
    	resource=null;
    	vmResRequired=null;
    	overloadHost=null;
    	vmList=null;
    	hostList=null;
	}

	// ------------------- PACKAGE LEVEL METHODS --------------------------
    TrafficSimEvent( float resource[][],Map<List<TrafficVm>,float[]> vmResRequired, 
    		Map<List<TrafficHost>,float[]> overloadHost,
    		List<TrafficVm> vmList,
    		 List<TrafficHost> hostList,
    		int evtype, double time, int src, int dest, int tag, Object edata) {
    	super(evtype,time, src,dest,  tag, edata);
		this.resource=resource;
		this.vmResRequired=vmResRequired;
		this.overloadHost=overloadHost;
		this.vmList=vmList;
		this.hostList=hostList;
    	
	}

    TrafficSimEvent( float resource[][],Map<List<TrafficVm>,float[]> vmResRequired, 
    		Map<List<TrafficHost>,float[]> overloadHost,
    		List<TrafficVm> vmList,
    		 List<TrafficHost> hostList,
    		 int evtype, double time, int src) {
    	super(evtype,time, src);
    	this.resource=resource;
		this.vmResRequired=vmResRequired;
		this.overloadHost=overloadHost;
		this.vmList=vmList;
		this.hostList=hostList;
		
    	
	}
	//TrafficSimEvent
    public void updateVmList(Map<List<TrafficVm>,float[]> vmRes)
    {   
    	
    	vmResRequired=vmRes;
    }
    
    
    public void updateHostList(Map<List<TrafficHost>,float[]> overloadhost){
    	
    	overloadHost=overloadhost;
    }
    
    public   List<TrafficVm> getTrafficVmList()
    {
    	return vmList;
    }
    public    List<TrafficHost> getTrafficHostList()
    {
    	return hostList;
    }
    
    public  Map<List<TrafficHost>,float[]> getoverloadHost()
    {
    	return overloadHost;
    }
    public  Map<List<TrafficVm>,float[]> getvmResRequired()
    {
    	return vmResRequired;
    }

}

