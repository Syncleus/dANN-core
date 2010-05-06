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

import com.syncleus.dann.graph.BidirectedEdge.EndState;
import com.syncleus.dann.graph.DirectedEdge;
import java.util.List;

public interface BayesianEdge<N extends BayesianNode> extends DirectedEdge<N>
{
	Object getState();
	
	//Parent methods
	List<N> getNodes();
	N getSourceNode();
	N getDestinationNode();

	N getLeftNode();
	N getRightNode();
	EndState getLeftEndState();
	EndState getRightEndState();
	boolean isIntroverted();
	boolean isExtraverted();
	boolean isDirected();
	boolean isHalfEdge();
	boolean isLooseEdge();
	boolean isOrdinaryEdge();
	boolean isLoop();
}
