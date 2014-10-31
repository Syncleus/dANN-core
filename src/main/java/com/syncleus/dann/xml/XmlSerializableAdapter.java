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
package com.syncleus.dann.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlSerializableAdapter<X, V extends XmlSerializable<X, ?>> extends XmlAdapter<X, V> {
    @Override
    public final X marshal(final V graph) {
        return graph.toXml();
    }

    @Override
    public V unmarshal(final X graphXml) {
        throw new IllegalStateException("You can not unmarshal XML through this adapter");
    }
}
