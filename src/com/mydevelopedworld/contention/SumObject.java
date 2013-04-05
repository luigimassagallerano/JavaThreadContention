package com.mydevelopedworld.contention;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author while mydevelopedworld.wordpress.com
 * 
 * Object used to collect the final result of the Threads increment operations
 */
public class SumObject{
	/**
	 * Used in CONTENTION modality:
	 * every Thread will atomically increment this variable
	 */
	public AtomicInteger value;
	/**
	 * Used in AVOID_CONTENTION modality:
	 * every Thread will write in a different array slot ignoring others Thread.
	 *  
	 */
	public int[] partialValues;

	public SumObject(int size){
		this.value = new AtomicInteger(0);
		this.partialValues = new int[size];
	}	
}
