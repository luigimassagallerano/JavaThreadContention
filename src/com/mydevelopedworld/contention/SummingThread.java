package com.mydevelopedworld.contention;

import static java.lang.System.out;

/**
 * 
 * @author while mydevelopedworld.wordpress.com
 * 
 * Thread that will perform the increment operations.
 * A Thread can be created in two different Modality: 
 * CONTENTION: The Thread will write on a shared variable and will perform synchronization operation.
 * AVOID_CONTENTION: The Thread will write on a reserved slot of a shared array in order to avoid contention.
 * 
 */
public class SummingThread implements Runnable {

	public enum Modality{
		CONTENTION,
		AVOID_CONTENTION;
	}
	
	/**
	 * Thread ID. 
	 * It is used also to access the reserved slot on the shared array in AVOID_CONTENTION modality
	 */
	private int idThread;
	/**
	 * Modality: CONTENTION or AVOID_CONTENTION
	 */
	private Modality modality;
	/**
	 * How many increment operations the Thread will perform
	 */
	private int loops;
	/**
	 * Object used to collect the final result of the Threads increment operations
	 */
	private SumObject so;

	public SummingThread(int idThread, Modality modality, int loops, SumObject so){
		this.idThread = idThread;
		this.modality = modality;
		this.so = so;
		this.loops = loops;
	}
	
	@Override
	public void run() {
		out.println("\tThread ["+this.idThread+"] started...");
		switch (this.modality) {
		case AVOID_CONTENTION:
			while(this.loops > 0){
				so.partialValues[this.idThread]++;
				this.loops--;
			}
			break;

		default: /* CONTENTION */
			while(this.loops > 0){
				so.value.incrementAndGet();
				this.loops--;
			}
			break;
		}	
		out.println("\tThread ["+this.idThread+"] ended.");
	}


}