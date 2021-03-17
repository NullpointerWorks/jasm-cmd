package com.nullpointerworks.cmd.jasm;

import java.util.List;

import com.nullpointerworks.jasm.asm.error.BuildError;

import com.nullpointerworks.jasm.asm.parser.Definition;
import com.nullpointerworks.jasm.asm.parser.Parser;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.parser.SourceFileParser;
import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.Assembler;
import com.nullpointerworks.jasm.asm.assembler.SourceCodeAssembler;

public class Asm 
{
	private VerboseListener vlParsing;
	private VerboseListener vlAssembling;
	
	public Asm()
	{
		
		
	}
	
	public void assemble()
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
		
		
		
		
	}
	
	
	
	
	
	
	
	private void printMachineCode(int offset, List<Integer> code) 
	{
		onPrint("-------------------------------");
		onPrint("Machine Code Start\n");
		
		int it = 0;
		int leng = ( code.size()+"" ).length();
		String padding = "";
		for (int k=0; k<leng; k++) padding += " ";
		String paddingFormat = "%0"+leng+"x";
		
		for (int j = offset, l = code.size(); j<l; j++)
		{
			Integer i = code.get(j);
			
			int b1 = (i>>24)&0xff;
			int b2 = (i>>16)&0xff;
			int b3 = (i>>8)&0xff;
			int b4 = (i)&0xff;
			String s1 = String.format("%02x ", b1);
			String s2 = String.format("%02x ", b2);
			String s3 = String.format("%02x ", b3);
			String s4 = String.format("%02x", b4);
			
			String addr = padding + String.format(paddingFormat, it);
			addr = "[ "+addr.substring(leng)+" ] ";
			System.out.println(addr+s1+s2+s3+s4);
			it++;
		}
		
		onPrint("\nMachine Code End");
		onPrint("-------------------------------");
	}
	
	private void onPrint(String msg) 
	{
		System.out.println(msg);
	}
}
