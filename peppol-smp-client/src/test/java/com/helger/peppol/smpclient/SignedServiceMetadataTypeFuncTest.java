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
package com.helger.peppol.smpclient;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.jaxb.validation.LoggingValidationEventHandler;
import com.helger.peppol.httpclient.TrustStoreBasedX509KeySelector;
import com.helger.peppol.smp.SignedServiceMetadataType;
import com.helger.peppol.smp.marshal.SMPMarshallerSignedServiceMetadataType;
import com.helger.security.keystore.EKeyStoreType;
import com.helger.xml.serialize.read.DOMReader;

/**
 * Test class for class {@link SignedServiceMetadataType}.
 *
 * @author Philip Helger
 */
public final class SignedServiceMetadataTypeFuncTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (SignedServiceMetadataTypeFuncTest.class);

  @Test
  public void testReadNetEDIResponse () throws Exception
  {
    final SMPMarshallerSignedServiceMetadataType aMarshaller = new SMPMarshallerSignedServiceMetadataType (true);
    aMarshaller.setValidationEventHandlerFactory (x -> new LoggingValidationEventHandler ());

    final byte [] aBytes = StreamHelper.getAllBytes (new ClassPathResource ("smp/signed-service-metadata1.xml"));
    assertNotNull (aBytes);

    final SignedServiceMetadataType aSSM = aMarshaller.read (aBytes);
    assertNotNull (aSSM);

    final Document aDocument = DOMReader.readXMLDOM (aBytes);
    assertNotNull (aDocument);

    // Find Signature element.
    final NodeList aNodeList = aDocument.getElementsByTagNameNS (XMLSignature.XMLNS, "Signature");
    assertNotNull (aNodeList);
    assertTrue (aNodeList.getLength () > 0);

    final EKeyStoreType eTruststoreType = SMPClientConfiguration.getTrustStoreType ();
    final String sTruststorePath = SMPClientConfiguration.getTrustStorePath ();
    final String sTrustStorePassword = SMPClientConfiguration.getTrustStorePassword ();
    final TrustStoreBasedX509KeySelector aKeySelector = new TrustStoreBasedX509KeySelector (eTruststoreType,
                                                                                            sTruststorePath,
                                                                                            sTrustStorePassword);

    // Create a DOMValidateContext and specify a KeySelector
    // and document context.
    // TODO OASIS BDXR SMP v2 can have more than one signature
    final DOMValidateContext aValidateContext = new DOMValidateContext (aKeySelector, aNodeList.item (0));
    final XMLSignatureFactory aSignatureFactory = XMLSignatureFactory.getInstance ("DOM");

    // Unmarshal the XMLSignature.
    final XMLSignature aSignature = aSignatureFactory.unmarshalXMLSignature (aValidateContext);
    assertNotNull (aSignature);

    // Validate the XMLSignature.
    final boolean bCoreValid = aSignature.validate (aValidateContext);
    assertFalse (bCoreValid);

    final boolean bSignatureValueValid = aSignature.getSignatureValue ().validate (aValidateContext);
    if (LOGGER.isInfoEnabled ())
      LOGGER.info ("  Signature value valid: " + bSignatureValueValid);
    if (!bSignatureValueValid)
    {
      // Check the validation status of each Reference.
      int nIndex = 0;
      final Iterator <?> i = aSignature.getSignedInfo ().getReferences ().iterator ();
      while (i.hasNext ())
      {
        final boolean bRefValid = ((Reference) i.next ()).validate (aValidateContext);
        if (LOGGER.isInfoEnabled ())
          LOGGER.info ("  Reference[" + nIndex + "] validity status: " + (bRefValid ? "valid" : "NOT valid!"));
        ++nIndex;
      }
    }
  }
}
