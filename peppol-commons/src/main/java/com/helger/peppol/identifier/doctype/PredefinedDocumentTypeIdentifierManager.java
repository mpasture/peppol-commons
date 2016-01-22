/**
 * Copyright (C) 2015-2016 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
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
package com.helger.peppol.identifier.doctype;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.peppol.identifier.IdentifierHelper;

/**
 * This class manages the predefined PEPPOL document identifiers the
 * <b>busdox-docid-qns</b> scheme.<br>
 * This class provides sanity methods around
 * {@link EPredefinedDocumentTypeIdentifier} which would be to bogus to generate
 * them.
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
@Immutable
public final class PredefinedDocumentTypeIdentifierManager
{
  private static final Map <String, IPeppolPredefinedDocumentTypeIdentifier> s_aCodes = new HashMap <String, IPeppolPredefinedDocumentTypeIdentifier> ();

  static
  {
    // Add all predefined document identifier
    for (final EPredefinedDocumentTypeIdentifier eDocID : EPredefinedDocumentTypeIdentifier.values ())
      s_aCodes.put (eDocID.getValue (), eDocID);
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final PredefinedDocumentTypeIdentifierManager s_aInstance = new PredefinedDocumentTypeIdentifierManager ();

  private PredefinedDocumentTypeIdentifierManager ()
  {}

  /**
   * @return A non-modifiable list of all PEPPOL document identifiers.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public static Collection <IPeppolPredefinedDocumentTypeIdentifier> getAllDocumentTypeIdentifiers ()
  {
    return CollectionHelper.newList (s_aCodes.values ());
  }

  /**
   * @return A non-<code>null</code> list of all PEPPOL document identifier IDs.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public static Set <String> getAllDocumentTypeIdentifierIDs ()
  {
    return CollectionHelper.newSet (s_aCodes.keySet ());
  }

  /**
   * Find the document identifier with the given ID. This search is done case
   * sensitive.
   *
   * @param sDocTypeIDValue
   *        The value to search. Without any identifier scheme! May be
   *        <code>null</code>.
   * @return <code>null</code> if no such document identifier exists.
   */
  @Nullable
  public static IPeppolPredefinedDocumentTypeIdentifier getDocumentTypeIdentifierOfID (@Nullable final String sDocTypeIDValue)
  {
    if (sDocTypeIDValue != null)
      for (final Map.Entry <String, IPeppolPredefinedDocumentTypeIdentifier> aEntry : s_aCodes.entrySet ())
      {
        // Use case insensitive identifier value comparison
        if (IdentifierHelper.areDocumentTypeIdentifierValuesEqual (sDocTypeIDValue, aEntry.getKey ()))
          return aEntry.getValue ();
      }
    return null;
  }

  /**
   * Check if a document identifier with the given ID exists.
   *
   * @param sDocTypeIDValue
   *        The value to search. Without any identifier scheme! May be
   *        <code>null</code>.
   * @return <code>true</code> if such a document identifier exists,
   *         <code>false</code> otherwise.
   */
  public static boolean containsDocumentTypeIdentifierWithID (@Nullable final String sDocTypeIDValue)
  {
    return getDocumentTypeIdentifierOfID (sDocTypeIDValue) != null;
  }
}
