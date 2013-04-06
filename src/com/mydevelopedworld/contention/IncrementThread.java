package com.mydevelopedworld.contention;

import static java.lang.System.out;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author while mydevelopedworld.wordpress.com
 * 
 * Thread that will perform the increment operations.
 * A Thread can be created in three different Modality: 
 * CONTENTION_ATOMIC_LONG: The Thread will write to an AtomicLong
 * CONTENTION_SYNC: The Thread will write on a shared variable using synchornized methods
 * AVOID_CONTENTION: The Thread will write on a reserved slot of a shared array in order to avoid contention.
 * 
 */
public class IncrementThread implements Runnable {

	/*
	 * Enum which holds the three different modalities 
	 */
	public enum Modality{
		CONTENTION_ATOMIC_LONG, // Every Thread will increment a shared AtomicLong
		CONTENTION_SYNC, // Every Thread will increment a shared int using a synchronized method
		AVOID_CONTENTION; // Every Thread will increment a reserved int on a shared array
	}

	/**
	 * Thread ID. 
	 * It is used also to access the reserved slot on the shared array in AVOID_CONTENTION modality
	 */
	private int idThread;
	/**
	 * Modality: CONTENTION_SYNC, CONTENTION_ATOMIC_LONG or AVOID_CONTENTION
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

	public IncrementThread(int idThread, Modality modality, int loops, SumObject so){
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
		default: 
			/* CONTENTION_ATOMIC_LONG */
			while(this.loops > 0){
				so.value.incrementAndGet();
				this.loops--;
			}
			break;
		}	
		out.println("\tThread ["+this.idThread+"] ended.");
	}


	/**
	 * Object used to collect the final result of the Threads increment operations.
	 * This class makes no sense without an IncrementThread, that's why it is a static nested class
	 */
	public static class SumObject{
		/**
		 * Used in CONTENTION_ATOMIC_LONG modality:
		 * every Thread will atomically increment this variable
		 */
		public AtomicLong value;
		/**
		 * Used in AVOID_CONTENTION modality:
		 * every Thread will write in a different array slot ignoring others Thread.
		 */
		public long[] partialValues;
		/**
		 * Used in CONTENTION_SYNC modality:
		 * every Thread will increment through synchronized method this variable
		 */
		private long valueSyn;

		public SumObject(int size){
			this.value = new AtomicLong(0);
			this.valueSyn = 0;
			this.partialValues = new long[size];
		}	

		public synchronized void increment(){
			this.valueSyn++;
		}

		/* Used only after Threads.join(): no need to be synchronized */
		public long getValueSyn(){ 
			return valueSyn;
		}
	}

}
