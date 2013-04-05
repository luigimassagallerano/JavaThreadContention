package com.mydevelopedworld.contention;

import static java.lang.System.out;

/**
 * 
 * @author while mydevelopedworld.wordpress.com
 * 
 * Thread that will perform the increment operations.
 * A Thread can be created in three different Modality: 
 * CONTENTION_ATOMIC_INT: The Thread will write to an AtomicInteger
 * CONTENTION_SYNC: The Thread will write on a shared variable using synchornized methods
 * AVOID_CONTENTION: The Thread will write on a reserved slot of a shared array in order to avoid contention.
 * 
 */
public class SummingThread implements Runnable {

	public enum Modality{
		CONTENTION_ATOMIC_INT,
		CONTENTION_SYNC,
		AVOID_CONTENTION;
	}
	
	/**
	 * Thread ID. 
	 * It is used also to access the reserved slot on the shared array in AVOID_CONTENTION modality
	 */
	private int idThread;
	/**
	 * Modality: CONTENTION_SYNC, CONTENTION_ATOMIC_INT or AVOID_CONTENTION
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
		case CONTENTION_SYNC:
			while(this.loops > 0){
				so.increment();
				this.loops--;
			}
		default: /* CONTENTION ATOMIC INT */
			while(this.loops > 0){
				so.value.incrementAndGet();
				this.loops--;
			}
			break;
		}	
		out.println("\tThread ["+this.idThread+"] ended.");
	}


}
