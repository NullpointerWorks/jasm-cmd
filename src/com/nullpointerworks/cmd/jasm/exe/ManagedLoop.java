package com.nullpointerworks.cmd.jasm.exe;

public abstract class ManagedLoop implements Runnable 
{
	private final long NANO = 1000_000_000;
	private final double inv_MICRO = 1d/1000000d;
	private final double inv_NANO = 1d/1000000000d;
	
	private Thread thread;
	private boolean running = true;
	private int target_update = 30; 
	private long ideal_time = NANO / target_update;
	private long nanotime_prev;
	
	public void setTargetRate(int fps) 
	{
		target_update = fps;
		ideal_time = NANO / target_update;
	}
	
	public void start()
	{
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop()
	{
		running = false;
	}
	
	@Override
	public void run()
	{
		onInit();
		
		ideal_time = NANO / target_update;
		nanotime_prev = System.nanoTime();
		long nanotime_curr;
		long nanotime_delta;
		long sleep;
		double timing;
		
		while (running)
		{
			nanotime_curr = System.nanoTime();
			nanotime_delta = nanotime_curr - nanotime_prev;
			nanotime_prev = nanotime_curr;
			timing = nanotime_delta * inv_NANO;
			onUpdate(timing);
			
			sleep = (long)( ((nanotime_curr - System.nanoTime() + ideal_time) * inv_MICRO));
			try
			{
				Thread.sleep( (sleep<0)?0:sleep );
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
		onDispose();
	}
	
	public abstract void onInit();
	public abstract void onUpdate(double dt);
	public abstract void onDispose();
}
