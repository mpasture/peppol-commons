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
package com.helger.peppol.smpclient;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;

import com.helger.commons.ValueEnforcer;
import com.helger.jaxb.AbstractJAXBMarshaller;

/**
 * This is the Apache HTTP client response handler to verify unsigned HTTP
 * response messages.
 *
 * @author Philip Helger
 * @param <T>
 *        The type of object to be handled.
 */
public final class SMPHttpResponseHandlerUnsigned <T> implements ResponseHandler <T>
{
  private final AbstractJAXBMarshaller <T> m_aMarshaller;

  public SMPHttpResponseHandlerUnsigned (@Nonnull final AbstractJAXBMarshaller <T> aMarshaller)
  {
    m_aMarshaller = ValueEnforcer.notNull (aMarshaller, "Marshaller");
  }

  @Nonnull
  public T handleResponse (@Nonnull final HttpResponse aHttpResponse) throws IOException
  {
    final StatusLine aStatusLine = aHttpResponse.getStatusLine ();
    final HttpEntity aEntity = aHttpResponse.getEntity ();
    if (aStatusLine.getStatusCode () >= 300)
      throw new HttpResponseException (aStatusLine.getStatusCode (), aStatusLine.getReasonPhrase ());
    if (aEntity == null)
      throw new ClientProtocolException ("Response from SMP server contains no content");

    // Read the payload
    final T ret = m_aMarshaller.read (aEntity.getContent ());
    if (ret == null)
      throw new ClientProtocolException ("Malformed XML document returned from SMP server");
    return ret;
  }

  @Nonnull
  public static <U> SMPHttpResponseHandlerUnsigned <U> create (@Nonnull final AbstractJAXBMarshaller <U> aMarshaller)
  {
    return new SMPHttpResponseHandlerUnsigned <U> (aMarshaller);
  }
}
