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

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTreeEdge extends AbstractBidirectedEdge implements TreeEdge
{
	private final NodePair<TreeNode> treeNodePair;

	protected AbstractTreeEdge(TreeNode leftNode, EndState leftEndState, TreeNode rightNode, EndState rightEndState)
	{
		super(leftNode, leftEndState, rightNode, rightEndState);
		this.treeNodePair = new NodePair<TreeNode>(leftNode, rightNode);
	}

	public final NodePair<TreeNode> getTreeNodePair()
	{
		return this.treeNodePair;
	}

	public boolean isLeaf()
	{
		if(this.getTreeNodePair().getLeftNode().isLeaf())
			return true;
		if(this.getTreeNodePair().getRightNode().isLeaf())
			return true;

		return false;
	}
}
