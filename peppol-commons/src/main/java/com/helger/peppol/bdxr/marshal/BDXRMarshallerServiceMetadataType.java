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
package com.helger.peppol.bdxr.marshal;

import com.helger.peppol.bdxr.ObjectFactory;
import com.helger.peppol.bdxr.ServiceMetadataType;

/**
 * A simple JAXB marshaller for the {@link ServiceMetadataType} type.
 *
 * @author Philip Helger
 */
public final class BDXRMarshallerServiceMetadataType extends AbstractBDXRMarshaller <ServiceMetadataType>
{
  public BDXRMarshallerServiceMetadataType ()
  {
    super (ServiceMetadataType.class, x -> new ObjectFactory ().createServiceMetadata (x));
  }
}
