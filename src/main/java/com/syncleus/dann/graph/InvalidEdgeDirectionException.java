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
package com.syncleus.dann.graph;

import org.omg.CORBA.DynAnyPackage.Invalid;

public class InvalidEdgeDirectionException extends InvalidEdgeException
{
	private static final long serialVersionUID = 10247021935792856L;

	public InvalidEdgeDirectionException()
	{
		super();
	}

	public InvalidEdgeDirectionException(final String msg)
	{
		super(msg);
	}

	public InvalidEdgeDirectionException(final String msg, final Throwable cause)
	{
		super(msg, cause);
	}

	public InvalidEdgeDirectionException(final Throwable cause)
	{
		super(cause);
	}
}
