package com.mydevelopedworld.contention;

import static java.lang.System.out;

import com.mydevelopedworld.contention.SummingThread.Modality;

/**
 * 
 * @author while mydevelopedworld.wordpress.com
 * 
 * Main class which tests that in Multithreading, when it is possible,
 * it's better to avoid Thread Contentions.
 * 
 * The test nees three args:
 * 1)Number of Thread
 * 2)Modality: AVOID CONTENTION or CONTENTION
 * 3)Number of loops
 * 
 * The test will run N Threads in the specified Modality and will perform
 * M loops of increment operation.
 * 
 * In the end the Test will print the result and the time it took to calculate it.
 *
 * Graphs of performance results can be found at: 
 * mydevelopedworld.wordpress.com
 *
 */
public class TestContention {

	private static void printUsage(){
		out.println("Please pass me three args: ");
		out.println("\tFirst arg: Number of Threads");
		out.println("\t\tPass a number > 0");
		out.println("\tSecond arg: Modality:");
		out.println("\t\tPass 0 to avoid contention");
		out.println("\t\tPass 1 to try with contention");
		out.println("\tThird arg: Number of Loops:");
		out.println("\t\tPass a number > 0");
		System.exit(-1);
	}

	public static void main(String... args){
		if(args.length != 3){
			TestContention.printUsage();
		}

		int nThread = 0; // Number of Threads that will be created
		int modality = 0; // Modality: 0 = AVOID_CONTENTION; 1 = CONTENTION
		int loops = 0; // How many increment operations every Thread will perform

		/* Checking the input args... */
		try{
			nThread = Integer.parseInt(args[0]);
			modality = Integer.parseInt(args[1]);		
			loops = Integer.parseInt(args[2]);
		}catch(NumberFormatException e){
			TestContention.printUsage();
		}
		if(nThread < 1 || modality < 0 || modality > 1 || loops < 1){
			TestContention.printUsage();
		}
		
		/* Object used to collect the final result of the Threads increment operations */
		SumObject so = new SumObject(nThread);
		/* Thread array, used to perform the Join() on all of them */
		Thread[] threads = new Thread[nThread];

		final long startTime = System.currentTimeMillis();
		if(modality == 0){ /* Avoid Contention */
			out.println(nThread+" Threads in modality AVOID CONTENTION will loop for "+loops+" times...");
			for(int i = 0; i < nThread; i++){
				threads[i] = new Thread(new SummingThread(i, Modality.AVOID_CONTENTION, loops, so));
				threads[i].start();
			}
		}else{ /* Contention */
			out.println(nThread+" Threads in modality CONTENTION will loop for "+loops+" times...");
			for(int i = 0; i < nThread; i++){
				threads[i] = new Thread(new SummingThread(i, Modality.CONTENTION, loops, so));
				threads[i].start();
			}
		}

		/* 
		 * I'll wait for all Threads.
		 * The join() creates a happens-before barrier
		 */
		for(int i = 0; i < nThread; i++){
			try {				
				threads[i].join(); 
			} catch (InterruptedException e) {
				e.printStackTrace(); 
			}
		}

		out.println("Done!");
		if(modality == 0){ /* Avoid Contention */
			int value = 0;
			for(int i = 0; i < so.partialValues.length; i++){
				/* 
				 * When a thread terminates and causes a Thread.join in another thread to return, 
				 * then all the statements executed by the terminated thread have a happens-before relationship 
				 * with all the statements following the successful join. 
				 * The effects of the code in the thread are now visible to the thread that performed the join.
				 */
				value += so.partialValues[i]; // This is safe!
			}
			out.println("\tResult: "+value);
		}else{ /* Contention */
			out.println("\tResult: "+so.value);
		}
		out.println("\tExpected : "+(nThread*loops));
		
		final long endTime = System.currentTimeMillis();
		out.println();
		out.println("Total execution time: " + ((endTime - startTime)/1000.0) + "s");
	}

}
