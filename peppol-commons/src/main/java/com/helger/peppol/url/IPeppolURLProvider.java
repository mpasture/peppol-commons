/**
 * Copyright (C) 2015-2018 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * The Original Code is Copyright The PEPPOL project (http://www.peppol.eu)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.helger.peppol.url;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.peppol.identifier.generic.participant.IParticipantIdentifier;
import com.helger.peppol.sml.ISMLInfo;

/**
 * Base interface for a customizable URL provider so that different URL encoding
 * schemes can be used.
 *
 * @author Philip Helger
 */
public interface IPeppolURLProvider extends Serializable
{
  /**
   * Get DNS record from ParticipantIdentifier.<br>
   * Example PEPPOL PI <code>iso6523-actorid-upis::0010:1234</code> using BDX
   * scheme would result in
   * <code>B-&lt;hash over PI-Value&gt;.&lt;PI-Scheme&gt;.&lt;sml-zone-name&gt;</code>
   * . This method ensures that the hash value is created from the UTF-8 lower
   * case value of the identifier. The result string never ends with a dot!
   *
   * @param aParticipantIdentifier
   *        Participant identifier. May not be <code>null</code>.
   * @param aSMLInfo
   *        The SML information object to be used. May not be <code>null</code>.
   * @return DNS record
   * @throws PeppolDNSResolutionException
   *         If the URL resolution failed.
   */
  @Nonnull
  default String getDNSNameOfParticipant (@Nonnull final IParticipantIdentifier aParticipantIdentifier,
                                          @Nonnull final ISMLInfo aSMLInfo) throws PeppolDNSResolutionException
  {
    ValueEnforcer.notNull (aParticipantIdentifier, "ParticipantIdentifier");
    ValueEnforcer.notNull (aSMLInfo, "SMLInfo");
    return getDNSNameOfParticipant (aParticipantIdentifier, aSMLInfo.getDNSZone ());
  }

  /**
   * Get DNS record from ParticipantIdentifier.<br>
   * Example PEPPOL PI <code>iso6523-actorid-upis::0010:1234</code> using BDX
   * scheme would result in
   * <code>B-&lt;hash over PI-Value&gt;.&lt;PI-Scheme&gt;.&lt;sml-zone-name&gt;</code>
   * . This method ensures that the hash value is created from the UTF-8 lower
   * case value of the identifier. The result string never ends with a dot!
   *
   * @param aParticipantIdentifier
   *        Participant identifier. May not be <code>null</code>.
   * @param sSMLZoneName
   *        e.g. <code>sml.peppolcentral.org.</code>. May be empty. If it is not
   *        empty, it must end with a dot!
   * @return DNS record. It does not contain any prefix like
   *         <code>http://</code> or any path suffix. It is the plain DNS host
   *         name. Since version 1.1.4 this method returns the DNS name without
   *         the trailing dot!
   * @throws PeppolDNSResolutionException
   *         If the URL resolution failed.
   * @throws IllegalArgumentException
   *         In case one argument is invalid
   */
  @Nonnull
  String getDNSNameOfParticipant (@Nonnull final IParticipantIdentifier aParticipantIdentifier,
                                  @Nullable final String sSMLZoneName) throws PeppolDNSResolutionException;

  /**
   * Get the SMP URI of the passed participant ID in the provided SML DNS zone
   * name.
   *
   * @param aParticipantIdentifier
   *        The participant ID. May not be <code>null</code>.
   * @param aSMLInfo
   *        The SML zone to use. May not be <code>null</code>.
   * @return A new URI starting with "http://" and never ending with a slash.
   * @throws PeppolDNSResolutionException
   *         If the URL resolution failed.
   * @see #getSMPURIOfParticipant(IParticipantIdentifier, String)
   * @see #getSMPURLOfParticipant(IParticipantIdentifier, ISMLInfo)
   * @see #getSMPURLOfParticipant(IParticipantIdentifier, String)
   */
  @Nonnull
  default URI getSMPURIOfParticipant (@Nonnull final IParticipantIdentifier aParticipantIdentifier,
                                      @Nonnull final ISMLInfo aSMLInfo) throws PeppolDNSResolutionException
  {
    ValueEnforcer.notNull (aParticipantIdentifier, "ParticipantIdentifier");
    ValueEnforcer.notNull (aSMLInfo, "SMLInfo");
    return getSMPURIOfParticipant (aParticipantIdentifier, aSMLInfo.getDNSZone ());
  }

