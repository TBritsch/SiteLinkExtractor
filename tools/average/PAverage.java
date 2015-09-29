package com.tb.ba.analysis.average;

import java.io.IOException;



public class PAverage {
	static String file1, page;

	public static void main(String[] args) {
		if(args.length == 0){
			printHelp();
		}

		for (int i = 0; i < args.length; i++) {
			if(args[i].equals("-h")){
				printHelp();
			}
			
		}
		int lines = Integer.MAX_VALUE;
		
		file1 = args[0];
		if(args.length > 1){
			lines = Integer.parseInt(args[1]);
		}
		
		try {
			double avg =  FileSearch.average(file1, lines);
			System.out.println("AVG: " + avg);
			System.out.println("STD: " + FileSearch.variance(file1, lines, avg));
		} catch (IOException e) {
			System.out.println("Error");
		}

	}

	private static void printHelp(){
		System.out.println("################################");
		System.out.println("################################");
		System.out.println("Calculates the Average of the pageRank");
		System.out.println("################################");
		System.out.println("################################");
		System.out.println("How to use:");
		System.out.println("");
		System.out.println("PAverage path/to/file");
		System.out.println("################################");
		System.out.println("################################");


		System.exit(0);
	}

}
