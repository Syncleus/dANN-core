package com.syncleus.dann.xml;

import javax.xml.bind.JAXBElement;

public interface XmlSerializable<T>
{
    /** Write stand alone element, including all instances **/
    public T toXml();
    /** Write nested element assuming recurring instances of components are already named **/
    public T toXml(Namer namer);
    /**Write nested element assuming recurring instances of components are already named, use preexisting XML objects**/
    public void toXml(T jaxbObject, Namer namer);
}
