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
package com.helger.peppol.bdxrclient;

import java.net.URI;

import javax.annotation.Nonnull;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.charset.CCharset;
import com.helger.commons.mime.CMimeType;
import com.helger.peppol.bdxr.ObjectFactory;
import com.helger.peppol.bdxr.ParticipantIdentifierType;
import com.helger.peppol.bdxr.ServiceGroupType;
import com.helger.peppol.bdxr.ServiceInformationType;
import com.helger.peppol.bdxr.ServiceMetadataType;
import com.helger.peppol.identifier.IDocumentTypeIdentifier;
import com.helger.peppol.identifier.IParticipantIdentifier;
import com.helger.peppol.identifier.IdentifierHelper;
import com.helger.peppol.sml.ISMLInfo;
import com.helger.peppol.smpclient.SMPHttpResponseHandlerWriteOperations;
import com.helger.peppol.smpclient.exception.SMPClientBadRequestException;
import com.helger.peppol.smpclient.exception.SMPClientException;
import com.helger.peppol.smpclient.exception.SMPClientNotFoundException;
import com.helger.peppol.smpclient.exception.SMPClientUnauthorizedException;
import com.helger.peppol.utils.BusdoxURLHelper;
import com.helger.web.http.CHTTPHeader;
import com.helger.web.http.basicauth.BasicAuthClientCredentials;

