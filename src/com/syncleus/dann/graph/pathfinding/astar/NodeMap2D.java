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
package com.syncleus.dann.graph.pathfinding.astar;

import java.awt.Point;

public class NodeMap2D extends NodeMap
{
	private Node[][] grid = new Node[10][10];
	
	private Node[] map = new Node[100];
	private Node start = null;
	private Node goal = null;
	
	public NodeMap2D(Point startCoord, Point goalCoord)
	{
		//populate the map
		for( int lcv = 0; lcv < map.length; lcv++)
		{
			map[lcv] = new Node();
		}
		
		//connect the map and populate the grid
		for( int x = 0; x < 10; x++ )
		{
			for( int y = 0; y < 10; y++ )
			{
				grid[x][y] = map[gridToMap(x,y)];
				
				if( (x == startCoord.getX())&&(y == startCoord.getY()) )
					this.start = grid[x][y];
				
				if( (x == goalCoord.getX())&&(y == goalCoord.getY()) )
					this.goal = grid[x][y];
				
				//connect to the north
				if( y < 9 )
					grid[x][y].connect(map[gridToMap(x,y + 1)]);
				//connect to the south
				if( y > 0 )
					grid[x][y].connect(map[gridToMap(x,y - 1)]);
				//connect to the west
				if( x > 0 )
					grid[x][y].connect(map[gridToMap(x - 1,y)]);
				//connect to the east
				if( x < 9 )
					grid[x][y].connect(map[gridToMap(x + 1,y)]);
			}
		}
	}
	
	public Node getNode(int x, int y)
	{
		return this.grid[x][y];
	}
	
	public Point getCoords(Node node)
	{
		for( int x = 0; x < 10; x++ )
		{
			for( int y = 0; y < 10; y++ )
			{
				if( this.grid[x][y] == node )
					return new Point(x, y);
			}
		}
		
		return null;
	}
	
	private static int gridToMap(int x, int y)
	{
		return (x*10) + y;
	}
	
	protected Node[] getMap()
	{
		return this.map;
	}
	
	protected Node getStart()
	{
		return this.start;
	}
	
	protected Node getGoal()
	{
		return this.goal;
	}
}
