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
package com.helger.peppol.identifier.bdxr.smp1;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.string.StringHelper;
import com.helger.commons.url.URLHelper;

/**
 * Helper methods for OASIS BDXR SMP identifiers.
 *
 * @author Philip Helger
 */
@Immutable
public final class BDXR1IdentifierHelper
{
  @PresentForCodeCoverage
  private static final BDXR1IdentifierHelper s_aInstance = new BDXR1IdentifierHelper ();

  private BDXR1IdentifierHelper ()
  {}

  /**
   * Check if the given identifier is valid. It is valid if it is empty or a
   * valid URI.<br>
   * The scheme of the participant identifier MUST be in the form of a URI.<br>
   * The scheme of the document identifier MUST be in the form of a URI.
   *
   * @param sScheme
   *        The scheme to check.
   * @return <code>true</code> if the passed scheme is a valid identifier
   *         scheme, <code>false</code> otherwise.
   */
  public static boolean isValidIdentifierScheme (@Nullable final String sScheme)
  {
    if (StringHelper.hasNoText (sScheme))
      return true;
    return URLHelper.getAsURI (sScheme) != null;
  }

  /**
   * Check if an identifier value is valid. Currently this check always returns
   * true.
   *
   * @param sValue
   *        The value to check. May be <code>null</code>.
   * @return <code>true</code> if the passed value is valid, <code>false</code>
   *         otherwise.
   */
  public static boolean isValidIdentifierValue (@Nullable final String sValue)
  {
    return true;
  }
}
