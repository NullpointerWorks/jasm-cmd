package com.nullpointerworks.cmd.jasm.exe;

import static com.nullpointerworks.jasm.vm.VMRegister.REG_A;
import static com.nullpointerworks.jasm.vm.VMRegister.REG_B;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.vm.BytecodeVirtualMachine;
import com.nullpointerworks.jasm.vm.InterruptListener;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMProcessException;
import com.nullpointerworks.jasm.vm.VirtualMachine;

public class BytecodeExecution implements InterruptListener
{
	public static final String Version = "1.0.0 beta";
	
	private int origin;
	private List<Integer> code;
	
	public BytecodeExecution()
	{
		origin = 0;
		code = new ArrayList<Integer>();
		
	}
	
	public void execute()
	{
		
		
		/*
		 * create virtual machine and run code
		 */
		VirtualMachine vm = new BytecodeVirtualMachine();
		vm.setMemorySize(2048);
		vm.setInterruptListener(this);
		vm.setOrigin(origin);
		vm.setMemory(0, code);
		
		boolean running = !vm.hasException();
		while(running) 
		{
			vm.nextInstruction();
			running = !vm.hasException();
		}
		
		while (vm.hasException())
		{
			VMProcessException ex = vm.getException();
			System.err.println( ex.getMemoryTrace() );
		}
	}
	
	
	
	@Override
	public void onInterrupt(VirtualMachine vm, int code) 
	{
		if (code == 0)
		{
			System.exit(0);
			return;
		}
		
		if (code == 1)
		{
			Register regA = vm.getRegister( REG_A );
			System.out.println( "A: "+regA.getValue() );
			return;
		}
		
		if (code == 2)
		{
			Register regB = vm.getRegister( REG_B );
			System.out.println( "B: "+regB.getValue() );
			return;
		}
		
		if (code == 3)
		{
			int V1_X = vm.getMemory(0);
			int V1_Y = vm.getMemory(1);

			System.out.println( "V1:");
			System.out.println( "  ("+V1_X+","+V1_Y+")");
			return;
		}
		
		if (code == 4)
		{
			String A = vm.getMemory(6)+"";
			String B = vm.getMemory(7)+"";
			String C = vm.getMemory(8)+"";
			String D = vm.getMemory(9)+"";
			
			System.out.println( "M1:");
			System.out.println( "  | "+A+" "+B+" |");
			System.out.println( "  | "+C+" "+D+" |");
			
			return;
		}
		
		if (code == 5)
		{
			String A = vm.getMemory(10)+"";
			String B = vm.getMemory(11)+"";
			String C = vm.getMemory(12)+"";
			String D = vm.getMemory(13)+"";
			
			System.out.println( "M2:");
			System.out.println( "  | "+A+" "+B+" |");
			System.out.println( "  | "+C+" "+D+" |");
			
			return;
		}
		
		if (code == 6)
		{
			String A = vm.getMemory(14)+"";
			String B = vm.getMemory(15)+"";
			String C = vm.getMemory(16)+"";
			String D = vm.getMemory(17)+"";
			
			System.out.println( "M3:");
			System.out.println( "  | "+A+" "+B+" |");
			System.out.println( "  | "+C+" "+D+" |");
			
			return;
		}
	}
}
