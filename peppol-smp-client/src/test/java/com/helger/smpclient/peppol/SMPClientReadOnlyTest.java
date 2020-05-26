/**
 * Copyright (C) 2015-2020 Philip Helger
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.smpclient.peppol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.peppol.sml.ESML;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.factory.PeppolIdentifierFactory;
import com.helger.smpclient.exception.SMPClientException;
import com.helger.smpclient.peppol.jaxb.SignedServiceMetadataType;
import com.helger.smpclient.url.BDXLURLProvider;
import com.helger.smpclient.url.PeppolDNSResolutionException;
import com.helger.smpclient.url.PeppolURLProvider;

/**
 * Test class for class {@link SMPClientReadOnly}
 *
 * @author Philip Helger
 */
public final class SMPClientReadOnlyTest
{
  @Test
  public void testGetSMPHostURI () throws SMPClientException, PeppolDNSResolutionException
  {
    IParticipantIdentifier aPI = PeppolIdentifierFactory.INSTANCE.createParticipantIdentifierWithDefaultScheme ("9915:test");

    // PEPPOL URL provider
    SMPClientReadOnly aSMPClient = new SMPClientReadOnly (PeppolURLProvider.INSTANCE, aPI, ESML.DIGIT_TEST);
    assertEquals ("http://B-85008b8279e07ab0392da75fa55856a2.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu/",
                  aSMPClient.getSMPHostURI ());

    // E-SENS URL provider
    aSMPClient = new SMPClientReadOnly (BDXLURLProvider.INSTANCE, aPI, ESML.DIGIT_TEST);
    if (true)
      assertEquals ("http://test-infra.peppol.at/", aSMPClient.getSMPHostURI ());
    else
      assertEquals ("http://BRZ-TEST-SMP.publisher.acc.edelivery.tech.ec.europa.eu/", aSMPClient.getSMPHostURI ());

    // This instance has a BOM inside
    aPI = PeppolIdentifierFactory.INSTANCE.createParticipantIdentifierWithDefaultScheme ("9917:5504033150");
    aSMPClient = new SMPClientReadOnly (PeppolURLProvider.INSTANCE, aPI, ESML.DIGIT_PRODUCTION);
    assertEquals ("http://B-2f67a0710cbc13c11ac8c0d64186ac5e.iso6523-actorid-upis.edelivery.tech.ec.europa.eu/",
                  aSMPClient.getSMPHostURI ());
    assertNotNull (aSMPClient.getServiceGroupOrNull (aPI));
  }

  @Test
  public void test2303 () throws Exception
  {
    final IParticipantIdentifier aPI = PeppolIdentifierFactory.INSTANCE.createParticipantIdentifierWithDefaultScheme ("9925:be0887290276");

    // PEPPOL URL provider
    final SMPClientReadOnly aSMPClient = new SMPClientReadOnly (PeppolURLProvider.INSTANCE, aPI, ESML.DIGIT_PRODUCTION);
    assertEquals ("http://B-c9f280672264cdb82eac528c265ed029.iso6523-actorid-upis.edelivery.tech.ec.europa.eu/",
                  aSMPClient.getSMPHostURI ());

    final IDocumentTypeIdentifier aDocumentTypeID = PeppolIdentifierFactory.INSTANCE.createDocumentTypeIdentifierWithDefaultScheme ("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1");
    aSMPClient.setXMLSchemaValidation (false);
    final SignedServiceMetadataType aSM = aSMPClient.getServiceMetadataOrNull (aPI, aDocumentTypeID);
    assertNotNull (aSM);
  }
}
