/**
 * Copyright (C) 2015-2017 Philip Helger (www.helger.com)
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
package com.helger.peppol.smp;

import java.io.Serializable;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.name.IHasName;
import com.helger.commons.type.ITypedObject;

/**
 * Base interface for SMP transport profiles. Two transport profiles are
 * considered equal if the IDs are equal. The name of a transport profile has no
 * semantic meaning and is only present for interpretation by humans.
 *
 * @author Philip Helger
 * @see ESMPTransportProfile for a set of predefined transport profiles
 */
@MustImplementEqualsAndHashcode
public interface ISMPTransportProfile extends ITypedObject <String>, IHasName, Serializable
{
  /**
   * Get the ID to be stored in an SMP endpoint.
   */
  @Nonnull
  @Nonempty
  String getID ();

  /**
   * The display name of this transport profile has no semantics and is just for
   * informational purposes. May neither be <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  String getName ();

  /**
   * Check if this transport profile is deprecated or not.
   *
   * @return <code>true</code> if it is deprecated, <code>false</code> if not.
   * @since 5.2.6
   */
  default boolean isDeprecated ()
  {
    return false;
  }
}
