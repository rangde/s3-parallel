package org.lis.aws.s3.helper;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lis.aws.s3.entity.Source;

public class Tracker extends AuditAware
{
	private Queue<Source> requestQueue = new ConcurrentLinkedQueue<Source>();
	
	public void add(Source source)
	{
		requestQueue.add(source);
	}
	
	public Source get()
	{
		Source source = requestQueue.poll();
		// Log if necessary to track the status
		return source;
	}
	
	public Integer getQueueSize()
	{
		return requestQueue.size();
	}
	
	public boolean isEmpty()
	{
		return requestQueue.isEmpty();
	}
}
