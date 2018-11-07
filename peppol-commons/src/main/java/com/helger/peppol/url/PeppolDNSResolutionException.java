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
package com.helger.peppol.url;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * New checked exception to be thrown if DNS resolution fails.
 *
 * @author Philip Helger
 * @since 6.2.0
 */
public class PeppolDNSResolutionException extends Exception
{
  public PeppolDNSResolutionException (@Nonnull final String sMessage)
  {
    super (sMessage);
  }

  public PeppolDNSResolutionException (@Nonnull final String sMessage, @Nullable final Throwable aCause)
  {
    super (sMessage, aCause);
  }
}