/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.tests.dann.neural;

import com.syncleus.dann.neural.SynapseDoesNotExistDannException;
import org.junit.*;

public class TestSynapseDoesNotExistException
{
	@Test(expected=SynapseDoesNotExistDannException.class)
	public void testDefault() throws SynapseDoesNotExistDannException
	{
		throw new SynapseDoesNotExistDannException();
	}

	@Test(expected=SynapseDoesNotExistDannException.class)
	public void testString() throws SynapseDoesNotExistDannException
	{
		throw new SynapseDoesNotExistDannException("This is just a test");
	}

	@Test(expected=SynapseDoesNotExistDannException.class)
	public void testCause() throws SynapseDoesNotExistDannException
	{
		throw new SynapseDoesNotExistDannException(new Exception());
	}

	@Test(expected=SynapseDoesNotExistDannException.class)
	public void testStringCause() throws SynapseDoesNotExistDannException
	{
		throw new SynapseDoesNotExistDannException("This is just a test", new Exception());
	}
}
