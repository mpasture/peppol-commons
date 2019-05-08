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
package com.helger.peppol.smpclient.functest;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.http.basicauth.BasicAuthClientCredentials;
import com.helger.peppol.identifier.IParticipantIdentifier;
import com.helger.peppol.smp.ServiceGroupType;
import com.helger.peppol.smpclient.MockSMPClientConfig;
import com.helger.peppol.smpclient.SMPClient;

/**
 * Check if an SMP installation is working. Prior to executing this class, make
 * sure that the file <code>functest.properties</code> filled correctly!
 *
 * @author Philip Helger
 */
public final class MainCheckIfSMPIsWorking
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainCheckIfSMPIsWorking.class);

  public static void main (final String [] args) throws Exception
  {
    final URI SMP_URI = MockSMPClientConfig.getSMPURI ();
    final BasicAuthClientCredentials SMP_CREDENTIALS = MockSMPClientConfig.getSMPCredentials ();
    final IParticipantIdentifier PARTICIPANT_ID = MockSMPClientConfig.getParticipantID ();

    // The main SMP client
    final SMPClient aClient = new SMPClient (SMP_URI);

    // Ensure that the service group does not exist
    LOGGER.info ("Ensuring that the service group is not existing!");
    if (aClient.getServiceGroupOrNull (PARTICIPANT_ID) != null)
    {
      LOGGER.info ("Deleting existing service group for init");
      aClient.deleteServiceGroup (PARTICIPANT_ID, SMP_CREDENTIALS);
      LOGGER.info ("Finished deletion of service group");
    }

    // Create, read and delete the service group
    LOGGER.info ("Creating the new service group");
    aClient.saveServiceGroup (PARTICIPANT_ID, SMP_CREDENTIALS);

    LOGGER.info ("Retrieving the service group");
    final ServiceGroupType aSGT = aClient.getServiceGroup (PARTICIPANT_ID);
    if (!aSGT.getParticipantIdentifier ().equals (PARTICIPANT_ID))
      throw new IllegalStateException ("Participant identifiers are not equal!");

    LOGGER.info ("Deleting the service group again");
    aClient.deleteServiceGroup (PARTICIPANT_ID, SMP_CREDENTIALS);

    LOGGER.info ("Checking if the service group is really deleted");
    if (aClient.getServiceGroupOrNull (PARTICIPANT_ID) != null)
      throw new IllegalStateException ("Deletion of the service group failed!");

    LOGGER.info ("Seems like the SMP is working as expected!");
  }
}
