package com.nullpointerworks.cmd.jasm;

import com.nullpointerworks.cmd.jasm.asm.SourceAssembler;
import com.nullpointerworks.cmd.jasm.exe.BytecodeExecution;

/*

## version
-version						requests the version of this assembler.


## source assembling
-asm							requests to assemble source code.
-in<...>						specifies the program entry file.
-out<...>						specifies the output filename.

# optional
-log<...>						writes parser and/or assembler information to a file.
	-log<P> 					Logs parser info to a file.
	-log<A> 					Logs assembler info to a file.
	-log<PA> 					Logs parser and assembler info to a file.


## execution of a binary
-exe							requests to execute a bytecode file.
-in<...>						specifies the input file to execute.

# optional
-mem<...>						set memory size.
-org<...>						set instruction pointer origin.
-spd<...>						set the VM speed in cycles per second.



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
		 * print version
		 */
		if (option.startsWith("-version"))
		{
			System.out.println();
			System.out.println("Assembler version");
			System.out.println("  version: "+SourceAssembler.Version);
			System.out.println();
			System.out.println("Execution Environment");
			System.out.println("  version: "+BytecodeExecution.Version);
			System.out.println();
			return;
		}
		
		/*
		 * execute binary
		 */
		if (option.startsWith("-exe"))
		{
			BytecodeExecution exe = new BytecodeExecution();
			
			int i = 1;
			int l = args.length;
			for (; i<l; i++)
			{
				String arg = args[i];
				
				if (arg.startsWith("-in<"))
				{
					String file = arg.substring(4, arg.length()-1);
					exe.loadFile(file);
					continue;
				}
				
				if (arg.startsWith("-mem<"))
				{
					String mem = arg.substring(5, arg.length()-1);
					
					continue;
				}
				
				if (arg.startsWith("-org<"))
				{
					String org = arg.substring(5, arg.length()-1);
					
					continue;
				}
				
				if (arg.startsWith("-spd<"))
				{
					String speed = arg.substring(5, arg.length()-1);
					
					continue;
				}
				
				System.out.println("Argument not recognized: "+arg);
			}
			
			exe.execute();
			
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
				
				if (arg.startsWith("-in<"))
				{
					String file = arg.substring(4, arg.length()-1);
					asm.setInputFile(file);
					continue;
				}
				
				if (arg.startsWith("-out<"))
				{
					String file = arg.substring(5, arg.length()-1);
					asm.setOutputFile(file);
					continue;
				}
				
				if (arg.startsWith("-log<"))
				{
					String log = arg.substring(5, arg.length()-1);
					if (log.contains("P"))
					{
						asm.setParserVerbose(true);
					}
					if (log.contains("A"))
					{
						asm.setAssemblerVerbose(true);
					}
				}
				
				System.out.println("Argument not recognized: "+arg);
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
