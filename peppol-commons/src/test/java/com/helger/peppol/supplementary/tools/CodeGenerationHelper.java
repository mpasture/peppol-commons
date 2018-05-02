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
package com.helger.peppol.supplementary.tools;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.StringHelper;
import com.helger.peppol.identifier.peppol.doctype.IPeppolDocumentTypeIdentifierParts;

@Immutable
final class CodeGenerationHelper
{
  private static final String SKIP_BIS_PREFIX = "urn:www.peppol.eu:bis:peppol";

  private CodeGenerationHelper ()
  {}

  @Nonnull
  @Nonempty
  public static String createShortcutDocumentTypeIDName (@Nonnull final IPeppolDocumentTypeIdentifierParts aDocIDParts)
  {
    // Create a shortcut constant with a more readable name!
    final String sCustomizationID = aDocIDParts.getCustomizationID ();

    // Invoice
    if ("urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0".equals (sCustomizationID))
      return "INVOICE_T010_BIS4A";
    if ("urn:www.cenbii.eu:transaction:biitrns010:ver2.0:extended:urn:www.peppol.eu:bis:peppol4a:ver2.0".equals (sCustomizationID))
      return "INVOICE_T010_BIS4A_V20";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol5a:ver1.0".equals (sCustomizationID))
      return "INVOICE_T010_BIS5A";
    if ("urn:www.cenbii.eu:transaction:biitrns010:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0".equals (sCustomizationID))
      return "INVOICE_T010_BIS5A_V20";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm015:ver1.0:#urn:www.peppol.eu:bis:peppol5a:ver1.0".equals (sCustomizationID))
      return "INVOICE_T015_BIS5A";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol6a:ver1.0".equals (sCustomizationID))
      return "INVOICE_T010_BIS6A";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm015:ver1.0:#urn:www.peppol.eu:bis:peppol6a:ver1.0".equals (sCustomizationID))
      return "INVOICE_T015_BIS6A";
    if ("urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0".equals (sCustomizationID))
      return "INVOICE_EN16931_PEPPOL_V30";

    // CreditNote
    if ("urn:www.cenbii.eu:transaction:biicoretrdm014:ver1.0:#urn:www.peppol.eu:bis:peppol5a:ver1.0".equals (sCustomizationID))
      return "CREDITNOTE_T014_BIS5A";
    if ("urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0".equals (sCustomizationID))
      return "CREDITNOTE_T014_BIS5A_V20";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm014:ver1.0:#urn:www.peppol.eu:bis:peppol6a:ver1.0".equals (sCustomizationID))
      return "CREDITNOTE_T014_BIS6A";

    // ApplicationResponse
    if ("urn:www.cenbii.eu:transaction:biicoretrdm057:ver1.0:#urn:www.peppol.eu:bis:peppol1a:ver1.0".equals (sCustomizationID))
      return "APPLICATIONRESPONSE_T057_BIS1A";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm058:ver1.0:#urn:www.peppol.eu:bis:peppol1a:ver1.0".equals (sCustomizationID))
      return "APPLICATIONRESPONSE_T058_BIS1A";
    if ("urn:www.cenbii.eu:transaction:biitrns071:ver2.0:extended:urn:www.peppol.eu:bis:peppol36a:ver1.0".equals (sCustomizationID))
      return "APPLICATIONRESPONSE_T071_BIS36A";

    // Catalogue
    if ("urn:www.cenbii.eu:transaction:biitrns019:ver2.0:extended:urn:www.peppol.eu:bis:peppol1a:ver4.0".equals (sCustomizationID))
      return "CATALOGUE_T019_BIS1A_V40";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm019:ver1.0:#urn:www.peppol.eu:bis:peppol1a:ver1.0".equals (sCustomizationID))
      return "CATALOGUE_T019_BIS1A";

    // Order
    if ("urn:www.cenbii.eu:transaction:biicoretrdm001:ver1.0:#urn:www.peppol.eu:bis:peppol3a:ver1.0".equals (sCustomizationID))
      return "ORDER_T001_BIS3A";
    if ("urn:www.cenbii.eu:transaction:biitrns001:ver2.0:extended:urn:www.peppol.eu:bis:peppol03a:ver2.0".equals (sCustomizationID))
      return "ORDER_T001_BIS03A_V20";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm001:ver1.0:#urn:www.peppol.eu:bis:peppol6a:ver1.0".equals (sCustomizationID))
      return "ORDER_T001_BIS6A";
    if ("urn:www.cenbii.eu:transaction:biitrns001:ver2.0:extended:urn:www.peppol.eu:bis:peppol28a:ver1.0".equals (sCustomizationID))
      return "ORDER_T001_BIS28A";
    if ("urn:www.cenbii.eu:transaction:biitrns076:ver2.0:extended:urn:www.peppol.eu:bis:peppol28a:ver1.0".equals (sCustomizationID))
      return "ORDER_T076_BIS28A";

    // OrderResponseSimple
    if ("urn:www.cenbii.eu:transaction:biicoretrdm002:ver1.0:#urn:www.peppol.eu:bis:peppol6a:ver1.0".equals (sCustomizationID))
      return "ORDERRESPONSESIMPLE_T002_BIS6A";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm003:ver1.0:#urn:www.peppol.eu:bis:peppol6a:ver1.0".equals (sCustomizationID))
      return "ORDERRESPONSESIMPLE_T003_BIS6A";

    // DespatchAdvice
    if ("urn:www.cenbii.eu:transaction:biitrns016:ver1.0:extended:urn:www.peppol.eu:bis:peppol30a:ver1.0".equals (sCustomizationID))
      return "DESPATCHADVICE_T016_BIS30A";

    // EHF
    if ("urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1".equals (sCustomizationID))
      return "INVOICE_T010_BIS4A_WWW_DIFI_NO_EHF_FAKTURA_VER1";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm014:ver1.0:#urn:www.cenbii.eu:profile:biixx:ver1.0#urn:www.difi.no:ehf:kreditnota:ver1".equals (sCustomizationID))
      return "CREDITNOTE_T014_WWW_CENBII_EU_PROFILE_BIIXX_VER1_0_WWW_DIFI_NO_EHF_KREDITNOTA_VER1";

    // Other stuff
    if ("urn:www.cenbii.eu:transaction:biicoretrdm993:ver0.1:#urn:www.peppol.eu:bis:peppol993a:ver1.0".equals (sCustomizationID))
      return "CATALOGUETEMPLATE_T993_BIS993A";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm992:ver0.1:#urn:www.peppol.eu:bis:peppol992a:ver1.0".equals (sCustomizationID))
      return "VIRTUALCOMPANYDOSSIERPACKAGE_T992_BIS992A";
    if ("urn:www.cenbii.eu:transaction:biicoretrdm991:ver0.1:#urn:www.peppol.eu:bis:peppol991a:ver1.0".equals (sCustomizationID))
      return "VIRTUALCOMPANYDOSSIER_T991_BIS991A";

    String sExt = sCustomizationID;
    sExt = StringHelper.replaceAll (sExt, "urn:", "");
    sExt = StringHelper.replaceAll (sExt, '.', '_');
    sExt = StringHelper.replaceAll (sExt, ':', '_');
    sExt = StringHelper.replaceAll (sExt, '#', '_');

    return (aDocIDParts.getLocalName () + "_" + sExt).toUpperCase (Locale.US);
  }

  @Nullable
  @Nonempty
  public static String createShortcutBISIDName (@Nonnull final String sBISID)
  {
    if (!sBISID.startsWith (SKIP_BIS_PREFIX))
      throw new IllegalArgumentException ("Invalid BIS ID: " + sBISID);

    String ret = "BIS" + sBISID.substring (SKIP_BIS_PREFIX.length ());
    final int nIndex = ret.indexOf (":ver");
    if (nIndex >= 0)
    {
      // Add version number
      String sVersion = "_V" + ret.substring (nIndex + 4, nIndex + 5) + ret.substring (nIndex + 6, nIndex + 7);
      if (sVersion.equals ("_V10"))
      {
        // For backwards compatibility
        sVersion = "";
      }
      ret = ret.substring (0, nIndex) + sVersion;
    }
    return ret.toUpperCase (Locale.US);
  }
}
