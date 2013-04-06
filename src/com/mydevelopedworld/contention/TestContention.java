package com.mydevelopedworld.contention;

import static java.lang.System.out;

import com.mydevelopedworld.contention.IncrementThread.Modality;

/**
 * 
 * @author while mydevelopedworld.wordpress.com
 * 
 * Main class which tests the performance of Multithreading Contention
 * 
 * The test nees three args:
 * 1) N: Number of Threads
 * 2) Modality: 0 = AVOID_CONTENTION; 1 = CONTENTION_ATOMIC_LONG; 2 = CONTENTION_SYNC
 * 3) M: Number of Loops
 * 
 * The test will run N Threads in the specified Modality and will perform
 * M loops of increment operation.
 * 
 * In both CONTENTION Mod the result is build directly by every Thread.
 * In AVOID_CONTENTION the result is build in the end when every Thread teminates.
 * 
 * In the end the Test will print the result and the time it took to calculate it.
 *
 * Graphs of performance results can be found at: 
 * mydevelopedworld.wordpress.com
 *
 */
public class TestContention {

	private static final int MODALITY_AVOID_CONTENTION = 0;
	private static final int MODALITY_CONTENTION_ATOMIC_LONG = 1;
	private static final int MODALITY_CONTENTION_SYNC = 2;

	private static void printUsage(){
		out.println("Please pass me three integer: ");
		out.println("\tFirst int: Number of Threads");
		out.println("\t\tPass a number > 0");
		out.println("\tSecond int: Modality:");
		out.println("\t\tPass 0 to avoid contention");
		out.println("\t\tPass 1 to try with AtomicInteger contention");
		out.println("\t\tPass 2 to try with Synchronized method contention");
		out.println("\tThird int: Number of Loops:");
		out.println("\t\tPass a number > 0");
		System.exit(-1);
	}

	public static void main(String... args){
		if(args.length != 3){
			TestContention.printUsage();
		}

		int nThread = 0; // Number of Threads
		int modality = 0; // Modality: 0 = AVOID_CONTENTION; 1 = CONTENTION_ATOMIC_LONG; 2 = CONTENTION_SYNC
		int loops = 0; // How many increment operations every Thread will perform

		/* Checking the input args... */
		try{
			nThread = Integer.parseInt(args[0]);
			modality = Integer.parseInt(args[1]);		
			loops = Integer.parseInt(args[2]);
			if(nThread < 1 || modality < 0 || modality > 2 || loops < 1){
				TestContention.printUsage();
			}
		}catch(NumberFormatException e){
			TestContention.printUsage();
		}		

		/* Object used to collect the final result of the Threads increment operations */
		IncrementThread.SumObject so = new IncrementThread.SumObject(nThread);
		/* Thread array, used to perform join() on all of them */
		Thread[] threads = new Thread[nThread];

		final long startTime = System.currentTimeMillis();
		
		switch(modality){
		/* Initializing and Starting the Threads... */
		case MODALITY_AVOID_CONTENTION:
			out.println(nThread+" Threads in modality AVOID_CONTENTION will loop "+loops+" times...");
			for(int i = 0; i < nThread; i++){
				threads[i] = new Thread(new IncrementThread(i, Modality.AVOID_CONTENTION, loops, so));
				threads[i].start();
			}
			break;
		case MODALITY_CONTENTION_ATOMIC_LONG:
			out.println(nThread+" Threads in modality CONTENTION_ATOMIC_LONG will loop "+loops+" times...");
			for(int i = 0; i < nThread; i++){
				threads[i] = new Thread(new IncrementThread(i, Modality.CONTENTION_ATOMIC_LONG, loops, so));
				threads[i].start();
			}
			break;
		default: /* CONTENTION_SYNC */
			out.println(nThread+" Threads in modality CONTENTION_SYNC will loop "+loops+" times...");
			for(int i = 0; i < nThread; i++){
				threads[i] = new Thread(new IncrementThread(i, Modality.CONTENTION_SYNC, loops, so));
				threads[i].start();
			}
			break;
		}

		/* 
		 * I'll wait for all Threads to be done...
		 */
		for(int i = 0; i < nThread; i++){
			try {				
				threads[i].join(); // The join() creates a happens-before barrier
			} catch (InterruptedException e) {
				e.printStackTrace(); 
			}
		}

		out.println("\nAll the Threads are done!");
		switch(modality){
			/* Showing the Result... */
			case MODALITY_AVOID_CONTENTION:
				long value = 0;
				/* Overhead in order to combine the partial results */
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
				break;
			case MODALITY_CONTENTION_SYNC:
				out.println("\tResult: "+so.getValueSyn());
				break;
			default: /* MODALITY_CONTENTION_ATOMIC_LONG */
				out.println("\tResult: "+so.value);				
				break;
			}
		
		/* Expected result */
		long result = (long)nThread * (long)loops;
		out.println("\tExpected: "+result);
		final long endTime = System.currentTimeMillis();
		
		/* How much time did it take? */
		out.println();
		out.println("Total execution time: " + ((endTime - startTime)/1000.0) + "s");
	}

}
