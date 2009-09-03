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

import com.syncleus.dann.graph.BidirectedGraph;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.TreeGraph;
import com.syncleus.dann.graph.WeightedBidirectedWalk;
import java.util.*;

/**
 * Represents a single artificial brain typically belonging to a single
 * artificial organism. It will contain a set of input and output neurons which
 * corelates to a specific dataset pattern.<br/>
 * <br/>
 * This class is abstract and must be extended in order to be used.
 *
 * @author Syncleus, Inc.
 * @since 1.0
 *
 */
public interface Brain extends BidirectedGraph<Neuron, Synapse, WeightedBidirectedWalk<Neuron,Synapse>>
{
	/**
	 * Obtains all InputNeurons contained within the brain.
	 *
	 *
	 * @return An unmodifiable Set of InputNeurons.
	 * @since 1.0
	 */
    public abstract Set<InputNeuron> getInputNeurons();



	/**
	 * Obtains all OutputNeurons contained within the brain.
	 *
	 *
	 * @return An unmodifiable Set of OutputNeurons
	 * @since 1.0
	 */
    public abstract Set<OutputNeuron> getOutputNeurons();



	/**
	 * Obtains all Neurons, including InputNeurons and OutputNeurons contained
	 * within the brain.
	 *
	 *
	 * @return An unmodifiable Set of all Neurons.
	 * @since 1.0
	 */
    public abstract Set<Neuron> getNodes();

	
	//Parent methods
	List<Synapse> getOutEdges(Neuron node);
	List<Synapse> getInEdges(Neuron node);
	int getIndegree(Neuron node);
	int getOutdegree(Neuron node);

	Set<BidirectedGraph<Neuron, Synapse, WeightedBidirectedWalk<Neuron,Synapse>>> getStrongComponents();
	boolean isStronglyConnected();
	boolean isPolytree();

	List<Synapse> getEdges(Neuron node);
	List<Synapse> getTraversableEdges(Neuron node);
	int getDegree(Neuron node);
	boolean isConnected(Neuron leftNode, Neuron rightNode);
	List<Neuron> getNeighbors(Neuron node);
	List<Neuron> getTraversableNeighbors(Neuron node);

	List<Synapse> getEdges();
	boolean isConnected();
	Set<Graph<Neuron, Synapse, WeightedBidirectedWalk<Neuron,Synapse>>> getConnectedComponents();
	boolean isMaximalConnected();
	boolean isCut(Graph<? extends Neuron, ? extends Synapse, ? extends WeightedBidirectedWalk<Neuron,Synapse>> subGraph);
	boolean isCut(Graph<? extends Neuron, ? extends Synapse, ? extends WeightedBidirectedWalk<Neuron,Synapse>> subGraph, Neuron begin, Neuron end);
	int getNodeConnectivity();
	int getEdgeConnectivity();
	int getNodeConnectivity(Neuron begin, Neuron end);
	int getEdgeConnectivity(Neuron begin, Neuron end);
	boolean isCompleteGraph();
	int getOrder();
	int getCycleCount();
	boolean isPancyclic();
	int getGirth();
	int getCircumference();
	boolean isTraceable();
	boolean isSpanning(WeightedBidirectedWalk<Neuron,Synapse> walk);
	boolean isSpanning(TreeGraph graph);
	boolean isTraversable();
	boolean isEularian(WeightedBidirectedWalk<Neuron,Synapse> walk);
	boolean isTree();
	boolean isSubGraph(Graph<? extends Neuron, ? extends Synapse, ? extends WeightedBidirectedWalk<Neuron,Synapse>> graph);
	boolean isKnot(Graph<? extends Neuron, ? extends Synapse, ? extends WeightedBidirectedWalk<Neuron,Synapse>> subGraph);
	int getTotalDegree();
	boolean isMultigraph();
	boolean isIsomorphic(Graph<? extends Neuron, ? extends Synapse, ? extends WeightedBidirectedWalk<Neuron,Synapse>> isomorphicGraph);
	boolean isHomomorphic(Graph<? extends Neuron, ? extends Synapse, ? extends WeightedBidirectedWalk<Neuron,Synapse>> homomorphicGraph);
	boolean isRegular();
}
