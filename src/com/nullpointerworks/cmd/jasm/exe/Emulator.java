package com.nullpointerworks.cmd.jasm.exe;

import java.util.List;

import com.nullpointerworks.jasm.vm.BytecodeVirtualMachine;
import com.nullpointerworks.jasm.vm.InterruptListener;
import com.nullpointerworks.jasm.vm.VMProcessException;
import com.nullpointerworks.jasm.vm.VirtualMachine;

public class Emulator extends ManagedLoop
{
	private VirtualMachine vm;
	private List<Integer> code;
	private int origin;
	private int memorySize;
	private InterruptListener listener;
	
	public Emulator()
	{
		vm = new BytecodeVirtualMachine();
		origin = 0;
		memorySize = 2048;
	}

	public void setMemorySize(int size) 
	{
		if (size < 1) return;
		memorySize = size;
	}

	public void setOrigin(int org) 
	{
		if (org < 1) return;
		origin = org;
	}
	
	public void setInterruptListener(InterruptListener il) 
	{
		listener = il;
	}

	public void setCode(List<Integer> code) 
	{
		this.code = code;
	}
	
	@Override
	public void onInit() 
	{
		vm.setInterruptListener(listener);
		vm.setOrigin(origin);
		vm.setMemorySize(memorySize);
		vm.setMemory(0,code);
		if (vm.hasException()) stop();
	}
	
	@Override
	public void onUpdate(double dt) 
	{
		vm.nextInstruction();
		if (vm.hasException()) stop();
	}
	
	@Override
	public void onDispose() 
	{
		while (vm.hasException())
		{
			VMProcessException ex = vm.getException();
			System.err.println( ex.getMemoryTrace() );
		}
		vm = null;
		code = null;
	}
}
