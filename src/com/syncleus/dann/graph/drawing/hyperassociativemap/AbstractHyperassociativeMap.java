/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graph.drawing.hyperassociativemap;

import java.util.*;
import java.util.concurrent.*;
import com.syncleus.dann.math.Vector;
import java.io.Serializable;
import org.apache.log4j.Logger;
import com.syncleus.dann.DannRuntimeException;
import com.syncleus.dann.InterruptedDannRuntimeException;
import com.syncleus.dann.UnexpectedDannError;


/**
 * Represents a collection of interconnected hyperassociative map nodes.
 *
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 *
 */
public abstract class AbstractHyperassociativeMap implements Serializable
{
	/**
	 * HashSet of all the nodes in this map.
	 *
	 * @since 1.0
	 */
    protected HashSet<HyperassociativeNode> nodes = new HashSet<HyperassociativeNode>();

	private int dimensions;
	private ThreadPoolExecutor threadExecutor;
	private final static Logger LOGGER = Logger.getLogger(AbstractHyperassociativeMap.class);

	private static class Align implements Callable<Vector>
	{
		private HyperassociativeNode node;
		private final static Logger LOGGER = Logger.getLogger(Align.class);

		public Align(HyperassociativeNode node)
		{
			this.node = node;
		}

		public Vector call()
		{
			try
			{
				this.node.align();
				return this.node.getLocation();
			}
			catch(Exception caught)
			{
				LOGGER.error("Throwable was caught by Align", caught);
				throw new DannRuntimeException("Throwable was caught by Align", caught);
			}
			catch(Error caught)
			{
				LOGGER.error("Throwable was caught by Align", caught);
				throw new Error("Throwable was caught by Align", caught);
			}
		}
	}

	/**
	 * Initializes a HyperassociativeMap of the specified dimensions.
	 *
	 *
	 * @param dimensions The number of dimensions for this map.
	 * @param threadExecutor the threadExecutor used to manage threads.
	 * @since 1.0
	 */
	public AbstractHyperassociativeMap(int dimensions, ThreadPoolExecutor threadExecutor)
	{
		this.dimensions = dimensions;
		this.threadExecutor = threadExecutor;
	}

	/**
	 * Initializes a HyperassociativeMap of the specified dimensions.
	 *
	 *
	 * @param dimensions The number of dimensions for this map.
	 * @since 1.0
	 */
	public AbstractHyperassociativeMap(int dimensions)
	{
		this.dimensions = dimensions;
		this.threadExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors()*5, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

	/**
	 * Gets the number of dimensions for this map.
	 *
	 *
	 * @return The number of dimensions for this map.
	 * @since 1.0
	 */
	public final int getDimensions()
	{
		return this.dimensions;
	}

	/**
	 * Gets all the nodes contained within this map.
	 *
	 *
	 * @return An unmodifiable Set of all the nodes in this map.
	 * @since 1.0
	 */
    public final Set<HyperassociativeNode> getNodes()
    {
        return Collections.unmodifiableSet(this.nodes);
    }

	private List<Future<Vector>> submitFutureAligns()
	{
		final ArrayList<Future<Vector>> futures = new ArrayList<Future<Vector>>();
		for(HyperassociativeNode node : this.nodes)
			futures.add(this.threadExecutor.submit(new Align(node)));
		return futures;
	}

	private Vector waitAndProcessFutures(final List<Future<Vector>> futures)
	{
		//wait for all nodes to finish aligning and calculate new center point
		Vector pointSum = new Vector(this.dimensions);
		try
		{
			for(Future<Vector> future : futures)
			{
				Vector newPoint = future.get();
				for(int dimensionIndex = 1; dimensionIndex <= this.dimensions; dimensionIndex++)
					pointSum = pointSum.setCoordinate(pointSum.getCoordinate(dimensionIndex) + newPoint.getCoordinate(dimensionIndex), dimensionIndex);
			}
		}
		catch(InterruptedException caught)
		{
			LOGGER.error("Align was unexpectidy interupted", caught);
			throw new InterruptedDannRuntimeException("Unexpected interuption. Get should block indefinately", caught);
		}
		catch(ExecutionException caught)
		{
			LOGGER.error("Align had an unexcepted problem executing.", caught);
			throw new UnexpectedDannError("Unexpected execution exception. Get should block indefinately", caught);
		}

		return pointSum;
	}

	private void recenterNodes(final Vector center)
	{
		for(HyperassociativeNode node : this.nodes)
			node.recenter(center);
	}

	/**
	 * Aligns all the nodes in this map by a single step.
	 *
	 *
	 * @since 1.0
	 */
    public void align()
    {
		//align all nodes in parallel
		final List<Future<Vector>> futures = this.submitFutureAligns();

		//wait for all nodes to finish aligning and calculate new sum of all the points
		Vector center = this.waitAndProcessFutures(futures);

		//divide each coordinate of the sum of all the points by the number of
		//nodes in order to calulate the average point, or cente rof all the
		//points
		for(int dimensionIndex = 1; dimensionIndex <= this.dimensions; dimensionIndex++)
			center = center.setCoordinate(center.getCoordinate(dimensionIndex)/((double)this.nodes.size()),dimensionIndex);

		this.recenterNodes(center);
    }
}
