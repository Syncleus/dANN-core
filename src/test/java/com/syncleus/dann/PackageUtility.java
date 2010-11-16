package com.syncleus.dann;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.*;
import org.apache.log4j.Logger;

public abstract class PackageUtility
{
	private static final Logger LOGGER = Logger.getLogger(PackageUtility.class);

	public static Class[] getClasses(final String pkgName) throws ClassNotFoundException
	{
		final ArrayList<Class> classes = new ArrayList<Class>();
		// Get a File object for the package
		File directory = null;
		String pkgPath;
		try
		{
			final ClassLoader cld = Thread.currentThread().getContextClassLoader();
			assert (cld != null);
			pkgPath = pkgName.replace('.', '/');
			final URL resource = cld.getResource(pkgPath);
			if( resource == null )
				throw new ClassNotFoundException("No resource for " + pkgPath);
			directory = new File(resource.getFile());
		}
		catch(NullPointerException x)
		{
			throw new ClassNotFoundException(pkgName + " (" + directory + ") does not appear to be a valid package");
		}
		if( directory.exists() )
		{
			// Get the list of the files contained in the package
			final String[] files = directory.list();
			for(final String file : files)
				if( file.endsWith(".class") )
					// removes the .class extension
					classes.add(Class.forName(pkgName + '.' + file.substring(0, file.length() - 6)));
			final Class[] classesA = new Class[classes.size()];
			classes.toArray(classesA);
			return classesA;
		}
		else
		{
			//first clean it up in case wer on *nix system
			String jarPath = directory.toString().replace("!/" + pkgPath, "").replace("file:", "");
			//now clean up for windows
			jarPath = jarPath.replace("!\\" + pkgPath.replace("/", "\\"), "").replace("file:", "");
			try
			{
				return PackageUtility.getClasses(jarPath, pkgName);
			}
			catch(FileNotFoundException caughtException)
			{
				LOGGER.error("Can not figure out the location of the jar: " + jarPath + ' ' + pkgName, caughtException);
				throw new DannError("Can not figure out the location of the jar: " + jarPath + ' ' + pkgName, caughtException);
			}
			catch(IOException caughtException)
			{
				LOGGER.error("IO error when opening jar: " + jarPath + ' ' + pkgName, caughtException);
				throw new DannError("IO error when opening jar: " + jarPath + ' ' + pkgName, caughtException);
			}
		}
	}

	public static Class[] getClasses(final String jarName, final String packageName) throws IOException
	{
		final ArrayList<Class> classes = new ArrayList<Class>();

		final String cleanedPackageName = packageName.replaceAll("\\.", "/");

		final JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
		JarEntry jarEntry;

		while( true )
		{
			jarEntry = jarFile.getNextJarEntry();
			if( jarEntry == null )
				break;
			if( (jarEntry.getName().startsWith(cleanedPackageName)) && (jarEntry.getName().endsWith(".class")) )
			{
				final String classFileName = jarEntry.getName().replaceAll("/", "\\.");
				try
				{
					classes.add(Class.forName(classFileName.substring(0, classFileName.length() - 6)));
				}
				catch(ClassNotFoundException caughtException)
				{
					throw new FileNotFoundException("class not found, do you have the right jar file?");
				}
			}
		}

		final Class[] classesRet = new Class[classes.size()];
		classes.toArray(classesRet);
		return classesRet;
	}
}
