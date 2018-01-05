/**
 * Copyright (C) 2015-2018 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * The Original Code is Copyright The PEPPOL project (http://www.peppol.eu)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.helger.peppol.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.xml.XMLFactory;

/**
 * Test class for class {@link W3CEndpointReferenceHelper}.
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
public final class W3CEndpointReferenceHelperTest
{
  @Test
  public void testAddress ()
  {
    final String sURL = "http://www.example.org/any";
    W3CEndpointReference aEPR = W3CEndpointReferenceHelper.createEndpointReference (sURL);
    assertEquals (sURL, W3CEndpointReferenceHelper.getAddress (aEPR));

    aEPR = W3CEndpointReferenceHelper.createEndpointReference ("");
    assertEquals ("", W3CEndpointReferenceHelper.getAddress (aEPR));
  }

  @Test
  public void testReferenceParameters ()
  {
    final ICommonsList <Element> aParams = new CommonsArrayList<> ();
    final Document aDummyDoc = XMLFactory.newDocument ();
    Element aElement = aDummyDoc.createElementNS ("urn:ns1", "element1");
    aElement.appendChild (aDummyDoc.createTextNode ("text1"));
    aParams.add (aElement);
    aElement = aDummyDoc.createElementNS ("urn:ns2", "element2");
    aElement.appendChild (aDummyDoc.createTextNode ("text2"));
    aParams.add (aElement);

    final String sURL = "http://www.example.org/any";
    final W3CEndpointReference aEPR = W3CEndpointReferenceHelper.createEndpointReference (sURL, aParams);
    assertEquals (sURL, W3CEndpointReferenceHelper.getAddress (aEPR));
    final ICommonsList <Element> aReads = W3CEndpointReferenceHelper.getReferenceParameters (aEPR);

    // Note: cannot directly compare aParams and aReads because of different
    // underlying documents
    assertEquals (aParams.size (), aReads.size ());
    for (int i = 0; i < aParams.size (); ++i)
    {
      final Element aParam = aParams.get (i);
      final Element aRead = aReads.get (i);
      assertEquals (aParam.getNamespaceURI (), aRead.getNamespaceURI ());
      assertEquals (aParam.getLocalName (), aRead.getLocalName ());
      assertEquals (aParam.getTextContent (), aRead.getTextContent ());
    }

    assertNotNull (W3CEndpointReferenceHelper.getReferenceParameter (aEPR, 0));
    assertNotNull (W3CEndpointReferenceHelper.getReferenceParameter (aEPR, 1));
  }
}
