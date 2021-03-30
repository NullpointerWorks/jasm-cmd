package com.nullpointerworks.cmd.jasm.exe.plugin;

import com.nullpointerworks.jasm.vm.InterruptListener;
import com.nullpointerworks.jasm.vm.VirtualMachine;

public abstract class VMPlugin implements InterruptListener
{
	public VMPlugin()
	{
		
	}
	
	public abstract void onInterrupt(VirtualMachine vm, int code);
}
