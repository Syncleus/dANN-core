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
package com.syncleus.dann.neural;

import com.syncleus.dann.*;

/**
 * This indicates that a required synapse does not exist.
 *
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 *
 */
public class SynapseDoesNotExistDannException extends DannException
{
	private static final long serialVersionUID = 1454119896780185614L;
	
	/**
	 * Creates a blank default exception.
	 *
	 * 
	 * @since 1.0
	 */
	public SynapseDoesNotExistDannException()
	{
	}

	/**
	 * Creates an exception with a message describing the cause.
	 *
	 * 
	 * @param msg A string describing the cause of the exception
	 * @since 1.0
	 */
	public SynapseDoesNotExistDannException(final String msg)
	{
		super(msg);
	}

	/**
	 * Creates an exception with a message describing the cause as well as the
	 * throwable which caused this exception to be thrown.
	 *
	 * 
	 * @param msg A string describing the cause of the exception
	 * @param cause The throwable which caused this exception
	 * @since 1.0
	 */
	public SynapseDoesNotExistDannException(final String msg, final Throwable cause)
	{
		super(msg, cause);
	}

	/**
	 * Creates an exception containing the throwable which caused this exception
	 * to be thrown.
	 *
	 * 
	 * @param cause The throwable which caused this exception
	 * @since 1.0
	 */
	public SynapseDoesNotExistDannException(final Throwable cause)
	{
		super(cause);
	}
}
