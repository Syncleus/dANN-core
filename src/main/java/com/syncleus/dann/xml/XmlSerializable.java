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

public interface XmlSerializable<T, N>
{
	/**
	 * Write stand alone element, including all instances.
	 *
	 * @return returns a serialized XML version of the object
	 */
	T toXml();

	/**
	 * Write nested element assuming recurring instances of components are
	 * already named.
	 *
	 * @param namer a class responsible for naming repeated occurances of the same object
	 * @return returns a serialized XML version of the object
	 */
	T toXml(Namer<N> namer);

	/**
	 * Write nested element assuming recurring instances of components are
	 * already named, use preexisting XML objects.
	 *
	 * @param jaxbObject The jaxbObject that will hold the serialization
	 * @param namer a class responsible for naming repeated occurances of the same object
	 */
	void toXml(T jaxbObject, Namer<N> namer);
}