/**
 * This class is used for calling the BDXR SMP REST interface. This particular
 * class also contains the non-standard writing methods. It inherits all reading
 * methods from {@link BDXRClientReadOnly}.
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
public class BDXRClient extends BDXRClientReadOnly
{
  // The default text/xml content type uses iso-8859-1!
  private static final ContentType CONTENT_TYPE_TEXT_XML = ContentType.create (CMimeType.TEXT_XML.getAsString (),
                                                                               CCharset.CHARSET_UTF_8_OBJ);

  private static final ObjectFactory s_aObjFactory = new ObjectFactory ();

  /**
   * Constructor with SML lookup
   *
   * @param aParticipantIdentifier
   *        The participant identifier to be used. Required to build the SMP
   *        access URI.
   * @param aSMLInfo
   *        The SML to be used. Required to build the SMP access URI.
   * @see BusdoxURLHelper#getSMPURIOfParticipant(IParticipantIdentifier,
   *      ISMLInfo)
   */
  public BDXRClient (@Nonnull final IParticipantIdentifier aParticipantIdentifier, @Nonnull final ISMLInfo aSMLInfo)
  {
    super (aParticipantIdentifier, aSMLInfo);
  }

  /**
   * Constructor with SML lookup
   *
   * @param aParticipantIdentifier
   *        The participant identifier to be used. Required to build the SMP
   *        access URI.
   * @param sSMLZoneName
   *        The SML DNS zone name to be used. Required to build the SMP access
   *        URI. Must end with a trailing dot (".") and may neither be
   *        <code>null</code> nor empty to build a correct URL. May not start
   *        with "http://". Example: <code>sml.peppolcentral.org.</code>
   * @see BusdoxURLHelper#getSMPURIOfParticipant(IParticipantIdentifier, String)
   */
  public BDXRClient (@Nonnull final IParticipantIdentifier aParticipantIdentifier,
                     @Nonnull @Nonempty final String sSMLZoneName)
  {
    super (aParticipantIdentifier, sSMLZoneName);
  }

  /**
   * Constructor with a direct SMP URL.<br>
   * Remember: must be HTTP and using port 80 only!
   *
   * @param aSMPHost
   *        The address of the SMP service. Must be port 80 and basic http only
   *        (no https!). Example: http://smpcompany.company.org
   */
  public BDXRClient (@Nonnull final URI aSMPHost)
  {
    super (aSMPHost);
  }

  /**
   * Saves a service group. The meta data references should not be set and are
   * not used.
   *
   * @param aServiceGroup
   *        The service group to save.
   * @param aCredentials
   *        The user name and password to use as aCredentials.
   * @throws SMPClientException
   *         in case something goes wrong
   * @throws SMPClientUnauthorizedException
   *         The user name or password was not correct.
   * @throws SMPClientNotFoundException
   *         A HTTP Not Found was received. This can happen if the service was
   *         not found.
   * @throws SMPClientBadRequestException
   *         The request was not well formed.
   */
  public void saveServiceGroup (@Nonnull final ServiceGroupType aServiceGroup,
                                @Nonnull final BasicAuthClientCredentials aCredentials) throws SMPClientException
  {
    ValueEnforcer.notNull (aServiceGroup, "ServiceGroup");
    ValueEnforcer.notNull (aCredentials, "Credentials");

    try
    {
      final String sBody = new BDXRMarshallerServiceGroupType ().getAsXMLString (aServiceGroup);
      final Request aRequest = Request.Put (getSMPHostURI () +
                                            IdentifierHelper.getIdentifierURIPercentEncoded (aServiceGroup.getParticipantIdentifier ()))
                                      .addHeader (CHTTPHeader.AUTHORIZATION, aCredentials.getRequestValue ())
                                      .bodyString (sBody, CONTENT_TYPE_TEXT_XML);
      executeRequest (aRequest).handleResponse (new SMPHttpResponseHandlerWriteOperations ());
    }
    catch (final Exception ex)
    {
      throw getConvertedException (ex);
    }
  }

  /**
   * Saves a service group. The meta data references should not be set and are
   * not used.
   *
   * @param aParticipantID
   *        The participant identifier for which the service group is to save.
   * @param aCredentials
   *        The user name and password to use as credentials.
   * @return The created {@link ServiceGroupType} object.
   * @throws SMPClientException
   *         in case something goes wrong
   * @throws SMPClientUnauthorizedException
   *         The user name or password was not correct.
   * @throws SMPClientNotFoundException
   *         A HTTP Not Found was received. This can happen if the service was
   *         not found.
   * @throws SMPClientBadRequestException
   *         The request was not well formed.
   */
  @Nonnull
  public ServiceGroupType saveServiceGroup (@Nonnull final IParticipantIdentifier aParticipantID,
                                            @Nonnull final BasicAuthClientCredentials aCredentials) throws SMPClientException
  {
    ValueEnforcer.notNull (aParticipantID, "ParticipantID");
    ValueEnforcer.notNull (aCredentials, "Credentials");

    final ServiceGroupType aServiceGroup = s_aObjFactory.createServiceGroupType ();
    final ParticipantIdentifierType aBDXRID = new ParticipantIdentifierType ();
    aBDXRID.setScheme (aParticipantID.getScheme ());
    aBDXRID.setValue (aParticipantID.getValue ());
    aServiceGroup.setParticipantIdentifier (aBDXRID);
    saveServiceGroup (aServiceGroup, aCredentials);
    return aServiceGroup;
  }

  /**
   * Deletes a service group given by its service group id.
   *
   * @param aServiceGroupID
   *        The service group id of the service group to delete.
   * @param aCredentials
   *        The user name and password to use as aCredentials.
   * @throws SMPClientException
   *         in case something goes wrong
   * @throws SMPClientNotFoundException
   *         The service group id did not exist.
   * @throws SMPClientUnauthorizedException
   *         The user name or password was not correct.
   * @throws SMPClientBadRequestException
   *         The request was not well formed.
   */
  public void deleteServiceGroup (@Nonnull final IParticipantIdentifier aServiceGroupID,
                                  @Nonnull final BasicAuthClientCredentials aCredentials) throws SMPClientException
  {
    ValueEnforcer.notNull (aCredentials, "Credentials");

    try
    {
      final Request aRequest = Request.Delete (getSMPHostURI () +
                                               IdentifierHelper.getIdentifierURIPercentEncoded (aServiceGroupID))
                                      .addHeader (CHTTPHeader.AUTHORIZATION, aCredentials.getRequestValue ());
      executeRequest (aRequest).handleResponse (new SMPHttpResponseHandlerWriteOperations ());
    }
    catch (final Exception ex)
    {
      throw getConvertedException (ex);
    }
  }

  /**
   * Saves a service meta data object. The ServiceGroupReference value is
   * ignored.
   *
   * @param aServiceMetadata
   *        The service meta data object to save.
   * @param aCredentials
   *        The user name and password to use as aCredentials.
   * @throws SMPClientException
   *         in case something goes wrong
   * @throws SMPClientUnauthorizedException
   *         The user name or password was not correct.
   * @throws SMPClientNotFoundException
   *         A HTTP Not Found was received. This can happen if the service was
   *         not found.
   * @throws SMPClientBadRequestException
   *         The request was not well formed.
   */
  public void saveServiceRegistration (@Nonnull final ServiceMetadataType aServiceMetadata,
                                       @Nonnull final BasicAuthClientCredentials aCredentials) throws SMPClientException
  {
    ValueEnforcer.notNull (aServiceMetadata, "ServiceMetadata");
    ValueEnforcer.notNull (aServiceMetadata.getServiceInformation (), "ServiceMetadata.ServiceInformation");
    ValueEnforcer.notNull (aServiceMetadata.getServiceInformation ().getParticipantIdentifier (),
                           "ServiceMetadata.ServiceInformation.ParticipantIdentifier");
    ValueEnforcer.notNull (aServiceMetadata.getServiceInformation ().getDocumentIdentifier (),
                           "ServiceMetadata.ServiceInformation.DocumentIdentifier");
    ValueEnforcer.notNull (aCredentials, "Credentials");

    final ServiceInformationType aServiceInformation = aServiceMetadata.getServiceInformation ();
    final IParticipantIdentifier aServiceGroupID = aServiceInformation.getParticipantIdentifier ();
    final IDocumentTypeIdentifier aDocumentTypeID = aServiceInformation.getDocumentIdentifier ();

    try
    {
      final String sBody = new BDXRMarshallerServiceMetadataType ().getAsXMLString (aServiceMetadata);
      final Request aRequest = Request.Put (getSMPHostURI () +
                                            IdentifierHelper.getIdentifierURIPercentEncoded (aServiceGroupID) +
                                            "/services/" +
                                            IdentifierHelper.getIdentifierURIPercentEncoded (aDocumentTypeID))
                                      .addHeader (CHTTPHeader.AUTHORIZATION, aCredentials.getRequestValue ())
                                      .bodyString (sBody, CONTENT_TYPE_TEXT_XML);
      executeRequest (aRequest).handleResponse (new SMPHttpResponseHandlerWriteOperations ());
    }
    catch (final Exception ex)
    {
      throw getConvertedException (ex);
    }
  }

  /**
   * Deletes a service meta data object given by its service group id and its
   * document type.
   *
   * @param aServiceGroupID
   *        The service group id of the service meta data to delete.
   * @param aDocumentTypeID
   *        The document type of the service meta data to delete.
   * @param aCredentials
   *        The user name and password to use as aCredentials.
   * @throws SMPClientException
   *         in case something goes wrong
   * @throws SMPClientUnauthorizedException
   *         The user name or password was not correct.
   * @throws SMPClientNotFoundException
   *         The service meta data object did not exist.
   * @throws SMPClientBadRequestException
   *         The request was not well formed.
   */
  public void deleteServiceRegistration (@Nonnull final IParticipantIdentifier aServiceGroupID,
                                         @Nonnull final IDocumentTypeIdentifier aDocumentTypeID,
                                         @Nonnull final BasicAuthClientCredentials aCredentials) throws SMPClientException
  {
    ValueEnforcer.notNull (aServiceGroupID, "ServiceGroupID");
    ValueEnforcer.notNull (aDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notNull (aCredentials, "Credentials");

    try
    {
      final Request aRequest = Request.Delete (getSMPHostURI () +
                                               IdentifierHelper.getIdentifierURIPercentEncoded (aServiceGroupID) +
                                               "/services/" +
                                               IdentifierHelper.getIdentifierURIPercentEncoded (aDocumentTypeID))
                                      .addHeader (CHTTPHeader.AUTHORIZATION, aCredentials.getRequestValue ());
      executeRequest (aRequest).handleResponse (new SMPHttpResponseHandlerWriteOperations ());
    }
    catch (final Exception ex)
    {
      throw getConvertedException (ex);
    }
  }
}
