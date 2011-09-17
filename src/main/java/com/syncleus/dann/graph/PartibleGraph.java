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

public interface PartibleGraph<
	  	N,
	  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N>>,
	  	NEP extends PartibleGraph.NodeEndpoint<N, E>,
	  	EEP extends PartibleGraph.EdgeEndpoint<N, E>
	  >  extends Graph<N,E,NEP,EEP>
{

	interface NodeEndpoint<
		  ON,
		  OE extends Cloud<ON,? extends Cloud.Endpoint<? extends ON>>
	  > extends Graph.NodeEndpoint<ON,OE>, PartibleCloud.Endpoint<ON>
	{
	};

	interface EdgeEndpoint<
		  ON,
		  OE extends Cloud<ON,? extends Cloud.Endpoint<? extends ON>>
	  > extends Graph.EdgeEndpoint<ON,OE>, PartibleCloud.Endpoint<OE>
	{
	};
}
