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
package com.syncleus.dann.genetics;

import com.syncleus.dann.UnexpectedDannError;
import org.apache.log4j.Logger;

/**
 * Represents a Gene which can mutate and expresses constant activity. The
 * activity of a ValueGene remains constant and only changes through mutation.
 * These are the only type of gene used in basic Genetic Algorithms.
 *
 * @param <N> The MutableNumber type used to back the value of this gene.
 * @author Syncleus, Inc.
 * @since 2.0
 *
 */
public abstract class AbstractValueGene<N extends MutableNumber> implements Gene, Cloneable
{
	private N value;
	private final static Logger LOGGER = Logger.getLogger(AbstractValueGene.class);

	/**
	 * Initializes a new instance of this class backed by the specified
	 * MutableNumber.
	 *
	 * @param value MutableNumber to back the ValueGene with.
	 * @since 2.0
	 */
	protected AbstractValueGene(N value)
	{
		this.value = value;
	}

	/**
	 * Initializes a new instance of this class that is a copy of the specified
	 * object.
	 *
	 * @param copyValueGene ValueGene to copy.
	 * @since 2.0
	 */
	protected AbstractValueGene(AbstractValueGene<N> copyValueGene)
	{
		this.value = copyValueGene.value;
	}

	/**
	 * Get the MutableNumber backing this object.
	 *
	 * @return The number backing this object.
	 * @since 2.0
	 */
	public final N getValue()
	{
		return this.value;
	}

	/**
	 * The current expression activity. This will be a constant value equal to
	 * the doubleValue of the backing number. This value will only change
	 * through mutation.
	 *
	 * @return The current expression activity.
	 * @since 2.0
	 */
	public final double expressionActivity()
	{
		return this.getValue().doubleValue();
	}

	/**
	 * Mutates this gene internally without copying first. This is used by
	 * children to recursivly mutate the gene.
	 *
	 * @param deviation deviation to use during mutation. A higher value
	 * represents more severe mutations. A deviation of 0 causes no mutation.
	 * @since 2.0
	 */
	@SuppressWarnings("unchecked")
	protected void internalMutate(double deviation)
	{
		this.value = (N) this.value.mutate(deviation);
	}

	/**
	 * All children of this class should override this method and return
	 * their own class type even if it is abstract. It should return a copy
	 * without any mutation.
	 *
	 * @return an exact copy of this object.
	 * @since 2.0
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AbstractValueGene clone()
	{
		try
		{
			AbstractValueGene copy = (AbstractValueGene) super.clone();
			copy.value = (N) this.value;
			return copy;
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
			throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
		}
	}

	/**
	 * This will make a copy of the object and mutate it. The mutation has
	 * a normal distribution multiplied by the deviation.
	 *
	 * @param deviation A double indicating how extreme the mutation will be.
	 * The greater the deviation the more drastically the object will mutate.
	 * A deviation of 0 should cause no mutation.
	 * @return A copy of the current object with potential mutations.
	 * @since 2.0
	 */
	public abstract AbstractValueGene<N> mutate(double deviation);
}
