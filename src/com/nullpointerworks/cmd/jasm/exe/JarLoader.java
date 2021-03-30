package com.nullpointerworks.cmd.jasm.exe;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarLoader 
{
	private List<Class<?>> loadedClasses;
	
	public JarLoader()
	{
		loadedClasses = new ArrayList<Class<?>>();
	}
	
	/**
	 * returns the first instance of the given class
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public Class<?> findBySuperClass(Class<?> clazz)
	{
		for (Class<?> cl : loadedClasses)
		{
			if (cl.getSuperclass() == clazz)
			{
				return cl;
			}
		}
		return null;
	}
	
	public void loadJarClasses(String jarPath)
	{
		try
		{
			loadJar(jarPath, loadedClasses);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void loadJar(String jarPath, List<Class<?>> classes) throws IOException
	{
		JarFile jarFile = new JarFile(jarPath);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + jarPath+"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		
		while (e.hasMoreElements()) 
		{
		    JarEntry je = e.nextElement();
		    
		    if(je.isDirectory()) continue; // skip directories
		    if(!je.getName().endsWith(".class")) continue; // skip everything thats not a .class
		    if(je.getName().contains("-info")) continue; // skip module and package info files
		    
		    String className = je.getName().substring(0,je.getName().length()-6); // remove '.class'
		    className = className.replace('/', '.');
		    
			try
			{
				Class<?> c = cl.loadClass(className);
			    classes.add(c);
			} 
			catch (ClassNotFoundException e1) 
			{
				e1.printStackTrace();
			}
		}
		
		jarFile.close();
	}
}
