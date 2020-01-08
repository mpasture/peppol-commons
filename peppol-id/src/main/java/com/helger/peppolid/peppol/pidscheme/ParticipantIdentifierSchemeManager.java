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
package com.helger.peppolid.peppol.pidscheme;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.state.ETriState;
import com.helger.commons.string.StringHelper;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.factory.PeppolIdentifierFactory;

/**
 * This class manages the PEPPOL Participant identifier schemes using the
 * <b>iso6523-actorid-upis</b> scheme.
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
public final class ParticipantIdentifierSchemeManager
{
  private static final ICommonsList <IParticipantIdentifierScheme> s_aPISchemes = new CommonsArrayList <> ();

  static
  {
    // Add all predefined identifier issuing agencies
    for (final EPredefinedParticipantIdentifierScheme eIIA : EPredefinedParticipantIdentifierScheme.values ())
      s_aPISchemes.add (eIIA);
  }

  @PresentForCodeCoverage
  private static final ParticipantIdentifierSchemeManager s_aInstance = new ParticipantIdentifierSchemeManager ();

  private ParticipantIdentifierSchemeManager ()
  {}

  /**
   * @return A non-modifiable list of all PEPPOL identifier issuing agencies.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public static ICommonsList <? extends IParticipantIdentifierScheme> getAllSchemes ()
  {
    return s_aPISchemes.getClone ();
  }

  /**
   * Find the agency with the respective ISO6523 value.
   *
   * @param sISO6523Code
   *        The value to search. May be <code>null</code>.
   * @return <code>null</code> if no such agency exists.
   */
  @Nullable
  public static IParticipantIdentifierScheme getSchemeOfISO6523Code (@Nullable final String sISO6523Code)
  {
    if (StringHelper.hasText (sISO6523Code))
      for (final IParticipantIdentifierScheme aAgency : s_aPISchemes)
        if (aAgency.getISO6523Code ().equalsIgnoreCase (sISO6523Code))
          return aAgency;
    return null;
  }

  /**
   * Check if an agency with the given ISO6523 value exists.
   *
   * @param sISO6523Code
   *        The value to search. May be <code>null</code>.
   * @return <code>true</code> if such an agency exists, <code>false</code>
   *         otherwise.
   */
  public static boolean containsSchemeWithISO6523Code (@Nullable final String sISO6523Code)
  {
    return getSchemeOfISO6523Code (sISO6523Code) != null;
  }

  /**
   * Get the schemeID code of the passed ISO6523 code. If the passed ISO6523
   * code is unknown, <code>null</code> is returned.
   *
   * @param sISO6523Code
   *        The value to search. May be <code>null</code>.
   * @return The matching schemeID or <code>null</code> if no agency with the
   *         given ISO6523 code exists.
   */
  @Nullable
  public static String getSchemeIDOfISO6523Code (@Nullable final String sISO6523Code)
  {
    final IParticipantIdentifierScheme aAgency = getSchemeOfISO6523Code (sISO6523Code);
    return aAgency == null ? null : aAgency.getSchemeID ();
  }

  /**
   * Find the agency with the respective schemeID value.
   *
   * @param sSchemeID
   *        The value to search. May be <code>null</code>.
   * @return <code>null</code> if no such agency exists.
   */
  @Nullable
  public static IParticipantIdentifierScheme getSchemeOfSchemeID (@Nullable final String sSchemeID)
  {
    if (StringHelper.hasText (sSchemeID))
      for (final IParticipantIdentifierScheme aAgency : s_aPISchemes)
        if (aAgency.getSchemeID ().equalsIgnoreCase (sSchemeID))
          return aAgency;
    return null;
  }

  /**
   * Check if an agency with the given schemeID value exists.
   *
   * @param sSchemeID
   *        The value to search. May be <code>null</code>.
   * @return <code>true</code> if such an agency exists, <code>false</code>
   *         otherwise.
   */
  public static boolean containsSchemeWithSchemeID (@Nullable final String sSchemeID)
  {
    return getSchemeOfSchemeID (sSchemeID) != null;
  }

  /**
   * Get the ISO6523 code of the passed schemeID. If the passed schemeID is
   * unknown, <code>null</code> is returned.
   *
   * @param sSchemeID
   *        The value to search. May be <code>null</code>.
   * @return The matching ISO6523 code or <code>null</code> if no agency with
   *         the given schemeID exists.
   */
  @Nullable
  public static String getISO6523CodeOfSchemeID (@Nullable final String sSchemeID)
  {
    final IParticipantIdentifierScheme aScheme = getSchemeOfSchemeID (sSchemeID);
    return aScheme == null ? null : aScheme.getISO6523Code ();
  }

  /**
   * Check if the specified ISO6523 value references a deprecated issuing
   * agency.
   *
   * @param sISO6523Code
   *        The value to search. May be <code>null</code>.
   * @return {@link ETriState#TRUE} if and only if an agency with the passed
   *         value was found and is deprecated. {@link ETriState#FALSE} if the
   *         agency was found and is not deprecated. {@link ETriState#UNDEFINED}
   *         if no such agency exists.
   */
  @Nonnull
  public static ETriState isSchemeWithISO6523CodeDeprecated (@Nullable final String sISO6523Code)
  {
    final IParticipantIdentifierScheme aAgency = getSchemeOfISO6523Code (sISO6523Code);
    return aAgency == null ? ETriState.UNDEFINED : ETriState.valueOf (aAgency.isDeprecated ());
  }

  /**
   * Check if the specified scheme ID references a deprecated issuing agency.
   *
   * @param sSchemeID
   *        The value to search. May be <code>null</code>.
   * @return {@link ETriState#TRUE} if and only if an agency with the passed
   *         value was found and is deprecated. {@link ETriState#FALSE} if the
   *         agency was found and is not deprecated. {@link ETriState#UNDEFINED}
   *         if no such agency exists.
   */
  @Nonnull
  public static ETriState isSchemeWithSchemeIDDeprecated (@Nullable final String sSchemeID)
  {
    final IParticipantIdentifierScheme aAgency = getSchemeOfSchemeID (sSchemeID);
    return aAgency == null ? ETriState.UNDEFINED : ETriState.valueOf (aAgency.isDeprecated ());
  }

  /**
   * Find the identifier scheme that matches the provided participant ID. This
   * method checks only participants that use the default PEPPOL identifier
   * scheme `iso6523-actorid-upis`.
   *
   * @param aParticipantID
   *        The participant ID to search. May be <code>null</code>.
   * @return <code>null</code> if no such agency exists or if the participant ID
   *         is not suitable.
   * @since 5.2.5
   */
  @Nullable
  public static IParticipantIdentifierScheme getSchemeOfIdentifier (@Nullable final IParticipantIdentifier aParticipantID)
  {
    if (aParticipantID != null &&
        aParticipantID.hasScheme (PeppolIdentifierFactory.INSTANCE.getDefaultParticipantIdentifierScheme ()))
    {
      final String sValue = aParticipantID.getValue ();
      // Value must be at least something like "1234:"
      if (sValue.length () > 5)
        return getSchemeOfISO6523Code (sValue.substring (0, 4));
    }
    return null;
  }
}
