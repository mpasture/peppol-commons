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
package com.helger.peppol.bdxr.smp1.marshal;

import com.helger.xsds.bdxr.smp1.ObjectFactory;
import com.helger.xsds.bdxr.smp1.ServiceMetadataType;

/**
 * A simple JAXB marshaller for the {@link ServiceMetadataType} type.
 *
 * @author Philip Helger
 */
public final class BDXR1MarshallerServiceMetadataType extends AbstractBDXR1Marshaller <ServiceMetadataType>
{
  public BDXR1MarshallerServiceMetadataType ()
  {
    super (ServiceMetadataType.class, true, new ObjectFactory ()::createServiceMetadata);
  }
}
