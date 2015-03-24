/**
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
package com.helger.peppol.identifier.process;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.busdox.transport.identifiers._1.ProcessIdentifierType;

import com.helger.commons.annotations.UnsupportedOperation;
import com.helger.peppol.identifier.CIdentifier;
import com.helger.peppol.identifier.IReadonlyIdentifier;
import com.helger.peppol.identifier.IdentifierUtils;

/**
 * This is an immutable sanity class around the {@link ProcessIdentifierType}
 * class with easier construction and some sanity access methods. It may be used
 * in all places where {@link ProcessIdentifierType} objects are required.<br>
 * Important note: this class implements {@link #equals(Object)} and
 * {@link #hashCode()} where its base class does not. So be careful when mixing
 * this class and its base class!<br>
 * For a mutable version, please check {@link SimpleProcessIdentifier}.
 * 
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
@Immutable
public final class ReadonlyProcessIdentifier extends ProcessIdentifierType implements IPeppolProcessIdentifier
{
  public ReadonlyProcessIdentifier (@Nonnull final IReadonlyIdentifier aIdentifier)
  {
    this (aIdentifier.getScheme (), aIdentifier.getValue ());
  }

  public ReadonlyProcessIdentifier (@Nullable final String sScheme, @Nullable final String sValue)
  {
    if (!IdentifierUtils.isValidIdentifierScheme (sScheme))
      throw new IllegalArgumentException ("Process identifier scheme '" + sScheme + "' is invalid!");
    if (!IdentifierUtils.isValidProcessIdentifierValue (sValue))
      throw new IllegalArgumentException ("Process identifier value '" + sValue + "' is invalid!");

    // Explicitly use the super methods, as the methods of this class throw an
    // exception!
    super.setScheme (sScheme);
    super.setValue (sValue);
  }

  @Override
  @UnsupportedOperation
  public void setValue (@Nullable final String sValue)
  {
    // This is how we make things read-only :)
    throw new UnsupportedOperationException ("setValue is forbidden on this class!");
  }

  @Override
  @UnsupportedOperation
  public void setScheme (@Nullable final String sScheme)
  {
    // This is how we make things read-only :)
    throw new UnsupportedOperationException ("setScheme is forbidden on this class!");
  }

  public boolean isDefaultScheme ()
  {
    return IdentifierUtils.hasDefaultProcessIdentifierScheme (this);
  }

  @Nonnull
  public String getURIEncoded ()
  {
    return IdentifierUtils.getIdentifierURIEncoded (this);
  }

  @Nonnull
  public String getURIPercentEncoded ()
  {
    return IdentifierUtils.getIdentifierURIPercentEncoded (this);
  }

  @Nonnull
  public static ReadonlyProcessIdentifier createWithDefaultScheme (@Nullable final String sValue)
  {
    return new ReadonlyProcessIdentifier (CIdentifier.DEFAULT_PROCESS_IDENTIFIER_SCHEME, sValue);
  }
}
