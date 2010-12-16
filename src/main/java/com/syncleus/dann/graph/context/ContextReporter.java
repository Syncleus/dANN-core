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
package com.syncleus.dann.graph.context;

public interface ContextReporter
{
	/**
	 * Determines if the graph has nodeContext enabled. If node context is enabled
	 * then all nodes which implement the ContextNode interface will be notified
	 * on context events (which graphs it is added or removed to as well as
	 * which edges a node is connected to). This also allows nodes to refuse
	 * to join networks or edges. When this is disabled the ContextNode interface
	 * is ignored and nodes will not be notified and they will have no control
	 * over their context.
	 *
	 * @return True if ContextNode is currently being honored on all nodes, false
	 * otherwise.
	 * @since 2.0
	 */
	boolean isContextEnabled();
}
