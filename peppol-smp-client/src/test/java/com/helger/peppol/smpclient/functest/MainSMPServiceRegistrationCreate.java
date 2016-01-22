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
package com.helger.peppol.smpclient.functest;

import java.net.URI;

import javax.annotation.Nonnull;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.datetime.PDTFactory;
import com.helger.peppol.identifier.doctype.SimpleDocumentTypeIdentifier;
import com.helger.peppol.identifier.participant.SimpleParticipantIdentifier;
import com.helger.peppol.identifier.process.SimpleProcessIdentifier;
import com.helger.peppol.smp.ESMPTransportProfile;
import com.helger.peppol.smp.EndpointType;
import com.helger.peppol.smp.ObjectFactory;
import com.helger.peppol.smp.ProcessListType;
import com.helger.peppol.smp.ProcessType;
import com.helger.peppol.smp.ServiceEndpointList;
import com.helger.peppol.smp.ServiceInformationType;
import com.helger.peppol.smp.ServiceMetadataType;
import com.helger.peppol.smpclient.MockSMPClientConfig;
import com.helger.peppol.smpclient.SMPClient;
import com.helger.web.http.basicauth.BasicAuthClientCredentials;

/**
 * @author philip
 */
public final class MainSMPServiceRegistrationCreate
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainSMPServiceRegistrationCreate.class);

  // SMP ObjectFactory
  private static final ObjectFactory s_aOF = new ObjectFactory ();

  @Nonnull
  private static LocalDateTime _createDate (final int nYear, final int nMonth, final int nDayOfMonth)
  {
    return PDTFactory.createLocalDateTime (nYear, nMonth, nDayOfMonth);
  }

  public static void main (final String [] args) throws Exception
  {
    final URI SMP_URI = MockSMPClientConfig.getSMPURI ();
    final BasicAuthClientCredentials SMP_CREDENTIALS = MockSMPClientConfig.getSMPCredentials ();
    final SimpleParticipantIdentifier PARTICIPANT_ID = MockSMPClientConfig.getParticipantID ();
    final SimpleDocumentTypeIdentifier DOCUMENT_ID = MockSMPClientConfig.getDocumentTypeID ();
    final SimpleProcessIdentifier PROCESS_ID = MockSMPClientConfig.getProcessTypeID ();
    final W3CEndpointReference START_AP_ENDPOINTREF = MockSMPClientConfig.getAPEndpointRef ();
    final String AP_CERT_STRING = MockSMPClientConfig.getAPCert ();
    final String AP_SERVICE_DESCRIPTION = MockSMPClientConfig.getAPServiceDescription ();
    final String AP_CONTACT_URL = MockSMPClientConfig.getAPContact ();
    final String AP_INFO_URL = MockSMPClientConfig.getAPInfo ();

    // The main SMP client
    final SMPClient aClient = new SMPClient (SMP_URI);

    // Create the service registration
    final ServiceMetadataType aServiceMetadata = s_aOF.createServiceMetadataType ();
    {
      final ServiceInformationType aServiceInformation = s_aOF.createServiceInformationType ();
      {
        final ProcessListType aProcessList = s_aOF.createProcessListType ();
        {
          final ProcessType aProcess = s_aOF.createProcessType ();
          {
            final ServiceEndpointList aServiceEndpointList = s_aOF.createServiceEndpointList ();
            {
              final EndpointType aEndpoint = s_aOF.createEndpointType ();
              aEndpoint.setEndpointReference (START_AP_ENDPOINTREF);
              aEndpoint.setTransportProfile (ESMPTransportProfile.TRANSPORT_PROFILE_AS2.getID ());
              aEndpoint.setCertificate (AP_CERT_STRING);
              aEndpoint.setServiceActivationDate (_createDate (2011, DateTimeConstants.JANUARY, 1));
              aEndpoint.setServiceExpirationDate (_createDate (2020, DateTimeConstants.DECEMBER, 31));
              aEndpoint.setServiceDescription (AP_SERVICE_DESCRIPTION);
              aEndpoint.setTechnicalContactUrl (AP_CONTACT_URL);
              aEndpoint.setTechnicalInformationUrl (AP_INFO_URL);
              aEndpoint.setMinimumAuthenticationLevel ("1");
              aEndpoint.setRequireBusinessLevelSignature (false);
              aServiceEndpointList.getEndpoint ().add (aEndpoint);
            }
            aProcess.setProcessIdentifier (PROCESS_ID);
            aProcess.setServiceEndpointList (aServiceEndpointList);
          }
          aProcessList.getProcess ().add (aProcess);
        }
        aServiceInformation.setDocumentIdentifier (DOCUMENT_ID);
        aServiceInformation.setParticipantIdentifier (PARTICIPANT_ID);
        aServiceInformation.setProcessList (aProcessList);
      }
      aServiceMetadata.setServiceInformation (aServiceInformation);
    }
    aClient.saveServiceRegistration (aServiceMetadata, SMP_CREDENTIALS);

    s_aLogger.info ("Done");
  }
}
