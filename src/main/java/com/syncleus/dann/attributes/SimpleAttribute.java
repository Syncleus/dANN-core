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
package com.syncleus.dann.attributes;

public class SimpleAttribute<A, T> implements Attribute<A, T> {
    private final A attributeId;
    private final Class<T> attributeValueType;

    public SimpleAttribute(final A attributeId, final Class<T> attributeValueType) {
        this.attributeId = attributeId;
        this.attributeValueType = attributeValueType;
    }

    @Override
    public final A getAttribute() {
        return this.attributeId;
    }

    @Override
    public final boolean isAttributeValue(final Object attributeValue) {
        return this.attributeValueType.isInstance(attributeValue);
    }

    @Override
    public final Class<T> getAttributeValueType() {
        return this.attributeValueType;
    }

    @Override
    public final boolean equals(final Object object) {
        return this.attributeId.equals(object);
    }

    @Override
    public final int hashCode() {
        return this.attributeId.hashCode();
    }
}
