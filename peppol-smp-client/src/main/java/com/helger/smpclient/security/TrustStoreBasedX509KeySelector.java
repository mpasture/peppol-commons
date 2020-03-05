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
package com.helger.smpclient.security;

import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.security.certificate.CertificateHelper;
import com.helger.security.keystore.ConstantKeySelectorResult;
import com.helger.security.keystore.EKeyStoreType;
import com.helger.security.keystore.KeyStoreHelper;

/**
 * Finds and returns a key using the data contained in a {@link KeyInfo} object
 *
 * @author Philip Helger
 * @see <a href=
 *      "http://java.sun.com/developer/technicalArticles/xml/dig_signature_api/">
 *      Programming with the Java XML Digital Signature API</a>
 */
public final class TrustStoreBasedX509KeySelector extends KeySelector
{
  private static final Logger LOGGER = LoggerFactory.getLogger (TrustStoreBasedX509KeySelector.class);

  private final EKeyStoreType m_eTruststoreType;
  private final String m_sTruststorePath;
  private final String m_sTrustStorePassword;

  private transient KeyStore m_aTrustStore;

  public TrustStoreBasedX509KeySelector (@Nonnull final EKeyStoreType eTruststoreType,
                                         @Nonnull final String sTruststorePath,
                                         @Nonnull final String sTruststorePassword)
  {
    ValueEnforcer.notNull (eTruststoreType, "TruststoreType");
    ValueEnforcer.notNull (sTruststorePath, "TruststorePath");
    ValueEnforcer.notNull (sTruststorePassword, "TruststorePassword");
    m_eTruststoreType = eTruststoreType;
    m_sTruststorePath = sTruststorePath;
    m_sTrustStorePassword = sTruststorePassword;
  }

  public static boolean algorithmEquals (@Nonnull final String sAlgURI, @Nonnull final String sAlgName)
  {
    if (sAlgName.equalsIgnoreCase ("DSA"))
    {
      if (sAlgURI.equalsIgnoreCase (SignatureMethod.DSA_SHA1) ||
          sAlgURI.equalsIgnoreCase ("http://www.w3.org/2009/xmldsig11#dsa-sha256"))
        return true;
    }
    else
      if (sAlgName.equalsIgnoreCase ("RSA"))
      {
        if (sAlgURI.equalsIgnoreCase (SignatureMethod.RSA_SHA1) ||
            sAlgURI.equalsIgnoreCase ("http://www.w3.org/2001/04/xmldsig-more#rsa-sha224") ||
            sAlgURI.equalsIgnoreCase ("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256") ||
            sAlgURI.equalsIgnoreCase ("http://www.w3.org/2001/04/xmldsig-more#rsa-sha384") ||
            sAlgURI.equalsIgnoreCase ("http://www.w3.org/2001/04/xmldsig-more#rsa-sha512") ||
            sAlgURI.equalsIgnoreCase ("http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160"))
          return true;
      }
      else
        if (sAlgName.equalsIgnoreCase ("EC"))
        {
          if (sAlgURI.equalsIgnoreCase ("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha224") ||
              sAlgURI.equalsIgnoreCase ("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256") ||
              sAlgURI.equalsIgnoreCase ("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha384") ||
              sAlgURI.equalsIgnoreCase ("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha512"))
            return true;
        }

    if (LOGGER.isWarnEnabled ())
      LOGGER.warn ("Algorithm mismatch between JCA/JCE public key algorithm name ('" +
                   sAlgName +
                   "') and signature algorithm URI ('" +
                   sAlgURI +
                   "')");
    return false;
  }

  @Override
  public KeySelectorResult select (@Nonnull final KeyInfo aKeyInfo,
                                   final KeySelector.Purpose aPurpose,
                                   @Nonnull final AlgorithmMethod aMethod,
                                   final XMLCryptoContext aCryptoContext) throws KeySelectorException
  {
    // For all XMLStructure
    final Iterator <?> aContentIter = aKeyInfo.getContent ().iterator ();
    while (aContentIter.hasNext ())
    {
      final XMLStructure aStructure = (XMLStructure) aContentIter.next ();
      if (aStructure instanceof X509Data)
      {
        final X509Data aX509Data = (X509Data) aStructure;
        // For all content - can be many different types
        final Iterator <?> aX509Iter = aX509Data.getContent ().iterator ();
        while (aX509Iter.hasNext ())
        {
          final Object aElement = aX509Iter.next ();
          if (aElement instanceof X509Certificate)
          {
            // We found a certificate
            final X509Certificate aCertificate = (X509Certificate) aElement;
            try
            {
              // Check if the certificate is expired or active.
              aCertificate.checkValidity ();

              // Checks whether the certificate is in the trusted store.
              final X509Certificate [] aCertArray = new X509Certificate [] { aCertificate };

              if (m_aTrustStore == null)
              {
                // Load once only
                m_aTrustStore = KeyStoreHelper.loadKeyStoreDirect (m_eTruststoreType,
                                                                   m_sTruststorePath,
                                                                   m_sTrustStorePassword);
              }

              // The PKIXParameters constructor may fail because:
              // - the trustAnchorsParameter is empty
              final PKIXParameters aPKIXParams = new PKIXParameters (m_aTrustStore);
              aPKIXParams.setRevocationEnabled (false);
              final CertificateFactory aCertificateFactory = CertificateHelper.getX509CertificateFactory ();
              final CertPath aCertPath = aCertificateFactory.generateCertPath (new CommonsArrayList <> (aCertArray));
              final CertPathValidator aPathValidator = CertPathValidator.getInstance ("PKIX");
              aPathValidator.validate (aCertPath, aPKIXParams);

              final PublicKey aPublicKey = aCertificate.getPublicKey ();

              // Make sure the algorithm is compatible with the method.
              if (algorithmEquals (aMethod.getAlgorithm (), aPublicKey.getAlgorithm ()))
                return new ConstantKeySelectorResult (aPublicKey);
              // Else a warning was already emitted
            }
            catch (final Exception ex)
            {
              throw new KeySelectorException ("Failed to select public key from certificate " + aCertificate, ex);
            }
          }
        }
      }
    }
    throw new KeySelectorException ("No public key found!");
  }
}
