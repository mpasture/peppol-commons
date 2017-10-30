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
package com.helger.peppol.identifier.peppol.doctype;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.peppol.identifier.generic.doctype.IBusdoxDocumentTypeIdentifierParts;

/**
 * Contains all the different fields of a document identifier for PEPPOL in BIS
 * V1 style. Note: the sub type identifier is specified in more detail than in
 * BusDox: <code>&lt;customization id&gt;::&lt;version&gt;</code> even more
 * detailed the customization ID can be split further:
 * <code>&lt;transactionId&gt;:#&lt;extensionId&gt;[#&lt;extensionId&gt;]::&lt;version&gt;</code>
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
public interface IPeppolDocumentTypeIdentifierParts extends IBusdoxDocumentTypeIdentifierParts
{
  /**
   * @return The transaction ID
   */
  @Nonnull
  @Nonempty
  String getTransactionID ();

  /**
   * @return The contained extension IDs
   */
  @Nonnull
  @Nonempty
  ICommonsList <String> getExtensionIDs ();

  /**
   * @return The version number
   */
  @Nonnull
  @Nonempty
  String getVersion ();

  /**
   * @return transaction ID + extension IDs (no version number)
   */
  @Nonnull
  @Nonempty
  String getAsUBLCustomizationID ();
}
