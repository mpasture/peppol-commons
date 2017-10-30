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
package com.helger.peppol.url;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.StringHelper;
import com.helger.commons.timing.StopWatch;
import com.helger.commons.url.URLHelper;

public class MainTestNAPTR
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainTestNAPTR.class);

  @Nullable
  private static String _getAppliedNAPTRRegEx (@Nonnull final String sRegEx, @Nonnull final String sDomainName)
  {
    final char cSep = sRegEx.charAt (0);
    final int nSecond = sRegEx.indexOf (cSep, 1);
    if (nSecond < 0)
      return null;
    final String sEre = sRegEx.substring (1, nSecond);
    final int nThird = sRegEx.indexOf (cSep, nSecond + 1);
    if (nThird < 0)
      return null;
    final String sRepl = sRegEx.substring (nSecond + 1, nThird);
    final String sFlags = sRegEx.substring (nThird + 1);

    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("NAPTR regex: '" + sEre + "' - '" + sRepl + "' - '" + sFlags + "'");

    final int nOptions = "i".equalsIgnoreCase (sFlags) ? Pattern.CASE_INSENSITIVE : 0;
    final String ret = RegExHelper.stringReplacePattern (sEre, nOptions, sDomainName, sRepl);

    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("  NAPTR replacement: '" + sDomainName + "' -> '" + ret + "'");
    return ret;
  }

  @Nullable
  private static String _resolveFromNAPTR (@Nonnull final String sDNSName) throws TextParseException
  {
    if (StringHelper.hasNoText (sDNSName))
      return null;

    final Lookup aLookup = new Lookup (sDNSName, Type.NAPTR);
    Record [] aRecords;
    do
    {
      aRecords = aLookup.run ();
    } while (aLookup.getResult () == Lookup.TRY_AGAIN);

    if (aLookup.getResult () != Lookup.SUCCESSFUL)
    {
      // Wrong domain name
      s_aLogger.warn ("Error looking up '" + sDNSName + "': " + aLookup.getErrorString ());
      return null;
    }

    final ICommonsList <NAPTRRecord> aMatchingRecords = new CommonsArrayList <> ();
    for (final Record record : aRecords)
    {
      final NAPTRRecord naptrRecord = (NAPTRRecord) record;
      if ("U".equalsIgnoreCase (naptrRecord.getFlags ()) && "Meta:SMP".equals (naptrRecord.getService ()))
        aMatchingRecords.add (naptrRecord);
    }

    if (aMatchingRecords.isEmpty ())
    {
      // No matching NAPTR present
      s_aLogger.warn ("No matching DNS NAPTR records returned for '" + sDNSName + "'");
      return null;
    }

    // Sort by order than by preference according to RFC 2915
    aMatchingRecords.sort ( (x, y) -> {
      int ret = x.getOrder () - y.getOrder ();
      if (ret == 0)
        ret = x.getPreference () - y.getPreference ();
      return ret;
    });
    for (final NAPTRRecord aRecord : aMatchingRecords)
    {
      // The "U" record is terminal, so a RegExp must be present
      final String sRegEx = aRecord.getRegexp ();
      if (StringHelper.getLength (sRegEx) > 3)
      {
        final String sFinalDNSName = _getAppliedNAPTRRegEx (sRegEx, sDNSName);
        if (sFinalDNSName != null)
        {
          if (s_aLogger.isDebugEnabled ())
            s_aLogger.debug ("Using '" + sFinalDNSName + "' for original DNS name '" + sDNSName + "'");
          return sFinalDNSName;
        }
      }
    }

    // Weird - no regexp present
    s_aLogger.warn ("None of the matching DNS NAPTR records for '" +
                    sDNSName +
                    "' has a valid regular expression. Details: " +
                    aMatchingRecords);
    return null;
  }

  public static void main (final String [] args) throws TextParseException
  {
    // Warm up cache
    URLHelper.getAsURI ("http://www.example.org");
    _getAppliedNAPTRRegEx ("!^.*$!http://EHEALTH-TEST-UPRC.publisher.acc.edelivery.tech.ec.europa.eu!", "bla");

    // D2CQSE7LLUJ6H2AR32Z4RCK2M3QXCNQHB7K7QXNUMIYR5ESSXXPQ.ehealth-actorid-qns.acc.edelivery.tech.ec.europa.eu.
    // 60 IN NAPTR 100 10 "U" "Meta:SMP"
    // "!^.*$!http://EHEALTH-TEST-UPRC.publisher.acc.edelivery.tech.ec.europa.eu!"
    // .
    final String sDN = "D2CQSE7LLUJ6H2AR32Z4RCK2M3QXCNQHB7K7QXNUMIYR5ESSXXPQ.ehealth-actorid-qns.acc.edelivery.tech.ec.europa.eu";
    for (int i = 0; i < 10; ++i)
    {
      final StopWatch aSW = StopWatch.createdStarted ();
      final String sURI = _resolveFromNAPTR (sDN);
      s_aLogger.info (aSW.stopAndGetMillis () + "ms " + sURI);
    }
  }
}
