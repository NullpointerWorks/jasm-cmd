package com.nullpointerworks.cmd.jasm;

import com.nullpointerworks.cmd.jasm.asm.SourceAssembler;
import com.nullpointerworks.cmd.jasm.exe.BytecodeExecution;
import com.nullpointerworks.jasm.asm.ParserUtility;

public class AssemblerCMD
{
	public static void main(String[] args) 
	{
		args = new String[] 
		{
			"-asm", 
			"-in<F:\\Development\\Projects\\jasm\\commandline\\main.jasm>", 
			"-out<F:\\Development\\Projects\\jasm\\commandline\\main.bin>", 
			"-log<PAM>",
		};
		new AssemblerCMD(args);
		
		args = new String[] 
		{
			"-exe", 
			"-in<F:\\Development\\Projects\\jasm\\commandline\\main.bin>",
			"-spd<50>"
		};
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
					if (ParserUtility.isInteger(mem))
					{
						int size = Integer.parseInt(mem);
						exe.setMemorySize(size);
					}
					continue;
				}
				
				if (arg.startsWith("-org<"))
				{
					String org = arg.substring(5, arg.length()-1);
					if (ParserUtility.isInteger(org))
					{
						int origin = Integer.parseInt(org);
						exe.setOrigin(origin);
					}
					continue;
				}
				
				if (arg.startsWith("-spd<"))
				{
					String speed = arg.substring(5, arg.length()-1);
					if (ParserUtility.isInteger(speed))
					{
						int s = Integer.parseInt(speed);
						exe.setCycleRate(s);
					}
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
					if (log.contains("M"))
					{
						asm.setMachineCodeVerbose(true);
					}
					continue;
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
