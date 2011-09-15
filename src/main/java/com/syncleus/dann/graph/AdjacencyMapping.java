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

import java.util.Map;
import java.util.Set;

public interface AdjacencyMapping<LK,RK> extends Set<AdjacencyMapping.Adjacency<LK,RK>>
{
	interface Adjacency<LK,RK>
	{
		public LK getLeftKey();
		public RK getRightKey();
	}

	boolean removeLeftKey(Object leftKey);
	boolean removeRightKey(Object rightKey);
	boolean remove(Object leftKey, Object rightKey);
	boolean putLeftKey(LK leftKey);
	boolean putRightKey(RK rightKey);
	boolean put(LK leftKey, RK rightKey);
	boolean contains(Object leftKey, Object rightKey);
	Set<RK> getRightKeys();
	Set<LK> getLeftKeys();
	Set<RK> getLeftAdjacency(Object leftKey);
	Set<LK> getRightAdjacency(Object rightKey);
}
