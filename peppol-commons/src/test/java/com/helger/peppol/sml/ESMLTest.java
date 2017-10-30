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
package com.helger.peppol.sml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Test class for class {@link ESML}.
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
public final class ESMLTest
{
  @Test
  public void testProduction ()
  {
    for (final ESML eSML : ESML.values ())
    {
      assertNotNull (eSML.getDNSZone ());
      assertNotNull (eSML.getPublisherDNSZone ());
      assertNotNull (eSML.getManagementServiceURL ());
      assertNotNull (eSML.getManageServiceMetaDataEndpointAddress ());
      assertNotNull (eSML.getManageParticipantIdentifierEndpointAddress ());
      eSML.isClientCertificateRequired ();
      assertSame (eSML, ESML.valueOf (eSML.name ()));
    }
  }

  @Test
  public void testNewProductionValues ()
  {
    assertEquals ("edelivery.tech.ec.europa.eu.", ESML.DIGIT_PRODUCTION.getDNSZone ());
    assertEquals ("publisher.edelivery.tech.ec.europa.eu.", ESML.DIGIT_PRODUCTION.getPublisherDNSZone ());
    assertEquals ("https://edelivery.tech.ec.europa.eu/edelivery-sml", ESML.DIGIT_PRODUCTION.getManagementServiceURL ());
    assertEquals ("https://edelivery.tech.ec.europa.eu/edelivery-sml/manageservicemetadata",
                  ESML.DIGIT_PRODUCTION.getManageServiceMetaDataEndpointAddress ().toExternalForm ());
    assertEquals ("https://edelivery.tech.ec.europa.eu/edelivery-sml/manageparticipantidentifier",
                  ESML.DIGIT_PRODUCTION.getManageParticipantIdentifierEndpointAddress ().toExternalForm ());

    assertEquals ("acc.edelivery.tech.ec.europa.eu.", ESML.DIGIT_TEST.getDNSZone ());
    assertEquals ("publisher.acc.edelivery.tech.ec.europa.eu.", ESML.DIGIT_TEST.getPublisherDNSZone ());
    assertEquals ("https://acc.edelivery.tech.ec.europa.eu/edelivery-sml", ESML.DIGIT_TEST.getManagementServiceURL ());
    assertEquals ("https://acc.edelivery.tech.ec.europa.eu/edelivery-sml/manageservicemetadata",
                  ESML.DIGIT_TEST.getManageServiceMetaDataEndpointAddress ().toExternalForm ());
    assertEquals ("https://acc.edelivery.tech.ec.europa.eu/edelivery-sml/manageparticipantidentifier",
                  ESML.DIGIT_TEST.getManageParticipantIdentifierEndpointAddress ().toExternalForm ());
  }
}
