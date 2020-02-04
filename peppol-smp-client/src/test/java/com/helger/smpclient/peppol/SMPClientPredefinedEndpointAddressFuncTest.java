/**
 * Copyright (C) 2015-2020 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * The Original Code is Copyright The PEPPOL project (http://www.peppol.eu)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.helger.smpclient.peppol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.cert.X509Certificate;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.helger.peppol.sml.ESML;
import com.helger.peppol.sml.ISMLInfo;
import com.helger.peppol.smp.ESMPTransportProfile;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.factory.PeppolIdentifierFactory;
import com.helger.peppolid.peppol.doctype.EPredefinedDocumentTypeIdentifier;
import com.helger.peppolid.peppol.process.EPredefinedProcessIdentifier;
import com.helger.smpclient.config.SMPClientConfiguration;
import com.helger.smpclient.url.IPeppolURLProvider;
import com.helger.smpclient.url.PeppolDNSResolutionException;
import com.helger.smpclient.url.PeppolURLProvider;

/**
 * Test class for class {@link SMPClient}.
 *
 * @author Philip Helger
 */
public final class SMPClientPredefinedEndpointAddressFuncTest
{
  private static final IParticipantIdentifier PI_AT_Test = PeppolIdentifierFactory.INSTANCE.createParticipantIdentifierWithDefaultScheme ("9915:test");
  private static final IParticipantIdentifier PI_AT_Prod = PeppolIdentifierFactory.INSTANCE.createParticipantIdentifierWithDefaultScheme ("9915:b");
  private static final IPeppolURLProvider URL_PROVIDER = PeppolURLProvider.INSTANCE;

  static
  {
    // Ensure the network system properties are assigned
    SMPClientConfiguration.getConfigFile ().applyAllNetworkSystemProperties ();
  }

  @Nonnull
  private static SMPClient _createSMPClient (@Nonnull final IParticipantIdentifier aParticipantIdentifier,
                                             @Nonnull final ISMLInfo aSMLInfo) throws PeppolDNSResolutionException
  {
    return new SMPClient (URL_PROVIDER, aParticipantIdentifier, aSMLInfo);
  }

  @Test
  public void testGetEndpointAddress () throws Exception
  {
    String sEndpointAddress;

    sEndpointAddress = _createSMPClient (PI_AT_Test,
                                         ESML.DIGIT_TEST).getEndpointAddress (PI_AT_Test,
                                                                              EPredefinedDocumentTypeIdentifier.INVOICE_T010_BIS4A_V20,
                                                                              EPredefinedProcessIdentifier.BIS4A_V2,
                                                                              ESMPTransportProfile.TRANSPORT_PROFILE_AS2);
    assertEquals ("https://test.erechnung.gv.at/as2", sEndpointAddress);

    sEndpointAddress = _createSMPClient (PI_AT_Prod,
                                         ESML.DIGIT_PRODUCTION).getEndpointAddress (PI_AT_Prod,
                                                                                    EPredefinedDocumentTypeIdentifier.INVOICE_T010_BIS4A_V20,
                                                                                    EPredefinedProcessIdentifier.BIS4A_V2,
                                                                                    ESMPTransportProfile.TRANSPORT_PROFILE_AS2);
    assertEquals ("https://www.erechnung.gv.at/as2", sEndpointAddress);
  }

  /**
   * This test reads a live certificate and reads out the serial number. The
   * current certificates are valid from 03/2017 - 03/2019. If you run this test
   * afterwards and it fails, either fix the numbers or ignore the test.
   *
   * @throws Exception
   *         on error
   */
  @Test
  public void testGetEndpointCertificate () throws Exception
  {
    X509Certificate aEndpointCertificate;

    aEndpointCertificate = _createSMPClient (PI_AT_Test,
                                             ESML.DIGIT_TEST).getEndpointCertificate (PI_AT_Test,
                                                                                      EPredefinedDocumentTypeIdentifier.INVOICE_T010_BIS4A_V20,
                                                                                      EPredefinedProcessIdentifier.BIS4A_V2,
                                                                                      ESMPTransportProfile.TRANSPORT_PROFILE_AS2);
    assertNotNull (aEndpointCertificate);
    assertEquals ("60887909668878219226152841132441057095", aEndpointCertificate.getSerialNumber ().toString ());

    aEndpointCertificate = _createSMPClient (PI_AT_Prod,
                                             ESML.DIGIT_PRODUCTION).getEndpointCertificate (PI_AT_Prod,
                                                                                            EPredefinedDocumentTypeIdentifier.INVOICE_T010_BIS4A_V20,
                                                                                            EPredefinedProcessIdentifier.BIS4A_V2,
                                                                                            ESMPTransportProfile.TRANSPORT_PROFILE_AS2);
    assertNotNull (aEndpointCertificate);
    assertEquals ("33760418496732648246480282249276568857", aEndpointCertificate.getSerialNumber ().toString ());
  }
}
