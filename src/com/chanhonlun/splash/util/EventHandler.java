package com.chanhonlun.splash.util;

/**
 * 
 * a handler when receiving data of type <T>
 * 
 * @author chanhonlun
 *
 * @param <T>
 */
public interface EventHandler<T> {
	
	/**
	 * it will be called when {@code EventEmitter} calls {@code emit(T data)}<br>
	 * handle the emitted data in this method
	 * 
	 * @param data
	 */
	public void handle(T data);
}
