package com.nullpointerworks.cmd.jasm.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.assembler.Assembler;
import com.nullpointerworks.jasm.asm.assembler.SourceCodeAssembler;
import com.nullpointerworks.jasm.asm.parser.Parser;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.parser.SourceFileParser;

public class SourceAssembler 
{
	public static final String Version = "1.0.0";
	
	private VerboseListener vlParsing;
	private VerboseListener vlAssembling;
	private VerboseListener vlMachineOut;
	
	private String inFile;
	private String outFile;
	private List<String> logText;
	
	public SourceAssembler()
	{
		logText = new ArrayList<String>();
		reset();
	}
	
	public void reset()
	{
		inFile = "";
		outFile = "";
		logText.clear();
		vlParsing = (e)->{};
		vlAssembling = (e)->{};
		vlMachineOut = (e)->{};
	}
	
	public void setInputFile(String in)
	{
		inFile = in;
	}
	
	public void setOutputFile(String out)
	{
		outFile = out;
	}
	
	public void setParserVerbose(boolean b) 
	{
		if (b) {vlParsing = (e)->{writeToFile(e);};}
		else {vlParsing = (e)->{};}
	}
	
	public void setAssemblerVerbose(boolean b) 
	{
		if (b) {vlAssembling = (e)->{writeToFile(e);};}
		else {vlAssembling = (e)->{};}
	}
	
	public void setMachineCodeVerbose(boolean b) 
	{
		if (b) {vlMachineOut = (e)->{writeToFile(e);};}
		else {vlMachineOut = (e)->{};}
	}
	
	public void assemble()
	{
		if (!errorCheck()) return;
		logText.clear();
		
		/*
		 * the parser formats the source code writing
		 */
		Parser parser = new SourceFileParser();
		parser.setVerboseListener(vlParsing);
		parser.parse(inFile);
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
		
		/*
		 * the assembler turns source code objects into machine code
		 */
		Assembler assemble = new SourceCodeAssembler();
		assemble.setVerboseListener(vlAssembling);
		assemble.draft(sourcecode);
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
		
		/*
		 * convert integers to bytes and write data to file
		 */
		int i = 0;
		int j = 0;
		int l = code.size();
		byte[] binary = new byte[l*4];
		for (; i<l; i++, j+=4)
		{
			Integer bytecode = code.get(i);
			int b1 = (bytecode>>24)&0xff;
			int b2 = (bytecode>>16)&0xff;
			int b3 = (bytecode>>8)&0xff;
			int b4 = (bytecode)&0xff;
			binary[j] 	= (byte)b1;
			binary[j+1] = (byte)b2;
			binary[j+2] = (byte)b3;
			binary[j+3] = (byte)b4;
		}
		
		File outputFile = new File(outFile);
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) 
		{
		    outputStream.write(binary);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		/*
		 * print log of lines were collected
		 */
		if (logText.size() > 0)
		{
			printMachineCode(0, code);
			
			try
			{
				FileWriter writer = new FileWriter( outFile+".log" );
			    for(String str: logText) 
			    {
			        writer.write(str+"\n");
			        writer.flush();
			    }
			    writer.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private boolean errorCheck() 
	{
		if (inFile.length() < 1)
		{
			return false;
		}
		
		if (outFile.length() < 1)
		{
			return false;
		}
		
		return true;
	}
	
	private void writeToFile(String msg) 
	{
		logText.add(msg);
	}
	
	private void printMachineCode(int offset, List<Integer> code) 
	{
		vlMachineOut.onPrint("-------------------------------");
		vlMachineOut.onPrint("Byte Code Start\n");
		
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
			vlMachineOut.onPrint(addr+s1+s2+s3+s4);
			it++;
		}
		
		vlMachineOut.onPrint("\nByte Code End");
		vlMachineOut.onPrint("-------------------------------");
	}
}
