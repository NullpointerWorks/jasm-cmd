package com.nullpointerworks.cmd.jasm;

import com.nullpointerworks.cmd.jasm.asm.SourceAssembler;
import com.nullpointerworks.cmd.jasm.exe.BytecodeExecution;

/*

## version
-version						requests the version of this assembler.


## source assembling
-asm							requests to assemble source code.
-i<...>							specifies the program entry file.
-o<...>							specifies the output filename.

# optional
-l<...>							writes parser and/or assembler information to a file.
	-l<P> 						Logs parser info to a file.
	-l<A> 						Logs assembler info to a file.
	-l<PA> 						Logs parser and assembler info to a file.

## execution of a binary
-exe							requests to execute a bytecode file.
-i<...>							specifies the input file to execute.
-m<...>							set memory size.
-s<...>							set the VM speed in cycles per second.





*/
public class AssemblerCMD
{
	
	public static void main(String[] args) 
	{
		new AssemblerCMD(args);
	}
	
	public AssemblerCMD(String[] args)
	{
		if (args.length < 1) return;
		
		String option = args[0];
		
		/*
		 * print versioning
		 */
		if (option.startsWith("-version"))
		{
			System.out.println();
			System.out.println("Assembler version");
			System.out.println("  version: "+SourceAssembler.Version);
			System.out.println("Execution Environment");
			System.out.println("  version: "+BytecodeExecution.Version);
			System.out.println();
			return;
		}
		
		/*
		 * do an assembly
		 */
		if (option.startsWith("-asm"))
		{
			SourceAssembler asm = new SourceAssembler();
			
			int i = 1;
			int l = args.length;
			for (; i<l; i++)
			{
				String arg = args[i];
				
				if (arg.startsWith("-i<"))
				{
					String file = arg.substring(3, arg.length()-1);
					asm.setInputFile(file);
					continue;
				}
				
				if (arg.startsWith("-o<"))
				{
					String file = arg.substring(3, arg.length()-1);
					asm.setOutputFile(file);
					continue;
				}
				
				if (arg.startsWith("-l<"))
				{
					String log = arg.substring(3, arg.length()-1);
					if (log.contains("P"))
					{
						asm.setParserVerbose(true);
					}
					if (log.contains("A"))
					{
						asm.setAssemblerVerbose(true);
					}
				}
			}
			
			asm.assemble();
			
			return;
		}
		
		/*
		 * error
		 */
		System.out.println("Unrecognized command: "+option+"\n");
		
	}
}
