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
package com.syncleus.dann;

/**
 * All dANN specific runtime exceptions that are thrown will either be a
 * DannRuntimeException or inherit from it.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 *
 */
public class DannRuntimeException extends RuntimeException
{
	/**
	 * Creates a blank default exception.
	 *
	 *
	 * @since 2.0
	 */
	public DannRuntimeException()
	{
	}

	/**
	 * Creates an exception with a message describing the cause.
	 *
	 *
	 * @param msg A string describing the cause of the exception
	 * @since 2.0
	 */
	public DannRuntimeException(String msg)
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
	 * @since 2.0
	 */
	public DannRuntimeException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

	/**
	 * Creates an exception containing the throwable which caused this exception
	 * to be thrown.
	 *
	 *
	 * @param cause The throwable which caused this exception
	 * @since 2.0
	 */
	public DannRuntimeException(Throwable cause)
	{
		super(cause);
	}
}
