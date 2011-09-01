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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;

public class FastFourierTransformerInputStream extends InputStream
{
	private final ObjectInputStream srcStream;
	private final FastFourierTransformer transformer;
	private int interval;
	private double[] buffer;

	public FastFourierTransformerInputStream(final InputStream inputStream, final FastFourierTransformer ourTransformer, final int ourInterval) throws IOException
	{
		if( inputStream == null )
			throw new IllegalArgumentException("inputStream can not be null");
		if( ourTransformer == null )
			throw new IllegalArgumentException("ourTransformer can not be null");
		if( ourInterval <= 0 )
			throw new IllegalArgumentException("ourInterval must be greater than 0");

		if( inputStream instanceof ObjectInputStream )
			this.srcStream = (ObjectInputStream) inputStream;
		else
			this.srcStream = new ObjectInputStream(inputStream);

		this.transformer = ourTransformer;
		this.interval = ourInterval;
	}

	public int transformsAvailable() throws IOException
	{
		final int numPerDouble = 8;
		final int doublesAvailable = (this.available() / numPerDouble)
				+ ((this.buffer == null) ? 0 : this.buffer.length);
		return doublesAvailable / this.transformer.getBlockSize();
	}

	public int readTransform(final DiscreteFourierTransform[] b, final int off, final int len) throws IOException
	{
		if( b == null )
			throw new IllegalArgumentException("b must not be null");
		if( off > b.length )
			throw new IllegalArgumentException("off is greater than length of b");
		if( off < 0 )
			throw new IllegalArgumentException("off must be greater than or equal to 0");
		if( len < 0 )
			throw new IllegalArgumentException("len must be greater than or equal to 0");
		if( (off + len) > b.length )
			throw new IllegalArgumentException("(off + len) is greater than length of b");

		if( len == 0 )
			return 0;

		if( b.length == 0 )
			return 0;

		if( this.transformsAvailable() <= 1 )
		{
			b[off] = this.readTransform();
			return 1;
		}
		else
		{
			int bufferSpace = b.length - off;
			if( bufferSpace > len )
				bufferSpace = len;
			final int transformCount = (bufferSpace < this.transformsAvailable() ? bufferSpace : this.transformsAvailable());
			for(int bIndex = off; bIndex < (transformCount + off); bIndex++)
				b[bIndex + off] = this.readTransform();
			return transformCount;
		}
	}

	public int readTransform(final DiscreteFourierTransform[] b) throws IOException
	{
		if( b == null )
			throw new IllegalArgumentException("b must not be null");

		if( b.length == 0 )
			return 0;

		if( this.transformsAvailable() <= 1 )
		{
			b[0] = this.readTransform();
			return 1;
		}
		else
		{
			final int transformCount = (b.length < this.transformsAvailable() ? b.length : this.transformsAvailable());
			for(int bIndex = 0; bIndex < transformCount; bIndex++)
				b[bIndex] = this.readTransform();
			return transformCount;
		}
	}

	public DiscreteFourierTransform readTransform() throws IOException
	{

		//make sure the buffer contains atleast one block, if not fill it. copy
		//block to signal
		final double[] signal;
		if( this.buffer == null )
			signal = new double[this.transformer.getBlockSize()];
		else
			signal = Arrays.copyOf(this.buffer, this.transformer.getBlockSize());

		if( (this.buffer == null) || (this.buffer.length <= this.transformer.getBlockSize()) )
		{
			//signal = Arrays.copyOf(this.buffer, this.transformer.getBlockSize());
			for(int signalIndex = (this.buffer == null ? 0 : this.buffer.length); signalIndex < signal.length; signalIndex++)
				signal[signalIndex] = this.srcStream.readDouble();
			this.buffer = signal;
		}

		//remove the time interval from the begining of the buffer and stream.
		int signalsToRemove = this.interval;
		if( this.buffer != null )
		{
			if( this.buffer.length <= signalsToRemove )
			{
				signalsToRemove -= this.buffer.length;
				this.buffer = null;
			}
			else
			{
				this.buffer = Arrays.copyOfRange(this.buffer, signalsToRemove, this.buffer.length);
				signalsToRemove = 0;
			}
		}

		if( signalsToRemove > 0 )
			assert this.srcStream.skip(signalsToRemove) == signalsToRemove;

		return this.transformer.transform(signal);
	}

	public int getBlockSize()
	{
		return this.transformer.getBlockSize();
	}

	public int getBitrate()
	{
		return this.transformer.getBitRate();
	}

	public int getInterval()
	{
		return this.interval;
	}

	public void setInterval(final int newInterval)
	{
		this.interval = newInterval;
	}

	@Override
	public int available() throws IOException
	{
		return this.srcStream.available();
	}

	@Override
	public void mark(final int readlimit)
	{
		this.srcStream.mark(readlimit);
	}

	@Override
	public boolean markSupported()
	{
		return this.srcStream.markSupported();
	}

	@Override
	public int read() throws IOException
	{
		return this.srcStream.read();
	}

	@Override
	public int read(final byte[] b) throws IOException
	{
		return this.srcStream.read(b);
	}

	@Override
	public int read(final byte[] b, final int off, final int len) throws IOException
	{
		return this.srcStream.read(b, off, len);
	}

	@Override
	public void close() throws IOException
	{
		this.srcStream.close();
	}

	@Override
	public void reset() throws IOException
	{
		this.srcStream.reset();
	}

	@Override
	public long skip(final long numBytes) throws IOException
	{
		return this.srcStream.skip(numBytes);
	}
}
