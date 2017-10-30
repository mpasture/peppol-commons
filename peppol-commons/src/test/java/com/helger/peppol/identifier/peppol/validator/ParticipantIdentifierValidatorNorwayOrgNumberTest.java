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
package com.helger.peppol.identifier.peppol.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link ParticipantIdentifierValidatorNorwayOrgNumber}.
 * 
 * @author philip
 */
public final class ParticipantIdentifierValidatorNorwayOrgNumberTest
{
  @Test
  public void test02 ()
  {
    assertTrue (ParticipantIdentifierValidatorNorwayOrgNumber.isValidOrganisationNumber ("123456785"));
    assertTrue (ParticipantIdentifierValidatorNorwayOrgNumber.isValidOrganisationNumber ("976098897"));
    assertTrue (ParticipantIdentifierValidatorNorwayOrgNumber.isValidOrganisationNumber ("991618112"));

    assertFalse (ParticipantIdentifierValidatorNorwayOrgNumber.isValidOrganisationNumber (null));
    assertFalse (ParticipantIdentifierValidatorNorwayOrgNumber.isValidOrganisationNumber (""));
    assertFalse (ParticipantIdentifierValidatorNorwayOrgNumber.isValidOrganisationNumber ("976098897 "));
    assertFalse (ParticipantIdentifierValidatorNorwayOrgNumber.isValidOrganisationNumber ("12345678-"));
    assertFalse (ParticipantIdentifierValidatorNorwayOrgNumber.isValidOrganisationNumber ("-23456789"));
    assertFalse (ParticipantIdentifierValidatorNorwayOrgNumber.isValidOrganisationNumber ("123456784"));
    assertFalse (ParticipantIdentifierValidatorNorwayOrgNumber.isValidOrganisationNumber ("323456780"));
  }
}
