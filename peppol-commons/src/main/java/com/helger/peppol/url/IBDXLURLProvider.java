package com.helger.peppol.url;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.peppol.identifier.generic.participant.IParticipantIdentifier;

/**
 * BDXP URL provider. Layout:
 * <code>strip-trailing(base32(sha256(lowercase(ID-VALUE))),"=")+"."+ID-SCHEME+"."+SML-ZONE-NAME</code>
 *
 * @author Philip Helger
 * @since 6.1.2
 */
public interface IBDXLURLProvider extends IPeppolURLProvider
{
  /**
   * @return <code>true</code> if value is lower cases before the value is
   *         hashed.
   */
  boolean isLowercaseValueBeforeHashing ();

  /**
   * @return <code>true</code> if internal DNS caching is enabled,
   *         <code>false</code> if not. By default it is enabled.
   */
  boolean isUseDNSCache ();

  /**
   * @return A copy of all entries currently in the cache. Never
   *         <code>null</code> but maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  ICommonsMap <String, String> getAllDNSCacheEntries ();

  /**
   * @return A copy of all entries currently in the cache. Never
   *         <code>null</code> but maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  @Deprecated
  default ICommonsMap <String, String> getAllCacheEntries ()
  {
    return getAllDNSCacheEntries ();
  }

  @Nonnull
  default String getDNSNameOfParticipant (@Nonnull final IParticipantIdentifier aParticipantIdentifier,
                                          @Nullable final String sSMLZoneName)
  {
    return getDNSNameOfParticipant (aParticipantIdentifier, sSMLZoneName, true);
  }

  @Nonnull
  default String getDNSNameOfParticipant (@Nonnull final IParticipantIdentifier aParticipantIdentifier,
                                          @Nullable final String sSMLZoneName,
                                          final boolean bDoNAPTRResolving)
  {
    return getDNSNameOfParticipant (aParticipantIdentifier, sSMLZoneName, bDoNAPTRResolving, null);
  }

  @Nonnull
  String getDNSNameOfParticipant (@Nonnull IParticipantIdentifier aParticipantIdentifier,
                                  @Nullable String sSMLZoneName,
                                  boolean bDoNAPTRResolving,
                                  @Nullable String sPrimaryDNSServer);
}
