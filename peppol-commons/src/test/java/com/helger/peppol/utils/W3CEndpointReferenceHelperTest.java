/**
 * Copyright (C) 2015-2016 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Version: MPL 1.1/EUPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at:
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Copyright The PEPPOL project (http://www.peppol.eu)
 *
 * Alternatively, the contents of this file may be used under the
 * terms of the EUPL, Version 1.1 or - as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence"); You may not use this work except in compliance
 * with the Licence.
 * You may obtain a copy of the Licence at:
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * If you wish to allow use of your version of this file only
 * under the terms of the EUPL License and not to allow others to use
 * your version of this file under the MPL, indicate your decision by
 * deleting the provisions above and replace them with the notice and
 * other provisions required by the EUPL License. If you do not delete
 * the provisions above, a recipient may use your version of this file
 * under either the MPL or the EUPL License.
 */
package com.helger.peppol.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
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
