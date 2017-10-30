/**
 * Copyright (C) 2015-2017 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * The Original Code is Copyright The PEPPOL project (http://www.peppol.eu)
 *
 * Version: MPL 2.0/EUPL 1.2
 * -
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * -
 * Alternatively, the contents of this file may be used under the
 * terms of the EUPL, Version 1.2 or - as soon they will be approved
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
 * -
 * If you wish to allow use of your version of this file only
 * under the terms of the EUPL License and not to allow others to use
 * your version of this file under the MPL, indicate your decision by
 * deleting the provisions above and replace them with the notice and
 * other provisions required by the EUPL License. If you do not delete
 * the provisions above, a recipient may use your version of this file
 * under either the MPL or the EUPL License.
 */
package com.helger.peppol.identifier.generic.doctype;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.compare.CompareHelper;
import com.helger.commons.compare.IComparator;
import com.helger.peppol.identifier.IIdentifier;

/**
 * Marker-interface that is specific for document type identifiers.<br>
 * This can be used as the read-only/immutable counterpart of the implementation
 * class.
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
public interface IDocumentTypeIdentifier extends IIdentifier
{
  /**
   * Check if the passed document type identifier has the same scheme and value
   * as this identifier. <code>equals</code> cannot be used in many cases,
   * because equals also checks if the implementation class is identical which
   * is not always the case.
   *
   * @param aOther
   *        The identifier to compare to. May be <code>null</code>.
   * @return <code>true</code> if the parameter is not <code>null</code> and has
   *         the same scheme and value as this
   */
  default boolean hasSameContent (@Nullable final IDocumentTypeIdentifier aOther)
  {
    // Check value before scheme because the possibility of a divergent value is
    // much higher
    return aOther != null && hasValue (aOther.getValue ()) && hasScheme (aOther.getScheme ());
  }

  default int compareTo (@Nonnull final IDocumentTypeIdentifier aOther)
  {
    int ret = CompareHelper.compare (getScheme (), aOther.getScheme ());
    if (ret == 0)
      ret = CompareHelper.compare (getValue (), aOther.getValue ());
    return ret;
  }

  @Nonnull
  static IComparator <IDocumentTypeIdentifier> comparator ()
  {
    return (a, b) -> a.compareTo (b);
  }
}
