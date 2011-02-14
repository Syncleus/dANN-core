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
package com.syncleus.dann.dataprocessing.signal.transform;

public interface FastFourierTransformer
{
	/**
	 * Transforms the given signal into a DiscreteFourierTransform.
	 * @param signal The signal to transform
	 * @return The DiscreteFourierTransform as a result.
	 */
	DiscreteFourierTransform transform(double[] signal);

	/**
	 * Inverts the transform. This is the inverse of transform()
	 * @param transform The transform to inverse
	 * @return The original signal.
	 * @see #transform(double[])
	 */
	double[] inverseTransform(DiscreteFourierTransform transform);

	/**
	 * Pads the double arrays to complex arrays, then performs the circular
	 * convolution.
	 * @param first The first matrix of doubles
	 * @param second The second matrix of doubles
	 * @return The circular convolution as a double[]
	 */
	double[] circularConvolve(double[] first, double[] second);

	double[] linearConvolve(double[] first, double[] second);

	/**
	 * Gets the current block size.
	 * @return The current block size.
	 */
	int getBlockSize();

	/**
	 * Sets the block size to use.
	 * @param blockSize The block size.
	 */
	void setBlockSize(int blockSize);

	/**
	 * Gets the bit-rate currently in use.
	 * @return The current bit-rate.
	 */
	int getBitrate();

	/**
	 * Sets the current bit-rate.
	 * @param bitRate The bit-rate to use.
	 */
	void setBitrate(int bitRate);
}
