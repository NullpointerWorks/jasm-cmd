package com.nullpointerworks.cmd.jasm.exe;

import java.util.List;

import com.nullpointerworks.jasm.vm.BytecodeVirtualMachine;
import com.nullpointerworks.jasm.vm.VMProcessException;
import com.nullpointerworks.jasm.vm.VirtualMachine;

public class Emulator extends ManagedLoop
{
	private VirtualMachine vm;
	private List<Integer> code;
	private int origin;
	private int memorySize;
	
	public Emulator()
	{
		vm = new BytecodeVirtualMachine();
		origin = 0;
		memorySize = 2048;
		
		
		
	}
	
	


	@Override
	public void onInit() 
	{
		
	}

	@Override
	public void onUpdate(double dt) 
	{
		
	}

	@Override
	public void onDispose() 
	{
		while (vm.hasException())
		{
			VMProcessException ex = vm.getException();
			System.err.println( ex.getMemoryTrace() );
		}
	}
}
