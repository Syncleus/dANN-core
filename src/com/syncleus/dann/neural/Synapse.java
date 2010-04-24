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

import com.syncleus.dann.graph.BidirectedEdge.EndState;
import com.syncleus.dann.graph.WeightedDirectedEdge;
import java.util.List;

public interface Synapse extends WeightedDirectedEdge<Neuron>
{
	double getInput();
	void setInput(double input);
	void setWeight(double weight);
	
	//Parent methods
	double getWeight();

	Neuron getSourceNode();
	Neuron getDestinationNode();

	List<Neuron> getNodes();

	Neuron getLeftNode();
	Neuron getRightNode();
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
