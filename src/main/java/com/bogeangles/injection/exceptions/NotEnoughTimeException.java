package com.bogeangles.injection.exceptions;

public class NotEnoughTimeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7745434343365165119L;
	int timeNeeded;
	public NotEnoughTimeException(int timeNeeded){
		this.timeNeeded = timeNeeded;
	}
	public int getTimeNeeded(){
		return timeNeeded;
	}
}
