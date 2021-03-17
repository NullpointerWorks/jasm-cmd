package com.nullpointerworks.cmd.jasm.exe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.nullpointerworks.jasm.vm.VMRegister.REG_A;
import static com.nullpointerworks.jasm.vm.VMRegister.REG_B;
import static com.nullpointerworks.jasm.vm.VMRegister.REG_C;
import static com.nullpointerworks.jasm.vm.VMRegister.REG_D;

import com.nullpointerworks.jasm.vm.BytecodeVirtualMachine;
import com.nullpointerworks.jasm.vm.InterruptListener;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMProcessException;
import com.nullpointerworks.jasm.vm.VirtualMachine;

public class BytecodeExecution implements InterruptListener
{
	public static final String Version = "1.0.0";

	private List<Integer> code;
	private int origin;
	private int memorySize;
	private int speed;
	
	
	public BytecodeExecution()
	{
		code = new ArrayList<Integer>();
		origin = 0;
		memorySize = 2048;
		speed = 1000;
		
		
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
	
	public void execute()
	{
		if (!errorCheck()) return; // TODO notify about error
		
		/*
		 * create virtual machine and run code
		 */
		VirtualMachine vm = new BytecodeVirtualMachine();
		vm.setMemorySize(2048);
		vm.setInterruptListener(this);
		vm.setOrigin(origin);
		vm.setMemory(0,code);
		
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
			Register reg = vm.getRegister( REG_A );
			System.out.println( "A: "+reg.getValue() );
			return;
		}
		
		if (code == 2)
		{
			Register reg = vm.getRegister( REG_B );
			System.out.println( "B: "+reg.getValue() );
			return;
		}
		
		if (code == 3)
		{
			Register reg = vm.getRegister( REG_C );
			System.out.println( "C: "+reg.getValue() );
			return;
		}
		
		if (code == 4)
		{
			Register reg = vm.getRegister( REG_D );
			System.out.println( "D: "+reg.getValue() );
			return;
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
