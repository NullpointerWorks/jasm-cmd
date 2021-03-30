package com.nullpointerworks.cmd.jasm;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import com.nullpointerworks.cmd.jasm.asm.SourceAssembler;
import com.nullpointerworks.cmd.jasm.exe.BytecodeExecution;
import com.nullpointerworks.cmd.jasm.exe.JarLoader;
import com.nullpointerworks.cmd.jasm.exe.plugin.VMPlugin;
import com.nullpointerworks.jasm.asm.ParserUtility;

public class AssemblerCMD
{
	public static void main(String[] args) 
	{
		new AssemblerCMD(args);
		
		/*
		args = new String[] 
		{
			"-asm", 
			"-in<F:\\Development\\Projects\\jasm\\commandline\\main.jasm>", 
			"-out<F:\\Development\\Projects\\jasm\\commandline\\main.bin>", 
			"-log<PTAM>",
		};
		new AssemblerCMD(args);
		
		args = new String[] 
		{
			"-exe", 
			"-in<F:\\Development\\Projects\\jasm\\commandline\\main.bin>",
			"-speed<50>"
		};
		new AssemblerCMD(args);
		//*/
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
			executeBinary(args);
			return;
		}
		
		/*
		 * do an assembly
		 */
		if (option.startsWith("-asm"))
		{
			assemblySourceCode(args);
			return;
		}
		
		/*
		 * error
		 */
		System.out.println("Unrecognized command: "+option+"\n");
	}
	
	private void assemblySourceCode(String[] args) 
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
				if (log.contains("T"))
				{
					asm.setTranslationVerbose(true);
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
	}

	private void executeBinary(String[] args) 
	{
		BytecodeExecution exe = new BytecodeExecution();
		
		int i = 1;
		int l = args.length;
		for (; i<l; i++)
		{
			String arg = args[i];
			
			if (arg.startsWith("-in<"))
			{
				String inFile = arg.substring(4, arg.length()-1);
				exe.loadFile(inFile);
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
			
			if (arg.startsWith("-speed<"))
			{
				String speed = arg.substring(7, arg.length()-1);
				if (ParserUtility.isInteger(speed))
				{
					int s = Integer.parseInt(speed);
					exe.setCycleRate(s);
				}
				continue;
			}
			
			System.out.println("Argument not recognized: "+arg);
		}
		
		// load plugins
		File file = new File("plugins/");
		if (!file.exists()) file.mkdirs();
		File[] list = file.listFiles();
		for (File f : list)
		{
			if (!f.isDirectory())
			{
				String path = f.getAbsolutePath();
				if (path.endsWith(".jar") || path.endsWith(".war"))
				{
					loadJarFile(path, exe);
				}
			}
		}
		
		exe.execute();
	}

	private void loadJarFile(String plug, BytecodeExecution exe)
	{
		JarLoader jloader = new JarLoader();
		jloader.loadJarClasses(plug);
		
		Class<?> foundClass = jloader.findBySuperClass(VMPlugin.class);
		if (foundClass == null) 
		{
			System.err.println(plug+" does not contain a 'VMPlugin' class instance.");
			return;
		}
		
		@SuppressWarnings("unchecked")
		Class<? extends VMPlugin> pluginClass = (Class<? extends VMPlugin>) foundClass;
		VMPlugin plugin = null;
		
		try 
		{
			plugin = pluginClass.getDeclaredConstructor().newInstance();
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) 
		{
			e.printStackTrace();
		}
		
		if (plugin != null)
		{
			System.out.println("Loaded plugin: "+foundClass.getName()+"\n    ("+plug+").");
			exe.addPlugin(plugin);
		}
		else
		{
			System.err.println("Failed to instantiate "+foundClass.getName()+"\n    ("+plug+").");
		}
	}
}
