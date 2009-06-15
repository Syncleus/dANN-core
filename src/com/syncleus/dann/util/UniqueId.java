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
package com.syncleus.dann.util;


import java.util.Random;
import java.io.Serializable;

public class UniqueId implements Comparable<UniqueId>, Serializable
{
    private static final Random rand = new Random();
    private byte[] uniqueData = null;



    public UniqueId(int size)
    {
        this.uniqueData = new byte[size];
        rand.nextBytes(this.uniqueData);
    }



    public int hashCode()
    {
        int dataIndex = 0;
        int hash = 0;
        while(dataIndex < this.uniqueData.length)
        {
            int intBlock = 0;
            for(int blockIndex = 0;blockIndex < 4;blockIndex++)
            {
                if(dataIndex < this.uniqueData.length)
                    intBlock += this.uniqueData[dataIndex] << blockIndex;
                else
                    break;

                dataIndex++;
            }

            hash ^= intBlock;
        }

        return hash;
    }



    public int compareTo(UniqueId compareWith)
    {
        return this.toString().compareTo(compareWith.toString());
    }



    public boolean equals(Object compareWithObj)
    {
        if(!(compareWithObj instanceof UniqueId))
            return false;

        UniqueId compareWith = (UniqueId)compareWithObj;

        if(this.compareTo(compareWith) != 0)
            return false;

        return true;
    }



    public String toString()
    {
		StringBuffer dataBuffer = new StringBuffer();
        for(int dataIndex = (this.uniqueData.length - 1);dataIndex >= 0;dataIndex--)
        {
            long currentData = convertByteToUnsignedLong(this.uniqueData[dataIndex]);
            String newHex = Long.toHexString(currentData);
            while(newHex.length() < 2)
                newHex = "0" + newHex;
            dataBuffer.append(newHex);
        }

        return dataBuffer.toString().toUpperCase();
    }

    /*
     * A better solution to the convert* methods needs to be found.
     */


    private static long convertByteToUnsignedLong(byte signedByte)
    {
        long unsignedLong = signedByte;
        if(unsignedLong < 0)
            unsignedLong += 256;

        return unsignedLong;
    }
}
