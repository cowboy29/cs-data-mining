/**
 * 
 */
package com.data.mining;
import java.util.*;

/**
 * @author Camaria
 *
 */
public class KNearestNeighbor {
	private int K;
	private ArrayList<Map.Entry<Integer, Double>> distance_list;
	private Queue<Integer> neighbor_list = new LinkedList<Integer>();
	private Instance new_instance;
	private ArrayList<Instance> training_list;
	
	public KNearestNeighbor(int k, ArrayList<Instance> t){
		K = k;
		training_list = t;
	}
	
	public int getK(){
		return K;
	}
	
	public void setK(int k){
		K = k;
	}
	/**
	 * calculate Minkowski distance norm=1 of a new instance to training instance 
	 */
	public ArrayList<Instance> getTraining(){
		return training_list;
	}
	
	public Instance getnew(){
		return new_instance;
	}
	
	public void setNewInstance(Instance n){
		new_instance = n;
	}
	public void calculateDistance(){
		
		Hashtable<Integer,Double> distance = new Hashtable<Integer,Double>();
		double temp_d = 0.0;
		
		for (Instance i : training_list){
			//compare individual feature
			temp_d = 0.0;
			for(int x=0; x < i.getFeature().size(); x++){
				
				//distance = 0
				temp_d += Math.pow(new_instance.getFeature().get(x).getValue() - i.getFeature().get(x).getValue(),2);
					
			}
			
			temp_d = Math.sqrt(temp_d); //for 1 instance
			distance.put(i.getID(),temp_d);
		}
		sortDistance(distance);
				
	}
	
	/**
	 * sort distance between new instance and training set
	 * @param distance
	 */
	private void sortDistance(Hashtable<Integer,Double> distance){
		ArrayList<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer,Double>>(distance.entrySet());
		
		//System.out.println(list);
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>(){
			public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2){
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		
		distance_list = list;
	}
	/**
	 * Find neighbor of new instance
	 */
	public void getKNeighbors(){
		int id = 0;
		int c = 0;
		for (int i=0; i < K; i++){
			//System.out.println(distance_list.get(i));
			id = distance_list.get(i).getKey();
			//System.out.println(id);
				
			for (Instance is : training_list){
				if(is.getID() == id){
					//System.out.println("c is " +is.getCategory());
					c = is.getCategory();
					//System.out.println(c);
					neighbor_list.offer(c);
					
					break;
				}
			}
			
		}
		
	}
	
	/**
	 * Assign class to the new instance
	 */
	public void assignCategory(){
		//keep a list of class candidate in case of tie
		//compare by higher vote
		ArrayList<Integer> cat_majorities = new ArrayList<Integer>();
		
		int majority = 0;
		int temp_majority = 0;
		int cat_majority = 0; //final class
		int cat_temp; //temporary class
		int i = neighbor_list.size();
		int y = 0;		
		while(!neighbor_list.isEmpty()){
			System.out.println(neighbor_list.toString());
			cat_temp = neighbor_list.poll();
			temp_majority = 1;
			//start round
			i = neighbor_list.size();
			while(y < i){ //while end of the list not reached
				int curr_cat = neighbor_list.poll();
				
				if(cat_temp == curr_cat){
					temp_majority += 1;
				}
				else{
					neighbor_list.offer(curr_cat);
				}
				i = neighbor_list.size();
				
				y++;
			}
			if(majority < temp_majority){
				cat_majority = cat_temp;
				//cat_majorities.add(cat_temp);
			}
			
		}
		//compare vote of each class before assignment
		if(cat_majorities.size() > 1){
			//TIES
			//more than 1 possible class
			//assign by percentage of vote
		}
		else{
			//No TIES
			//new_instance.setAssignedCategory(cat_majority);
		}
		
		new_instance.setAssignedCategory(cat_majority);
	}
	
	/**
	 * Assign class to training data set
	 */
	public void trainDataset(){
		for(Instance i : training_list){
			i.setAssignedCategory(i.getCategory());
			System.out.println("i " + i.getAssignedCategory());
		}
	}
	
	public void runKNN(){
		trainDataset();
		System.out.println("next");
		calculateDistance();
		System.out.println("next");
		getKNeighbors();
		System.out.println("next");
		assignCategory();
		System.out.println("done");
		
	}

}
