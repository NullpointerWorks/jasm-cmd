package com.nullpointerworks.cmd.jasm;

import static com.nullpointerworks.jasm.vm.VMRegister.*;

import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.Assembler;
import com.nullpointerworks.jasm.asm.assembler.SourceCodeAssembler;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.Definition;
import com.nullpointerworks.jasm.asm.parser.Parser;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.parser.SourceFileParser;
import com.nullpointerworks.jasm.vm.BytecodeVirtualMachine;
import com.nullpointerworks.jasm.vm.InterruptListener;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMProcessException;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/*

# version
-version | -v					requests the version of this assembler

# source assembling
-asm							ask to assemble source code	
-i<...>							specifies the program entry file
-o<...>							specifies the output filename
-v<...>							verbose parser and/or assembling flags




*/
public class MainConsoleVM implements InterruptListener, VerboseListener
{
	public static void main(String[] args) 
	{
		new MainConsoleVM(args);
	}
	
	public MainConsoleVM(String[] args)
	{
		
		
		
		
		
		
		
		
		/*
		 * the parser formats the source code writing
		 */
		Parser parser = new SourceFileParser();
		//parser.setVerboseListener(this);
		parser.parse("playground.jasm");
		if(parser.hasErrors())
		{
			List<BuildError> errors = parser.getErrors();
			for (BuildError be : errors)
			{
				System.out.println( be.getDescription() );
			}
			return;
		}
		List<SourceCode> sourcecode = parser.getSourceCode();
		List<Definition> definitions = parser.getDefinitions();
		int origin = parser.getOrigin();
		
		/*
		 * the assembler turns source code objects into machine code
		 */
		Assembler assemble = new SourceCodeAssembler();
		//assemble.setVerboseListener(this);
		assemble.draft(sourcecode, definitions, origin);
		if(assemble.hasErrors())
		{
			List<BuildError> errors = assemble.getErrors();
			for (BuildError be : errors)
			{
				System.out.println( be.getDescription() );
			}
			return;
		}
		List<Integer> code = assemble.getMachineCode();
		//printMachineCode(origin, code);
		
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
	public void onPrint(String msg) 
	{
		System.out.println(msg);
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
