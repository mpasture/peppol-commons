/**
 * Copyright (C) 2015-2017 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Note: some files, that were not part of the original package are currently
 *   licensed under Apache 2.0 license - https://www.apache.org/licenses/LICENSE-2.0
 *   The respective files contain a special class header!
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
package com.helger.peppol.smpclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.peppol.identifier.factory.PeppolIdentifierFactory;
import com.helger.peppol.identifier.generic.participant.IParticipantIdentifier;
import com.helger.peppol.sml.ESML;
import com.helger.peppol.smpclient.exception.SMPClientException;
import com.helger.peppol.url.EsensURLProvider;
import com.helger.peppol.url.PeppolURLProvider;

/**
 * Test class for class {@link SMPClientReadOnly}
 *
 * @author Philip Helger
 */
public final class SMPClientReadOnlyTest
{
  @Test
  public void testGetSMPHostURI () throws SMPClientException
  {
    IParticipantIdentifier aPI = PeppolIdentifierFactory.INSTANCE.createParticipantIdentifierWithDefaultScheme ("9915:test");

    // PEPPOL URL provider
    SMPClientReadOnly aSMPClient = new SMPClientReadOnly (PeppolURLProvider.INSTANCE, aPI, ESML.DIGIT_TEST);
    assertEquals ("http://B-85008b8279e07ab0392da75fa55856a2.iso6523-actorid-upis.acc.edelivery.tech.ec.europa.eu/",
                  aSMPClient.getSMPHostURI ());

    // E-SENS URL provider
    aSMPClient = new SMPClientReadOnly (EsensURLProvider.INSTANCE, aPI, ESML.DIGIT_TEST);
    assertEquals ("http://BRZ-TEST-SMP.publisher.acc.edelivery.tech.ec.europa.eu/", aSMPClient.getSMPHostURI ());

    // This instance has a BOM inside
    aPI = PeppolIdentifierFactory.INSTANCE.createParticipantIdentifierWithDefaultScheme ("9917:5504033150");
    aSMPClient = new SMPClientReadOnly (PeppolURLProvider.INSTANCE, aPI, ESML.DIGIT_PRODUCTION);
    assertNotNull (aSMPClient.getServiceGroupOrNull (aPI));
  }
}
