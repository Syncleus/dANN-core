package com.syncleus.dann.xml;

public interface XmlSerializable<T, N>
{
	/**
	 * Write stand alone element, including all instances.
	 */
	public T toXml();

	/**
	 * Write nested element assuming recurring instances of components are
	 * already named.
	 */
	public T toXml(Namer<N> namer);

	/**
	 * Write nested element assuming recurring instances of components are
	 * already named, use preexisting XML objects.
	 */
	public void toXml(T jaxbObject, Namer<N> namer);
}
