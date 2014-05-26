/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2010, The University of Melbourne, Australia
 */
/*
 * CloudCoordinator: 该抽象类用于扩展联合的基于云的数据中心。它负责周期性地监测数据中心资源的内部状态和动态load-shredding决策也在基于该类的基础上。该组件的具体实现包括了专门的传感器和load-shredding期间应该遵循的策略。监测数据中心资源是由updateDatacenter()方法通过发送需查询的传感器来执行的。服务/资源发现是由setDatacenter()
抽象方法实现的，它可扩展实现自定义协议和机制（多广播，广播，p2p）。此外，该组件也可以扩展基于云的服务的仿真，如AmazonEC2 Load-Balancer。开发者希望通过多个云实现部署他们的应用服务，可通过扩展该类来实现他们自定义内部云供应策略。

 */
package org.cloudbus.cloudsim;

import java.util.List;

/**
 * This class represents the coordinator of a federation of clouds.
 * It interacts with other clouds coordinators in order to exchange
 * virtual machines and user applicatoins, if required.
 *
 * @author		Rodrigo N. Calheiros
 * @since		CloudSim Toolkit 1.0
 */
public abstract class CloudCoordinator {

	/** The datacenter. */
	protected FederatedDatacenter datacenter;

	/** The federation. */
	protected List<Integer> federation;

	/**
	 * Defines the FederatedDataCenter this coordinator works for.
	 *
	 * @param datacenter FederatedDataCenter associated to this coordinator.
	 *
	 * @pre $none
	 * @post $none
	 */
	public void setDatacenter(FederatedDatacenter datacenter){
		this.datacenter = datacenter;
	}

	/**
	 * Informs about the other data centers that are part of the federation.
	 *
	 * @param federationList List of DataCenters ids that are part of the federation
	 *
	 * @pre federationList != null
	 * @post $none
	 */
	public void setFederation(List<Integer> federationList) {
		this.federation = federationList;
	}

	/**
	 * This method is periodically called by the FederatedDataCenter to
	 * makethe coordinator update the sensors measuring in order to decide
	 * if modification in the data center must be processed. This modification
	 * requires migration of virtual machines and/or user applications from
	 * one data center to another.
	 *
	 * @pre $none
	 * @post $none
	 */
	public void updateDatacenter() {
		for (Sensor<Double> s : this.datacenter.getSensors()) {
			int result = s.monitor();
			if (result != 0) {
				migrate(s, result);
			}
		}
	}

	/**
	 * Implements a specific migration policy to be deployed by the cloud coordinator.
	 *
	 * @param result the result vof the last measurement:
	 * -1 if the measurement fell below the lower threshold
	 * +1 if the measurement fell above the higher threshold
	 * @param sensor the sensor
	 *
	 * @pre sensor != null
	 * @post $none
	 */
	protected abstract void migrate(Sensor<Double> sensor,int result);

}