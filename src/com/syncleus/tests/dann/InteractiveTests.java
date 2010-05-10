/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.tests.dann;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import org.apache.log4j.*;
import org.junit.runner.*;

public class InteractiveTests
{
	private static final Class<? extends Annotation> TEST_ANNOTATION = org.junit.Test.class;
	private static final Logger LOGGER = Logger.getLogger(InteractiveTests.class);

	private static class ClassComparator implements Comparator<Class>, Serializable
	{
		private static final long serialVersionUID = -5688218293882769266L;

		public int compare(final Class first, final Class second)
		{
			return first.toString().compareTo(second.toString());
		}

		@Override
		public boolean equals(final Object compareWith)
		{
			return compareWith instanceof com.syncleus.tests.dann.InteractiveTests.ClassComparator;
		}

		@Override
		public int hashCode()
		{
			return super.hashCode();
		}
	}

	private static class MethodComparator implements Comparator<Method>, Serializable
	{
		private static final long serialVersionUID = 493385418023700863L;

		public int compare(final Method first, final Method second)
		{
			return first.toString().compareTo(second.toString());
		}

		@Override
		public boolean equals(final Object compareWith)
		{
			return compareWith instanceof com.syncleus.tests.dann.InteractiveTests.ClassComparator;
		}

		@Override
		public int hashCode()
		{
			return super.hashCode();
		}
	}

	private static boolean isTestClass(final Class testClass)
	{
		for(final Method currentMethod : testClass.getDeclaredMethods())
			if (currentMethod.isAnnotationPresent(TEST_ANNOTATION))
				return true;
		return false;
	}

	private static Map<Class, Set<Method>> getTestPoints()
	{
		final Map<Class, Set<Method>> testPoints = new TreeMap<Class, Set<Method>>(new ClassComparator());
		final Class[] classes;
		try
		{
			classes = PackageUtility.getClasses("com.syncleus.tests.dann");
		}
		catch (ClassNotFoundException caughtException)
		{
			throw new AssertionError("com.syncleus.tests.dann can not be searched!");
		}
		for(final Class packageClass : classes)
		{
			final String fullClassString = packageClass.toString();
			final int classNameIndex = fullClassString.lastIndexOf('.') + 1;
			final String classString = fullClassString.substring(classNameIndex);
			if ((!classString.contains("$")) && (InteractiveTests.isTestClass(packageClass)))
			{
				//check to make sure there is a default constructor
				boolean hasDefaultContructor = false;
				final Constructor[] constructors = packageClass.getConstructors();
				for(final Constructor contructor : constructors)
					if (contructor.getTypeParameters().length == 0)
						hasDefaultContructor = true;
				assert hasDefaultContructor;
				//find test point methods
				final Set<Method> testMethods = new TreeSet<Method>(new MethodComparator());
				testPoints.put(packageClass, testMethods);
				final Method[] packageClassMethods = packageClass.getDeclaredMethods();
				for(final Method packageClassMethod : packageClassMethods)
				{
					if (packageClassMethod.isAnnotationPresent(TEST_ANNOTATION))
						testMethods.add(packageClassMethod);
				}
			}
		}
		return testPoints;
	}

	private static Method selectTest(final Map<Class, Set<Method>> testPoints) throws Exception
	{
		int currentChoice = 1;
		final Map<Integer, Method> choices = new HashMap<Integer, Method>();
		for(final Map.Entry<Class, Set<Method>> classSetEntry : testPoints.entrySet())
		{
			System.out.println(classSetEntry.getKey().toString() + ':');
			final Set<Method> testPointMethods = classSetEntry.getValue();
			for(final Method testPointMethod : testPointMethods)
			{
				System.out.println(currentChoice + ": " + testPointMethod.getName());
				choices.put(currentChoice, testPointMethod);
				currentChoice++;
			}
		}
		System.out.println();
		System.out.println("Input Test Number: ");
		final BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		final Integer selection = Integer.valueOf(inReader.readLine());
		if ((selection == null) || (selection <= 0) || (selection >= currentChoice) || (!choices.containsKey(selection)))
		{
			System.out.println("invalid selection");
			System.exit(1);
		}
		return choices.get(selection);
	}

	public static void main(final String[] args)
	{
		try
		{
			final Properties logProperties = new Properties();
			logProperties.setProperty("log4j.rootLogger", "all,console");
			logProperties.setProperty("log4j.appender.console", "org.apache.log4j.ConsoleAppender");
			logProperties.setProperty("log4j.appender.console.Target", "System.out");
			logProperties.setProperty("log4j.appender.console.layout", "org.apache.log4j.PatternLayout");
			logProperties.setProperty("log4j.appender.console.Threshold", "info");
			PropertyConfigurator.configure(logProperties);
			final Map<Class, Set<Method>> tests = InteractiveTests.getTestPoints();
			final Method test = InteractiveTests.selectTest(tests);
			final Class testClass = test.getDeclaringClass();
			//run unit test
			final JUnitCore jUnit = new JUnitCore();
			final Request testRequest = Request.method(testClass, test.getName());
			System.out.println("Running " + testClass + '.' + test.getName());
			final Result testResult = jUnit.run(testRequest);
			if (testResult.wasSuccessful())
				System.out.print("Successful: ");
			else
				System.out.print("Failure: ");
			System.out.println(((double) testResult.getRunTime()) / 1000.0 + " sec");
		}
		catch (Throwable caughtException)
		{
			caughtException.printStackTrace();
			System.exit(0);
		}
	}
}
