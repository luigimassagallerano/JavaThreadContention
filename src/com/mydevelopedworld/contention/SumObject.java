package com.mydevelopedworld.contention;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author while mydevelopedworld.wordpress.com
 * 
 * Object used to collect the final result of the Threads increment operations
 */
public class SumObject{
	/**
	 * Used in CONTENTION_ATOMIC_INT modality:
	 * every Thread will atomically increment this variable
	 */
	public AtomicLong value;
	/**
	 * Used in AVOID_CONTENTION modality:
	 * every Thread will write in a different array slot ignoring others Thread.
	 *  
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
	
	public long getValueSyn(){
		return valueSyn;
	}
	
}
