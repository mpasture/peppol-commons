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
package com.helger.peppolid.peppol.process;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.CodingStyleguideUnaware;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.version.Version;
import com.helger.peppolid.IProcessIdentifier;


/**
 * This file was automatically generated.
 * Do NOT edit!
 */
@CodingStyleguideUnaware
public enum EPredefinedProcessIdentifier
    implements IPeppolPredefinedProcessIdentifier
{

    /**
     * <b>This item is deprecated since version 1.2.1 and should not be used to issue new identifiers!</b><br><code>urn:www.cenbii.eu:profile:bii01:ver1.0</code><br>
     * 
     * @since code list 1.0.0
     */
    @Deprecated
    urn_www_cenbii_eu_profile_bii01_ver1_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii01:ver1.0", "BIS 1A", Version.parse("1.0.0"), true),

    /**
     * <code>urn:www.cenbii.eu:profile:bii01:ver2.0</code><br>
     * 
     * @since code list 1.2.0
     */
    urn_www_cenbii_eu_profile_bii01_ver2_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii01:ver2.0", "BIS 1A", Version.parse("1.2.0"), false),

    /**
     * <b>This item is deprecated since version 1.2.1 and should not be used to issue new identifiers!</b><br><code>urn:www.cenbii.eu:profile:bii03:ver1.0</code><br>
     * 
     * @since code list 1.0.0
     */
    @Deprecated
    urn_www_cenbii_eu_profile_bii03_ver1_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii03:ver1.0", "BIS 3A", Version.parse("1.0.0"), true),

    /**
     * <code>urn:www.cenbii.eu:profile:bii03:ver2.0</code><br>
     * 
     * @since code list 1.2.0
     */
    urn_www_cenbii_eu_profile_bii03_ver2_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii03:ver2.0", "BIS 3A", Version.parse("1.2.0"), false),

    /**
     * <b>This item is deprecated since version 1.2.1 and should not be used to issue new identifiers!</b><br><code>urn:www.cenbii.eu:profile:bii04:ver1.0</code><br>
     * 
     * @since code list 1.0.0
     */
    @Deprecated
    urn_www_cenbii_eu_profile_bii04_ver1_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii04:ver1.0", "BIS 4A", Version.parse("1.0.0"), true),

    /**
     * <code>urn:www.cenbii.eu:profile:bii04:ver2.0</code><br>
     * 
     * @since code list 1.2.0
     */
    urn_www_cenbii_eu_profile_bii04_ver2_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii04:ver2.0", "BIS 4A", Version.parse("1.2.0"), false),

    /**
     * <b>This item is deprecated since version 1.2.1 and should not be used to issue new identifiers!</b><br><code>urn:www.cenbii.eu:profile:bii05:ver1.0</code><br>
     * 
     * @since code list 1.1.0
     */
    @Deprecated
    urn_www_cenbii_eu_profile_bii05_ver1_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii05:ver1.0", "BIS 5A", Version.parse("1.1.0"), true),

    /**
     * <code>urn:www.cenbii.eu:profile:bii05:ver2.0</code><br>
     * 
     * @since code list 1.2.0
     */
    urn_www_cenbii_eu_profile_bii05_ver2_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii05:ver2.0", "BIS 5A", Version.parse("1.2.0"), false),

    /**
     * <code>urn:fdc:peppol.eu:2017:poacc:billing:01:1.0</code><br>
     * 
     * @since code list 2
     */
    urn_fdc_peppol_eu_2017_poacc_billing_01_1_0("cenbii-procid-ubl", "urn:fdc:peppol.eu:2017:poacc:billing:01:1.0", "BIS 5A", Version.parse("2"), false),

    /**
     * <b>This item is deprecated since version 1.2.1 and should not be used to issue new identifiers!</b><br><code>urn:www.cenbii.eu:profile:bii06:ver1.0</code><br>
     * 
     * @since code list 1.0.0
     */
    @Deprecated
    urn_www_cenbii_eu_profile_bii06_ver1_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii06:ver1.0", "BIS 6A", Version.parse("1.0.0"), true),

    /**
     * <code>urn:www.cenbii.eu:profile:bii28:ver2.0</code><br>
     * 
     * @since code list 1.2.0
     */
    urn_www_cenbii_eu_profile_bii28_ver2_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii28:ver2.0", "BIS 28A", Version.parse("1.2.0"), false),

    /**
     * <code>urn:www.cenbii.eu:profile:bii30:ver2.0</code><br>
     * 
     * @since code list 1.2.0
     */
    urn_www_cenbii_eu_profile_bii30_ver2_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii30:ver2.0", "BIS 30A", Version.parse("1.2.0"), false),

    /**
     * <code>urn:www.cenbii.eu:profile:bii36:ver2.0</code><br>
     * 
     * @since code list 1.2.0
     */
    urn_www_cenbii_eu_profile_bii36_ver2_0("cenbii-procid-ubl", "urn:www.cenbii.eu:profile:bii36:ver2.0", "BIS 36A", Version.parse("1.2.0"), false),

    /**
     * <code>urn:fdc:peppol.eu:2017:pracc:p001:01:1.0</code><br>
     * 
     * @since code list 3
     */
    urn_fdc_peppol_eu_2017_pracc_p001_01_1_0("cenbii-procid-ubl", "urn:fdc:peppol.eu:2017:pracc:p001:01:1.0", "P001", Version.parse("3"), false),

    /**
     * <code>urn:fdc:peppol.eu:2017:pracc:p002:01:1.0</code><br>
     * 
     * @since code list 3
     */
    urn_fdc_peppol_eu_2017_pracc_p002_01_1_0("cenbii-procid-ubl", "urn:fdc:peppol.eu:2017:pracc:p002:01:1.0", "P002", Version.parse("3"), false),

    /**
     * <code>urn:fdc:peppol.eu:2017:pracc:p003:01:1.0</code><br>
     * 
     * @since code list 3
     */
    urn_fdc_peppol_eu_2017_pracc_p003_01_1_0("cenbii-procid-ubl", "urn:fdc:peppol.eu:2017:pracc:p003:01:1.0", "P003", Version.parse("3"), false),

    /**
     * <code>Reference-Utility-1.0</code><br>
     * 
     * @since code list 3
     */
    oioubl_procid_ubl_Reference_Utility_1_0("oioubl-procid-ubl", "Reference-Utility-1.0", null, Version.parse("3"), false),

    /**
     * <code>Procurement-ReminderOnly-1.0</code><br>
     * 
     * @since code list 3
     */
    oioubl_procid_ubl_Procurement_ReminderOnly_1_0("oioubl-procid-ubl", "Procurement-ReminderOnly-1.0", null, Version.parse("3"), false),

    /**
     * <code>urn:www.peppol.eu:profile:bis63a:ver1.0</code><br>
     * 
     * @since code list 4
     */
    urn_www_peppol_eu_profile_bis63a_ver1_0("cenbii-procid-ubl", "urn:www.peppol.eu:profile:bis63a:ver1.0", "BIS 63A", Version.parse("4"), false),

    /**
     * <code>urn:fdc:peppol.eu:poacc:bis:catalogue_only:3</code><br>
     * 
     * @since code list 4
     */
    urn_fdc_peppol_eu_poacc_bis_catalogue_only_3("cenbii-procid-ubl", "urn:fdc:peppol.eu:poacc:bis:catalogue_only:3", null, Version.parse("4"), false),

    /**
     * <code>urn:fdc:peppol.eu:poacc:bis:order_only:3</code><br>
     * 
     * @since code list 4
     */
    urn_fdc_peppol_eu_poacc_bis_order_only_3("cenbii-procid-ubl", "urn:fdc:peppol.eu:poacc:bis:order_only:3", null, Version.parse("4"), false),

    /**
     * <code>urn:fdc:peppol.eu:poacc:bis:invoice_response:3</code><br>
     * 
     * @since code list 4
     */
    urn_fdc_peppol_eu_poacc_bis_invoice_response_3("cenbii-procid-ubl", "urn:fdc:peppol.eu:poacc:bis:invoice_response:3", null, Version.parse("4"), false),

    /**
     * <code>urn:fdc:peppol.eu:poacc:bis:punch_out:3</code><br>
     * 
     * @since code list 4
     */
    urn_fdc_peppol_eu_poacc_bis_punch_out_3("cenbii-procid-ubl", "urn:fdc:peppol.eu:poacc:bis:punch_out:3", null, Version.parse("4"), false),

    /**
     * <code>urn:fdc:peppol.eu:poacc:bis:ordering:3</code><br>
     * 
     * @since code list 4
     */
    urn_fdc_peppol_eu_poacc_bis_ordering_3("cenbii-procid-ubl", "urn:fdc:peppol.eu:poacc:bis:ordering:3", null, Version.parse("4"), false),

    /**
     * <code>urn:fdc:peppol.eu:poacc:bis:despatch_advice:3</code><br>
     * 
     * @since code list 4
     */
    urn_fdc_peppol_eu_poacc_bis_despatch_advice_3("cenbii-procid-ubl", "urn:fdc:peppol.eu:poacc:bis:despatch_advice:3", null, Version.parse("4"), false),

    /**
     * <code>urn:fdc:peppol.eu:poacc:bis:order_agreement:3</code><br>
     * 
     * @since code list 4
     */
    urn_fdc_peppol_eu_poacc_bis_order_agreement_3("cenbii-procid-ubl", "urn:fdc:peppol.eu:poacc:bis:order_agreement:3", null, Version.parse("4"), false),

    /**
     * <code>urn:fdc:peppol.eu:poacc:bis:mlr:3</code><br>
     * 
     * @since code list 4
     */
    urn_fdc_peppol_eu_poacc_bis_mlr_3("cenbii-procid-ubl", "urn:fdc:peppol.eu:poacc:bis:mlr:3", null, Version.parse("4"), false);
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii01_ver1_0}
     */
    @Deprecated
    public static final EPredefinedProcessIdentifier BIS1A_V1 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii01_ver1_0;
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii01_ver2_0}
     */
    public static final EPredefinedProcessIdentifier BIS1A_V4 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii01_ver2_0;
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii03_ver1_0}
     */
    @Deprecated
    public static final EPredefinedProcessIdentifier BIS3A_V1 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii03_ver1_0;
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii03_ver2_0}
     */
    public static final EPredefinedProcessIdentifier BIS3A_V2 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii03_ver2_0;
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii04_ver1_0}
     */
    @Deprecated
    public static final EPredefinedProcessIdentifier BIS4A_V1 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii04_ver1_0;
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii04_ver2_0}
     */
    public static final EPredefinedProcessIdentifier BIS4A_V2 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii04_ver2_0;
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii05_ver1_0}
     */
    @Deprecated
    public static final EPredefinedProcessIdentifier BIS5A_V1 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii05_ver1_0;
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii05_ver2_0}
     */
    public static final EPredefinedProcessIdentifier BIS5A_V2 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii05_ver2_0;
    /**
     * Same as {@link #urn_fdc_peppol_eu_2017_poacc_billing_01_1_0}
     */
    public static final EPredefinedProcessIdentifier BIS5A_V3 = EPredefinedProcessIdentifier.urn_fdc_peppol_eu_2017_poacc_billing_01_1_0;
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii06_ver1_0}
     */
    @Deprecated
    public static final EPredefinedProcessIdentifier BIS6A_V1 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii06_ver1_0;
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii28_ver2_0}
     */
    public static final EPredefinedProcessIdentifier BIS28A_V1 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii28_ver2_0;
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii30_ver2_0}
     */
    public static final EPredefinedProcessIdentifier BIS30A_V1 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii30_ver2_0;
    /**
     * Same as {@link #urn_www_cenbii_eu_profile_bii36_ver2_0}
     */
    public static final EPredefinedProcessIdentifier BIS36A_V1 = EPredefinedProcessIdentifier.urn_www_cenbii_eu_profile_bii36_ver2_0;
    /**
     * Same as {@link #urn_fdc_peppol_eu_2017_pracc_p001_01_1_0}
     */
    public static final EPredefinedProcessIdentifier P001_V1 = EPredefinedProcessIdentifier.urn_fdc_peppol_eu_2017_pracc_p001_01_1_0;
    /**
     * Same as {@link #urn_fdc_peppol_eu_2017_pracc_p002_01_1_0}
     */
    public static final EPredefinedProcessIdentifier P002_V1 = EPredefinedProcessIdentifier.urn_fdc_peppol_eu_2017_pracc_p002_01_1_0;
    /**
     * Same as {@link #urn_fdc_peppol_eu_2017_pracc_p003_01_1_0}
     */
    public static final EPredefinedProcessIdentifier P003_V1 = EPredefinedProcessIdentifier.urn_fdc_peppol_eu_2017_pracc_p003_01_1_0;
    /**
     * Same as {@link #urn_www_peppol_eu_profile_bis63a_ver1_0}
     */
    public static final EPredefinedProcessIdentifier BIS63A_V1 = EPredefinedProcessIdentifier.urn_www_peppol_eu_profile_bis63a_ver1_0;
    private final String m_sScheme;
    private final String m_sID;
    private final String m_sBISID;
    private final Version m_aSince;
    private final boolean m_bDeprecated;

    private EPredefinedProcessIdentifier(@Nonnull @Nonempty final String sScheme,
        @Nonnull @Nonempty final String sID,
        @Nullable final String sBISID,
        @Nonnull final Version aSince,
        final boolean bDeprecated) {
        m_sScheme = sScheme;
        m_sID = sID;
        m_sBISID = sBISID;
        m_aSince = aSince;
        m_bDeprecated = bDeprecated;
    }

    @Nonnull
    @Nonempty
    public String getScheme() {
        return m_sScheme;
    }

    @Nonnull
    @Nonempty
    public String getValue() {
        return m_sID;
    }

    @Nullable
    public String getBISID() {
        return m_sBISID;
    }

    @Nonnull
    public PeppolProcessIdentifier getAsProcessIdentifier() {
        return new PeppolProcessIdentifier(this);
    }

    @Nonnull
    public Version getSince() {
        return m_aSince;
    }

    public boolean isDeprecated() {
        return m_bDeprecated;
    }

    @Nullable
    public static EPredefinedProcessIdentifier getFromProcessIdentifierOrNull(@Nullable final IProcessIdentifier aProcessID) {
        if (aProcessID!= null) {
            for (EPredefinedProcessIdentifier e: EPredefinedProcessIdentifier.values()) {
                if (e.hasScheme(aProcessID.getScheme())&&e.hasValue(aProcessID.getValue())) {
                    return e;
                }
            }
        }
        return null;
    }
}
