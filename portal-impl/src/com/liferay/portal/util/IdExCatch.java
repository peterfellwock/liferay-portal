package com.liferay.portal.util;

import java.util.ArrayList;
import java.util.List;
public class IdExCatch {

	private List<ExCatch> catches;
	private long highest = 0;

	public IdExCatch(ExCatch ex) {
		
		catches = new ArrayList<ExCatch>();
		catches.add(ex);
		
		highest = ex.getMVCC();
	}

	public void put(ExCatch ex) {

		
		/** if mvcc is same or higher save stack and move on **/
		if (ex.getMVCC() >= highest) {
			catches.add(ex);
			highest = ex.getMVCC();
		}else {
			
			/** mvcc is lower than highest, prossible stale **/
			
			/** header **/
			printCollisionNotification(ex);

			/** print summary **/
			printSummary(ex);

			/** print exceptions **/
			printExceptions(ex);
		}
	}

	private void printSummary(ExCatch challenger) {

		System.out.println(
				"---------------------------SUMMARY-----------------------------");
		
		/** controls **/
		long endMvccId = this.highest;
		/** loop backwards till incoming -1**/
		long stopMvccId = challenger.getMVCC() -1;
		boolean keepPrinting = true;
		int position = catches.size() -1;
		
		while(keepPrinting) {
			ExCatch temp = catches.get(position);
			
			if(stopMvccId > temp.getMVCC()){
				keepPrinting = false;
			}else{
				temp.print();
			}
			position--;

		}
		System.out.println("Challenger::");
		challenger.print();
		System.out.println(
				"------------------------END OF SUMMARY--------------------------");
		
	}
	
	private void printExceptions(ExCatch challenger) {

		System.out.println(
			"---------------------SUMMARY of EXCEPTIONS-----------------------");
		
		/** controls **/
		long endMvccId = this.highest;
		/** loop backwards till incoming -1**/
		long stopMvccId = challenger.getMVCC() -1;
		boolean keepPrinting = true;
		int position = catches.size() -1;
		
		while(keepPrinting) {
			ExCatch temp = catches.get(position);
			
			if(stopMvccId > temp.getMVCC()){
				keepPrinting = false;
			}else{
				temp.print();
				temp.printEx();
				System.out.println("---------------------------------NEXT!");
			}
			position--;


		}
		System.out.println("Challenger::");
		challenger.print();
		System.out.println(
			"-------------------END SUMMARY of EXCEPTIONS---------------------");
		
	}

	public long getHighest() {
		return highest;
	}
	
	public void printCollisionNotification(ExCatch challenger){
		System.out.println(
				"\n\n--------------------------------------------------------------------");
			System.out.println(
				"-------Possible collision-----------------------------------------------");
			System.out.println(
				"Highest mvvcc for ID:" + challenger.getKey() + " is:" + highest);

			System.out.println(
				"-------------------Printing all ExCatches from collision");
	}
	

	public static void main(String[] args) {

		ExCatch catch52 = new ExCatch(
				new Exception(), "111", 53, "number53", 666);
		ExCatch catch53 = new ExCatch(
			new Exception(), "111", 53, "number53", 666);
		ExCatch catch54 = new ExCatch(
			new Exception(), "111", 54, "number54", 666);
		ExCatch catch55 = new ExCatch(
			new Exception(), "111", 55, "number55", 666);
		ExCatch catch56 = new ExCatch(
			new Exception(), "111", 56, "number56", 666);
		ExCatch catch566 = new ExCatch(
				new Exception(), "111", 56, "Anothernumber56", 666);
		ExCatch catch5666 = new ExCatch(
				new Exception(), "111", 56, "Anothernumber566", 666);
		ExCatch catch57 = new ExCatch(
			new Exception(), "111", 57, "number57", 666);
		ExCatch catch58 = new ExCatch(
			new Exception(), "111", 58, "number58", 666);
		ExCatch catch552 = new ExCatch(
			new Exception(), "111", 55, "number552", 666);

		ExCatchUtil.add(catch52);
		ExCatchUtil.add(catch53);
		ExCatchUtil.add(catch54);
		ExCatchUtil.add(catch55);
		ExCatchUtil.add(catch56);
		ExCatchUtil.add(catch566);
		ExCatchUtil.add(catch5666);
		ExCatchUtil.add(catch57);
		ExCatchUtil.add(catch58);
		ExCatchUtil.add(catch552);
	}

}