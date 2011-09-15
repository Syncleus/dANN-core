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

public interface AssignableGraph<
	  	N,
	  	E extends Edge<N,? extends Edge.Endpoint<N>>,
	  	NEP extends AssignableGraph.NodeEndpoint<N, E>,
	  	EEP extends AssignableGraph.EdgeEndpoint<N, E>
	  >  extends Graph<N,E,NEP,EEP>, MutableEdge<Object,Graph.Endpoint<?,?,?>>
//public interface AssignableGraph<P extends Edge<N>, N extends P, E extends P> extends Graph<N,E>, Edge<P>, MutableEdge<P>
//public interface AssignableGraph<P extends AssignableGraph.Foo, N extends P, E extends Edge<N> & AssignableGraph.Foo> extends Graph<N,E>, Edge<P>, MutableEdge<P>
{
/*
	@Override
	Set<? extends Graph.EdgeEndpoint<? extends Foo, N, E, E>> getEdgeEndpoints();

	void test()
	{
		Graph.EdgeEndpoint<? extends Object, N, E, E> test = ((Graph<N,E>)this).getEdgeEndpoints().iterator().next();
		Graph.EdgeEndpoint<? extends Foo, N, E, E> test2 = this.getEdgeEndpoints().iterator().next();
	}

	interface Foo
	{
	}
*/


	interface NodeEndpoint<
		  ON,
		  OE extends Edge<ON,? extends Edge.Endpoint<ON>>
	  > extends Graph.NodeEndpoint<ON,OE>, MutableEdge.Endpoint<ON>
	{
		void setTarget(ON newTarget) throws InvalidGraphException;
	};

	interface EdgeEndpoint<
		  ON,
		  OE extends Edge<ON,? extends Edge.Endpoint<ON>>
	  > extends Graph.EdgeEndpoint<ON,OE>, MutableEdge.Endpoint<OE>
	{
		void setTarget(OE newTarget) throws InvalidGraphException;
	};

/*
	interface Endpoint<PN, EN extends PN, NN extends PN, MN extends PN> extends Graph.Endpoint<MN,EN,NN,PN>, MutableEdge.Endpoint<PN, MN>
	{
	};
*/
}
