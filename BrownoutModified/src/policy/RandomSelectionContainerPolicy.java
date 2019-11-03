package policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import brownout.BrownoutMain;
import model.CompareByContainerUtilization;
import model.Container;
import model.WorkerNode;


/**
 * The Random Selection Container (RSC) policy takes advantage
 * of a random selection of a number of optional container to 
 * reduce energy consumption. 
 * @author minxianx
 *
 */
public class RandomSelectionContainerPolicy implements AbstractPolicyInterface{

	
	double dimmerValue;
	ArrayList<WorkerNode> wnl;
	HashSet<String> connectionSet = new HashSet<String>();
	ArrayList<ArrayList<Container>> subSetOfContainerList;
	Random random;
	int index;
	
	public RandomSelectionContainerPolicy(double p_dimmerValue, ArrayList<WorkerNode> p_wnl){
		this.dimmerValue = p_dimmerValue;
		this.wnl = p_wnl;
	}
	

	public ArrayList<Container> getDeactivatedContainerList() {
		// TODO Auto-generated method stub
		ArrayList<Container> deactivatedContainerList = new ArrayList<Container>();
		for(WorkerNode wn: wnl){
			Collections.sort(wn.getContainersList(), new CompareByContainerUtilization());
			
			if(wn.getCpuUtil() >= BrownoutMain.OVERLOAD_THRESHOLD){
				if(wn.getContainersList().get(0).getCpuUtil() >= dimmerValue && wn.getContainersList().size()>0) {
					deactivatedContainerList.add(wn.getContainersList().get(0));
					connectionSet.add(wn.getContainersList().get(0).getConnection());
				}
				

				
				subSetOfContainerList = getSetOfContainerList(wn.getContainersList());
				
				random = new Random();
			    index = random.nextInt() % subSetOfContainerList.size() + 1;
				ArrayList<Container> con = subSetOfContainerList.get(index);
			    if(getUtilizationOfDeactivatedContainerList(con) < dimmerValue){
			    	deactivatedContainerList = con;
			    }
			    
				
				for(Container container: deactivatedContainerList) {
					connectionSet.add(container.getConnection());
				}
				
				
				//find other connected containers
				for(Container container: wn.getContainersList()) {
					if(connectionSet.contains(container.getConnection())) {
						deactivatedContainerList.add(container);
					}
				}
			}
		}
		return deactivatedContainerList;
	}
	
	/**
	 * Get the utilization of deactivate containers
	 * @param deactivatedContainerList
	 * @return
	 */
	public double getUtilizationOfDeactivatedContainerList(ArrayList<Container> deactivatedContainerList) {
		double sumUtilization =0.0f;
		for(Container container : deactivatedContainerList) {
			sumUtilization += container.getCpuUtil();
			
		}
		
		return sumUtilization;
	}
	
	
	
	/**
	 * Find all the subsets of container list
	 * @param cl
	 * @return
	 */
	ArrayList<ArrayList<Container>> getSetOfContainerList(ArrayList<Container> cl){
		subSetOfContainerList = new ArrayList<ArrayList<Container>>(); 
		ArrayList<Container> tempConlist = new ArrayList<Container>();
		if(cl.size() == 0) {
			subSetOfContainerList.add(null);
			return subSetOfContainerList;
		}
		
		ArrayList<ArrayList<Container>> sublist = getSetOfContainerList((ArrayList)cl.subList(1, cl.size()-1));
				
		for(ArrayList<Container> tempConlist1: sublist) {
			if(tempConlist1.size() == 0) {
				subSetOfContainerList.add((ArrayList)cl.subList(0, 1));
			}else {
				tempConlist1.add(cl.get(0));
				subSetOfContainerList.add(tempConlist1);
			}
		}
		subSetOfContainerList.addAll(sublist);
		
		
		
		return subSetOfContainerList;
	}

}
