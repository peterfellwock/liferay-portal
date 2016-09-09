package com.liferay.portal.util;

import java.util.HashMap;

public class IdExCatch {
	
	private HashMap<String, ExCatch> catches;
	private long highest = 0;
	
	
	public IdExCatch(ExCatch ex) {
		super();
		catches = new HashMap<String, ExCatch>();
		catches.put(ex.getStringMVCC(), ex);
	}
	
	public void put(ExCatch ex){

		if(ex.getMVCC() >= highest){
			catches.put(ex.getStringMVCC(), ex);
			highest = ex.getMVCC();
		}else{
			System.out.println("-------------------------->SOMEONE IS TRYING TO ADD MVCC STALE!!!!!");
			System.out.println("-------------------------->SOMEONE IS TRYING TO ADD MVCC STALE!!!!!");
			System.out.println("Highest mvvcc for ID:" + ex.getKey() + " is:" + highest);
			
			
			System.out.println("-------------------Printing all ExCatches from collision");
			for(long x = ex.getMVCC() ; x <= highest ; x++){
				
				ExCatch temp = catches.get(x+ "");
				if(temp == null){
					System.out.println("->Could not find ExCatch with mvcc" + x);
				}else{
					temp.print();
				}
			}
			System.out.println("+ colliding ExCatch");
			ex.print();
			System.out.println("-------------------------------------------------------------------------");
			System.out.println("-------------------Printing all ExCatches from collision with Exceptions");
			for(long x = ex.getMVCC() ; x <= highest ; x++){
				
				ExCatch temp = catches.get(x+ "");
				if(temp == null){
					System.out.println("->Could not find ExCatch with mvcc" + x);
				}else{
					temp.print();
					temp.printEx();
				}
				System.out.println("-----------!!!!!!!!!!!!!!!!!!!!!!NEXT");
			}
			System.out.println("+ colliding ExCatch with Exception");
			ex.print();
			ex.printEx();
			System.out.println("-------------------------------------------------------------------------");

		}
	}
	
	public long getHighest(){
		return highest;
	}
	
	
	public static void main(String[] args){

		ExCatch catch53 = new ExCatch(new Exception(), "111", 53, "number53", 666);
		ExCatch catch54 = new ExCatch(new Exception(), "111", 54, "number54", 666);
		ExCatch catch55 = new ExCatch(new Exception(), "111", 55, "number55", 666);
		ExCatch catch56 = new ExCatch(new Exception(), "111", 56, "number56", 666);
		ExCatch catch57 = new ExCatch(new Exception(), "111", 57, "number57", 666);
		ExCatch catch58 = new ExCatch(new Exception(), "111", 58, "number58", 666);
		ExCatch catch552 = new ExCatch(new Exception(), "111", 55, "number552", 666);
		
		ExCatchUtil.add(catch53);
		ExCatchUtil.add(catch54);
		ExCatchUtil.add(catch55);
		ExCatchUtil.add(catch56);
		ExCatchUtil.add(catch57);
		ExCatchUtil.add(catch58);
		ExCatchUtil.add(catch552);
		
	}

}
