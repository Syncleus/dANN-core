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
package com.syncleus.dann.xml;

import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Namer<K>
{
	private final AtomicLong currentNodeName = new AtomicLong(0);
	private final WeakHashMap<K, Long> nodeNames = new WeakHashMap<K, Long>();

	public String getNameOrCreate(final K node)
	{
		Long nodeName = nodeNames.get(node);

		if (nodeName == null)
		{
			nodeName = currentNodeName.incrementAndGet();
			nodeNames.put(node, nodeName);
		}

		return nodeName.toString();
	}

	public String getName(final K node)
	{
		return nodeNames.get(node).toString();
	}

	public boolean hasName(final K node)
	{
		return nodeNames.containsKey(node);
	}

	public void reset()
	{
		this.nodeNames.clear();
		this.currentNodeName.set(0);
	}
}
