/**
 * Copyright (C) 2015-2017 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * The Original Code is Copyright The PEPPOL project (http://www.peppol.eu)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.helger.peppol.smpclient.functest;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.peppol.identifier.generic.participant.IParticipantIdentifier;
import com.helger.peppol.smp.CompleteServiceGroupType;
import com.helger.peppol.smp.ServiceMetadataType;
import com.helger.peppol.smpclient.MockSMPClientConfig;
import com.helger.peppol.smpclient.SMPClient;
import com.helger.peppol.smpclient.utils.SMPDebugHelper;

/**
 * @author philip
 */
public final class MainSMPServiceGroupCompleteList
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainSMPServiceGroupCompleteList.class);

  public static void main (final String [] args) throws Exception
  {
    final URI SMP_URI = MockSMPClientConfig.getSMPURI ();
    final IParticipantIdentifier PARTICIPANT_ID = MockSMPClientConfig.getParticipantID ();

    // The main SMP client
    final SMPClient aClient = new SMPClient (SMP_URI);

    // Get the service group reference list
    final CompleteServiceGroupType aCompleteServiceGroup = aClient.getCompleteServiceGroupOrNull (PARTICIPANT_ID);

    if (aCompleteServiceGroup == null)
      s_aLogger.error ("Failed to get complete service group for " + PARTICIPANT_ID);
    else
    {
      s_aLogger.info (SMPDebugHelper.getAsString (aCompleteServiceGroup.getServiceGroup ()));
      for (final ServiceMetadataType aServiceMetadata : aCompleteServiceGroup.getServiceMetadata ())
        s_aLogger.info (SMPDebugHelper.getAsString (aServiceMetadata));
    }

    s_aLogger.info ("Done");
  }
}
