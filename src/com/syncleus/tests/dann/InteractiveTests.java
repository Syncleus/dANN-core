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
import java.util.*;
import java.lang.reflect.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

public class InteractiveTests
{
	private static class ClassComparator implements Comparator<Class>
	{
		public int compare(Class first, Class second)
		{
			return first.toString().compareTo(second.toString());
		}

		@Override
		public boolean equals(Object compareWith)
		{
			if(compareWith instanceof ClassComparator)
				return true;
			return false;
		}

		@Override
		public int hashCode()
		{
			return super.hashCode();
		}
	}

	private static class MethodComparator implements Comparator<Method>
	{
		public int compare(Method first, Method second)
		{
			return first.toString().compareTo(second.toString());
		}

		@Override
		public boolean equals(Object compareWith)
		{
			if(compareWith instanceof ClassComparator)
				return true;
			return false;
		}

		@Override
		public int hashCode()
		{
			return super.hashCode();
		}
	}

	private static Map<Class, Set<Method>> getTestPoints()
	{
		final Map<Class, Set<Method>> testPoints = new TreeMap<Class, Set<Method>>(new ClassComparator());
		final Class[] classes;
		try
		{
			classes = PackageUtility.getClasses("com.syncleus.tests.dann");
		}
		catch(ClassNotFoundException caughtException)
		{
			throw new AssertionError("com.syncleus.tests.dann can not be searched!");
		}
		for(Class packageClass : classes)
		{
			final String fullClassString = packageClass.toString();
			final int classNameIndex = fullClassString.lastIndexOf(".") + 1;
			final String classString = fullClassString.substring(classNameIndex);
			if((!classString.contains("$")) && (classString.startsWith("Test")) )
			{
				//check to make sure there is a default constructor
				boolean hasDefaultContructor = false;
				final Constructor[] constructors = packageClass.getConstructors();
				for(Constructor contructor : constructors)
					if(contructor.getTypeParameters().length == 0)
						hasDefaultContructor = true;
				assert hasDefaultContructor;

				//find test point methods
				final Set<Method> testMethods = new TreeSet<Method>(new MethodComparator());
				testPoints.put(packageClass, testMethods);
				final Method[] packageClassMethods = packageClass.getDeclaredMethods();
				for(Method packageClassMethod : packageClassMethods)
				{
					if(packageClassMethod.getName().startsWith("test"))
						testMethods.add(packageClassMethod);
				}
			}
		}

		return testPoints;
	}

	private static Method selectTest(final Map<Class, Set<Method>> testPoints) throws Exception
	{
		final Set<Class> testClasses = testPoints.keySet();
		int currentChoice = 1;
		final Map<Integer, Method> choices = new HashMap<Integer, Method>();
		for(Class testClass : testClasses)
		{
			System.out.println(testClass.toString() + ":");
			final Set<Method> testPointMethods = testPoints.get(testClass);
			for(Method testPointMethod : testPointMethods)
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

		if( (selection == null) || (selection <= 0) || (selection >= currentChoice) || (!choices.containsKey(selection)) )
		{
			System.out.println("invalid selection");
			System.exit(1);
		}
		return choices.get(selection);
	}

	public static void main(String[] args)
	{
		try
		{
			final Map<Class, Set<Method>> tests = InteractiveTests.getTestPoints();
			final Method test = InteractiveTests.selectTest(tests);
			final Class testClass = test.getDeclaringClass();

			//run unit test
			final JUnitCore jUnit = new JUnitCore();
			final Request testRequest = Request.method(testClass, test.getName());
			System.out.println("Running " + testClass + "." + test.getName());
			Result testResult = jUnit.run(testRequest);
			if( testResult.wasSuccessful() )
				System.out.print("Successful: ");
			else
				System.out.print("Failure: ");
			System.out.println(((double)testResult.getRunTime())/1000.0 + " sec");
		}
		catch(Throwable caughtException)
		{
			caughtException.printStackTrace();
			System.exit(0);
		}
	}
}
