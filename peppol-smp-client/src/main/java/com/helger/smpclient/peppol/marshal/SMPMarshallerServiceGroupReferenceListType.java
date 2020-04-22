/**
 * Copyright (C) 2015-2020 Philip Helger
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.smpclient.peppol.marshal;

import com.helger.smpclient.peppol.jaxb.ObjectFactory;
import com.helger.smpclient.peppol.jaxb.ServiceGroupReferenceListType;

/**
 * A simple JAXB marshaller for the {@link ServiceGroupReferenceListType} type.
 *
 * @author Philip Helger
 */
public class SMPMarshallerServiceGroupReferenceListType extends AbstractSMPMarshaller <ServiceGroupReferenceListType>
{
  @Deprecated
  public SMPMarshallerServiceGroupReferenceListType ()
  {
    this (true);
  }

  public SMPMarshallerServiceGroupReferenceListType (final boolean bValidationEnabled)
  {
    super (ServiceGroupReferenceListType.class,
           bValidationEnabled,
           new ObjectFactory ()::createServiceGroupReferenceList);
  }
}
