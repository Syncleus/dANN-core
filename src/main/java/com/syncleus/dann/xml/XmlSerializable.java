package com.syncleus.dann.xml;

public interface XmlSerializable<T, N>
{
	/**
	 * Write stand alone element, including all instances.
	 */
	T toXml();

	/**
	 * Write nested element assuming recurring instances of components are
	 * already named.
	 */
	T toXml(Namer<N> namer);

	/**
	 * Write nested element assuming recurring instances of components are
	 * already named, use preexisting XML objects.
	 */
	void toXml(T jaxbObject, Namer<N> namer);
}
