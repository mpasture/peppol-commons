/**
 * Copyright (C) 2015 Philip Helger (www.helger.com)
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
package com.helger.peppol.sml;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotations.Nonempty;
import com.helger.commons.exceptions.InitializationException;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

/**
 * Simple enumeration for differentiating the different available SMLs.
 * <ul>
 * <li>DIGIT_PRODUCTION - DIGIT production URL - valid from June 9th, 2015
 * <li>DIGIT_TEST - DIGIT test URL - valid from June 9th, 2015
 * <li>PRODUCTION - used for production environment - valid until June 9th, 2015
 * </li>
 * <li>TEST - used for test environment - valid until June 9th, 2015</li>
 * <li>DEVELOPMENT - used for development environment - valid until June 9th,
 * 2015</li>
 * <li>DEVELOPMENT_LOCAL - used for development environment assuming the
 * management application is running on localhost in the ROOT context!</li>
 * </ul>
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
public enum ESML implements ISMLInfo, IHasID <String>
{
  /** DIGIT production URL - valid from June 9th, 2015 */
  DIGIT_PRODUCTION ("digitprod", "edelivery.tech.ec.europa.eu.", "https://edelivery.tech.ec.europa.eu/edelivery-sml", true),
  /** DIGIT test URL - valid from June 9th, 2015 */
  DIGIT_TEST ("digittest", "acc.edelivery.tech.ec.europa.eu.", "https://acc.edelivery.tech.ec.europa.eu/edelivery-sml", true),
  /** https://sml.peppolcentral.org - valid until June 9th, 2015 */
  PRODUCTION ("brzprod", "sml.peppolcentral.org.", "https://sml.peppolcentral.org", true),
  /** https://smk.peppolcentral.org - valid until June 9th, 2015 */
  TEST ("brztest", "smk.peppolcentral.org.", "https://smk.peppolcentral.org", true),
  /** http://plixvde2 - valid until June 9th, 2015 */
  DEVELOPMENT ("brzdev", "smj.peppolcentral.org.", "http://plixvde2/ServiceMetadataLocatorManagement", false),
  /** http://localhost:8080 */
  DEVELOPMENT_LOCAL ("local", "smj.peppolcentral.org.", "http://localhost:8080", false);

  private final String m_sID;
  private final String m_sDNSZone;
  private final String m_sManagementServiceURL;
  private final boolean m_bRequiresClientCertificate;
  private final URL m_aManageServiceMetaDataEndpointAddress;
  private final URL m_aManageParticipantIdentifierEndpointAddress;

  /**
   * Constructor
   *
   * @param sID
   *        The internal ID. Neither <code>null</code> nor empty.
   * @param sDNSZone
   *        DNS zone name. Must not start with a "." but must end with a "."
   * @param sManagementServiceURL
   *        The base service URL uses as the basis for participant and metadata
   *        management.
   * @param bRequiresClientCertificate
   *        <code>true</code> if this server requires a client certificate,
   *        <code>false</code> if not.
   * @throws MalformedURLException
   */
  private ESML (@Nonnull @Nonempty final String sID,
                @Nonnull @Nonempty final String sDNSZone,
                @Nonnull @Nonempty final String sManagementServiceURL,
                final boolean bRequiresClientCertificate)
  {
    m_sID = sID;
    m_sDNSZone = sDNSZone;
    m_sManagementServiceURL = sManagementServiceURL;
    m_bRequiresClientCertificate = bRequiresClientCertificate;
    try
    {
      // Create once
      m_aManageServiceMetaDataEndpointAddress = new URL (m_sManagementServiceURL +
                                                         '/' +
                                                         CSMLDefault.MANAGEMENT_SERVICE_METADATA);
      m_aManageParticipantIdentifierEndpointAddress = new URL (m_sManagementServiceURL +
                                                               '/' +
                                                               CSMLDefault.MANAGEMENT_SERVICE_PARTICIPANTIDENTIFIER);
    }
    catch (final MalformedURLException ex)
    {
      throw new InitializationException ("Failed to convert to URL", ex);
    }
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public String getDNSZone ()
  {
    return m_sDNSZone;
  }

  @Nonnull
  @Nonempty
  public String getPublisherDNSName ()
  {
    return CSMLDefault.DNS_PUBLISHER_SUBZONE + m_sDNSZone;
  }

  @Nonnull
  @Nonempty
  public String getManagementServiceURL ()
  {
    return m_sManagementServiceURL;
  }

  @Nonnull
  public URL getManageServiceMetaDataEndpointAddress ()
  {
    return m_aManageServiceMetaDataEndpointAddress;
  }

  @Nonnull
  public URL getManageParticipantIdentifierEndpointAddress ()
  {
    return m_aManageParticipantIdentifierEndpointAddress;
  }

  public boolean requiresClientCertificate ()
  {
    return m_bRequiresClientCertificate;
  }

  @Nullable
  public static ESML getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (ESML.class, sID);
  }

  @Nullable
  public static ESML getFromIDOrDefault (@Nullable final String sID, @Nullable final ESML eDefault)
  {
    return EnumHelper.getFromIDOrDefault (ESML.class, sID, eDefault);
  }
}
