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
package com.helger.peppolid.peppol.transportprofile;

import com.helger.commons.annotation.CodingStyleguideUnaware;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.version.Version;
import javax.annotation.Nonnull;


/**
 * This file was automatically generated.
 * Do NOT edit!
 */
@CodingStyleguideUnaware
public enum EPredefinedTransportProfileIdentifier
    implements IPredefinedTransportProfileIdentifier
{

    /**
     * <b>This item is deprecated since version 1.0.0 and should not be used to issue new identifiers!</b><br><code>busdox-transport-start</code><br>
     * 
     * @since code list 1.0.0
     */
    @Deprecated
    busdox_transport_start("START", "1.0.1", "busdox-transport-start", Version.parse("1.0.0"), true),

    /**
     * <code>busdox-transport-as2-ver1p0</code><br>
     * 
     * @since code list 1.0.0
     */
    busdox_transport_as2_ver1p0("AS2", "1.0", "busdox-transport-as2-ver1p0", Version.parse("1.0.0"), false),

    /**
     * <b>This item is deprecated since version 3 and should not be used to issue new identifiers!</b><br><code>peppol-transport-as4-v1_0</code><br>
     * 
     * @since code list 2
     */
    @Deprecated
    peppol_transport_as4_v1_0("AS4", "1.0", "peppol-transport-as4-v1_0", Version.parse("2"), true),

    /**
     * <code>peppol-transport-as4-v2_0</code><br>
     * 
     * @since code list 3
     */
    peppol_transport_as4_v2_0("AS4", "2.0", "peppol-transport-as4-v2_0", Version.parse("3"), false),

    /**
     * <code>busdox-transport-as2-ver2p0</code><br>
     * 
     * @since code list 5
     */
    busdox_transport_as2_ver2p0("AS2", "2.0", "busdox-transport-as2-ver2p0", Version.parse("5"), false);
    /**
     * Same as {@link #busdox_transport_start}
     */
    @Deprecated
    public static final EPredefinedTransportProfileIdentifier START_1_0_1 = EPredefinedTransportProfileIdentifier.busdox_transport_start;
    /**
     * Same as {@link #busdox_transport_as2_ver1p0}
     */
    public static final EPredefinedTransportProfileIdentifier AS2_1_0 = EPredefinedTransportProfileIdentifier.busdox_transport_as2_ver1p0;
    /**
     * Same as {@link #peppol_transport_as4_v1_0}
     */
    @Deprecated
    public static final EPredefinedTransportProfileIdentifier AS4_1_0 = EPredefinedTransportProfileIdentifier.peppol_transport_as4_v1_0;
    /**
     * Same as {@link #peppol_transport_as4_v2_0}
     */
    public static final EPredefinedTransportProfileIdentifier AS4_2_0 = EPredefinedTransportProfileIdentifier.peppol_transport_as4_v2_0;
    /**
     * Same as {@link #busdox_transport_as2_ver2p0}
     */
    public static final EPredefinedTransportProfileIdentifier AS2_2_0 = EPredefinedTransportProfileIdentifier.busdox_transport_as2_ver2p0;
    private final String m_sProtocol;
    private final String m_sProfileVersion;
    private final String m_sProfileID;
    private final Version m_aSince;
    private final boolean m_bDeprecated;

    private EPredefinedTransportProfileIdentifier(@Nonnull @Nonempty final String sProtocol,
        @Nonnull @Nonempty final String sProfileVersion,
        @Nonnull @Nonempty final String sProfileID,
        @Nonnull final Version aSince,
        final boolean bDeprecated) {
        m_sProtocol = sProtocol;
        m_sProfileVersion = sProfileVersion;
        m_sProfileID = sProfileID;
        m_aSince = aSince;
        m_bDeprecated = bDeprecated;
    }

    @Nonnull
    @Nonempty
    public String getProtocol() {
        return m_sProtocol;
    }

    @Nonnull
    @Nonempty
    public String getProfileVersion() {
        return m_sProfileVersion;
    }

    @Nonnull
    @Nonempty
    public String getProfileID() {
        return m_sProfileID;
    }

    @Nonnull
    public Version getSince() {
        return m_aSince;
    }

    public boolean isDeprecated() {
        return m_bDeprecated;
    }
}
