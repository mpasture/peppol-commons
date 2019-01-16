package com.helger.peppol.identifier.peppol.transportprofile;

import com.helger.commons.annotation.CodingStyleguideUnaware;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.version.Version;
import javax.annotation.Nonnull;


/**
 * This file was automatically generated.
 * Do NOT edit!
 */
@CodingStyleguideUnaware
public enum EPredefinedTransportProfileIdentifier {

    /**
     * <b>This item is deprecated since version 1.0.0 and should not be used to issue new identifiers!</b><br><code>busdox-transport-start</code><br>
     * 
     * @since code list 1.0.0
     */
    @Deprecated
    busdox_transport_start("START", "1.0.1", "busdox-transport-start", Version.parse("1.0.0")),

    /**
     * <code>busdox-transport-as2-ver1p0</code><br>
     * 
     * @since code list 1.0.0
     */
    busdox_transport_as2_ver1p0("AS2", "1.0", "busdox-transport-as2-ver1p0", Version.parse("1.0.0")),

    /**
     * <b>This item is deprecated since version 3 and should not be used to issue new identifiers!</b><br><code>peppol-transport-as4-v1_0</code><br>
     * 
     * @since code list 2
     */
    @Deprecated
    peppol_transport_as4_v1_0("AS4", "1.0", "peppol-transport-as4-v1_0", Version.parse("2")),

    /**
     * <code>peppol-transport-as4-v2_0</code><br>
     * 
     * @since code list 3
     */
    peppol_transport_as4_v2_0("AS4", "2.0", "peppol-transport-as4-v2_0", Version.parse("3"));
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
    private final String m_sProtocol;
    private final String m_sProfileVersion;
    private final String m_sProfileID;
    private final Version m_aSince;

    private EPredefinedTransportProfileIdentifier(
        @Nonnull
        @Nonempty
        final String sProtocol,
        @Nonnull
        @Nonempty
        final String sProfileVersion,
        @Nonnull
        @Nonempty
        final String sProfileID,
        @Nonnull
        final Version aSince) {
        m_sProtocol = sProtocol;
        m_sProfileVersion = sProfileVersion;
        m_sProfileID = sProfileID;
        m_aSince = aSince;
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
}
