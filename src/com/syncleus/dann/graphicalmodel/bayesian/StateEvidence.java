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
package com.syncleus.dann.graphicalmodel.bayesian;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StateEvidence extends HashMap<Enum,Integer>
{
	private long totalEvidence;

	public long getTotalEvidence()
	{
		return this.totalEvidence;
	}

	public double getPercentage(Enum key)
	{
		return this.get(key).doubleValue() / ((double)this.totalEvidence);
	}

	@Override
	public Integer put(Enum key, Integer value)
	{
		Integer old = super.put(key,value);
		if(old != null)
			this.totalEvidence -= old;
		this.totalEvidence += value;

		return old;
	}

	@Override
	public void putAll(Map<? extends Enum,? extends Integer> map)
	{
		Map<Enum,Integer> oldMap = new HashMap<Enum,Integer>(this);
		super.putAll(map);

		for(Entry<? extends Enum,? extends Integer> entry : map.entrySet())
		{
			Integer oldEvidence = oldMap.get(entry.getKey());
			Integer newEvidence = this.get(entry.getKey());
			this.totalEvidence = (this.totalEvidence - oldEvidence) + newEvidence;
		}
	}
}
