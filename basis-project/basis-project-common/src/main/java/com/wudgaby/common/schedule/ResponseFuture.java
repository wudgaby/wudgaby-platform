package com.wudgaby.common.schedule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseFuture {
	private static final Map<Long, ResponseFuture> FUTURE = new ConcurrentHashMap<>();
	private final long DEFAULT_TIMEOUT = 30000;
	private volatile boolean isDone;
	private Object respObject;
	private long timeout = DEFAULT_TIMEOUT;
	private int errorCode;
	private String errorMsg;
	
	public Object get(long timeout) throws RuntimeException{
		if(timeout > 0){
			this.timeout = timeout;
		}
		return get();
	}
	
	public Object get() throws RuntimeException{
		long start = System.currentTimeMillis();
		synchronized (this) {
			while(!isDone){
				try {
					wait(timeout);
					if (isDone || System.currentTimeMillis() - start > timeout) {
	                    break;
	                }
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
		if(!isDone){
			throw new RuntimeException("超时");
		}
		return respObject;
	}
	
	//客户端收到服务端结果后，回调时相关方法，即设置isDone = true并notifyAll()
	public void handleResponse(Object _appResponse) {
		respObject = _appResponse; //将远程调用结果设置到callback中来
		setDone();
	}
	
	public void onException(int _errorType, String _errorMsg) {
		errorCode = _errorType;
        errorMsg = _errorMsg;
		setDone();
	}
	
	private void setDone(){
		isDone = true;
		//获取锁，因为前面wait()已经释放了callback的锁了
        synchronized (this) { 
        	// 唤醒处于等待的线程
            notifyAll(); 
        }
	}
}
