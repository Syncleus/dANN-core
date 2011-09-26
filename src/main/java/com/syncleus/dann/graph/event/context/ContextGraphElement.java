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
package com.syncleus.dann.graph.event.context;

import java.util.Set;
import com.syncleus.dann.graph.CloudGraph;

public interface ContextGraphElement<
	  	GE extends CloudGraph.Endpoint<?, ?,?>,
	  	G extends CloudGraph<?, ?, ? extends GE, ? extends GE>
	  >
{
	void changingGraphContext( Set<? extends G> joiningAsNode, Set<? extends GE> joiningAsEdge, Set<?> leavingContexts) throws RejectedContextException;
	void changedGraphContext(Set<? extends GE> joinedAsNode, Set<? extends GE> joinedAsEdge, Set<?> leftContexts);
}
