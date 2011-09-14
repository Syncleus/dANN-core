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
package com.syncleus.dann.graph.context;

//This class is a runtime class to encapsulate the RejectedContextException. This allows the context to be optional
//without having actions blocked by context events needing to explicitly handle exceptions (as you do with non-runtime
//exceptions)
public class InvalidContextException extends RuntimeException
{
	private static final long serialVersionUID = 1385401237548091754L;

	public InvalidContextException(final String msg, final RejectedContextException cause)
	{
		super(msg, cause);
	}

	public InvalidContextException(final RejectedContextException cause)
	{
		super(cause);
	}
}