  /**
   * Get the SMP URI of the passed participant ID in the provided SML DNS zone
   * name.
   *
   * @param aParticipantIdentifier
   *        The participant ID. May not be <code>null</code>.
   * @param sSMLZoneName
   *        The SML zone to use. May be <code>null</code>.
   * @return A new URI starting with "http://" and never ending with a slash.
   * @throws PeppolDNSResolutionException
   *         If the URL resolution failed.
   * @see #getSMPURIOfParticipant(IParticipantIdentifier, ISMLInfo)
   * @see #getSMPURLOfParticipant(IParticipantIdentifier, ISMLInfo)
   * @see #getSMPURLOfParticipant(IParticipantIdentifier, String)
   */
  @Nonnull
  default URI getSMPURIOfParticipant (@Nonnull final IParticipantIdentifier aParticipantIdentifier,
                                      @Nullable final String sSMLZoneName) throws PeppolDNSResolutionException
  {
    ValueEnforcer.notNull (aParticipantIdentifier, "ParticipantIdentifier");

    final String sURIString = "http://" + getDNSNameOfParticipant (aParticipantIdentifier, sSMLZoneName);

    try
    {
      return new URI (sURIString);
    }
    catch (final URISyntaxException ex)
    {
      throw new IllegalArgumentException ("Error building SMP URI from string '" + sURIString + "'", ex);
    }
  }

  /**
   * Get the SMP URL of the passed participant ID in the provided SML DNS zone
   * name.
   *
   * @param aParticipantIdentifier
   *        The participant ID. May not be <code>null</code>.
   * @param aSMLInfo
   *        The SML zone to use. May not be <code>null</code>.
   * @return A new URL with scheme "http:" and never ending with a slash.
   * @throws PeppolDNSResolutionException
   *         If the URL resolution failed.
   * @see #getSMPURIOfParticipant(IParticipantIdentifier, String)
   * @see #getSMPURIOfParticipant(IParticipantIdentifier, ISMLInfo)
   * @see #getSMPURLOfParticipant(IParticipantIdentifier, String)
   */
  @Nonnull
  default URL getSMPURLOfParticipant (@Nonnull final IParticipantIdentifier aParticipantIdentifier,
                                      @Nonnull final ISMLInfo aSMLInfo) throws PeppolDNSResolutionException
  {
    ValueEnforcer.notNull (aParticipantIdentifier, "ParticipantIdentifier");
    ValueEnforcer.notNull (aSMLInfo, "SMLInfo");

    return getSMPURLOfParticipant (aParticipantIdentifier, aSMLInfo.getDNSZone ());
  }

  /**
   * Get the SMP URL of the passed participant ID in the provided SML DNS zone
   * name.
   *
   * @param aParticipantIdentifier
   *        The participant ID. May not be <code>null</code>.
   * @param sSMLZoneName
   *        The SML zone name to use. May be <code>null</code>.
   * @return A new URL with scheme "http:" and never ending with a slash.
   * @throws PeppolDNSResolutionException
   *         If the URL resolution failed.
   * @see #getSMPURIOfParticipant(IParticipantIdentifier, String)
   * @see #getSMPURIOfParticipant(IParticipantIdentifier, ISMLInfo)
   * @see #getSMPURLOfParticipant(IParticipantIdentifier, ISMLInfo)
   */
  @Nonnull
  default URL getSMPURLOfParticipant (@Nonnull final IParticipantIdentifier aParticipantIdentifier,
                                      @Nullable final String sSMLZoneName) throws PeppolDNSResolutionException
  {
    ValueEnforcer.notNull (aParticipantIdentifier, "ParticipantIdentifier");

    final URI aURI = getSMPURIOfParticipant (aParticipantIdentifier, sSMLZoneName);
    try
    {
      return aURI.toURL ();
    }
    catch (final MalformedURLException ex)
    {
      throw new IllegalArgumentException ("Error building SMP URL from URI: " + aURI, ex);
    }
  }
}
