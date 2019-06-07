/**
 * Copyright (C) 2015-2019 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * The Original Code is Copyright The PEPPOL project (http://www.peppol.eu)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.helger.peppol.smp.marshal;

import javax.annotation.Nonnull;
import javax.xml.bind.JAXBElement;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.functional.IFunction;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.jaxb.GenericJAXBMarshaller;
import com.helger.peppol.smp.ObjectFactory;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xsds.xmldsig.CXMLDSig;

/**
 * Abstract JAXB marshaller with namespace prefix mapping
 *
 * @author Philip Helger
 * @param <JAXBTYPE>
 *        JAXB type to use
 */
public abstract class AbstractSMPMarshaller <JAXBTYPE> extends GenericJAXBMarshaller <JAXBTYPE>
{
  public static final ClassPathResource XSD1 = new ClassPathResource ("/schemas/Identifiers-1.0.xsd",
                                                                      AbstractSMPMarshaller.class.getClassLoader ());
  public static final ClassPathResource XSD2 = new ClassPathResource ("/schemas/ws-addr.xsd",
                                                                      AbstractSMPMarshaller.class.getClassLoader ());
  public static final ClassPathResource XSD3 = new ClassPathResource ("/schemas/ServiceMetadataPublishingTypes-1.0.xsd",
                                                                      AbstractSMPMarshaller.class.getClassLoader ());

  public AbstractSMPMarshaller (@Nonnull final Class <JAXBTYPE> aType,
                                final boolean bValidationEnabled,
                                @Nonnull final IFunction <JAXBTYPE, JAXBElement <JAXBTYPE>> aWrapper)
  {
    super (aType,
           bValidationEnabled ? new CommonsArrayList <> (CXMLDSig.getXSDResource (), XSD1, XSD2, XSD3) : null,
           aWrapper);

    final MapBasedNamespaceContext aNSContext = new MapBasedNamespaceContext ();
    aNSContext.addMapping ("smp", ObjectFactory._ServiceGroup_QNAME.getNamespaceURI ());
    aNSContext.addMapping ("id", "http://busdox.org/transport/identifiers/1.0/");
    aNSContext.addMapping (CXMLDSig.DEFAULT_PREFIX, CXMLDSig.NAMESPACE_URI);
    aNSContext.addMapping ("wsa", "http://www.w3.org/2005/08/addressing");
    setNamespaceContext (aNSContext);
  }
}
