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
package com.helger.peppol.smp;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.lang.ICloneable;
import com.helger.commons.state.EChange;
import com.helger.commons.string.ToStringGenerator;
import com.helger.commons.type.ObjectType;

/**
 * A stand-alone implementation of the {@link ISMPTransportProfile}. For a set
 * of predefined transport profiles have a look at {@link ESMPTransportProfile}.
 */
@NotThreadSafe
public class SMPTransportProfile implements ISMPTransportProfile, ICloneable <SMPTransportProfile>
{
  public static final ObjectType OT = new ObjectType ("smp.transport.profile");
  public static final boolean DEFAULT_DEPRECATED = false;

  private final String m_sID;
  private String m_sName;
  private boolean m_bIsDeprecated;

  public SMPTransportProfile (@Nonnull final ISMPTransportProfile aOther)
  {
    this (aOther.getID (), aOther.getName (), aOther.isDeprecated ());
  }

  public SMPTransportProfile (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    this (sID, sName, DEFAULT_DEPRECATED);
  }

  public SMPTransportProfile (@Nonnull @Nonempty final String sID,
                              @Nonnull @Nonempty final String sName,
                              final boolean bIsDeprecated)
  {
    m_sID = ValueEnforcer.notEmpty (sID, "ID");
    setName (sName);
    setDeprecated (bIsDeprecated);
  }

  @Nonnull
  public ObjectType getObjectType ()
  {
    return OT;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nonnull
  public final EChange setName (@Nonnull @Nonempty final String sName)
  {
    ValueEnforcer.notEmpty (sName, "Name");
    if (sName.equals (m_sName))
      return EChange.UNCHANGED;
    m_sName = sName;
    return EChange.CHANGED;
  }

  public boolean isDeprecated ()
  {
    return m_bIsDeprecated;
  }

  @Nonnull
  public final EChange setDeprecated (final boolean bIsDeprecated)
  {
    if (bIsDeprecated == m_bIsDeprecated)
      return EChange.UNCHANGED;
    m_bIsDeprecated = bIsDeprecated;
    return EChange.CHANGED;
  }

  @Nonnull
  public SMPTransportProfile getClone ()
  {
    return new SMPTransportProfile (this);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final SMPTransportProfile rhs = (SMPTransportProfile) o;
    return m_sID.equals (rhs.m_sID);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sID).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID)
                                       .append ("Name", m_sName)
                                       .append ("Deprecated", m_bIsDeprecated)
                                       .getToString ();
  }
}
