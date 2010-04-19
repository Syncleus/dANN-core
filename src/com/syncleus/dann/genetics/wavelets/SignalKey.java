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
package com.syncleus.dann.genetics.wavelets;

import java.util.Map;

public class SignalKey extends AbstractKey implements Cloneable
{
	public SignalKey()
	{
		super();
	}
	
	public SignalKey(AbstractKey copy)
	{
		super(copy);
	}

	public SignalKey(Map<Integer, Boolean> points)
	{
		super(points);
	}

	public SignalKey(String keyString)
	{
		super(keyString);
	}

	@Override
	public SignalKey clone()
	{
		return (SignalKey) super.clone();
	}

	@Override
	public SignalKey mutate(double deviation)
	{
		return (SignalKey) super.mutate(deviation);
	}
}
