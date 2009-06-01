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
package com.syncleus.dann;


/**
 * This indicates that a required synapse does not exist.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public class SynapseDoesNotExistException extends DannException
{
	public SynapseDoesNotExistException()
	{
	}
	
	public SynapseDoesNotExistException(String msg)
	{
		super(msg);
	}

	public SynapseDoesNotExistException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

	public SynapseDoesNotExistException(Throwable cause)
	{
		super(cause);
	}
}
