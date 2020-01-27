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
package com.helger.peppol.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

import org.junit.Test;

import com.helger.security.keystore.KeyStoreHelper;

/**
 * Test class for class {@link PeppolKeyStoreHelper}.
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
@SuppressWarnings ("deprecation")
public final class PeppolKeyStoreHelperTest
{
  @Test
  public void testConstants ()
  {
    assertNotNull (PeppolKeyStoreHelper.Config2010.CERTIFICATE_PRODUCTION_ROOT);
    assertNotNull (PeppolKeyStoreHelper.Config2010.CERTIFICATE_PRODUCTION_AP);
    assertNotNull (PeppolKeyStoreHelper.Config2010.CERTIFICATE_PRODUCTION_SMP);

    assertNotNull (PeppolKeyStoreHelper.Config2010.CERTIFICATE_PILOT_ROOT);
    assertNotNull (PeppolKeyStoreHelper.Config2010.CERTIFICATE_PILOT_AP);
    assertNotNull (PeppolKeyStoreHelper.Config2010.CERTIFICATE_PILOT_SMP);

    assertNotNull (PeppolKeyStoreHelper.Config2018.CERTIFICATE_PRODUCTION_ROOT);
    assertNotNull (PeppolKeyStoreHelper.Config2018.CERTIFICATE_PRODUCTION_AP);
    assertNotNull (PeppolKeyStoreHelper.Config2018.CERTIFICATE_PRODUCTION_SMP);

    assertNotNull (PeppolKeyStoreHelper.Config2018.CERTIFICATE_PILOT_ROOT);
    assertNotNull (PeppolKeyStoreHelper.Config2018.CERTIFICATE_PILOT_AP);
    assertNotNull (PeppolKeyStoreHelper.Config2018.CERTIFICATE_PILOT_SMP);
  }

  @Test
  public void testLoadTrustStore2010Production () throws Exception
  {
    // Load trust store
    final KeyStore aTrustStore = KeyStoreHelper.loadKeyStoreDirect (PeppolKeyStoreHelper.TRUSTSTORE_TYPE,
                                                                    PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PRODUCTION_CLASSPATH,
                                                                    PeppolKeyStoreHelper.TRUSTSTORE_PASSWORD);
    assertNotNull (aTrustStore);

    // Ensure all name entries are contained
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PRODUCTION_ALIAS_ROOT));
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PRODUCTION_ALIAS_AP));
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PRODUCTION_ALIAS_SMP));

    {
      final X509Certificate aCertAP = (X509Certificate) aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PRODUCTION_ALIAS_AP);
      final String sIssuerName = aCertAP.getIssuerX500Principal ().getName ();
      assertEquals ("CN=PEPPOL Root CA,O=NATIONAL IT AND TELECOM AGENCY,C=DK", sIssuerName);
      final String sSubjectName = aCertAP.getSubjectX500Principal ().getName ();
      assertEquals ("CN=PEPPOL ACCESS POINT CA,O=NATIONAL IT AND TELECOM AGENCY,C=DK", sSubjectName);
    }
    {
      final X509Certificate aCertSMP = (X509Certificate) aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PRODUCTION_ALIAS_SMP);
      final String sIssuerName = aCertSMP.getIssuerX500Principal ().getName ();
      assertEquals ("CN=PEPPOL Root CA,O=NATIONAL IT AND TELECOM AGENCY,C=DK", sIssuerName);
      final String sSubjectName = aCertSMP.getSubjectX500Principal ().getName ();
      assertEquals ("CN=PEPPOL SERVICE METADATA PUBLISHER CA,O=NATIONAL IT AND TELECOM AGENCY,C=DK", sSubjectName);
    }
  }

  @Test
  public void testLoadTrustStore2010Pilot () throws Exception
  {
    // Load trust store
    final KeyStore aTrustStore = KeyStoreHelper.loadKeyStoreDirect (PeppolKeyStoreHelper.TRUSTSTORE_TYPE,
                                                                    PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PILOT_CLASSPATH,
                                                                    PeppolKeyStoreHelper.TRUSTSTORE_PASSWORD);
    assertNotNull (aTrustStore);

    // Ensure all name entries are contained
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PILOT_ALIAS_ROOT));
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PILOT_ALIAS_AP));
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PILOT_ALIAS_SMP));

    {
      final X509Certificate aCertAP = (X509Certificate) aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PILOT_ALIAS_AP);
      final String sIssuerName = aCertAP.getIssuerX500Principal ().getName ();
      assertEquals ("CN=PEPPOL Root TEST CA,OU=FOR TEST PURPOSES ONLY,O=NATIONAL IT AND TELECOM AGENCY,C=DK",
                    sIssuerName);
      final String sSubjectName = aCertAP.getSubjectX500Principal ().getName ();
      assertEquals ("CN=PEPPOL ACCESS POINT TEST CA,OU=FOR TEST PURPOSES ONLY,O=NATIONAL IT AND TELECOM AGENCY,C=DK",
                    sSubjectName);
    }
    {
      final X509Certificate aCertSMP = (X509Certificate) aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2010.TRUSTSTORE_PILOT_ALIAS_SMP);
      final String sIssuerName = aCertSMP.getIssuerX500Principal ().getName ();
      assertEquals ("CN=PEPPOL Root TEST CA,OU=FOR TEST PURPOSES ONLY,O=NATIONAL IT AND TELECOM AGENCY,C=DK",
                    sIssuerName);
      final String sSubjectName = aCertSMP.getSubjectX500Principal ().getName ();
      assertEquals ("CN=PEPPOL SERVICE METADATA PUBLISHER TEST CA,OU=FOR TEST PURPOSES ONLY,O=NATIONAL IT AND TELECOM AGENCY,C=DK",
                    sSubjectName);
    }
  }

  @Test
  public void testLoadTrustStore2018Production () throws Exception
  {
    // Load trust store
    final KeyStore aTrustStore = KeyStoreHelper.loadKeyStoreDirect (PeppolKeyStoreHelper.TRUSTSTORE_TYPE,
                                                                    PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PRODUCTION_CLASSPATH,
                                                                    PeppolKeyStoreHelper.TRUSTSTORE_PASSWORD);
    assertNotNull (aTrustStore);

    // Ensure all name entries are contained
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PRODUCTION_ALIAS_ROOT));
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PRODUCTION_ALIAS_AP));
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PRODUCTION_ALIAS_SMP));

    {
      final X509Certificate aCertAP = (X509Certificate) aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PRODUCTION_ALIAS_AP);
      final String sIssuerName = aCertAP.getIssuerX500Principal ().getName ();
      assertEquals ("CN=PEPPOL Root CA - G2,O=OpenPEPPOL AISBL,C=BE", sIssuerName);
      final String sSubjectName = aCertAP.getSubjectX500Principal ().getName ();
      assertEquals ("CN=PEPPOL ACCESS POINT CA - G2,O=OpenPEPPOL AISBL,C=BE", sSubjectName);
    }
    {
      final X509Certificate aCertSMP = (X509Certificate) aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PRODUCTION_ALIAS_SMP);
      final String sIssuerName = aCertSMP.getIssuerX500Principal ().getName ();
      assertEquals ("CN=PEPPOL Root CA - G2,O=OpenPEPPOL AISBL,C=BE", sIssuerName);
      final String sSubjectName = aCertSMP.getSubjectX500Principal ().getName ();
      assertEquals ("CN=PEPPOL SERVICE METADATA PUBLISHER CA - G2,O=OpenPEPPOL AISBL,C=BE", sSubjectName);
    }
  }

  @Test
  public void testLoadTrustStore2018Pilot () throws Exception
  {
    // Load trust store
    final KeyStore aTrustStore = KeyStoreHelper.loadKeyStoreDirect (PeppolKeyStoreHelper.TRUSTSTORE_TYPE,
                                                                    PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PILOT_CLASSPATH,
                                                                    PeppolKeyStoreHelper.TRUSTSTORE_PASSWORD);
    assertNotNull (aTrustStore);

    // Ensure all name entries are contained
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PILOT_ALIAS_ROOT));
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PILOT_ALIAS_AP));
    assertNotNull (aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PILOT_ALIAS_SMP));

    {
      final X509Certificate aCertAP = (X509Certificate) aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PILOT_ALIAS_AP);
      final String sIssuerName = aCertAP.getIssuerX500Principal ().getName ();
      assertEquals ("CN=PEPPOL Root TEST CA - G2,OU=FOR TEST ONLY,O=OpenPEPPOL AISBL,C=BE", sIssuerName);
      final String sSubjectName = aCertAP.getSubjectX500Principal ().getName ();
      assertEquals ("CN=PEPPOL ACCESS POINT TEST CA - G2,OU=FOR TEST ONLY,O=OpenPEPPOL AISBL,C=BE", sSubjectName);
    }
    {
      final X509Certificate aCertSMP = (X509Certificate) aTrustStore.getCertificate (PeppolKeyStoreHelper.Config2018.TRUSTSTORE_PILOT_ALIAS_SMP);
      final String sIssuerName = aCertSMP.getIssuerX500Principal ().getName ();
      assertEquals ("CN=PEPPOL Root TEST CA - G2,OU=FOR TEST ONLY,O=OpenPEPPOL AISBL,C=BE", sIssuerName);
      final String sSubjectName = aCertSMP.getSubjectX500Principal ().getName ();
      assertEquals ("CN=PEPPOL SERVICE METADATA PUBLISHER TEST CA - G2,OU=FOR TEST ONLY,O=OpenPEPPOL AISBL,C=BE",
                    sSubjectName);
    }
  }
}
