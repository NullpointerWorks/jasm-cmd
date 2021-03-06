package com.nullpointerworks.cmd.jasm.exe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.cmd.jasm.exe.plugin.VMPlugin;
import com.nullpointerworks.jasm.vm.InterruptListener;
import com.nullpointerworks.jasm.vm.VirtualMachine;

public class BytecodeExecution implements InterruptListener
{
	public static final String Version = "1.0.0";

	private List<VMPlugin> plugins;
	private List<Integer> code;
	private Emulator emu;
	
	public BytecodeExecution()
	{
		plugins = new ArrayList<VMPlugin>();
		code = new ArrayList<Integer>();
		emu = new Emulator();
	}

	public void addPlugin(VMPlugin plugin) 
	{
		plugins.add(plugin);
	}
	
	public void loadFile(String filename)
	{
		File f = new File(filename);
		byte[] bytes = null;
		
		try 
		{
			bytes = readBinary( f.getAbsolutePath() );
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if (bytes==null)
		{
			return;
		}
		
		int i = 0;
		int l = bytes.length;
		for (; i<l; i+=4)
		{
			byte b1 = bytes[i];
			byte b2 = bytes[i+1];
			byte b3 = bytes[i+2];
			byte b4 = bytes[i+3];
			
			int i1 = (b1 << 24);
			int i2 = (b2 << 16);
			int i3 = (b3 << 8);
			int i4 = (b4);
			
			int bytecode = i1|i2|i3|i4;
			
			code.add(bytecode);
		}
	}
	
	public void setMemorySize(int size) 
	{
		emu.setMemorySize(size);
	}
	
	public void setOrigin(int org) 
	{
		emu.setOrigin(org);
	}

	public void setCycleRate(int s) 
	{
		emu.setCycleRate(s);
	}
	
	public void execute()
	{
		if (!errorCheck()) return; // TODO notify about error
		emu.setInterruptListener(this);
		emu.setCode(code);
		emu.start();
	}
	
	@Override
	public void onInterrupt(VirtualMachine vm, int code) 
	{
		if (code == 0)
		{
			System.exit(0);
			return;
		}
		
		for (VMPlugin plug : plugins)
		{
			plug.onInterrupt(vm, code);
		}
	}
	
	private boolean errorCheck() 
	{
		if (code.size() < 1) return false;
		return true;
	}
	
	private byte[] readBinary(String file) throws IOException
	{
		Path path = Paths.get(file);
	    return Files.readAllBytes(path);
	}
}
