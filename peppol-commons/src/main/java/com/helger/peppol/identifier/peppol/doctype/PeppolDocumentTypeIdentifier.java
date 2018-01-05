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
package com.helger.peppol.identifier.peppol.doctype;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.annotation.DevelopersNote;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.compare.CompareHelper;
import com.helger.commons.lang.ICloneable;
import com.helger.commons.string.StringHelper;
import com.helger.peppol.identifier.CIdentifier;
import com.helger.peppol.identifier.DocumentIdentifierType;
import com.helger.peppol.identifier.generic.doctype.IDocumentTypeIdentifier;
import com.helger.peppol.identifier.peppol.PeppolIdentifierHelper;

/**
 * A special document type identifier that handles the specialties of PEPPOL
 * (like fixed default scheme) etc.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class PeppolDocumentTypeIdentifier extends DocumentIdentifierType implements
                                          IMutablePeppolDocumentTypeIdentifier,
                                          Comparable <PeppolDocumentTypeIdentifier>,
                                          ICloneable <PeppolDocumentTypeIdentifier>
{
  @DevelopersNote ("Don't invoke manually. Always use the IdentifierFactory!")
  public PeppolDocumentTypeIdentifier (@Nonnull final IDocumentTypeIdentifier aIdentifier)
  {
    this (aIdentifier.getScheme (), aIdentifier.getValue ());
  }

  @Nonnull
  private static String _verifyScheme (@Nullable final String sScheme)
  {
    if (!IPeppolDocumentTypeIdentifier.isValidScheme (sScheme))
      throw new IllegalArgumentException ("Peppol Document Type identifier scheme '" + sScheme + "' is invalid!");
    return sScheme;
  }

  @Nonnull
  private static String _verifyValue (@Nonnull final String sValue)
  {
    if (!IPeppolDocumentTypeIdentifier.isValidValue (sValue))
      throw new IllegalArgumentException ("Peppol Document Type identifier value '" + sValue + "' is invalid!");
    return sValue;
  }

  @DevelopersNote ("Don't invoke manually. Always use the IdentifierFactory!")
  public PeppolDocumentTypeIdentifier (@Nullable final String sScheme, @Nonnull final String sValue)
  {
    this (true, _verifyScheme (sScheme), _verifyValue (sValue));
  }

  /**
   * Private constructor that passed the pre-checked values directly to the
   * super class. Has a dummy parameter for a unique signature.
   *
   * @param bVerified
   *        dummy
   * @param sScheme
   *        Identifier scheme. May not be <code>null</code>.
   * @param sValue
   *        Identifier value. May not be <code>null</code>.
   */
  protected PeppolDocumentTypeIdentifier (final boolean bVerified,
                                          @Nonnull final String sScheme,
                                          @Nonnull final String sValue)
  {
    setScheme (sScheme);
    setValue (sValue);
  }

  public int compareTo (@Nonnull final PeppolDocumentTypeIdentifier aOther)
  {
    int ret = CompareHelper.compare (getScheme (), aOther.getScheme ());
    if (ret == 0)
      ret = CompareHelper.compare (getValue (), aOther.getValue ());
    return ret;
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public PeppolDocumentTypeIdentifier getClone ()
  {
    return new PeppolDocumentTypeIdentifier (this);
  }

  /**
   * Create a new document type identifier that uses the default schema
   * {@link PeppolIdentifierHelper#DEFAULT_DOCUMENT_TYPE_SCHEME}
   *
   * @param sValue
   *        The identifier value like
   *        <code>urn:oasis:names:specification:ubl:schema:xsd:Order-2::Order##urn:www.cenbii.eu:transaction:biicoretrdm001:ver1.0:#urn:www.peppol.eu:bis:peppol3a:ver1.0::2.0</code>
   * @return The created {@link PeppolDocumentTypeIdentifier} and never
   *         <code>null</code>.
   * @deprecated Use
   *             {@link com.helger.peppol.identifier.factory.IIdentifierFactory#createDocumentTypeIdentifierWithDefaultScheme(String)}
   *             instead
   */
  @Nonnull
  @Deprecated
  public static PeppolDocumentTypeIdentifier createWithDefaultScheme (@Nonnull final String sValue)
  {
    return new PeppolDocumentTypeIdentifier (PeppolIdentifierHelper.DEFAULT_DOCUMENT_TYPE_SCHEME, sValue);
  }

  /**
   * Create a new document type identifier from the URI representation. This is
   * the inverse operation of {@link #getURIEncoded()}.
   *
   * @param sURIPart
   *        The URI part in the format <code>scheme::value</code> (e.g.
   *        <code>busdox-docid-qns::urn:oasis:names:specification:ubl:schema:xsd:Order-2::Order##urn:www.cenbii.eu:transaction:biicoretrdm001:ver1.0:#urn:www.peppol.eu:bis:peppol3a:ver1.0::2.0</code>
   *        ). It must NOT be percent encoded!
   * @return The created {@link PeppolDocumentTypeIdentifier} and never
   *         <code>null</code>.
   * @throws IllegalArgumentException
   *         If the passed identifier is not a valid URI encoded identifier
   */
  @Nonnull
  @Deprecated
  public static PeppolDocumentTypeIdentifier createFromURIPart (@Nonnull final String sURIPart) throws IllegalArgumentException
  {
    final PeppolDocumentTypeIdentifier ret = createFromURIPartOrNull (sURIPart);
    if (ret == null)
      throw new IllegalArgumentException ("Peppol Document type identifier '" +
                                          sURIPart +
                                          "' did not include correct delimiter: " +
                                          CIdentifier.URL_SCHEME_VALUE_SEPARATOR);
    return ret;
  }

  /**
   * Create a new document type identifier from the URI representation. This is
   * the inverse operation of {@link #getURIEncoded()}. The URI part must have
   * the layout <code>scheme::value</code>. This method returns only valid
   * document type identifier schemes and document type identifier values.
   *
   * @param sURIPart
   *        The URI part in the format <code>scheme::value</code> (e.g.
   *        <code>busdox-docid-qns::urn:oasis:names:specification:ubl:schema:xsd:Order-2::Order##urn:www.cenbii.eu:transaction:biicoretrdm001:ver1.0:#urn:www.peppol.eu:bis:peppol3a:ver1.0::2.0</code>
   *        ) . It must NOT be percent encoded! May be <code>null</code>.
   * @return The created {@link PeppolDocumentTypeIdentifier} or
   *         <code>null</code> if the passed identifier is not a valid URI
   *         encoded identifier
   * @deprecated Use
   *             {@link com.helger.peppol.identifier.factory.IIdentifierFactory#parseDocumentTypeIdentifier(String)}
   *             instead
   */
  @Nullable
  @Deprecated
  public static PeppolDocumentTypeIdentifier createFromURIPartOrNull (@Nullable final String sURIPart)
  {
    if (sURIPart == null)
      return null;

    // This is quicker than splitting with RegEx!
    final ICommonsList <String> aSplitted = StringHelper.getExploded (CIdentifier.URL_SCHEME_VALUE_SEPARATOR,
                                                                      sURIPart,
                                                                      2);
    if (aSplitted.size () != 2)
      return null;

    return createIfValid (aSplitted.get (0), aSplitted.get (1));
  }

  /**
   * Take the passed identifier scheme and value try to convert it back to a
   * document identifier. If the passed scheme is invalid or if the passed value
   * is invalid, <code>null</code> is returned.
   *
   * @param sScheme
   *        The identifier scheme. May be <code>null</code> in which case
   *        <code>null</code> is returned.
   * @param sValue
   *        The identifier value. May be <code>null</code> in which case
   *        <code>null</code> is returned.
   * @return The document type identifier or <code>null</code> if any of the
   *         parts is invalid.
   * @see IPeppolDocumentTypeIdentifier#isValidScheme(String)
   * @see IPeppolDocumentTypeIdentifier#isValidValue(String)
   */
  @Nullable
  public static PeppolDocumentTypeIdentifier createIfValid (@Nullable final String sScheme,
                                                            @Nullable final String sValue)
  {
    if (IPeppolDocumentTypeIdentifier.isValidScheme (sScheme) && IPeppolDocumentTypeIdentifier.isValidValue (sValue))
      return new PeppolDocumentTypeIdentifier (true, sScheme, sValue);
    return null;
  }

  /**
   * Check if the passed document type identifier is valid. This method checks
   * for the existence of the scheme and the value and validates both.
   *
   * @param sURIPart
   *        The document type identifier to be checked (including the scheme).
   *        May be <code>null</code>.
   * @return <code>true</code> if the document type identifier is valid,
   *         <code>false</code> otherwise
   */
  @Deprecated
  public static boolean isValidURIPart (@Nullable final String sURIPart)
  {
    return createFromURIPartOrNull (sURIPart) != null;
  }
}
