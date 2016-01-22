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
package com.helger.peppol.identifier.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.helger.commons.mock.CommonsTestHelper;
import com.helger.commons.string.StringHelper;
import com.helger.peppol.identifier.CIdentifier;

/**
 * Test class for class {@link ReadOnlyProcessIdentifier}.
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
public final class ReadOnlyProcessIdentifierTest
{
  @Test
  public void testCtor ()
  {
    final ReadOnlyProcessIdentifier aID = new ReadOnlyProcessIdentifier ("scheme", "value");
    assertEquals ("scheme", aID.getScheme ());
    assertEquals ("value", aID.getValue ());

    final ReadOnlyProcessIdentifier aID2 = new ReadOnlyProcessIdentifier (aID);
    assertEquals ("scheme", aID2.getScheme ());
    assertEquals ("value", aID2.getValue ());
  }

  @Test
  public void testBasicMethods ()
  {
    final ReadOnlyProcessIdentifier aID1 = new ReadOnlyProcessIdentifier ("scheme", "value");
    final ReadOnlyProcessIdentifier aID2 = new ReadOnlyProcessIdentifier ("scheme", "value");
    final ReadOnlyProcessIdentifier aID3 = new ReadOnlyProcessIdentifier ("scheme2", "value");
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aID1, aID2);
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (aID1, aID3);
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (aID2, aID3);
  }

  @Test
  public void testURIStuff ()
  {
    final ReadOnlyProcessIdentifier aID1 = new ReadOnlyProcessIdentifier ("scheme1", "value1");
    assertEquals ("scheme1::value1", aID1.getURIEncoded ());
    assertEquals ("scheme1%3A%3Avalue1", aID1.getURIPercentEncoded ());
  }

  @Test
  public void testConstraints ()
  {
    try
    {
      // null key not allowed
      new ReadOnlyProcessIdentifier (null, "value");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null value not allowed
      new ReadOnlyProcessIdentifier (CIdentifier.DEFAULT_PROCESS_IDENTIFIER_SCHEME, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // Both null not allowed
      new ReadOnlyProcessIdentifier (null, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // Empty is not allowed
      new ReadOnlyProcessIdentifier (CIdentifier.DEFAULT_PROCESS_IDENTIFIER_SCHEME, "");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // Cannot be mapped to ISO-8859-1:
      new ReadOnlyProcessIdentifier (CIdentifier.DEFAULT_PROCESS_IDENTIFIER_SCHEME, "Љ");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // Scheme too long
      new ReadOnlyProcessIdentifier (CIdentifier.DEFAULT_PROCESS_IDENTIFIER_SCHEME +
                                         StringHelper.getRepeated ('a', CIdentifier.MAX_IDENTIFIER_SCHEME_LENGTH + 1),
                                     "abc");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // Value too long
      new ReadOnlyProcessIdentifier (CIdentifier.DEFAULT_PROCESS_IDENTIFIER_SCHEME,
                                     StringHelper.getRepeated ('a', CIdentifier.MAX_PROCESS_IDENTIFIER_VALUE_LENGTH + 1));
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
