/**
 * Copyright (C) 2015-2020 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * The Original Code is Copyright The PEPPOL project (http://www.peppol.eu)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.helger.peppolid.supplementary.tools;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.CodingStyleguideUnaware;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.CommonsHashSet;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.commons.functional.IThrowingConsumer;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.StringParser;
import com.helger.commons.version.Version;
import com.helger.genericode.Genericode10CodeListMarshaller;
import com.helger.genericode.excel.ExcelReadOptions;
import com.helger.genericode.excel.ExcelSheetToCodeList10;
import com.helger.genericode.v10.CodeListDocument;
import com.helger.genericode.v10.Row;
import com.helger.genericode.v10.UseType;
import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JClassAlreadyExistsException;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JDocComment;
import com.helger.jcodemodel.JEnumConstant;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JForEach;
import com.helger.jcodemodel.JInvocation;
import com.helger.jcodemodel.JLambdaMethodRef;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import com.helger.jcodemodel.writer.JCMWriter;
import com.helger.peppolid.CIdentifier;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.peppolid.factory.PeppolIdentifierFactory;
import com.helger.peppolid.peppol.IPeppolIdentifier;
import com.helger.peppolid.peppol.PeppolIdentifierHelper;
import com.helger.peppolid.peppol.doctype.IPeppolDocumentTypeIdentifierParts;
import com.helger.peppolid.peppol.doctype.IPeppolPredefinedDocumentTypeIdentifier;
import com.helger.peppolid.peppol.doctype.PeppolDocumentTypeIdentifier;
import com.helger.peppolid.peppol.doctype.PeppolDocumentTypeIdentifierParts;
import com.helger.peppolid.peppol.pidscheme.IParticipantIdentifierScheme;
import com.helger.peppolid.peppol.process.PeppolProcessIdentifier;
import com.helger.peppolid.peppol.transportprofile.IPredefinedTransportProfileIdentifier;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.MicroDocument;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;

/**
 * Utility class to create the Genericode files from the Excel code list. Also
 * creates Java source files with the predefined identifiers.
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
public final class MainCreatePredefinedEnumsFromExcel_v7
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainCreatePredefinedEnumsFromExcel_v7.class);
  private static final Version CODELIST_VERSION = new Version (7);
  private static final String CODELIST_FILE_SUFFIX = " draft";
  private static final String RESULT_DIRECTORY = "src/main/resources/codelists/";
  private static final String RESULT_PACKAGE_PREFIX = "com.helger.peppolid.peppol.";
  private static final JCodeModel s_aCodeModel = new JCodeModel ();
  private static final String DO_NOT_EDIT = "This file was automatically generated.\nDo NOT edit!";
  private static final boolean DEFAULT_ISSUED_BY_OPENPEPPOL = false;
  private static final String FILENAME_SUFFIX = "V7";
  private static final ICommonsMap <IProcessIdentifier, ICommonsList <String>> KNOWN_PROCESS_IDS = new CommonsLinkedHashMap <> ();

  private static boolean _parseIssuedByOpenPEPPOL (final String s)
  {
    return CodeGenerationHelper.parseBoolean (s, DEFAULT_ISSUED_BY_OPENPEPPOL);
  }

  @Nonnull
  private static ICommonsList <IProcessIdentifier> _getProcIDs (@Nonnull final String sProcessIDs)
  {
    final ICommonsList <IProcessIdentifier> ret = new CommonsArrayList <> ();
    for (final String s : StringHelper.getExploded ('\n', StringHelper.replaceAll (sProcessIDs, '\r', '\n')))
    {
      final String sProcessID = s.trim ();
      if (StringHelper.hasNoText (sProcessID))
        throw new IllegalStateException ("Found empty process ID in '" + sProcessIDs + "'");
      final IProcessIdentifier aProcID = PeppolIdentifierFactory.INSTANCE.parseProcessIdentifier (sProcessID);
      if (aProcID == null)
        throw new IllegalStateException ("Failed to parse process ID '" + sProcessID + "'");
      ret.add (aProcID);
    }
    if (ret.isEmpty ())
      throw new IllegalStateException ("Found no single process ID in '" + sProcessIDs + "'");
    return ret;
  }

  private static void _writeGenericodeFile (@Nonnull final CodeListDocument aCodeList, @Nonnull final String sFilename)
  {
    final MapBasedNamespaceContext aNsCtx = new MapBasedNamespaceContext ();
    aNsCtx.setDefaultNamespaceURI ("");
    aNsCtx.addMapping ("gc", "http://docs.oasis-open.org/codelist/ns/genericode/1.0/");
    aNsCtx.addMapping ("ext", "urn:www.helger.com:schemas:genericode-ext");

    final Genericode10CodeListMarshaller aMarshaller = new Genericode10CodeListMarshaller ();
    aMarshaller.setNamespaceContext (aNsCtx);
    aMarshaller.setFormattedOutput (true);
    if (aMarshaller.write (aCodeList, new File (sFilename)).isFailure ())
      throw new IllegalStateException ("Failed to write file " + sFilename);
    LOGGER.info ("Wrote Genericode file " + sFilename);
  }

  private static void _handleDocumentTypes (final Sheet aDocumentSheet) throws URISyntaxException
  {
    // Create GeneriCode file
    final ExcelReadOptions <UseType> aReadOptions = new ExcelReadOptions <UseType> ().setLinesToSkip (1)
                                                                                     .setLineIndexShortName (0);
    {
      int nCol = 0;
      aReadOptions.addColumn (nCol++, "profilecode", UseType.OPTIONAL, "string", false);
      aReadOptions.addColumn (nCol++, "scheme", UseType.REQUIRED, "string", true);
      aReadOptions.addColumn (nCol++, "id", UseType.REQUIRED, "string", true);
      aReadOptions.addColumn (nCol++, "since", UseType.REQUIRED, "string", false);
      aReadOptions.addColumn (nCol++, "deprecated", UseType.REQUIRED, "boolean", false);
      aReadOptions.addColumn (nCol++, "deprecated-since", UseType.OPTIONAL, "string", false);
      aReadOptions.addColumn (nCol++, "comment", UseType.OPTIONAL, "string", false);
      aReadOptions.addColumn (nCol++, "issued-by-openpeppol", UseType.REQUIRED, "boolean", false);
      aReadOptions.addColumn (nCol++, "bis-version", UseType.OPTIONAL, "int", false);
      aReadOptions.addColumn (nCol++, "domain-community", UseType.REQUIRED, "string", false);
      aReadOptions.addColumn (nCol++, "process-ids", UseType.REQUIRED, "string", false);
    }
    final CodeListDocument aCodeList = ExcelSheetToCodeList10.convertToSimpleCodeList (aDocumentSheet,
                                                                                       aReadOptions,
                                                                                       "PeppolDocumentTypeIdentifier",
                                                                                       CODELIST_VERSION.getAsString (),
                                                                                       new URI ("urn:peppol.eu:names:identifier:documenttypes"),
                                                                                       new URI ("urn:peppol.eu:names:identifier:documenttypes-2.0"),
                                                                                       null);
    _writeGenericodeFile (aCodeList, RESULT_DIRECTORY + "PeppolDocumentTypeIdentifier" + FILENAME_SUFFIX + ".gc");

    // Save as XML
    {
      final IMicroDocument aDoc = new MicroDocument ();
      aDoc.appendComment (DO_NOT_EDIT);
      final IMicroElement eRoot = aDoc.appendElement ("root");
      eRoot.setAttribute ("version", CODELIST_VERSION.getAsString ());
      for (final Row aRow : aCodeList.getSimpleCodeList ().getRow ())
      {
        final String sProfileCode = CodeGenerationHelper.getRowValue (aRow, "profilecode");
        final String sScheme = CodeGenerationHelper.getRowValue (aRow, "scheme");
        final String sID = CodeGenerationHelper.getRowValue (aRow, "id");
        final String sSince = CodeGenerationHelper.getRowValue (aRow, "since");
        final boolean bDeprecated = CodeGenerationHelper.parseDeprecated (CodeGenerationHelper.getRowValue (aRow,
                                                                                                              "deprecated"));
        final String sDeprecatedSince = CodeGenerationHelper.getRowValue (aRow, "deprecated-since");
        if (bDeprecated && StringHelper.hasNoText (sDeprecatedSince))
          throw new IllegalStateException ("Code list entry is deprecated but there is no deprecated-since entry");
        final boolean bIssuedByOpenPEPPOL = _parseIssuedByOpenPEPPOL (CodeGenerationHelper.getRowValue (aRow,
                                                                                                         "issued-by-openpeppol"));
        final String sBISVersion = CodeGenerationHelper.getRowValue (aRow, "bis-version");
        if (bIssuedByOpenPEPPOL && StringHelper.hasNoText (sBISVersion))
          throw new IllegalStateException ("If issued by OpenPEPPOL, a BIS version is required");
        if (StringHelper.hasText (sBISVersion) && !StringParser.isUnsignedInt (sBISVersion))
          throw new IllegalStateException ("Code list entry has an invalid BIS version number - must be numeric");
        final String sDomainCommunity = CodeGenerationHelper.getRowValue (aRow, "domain-community");
        final String sProcessIDs = CodeGenerationHelper.getRowValue (aRow, "process-ids");

        final IMicroElement eAgency = eRoot.appendElement ("document-type");
        eAgency.setAttribute ("profilecode", sProfileCode);
        eAgency.setAttribute ("scheme", sScheme);
        eAgency.setAttribute ("id", sID);
        eAgency.setAttribute ("since", sSince);
        eAgency.setAttribute ("deprecated", bDeprecated);
        eAgency.setAttribute ("deprecated-since", sDeprecatedSince);
        eAgency.setAttribute ("issued-by-openpeppol", bIssuedByOpenPEPPOL);
        eAgency.setAttribute ("bis-version", sBISVersion);
        eAgency.setAttribute ("domain-community", sDomainCommunity);
        final ICommonsList <IProcessIdentifier> aProcIDs = _getProcIDs (sProcessIDs);
        for (final IProcessIdentifier aProcID : aProcIDs)
        {
          eAgency.appendElement ("process-id")
                 .setAttribute ("scheme", aProcID.getScheme ())
                 .setAttribute ("value", aProcID.getValue ());
          KNOWN_PROCESS_IDS.computeIfAbsent (aProcID, k -> new CommonsArrayList <> ())
                           .add (CIdentifier.getURIEncoded (sScheme, sID));
        }
      }
      MicroWriter.writeToFile (aDoc,
                               new File (RESULT_DIRECTORY + "PeppolDocumentTypeIdentifier" + FILENAME_SUFFIX + ".xml"));
    }

    // Create Java source
    try
    {
      final JDefinedClass jEnum = s_aCodeModel._package (RESULT_PACKAGE_PREFIX + "doctype")
                                              ._enum ("EPredefinedDocumentTypeIdentifier" + FILENAME_SUFFIX)
                                              ._implements (IPeppolPredefinedDocumentTypeIdentifier.class);
      jEnum.annotate (CodingStyleguideUnaware.class);
      jEnum.javadoc ().add (DO_NOT_EDIT);

      final ICommonsSet <String> aAllShortcutNames = new CommonsHashSet <> ();

      // Add all enum constants
      for (final Row aRow : aCodeList.getSimpleCodeList ().getRow ())
      {
        final String sProfileCode = CodeGenerationHelper.getRowValue (aRow, "profilecode");
        final String sScheme = CodeGenerationHelper.getRowValue (aRow, "scheme");
        final String sID = CodeGenerationHelper.getRowValue (aRow, "id");
        final String sSince = CodeGenerationHelper.getRowValue (aRow, "since");
        final boolean bDeprecated = CodeGenerationHelper.parseDeprecated (CodeGenerationHelper.getRowValue (aRow,
                                                                                                              "deprecated"));
        final String sDeprecatedSince = CodeGenerationHelper.getRowValue (aRow, "deprecated-since");
        if (bDeprecated && StringHelper.hasNoText (sDeprecatedSince))
          throw new IllegalStateException ("Code list entry is deprecated but there is no deprecated-since entry");
        final boolean bIssuedByOpenPEPPOL = _parseIssuedByOpenPEPPOL (CodeGenerationHelper.getRowValue (aRow,
                                                                                                         "issued-by-openpeppol"));
        final String sBISVersion = CodeGenerationHelper.getRowValue (aRow, "bis-version");
        if (bIssuedByOpenPEPPOL && StringHelper.hasNoText (sBISVersion))
          throw new IllegalStateException ("If issued by OpenPEPPOL, a BIS version is required");
        if (StringHelper.hasText (sBISVersion) && !StringParser.isUnsignedInt (sBISVersion))
          throw new IllegalStateException ("Code list entry has an invalid BIS version number - must be numeric");
        final int nBISVersion = StringParser.parseInt (sBISVersion, -1);
        final String sDomainCommunity = CodeGenerationHelper.getRowValue (aRow, "domain-community");
        final String sProcessIDs = CodeGenerationHelper.getRowValue (aRow, "process-ids");
        final ICommonsList <IProcessIdentifier> aProcIDs = _getProcIDs (sProcessIDs);

        // Split ID in it's pieces
        final IPeppolDocumentTypeIdentifierParts aDocIDParts = PeppolDocumentTypeIdentifierParts.extractFromString (sID);

        final String sEnumConstName = RegExHelper.getAsIdentifier (sID);
        final JEnumConstant jEnumConst = jEnum.enumConstant (sEnumConstName);
        if (bDeprecated)
        {
          jEnumConst.annotate (Deprecated.class);
          jEnumConst.javadoc ()
                    .add ("<b>This item is deprecated since version " +
                          sDeprecatedSince +
                          " and should not be used to issue new identifiers!</b><br>");
        }

        jEnumConst.arg (JExpr.lit (sScheme));
        final JInvocation aNew = JExpr._new (s_aCodeModel.ref (PeppolDocumentTypeIdentifierParts.class))
                                      .arg (aDocIDParts.getRootNS ())
                                      .arg (aDocIDParts.getLocalName ())
                                      .arg (aDocIDParts.getCustomizationID ())
                                      .arg (aDocIDParts.getVersion ());
        jEnumConst.arg (aNew);

        jEnumConst.arg (JExpr.lit (sProfileCode));
        jEnumConst.arg (s_aCodeModel.ref (Version.class).staticInvoke ("parse").arg (sSince));
        jEnumConst.arg (JExpr.lit (bDeprecated));
        jEnumConst.arg (StringHelper.hasNoText (sDeprecatedSince) ? JExpr._null ()
                                                                  : s_aCodeModel.ref (Version.class)
                                                                                .staticInvoke ("parse")
                                                                                .arg (sDeprecatedSince));
        jEnumConst.arg (JExpr.lit (bIssuedByOpenPEPPOL));
        jEnumConst.arg (JExpr.lit (nBISVersion));
        jEnumConst.arg (JExpr.lit (sDomainCommunity));

        final JInvocation eProcIDs = JExpr._new (s_aCodeModel.ref (CommonsArrayList.class).narrowEmpty ());
        for (final IProcessIdentifier aProcID : aProcIDs)
          eProcIDs.arg (JExpr.lit (aProcID.getURIEncoded ()));
        jEnumConst.arg (eProcIDs);

        jEnumConst.javadoc ().add ("<code>" + sID + "</code><br>");
        jEnumConst.javadoc ().addTag (JDocComment.TAG_SINCE).add ("code list " + sSince);

        // Also create a shortcut for more readable names
        final String sShortcutName = CodeGenerationHelper.createShortcutDocumentTypeIDName (aDocIDParts);
        if (sShortcutName != null)
        {
          // Make unique name
          int nNext = 2;
          String sRealShortcutName = sShortcutName;
          while (!aAllShortcutNames.add (sRealShortcutName))
          {
            sRealShortcutName = sShortcutName + nNext;
            nNext++;
          }

          final JFieldVar aShortcut = jEnum.field (JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                                                   jEnum,
                                                   sRealShortcutName,
                                                   jEnumConst);
          if (bDeprecated)
            aShortcut.annotate (Deprecated.class);
          aShortcut.javadoc ().add ("Same as {@link #" + sEnumConstName + "}");
        }
      }

      // fields
      final JFieldVar fScheme = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sScheme");
      final JFieldVar fParts = jEnum.field (JMod.PRIVATE | JMod.FINAL,
                                            IPeppolDocumentTypeIdentifierParts.class,
                                            "m_aParts");
      final JFieldVar fID = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sID");
      final JFieldVar fProfileCode = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sProfileCode");
      final JFieldVar fSince = jEnum.field (JMod.PRIVATE | JMod.FINAL, Version.class, "m_aSince");
      final JFieldVar fDeprecated = jEnum.field (JMod.PRIVATE | JMod.FINAL, boolean.class, "m_bDeprecated");
      final JFieldVar fDeprecatedSince = jEnum.field (JMod.PRIVATE | JMod.FINAL, Version.class, "m_aDeprecatedSince");
      final JFieldVar fIssuedByOpenPEPPOL = jEnum.field (JMod.PRIVATE | JMod.FINAL,
                                                         boolean.class,
                                                         "m_bIssuedByOpenPEPPOL");
      final JFieldVar fBISVersion = jEnum.field (JMod.PRIVATE | JMod.FINAL, int.class, "m_nBISVersion");
      final JFieldVar fDomainCommunity = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sDomainCommunity");
      final JFieldVar fProcIDs = jEnum.field (JMod.PRIVATE | JMod.FINAL,
                                              s_aCodeModel.ref (ICommonsList.class).narrow (IProcessIdentifier.class),
                                              "m_aProcIDs");

      // Constructor
      final JMethod jCtor = jEnum.constructor (JMod.PRIVATE);
      final JVar jScheme = jCtor.param (JMod.FINAL, String.class, "sScheme");
      jScheme.annotate (Nonnull.class);
      jScheme.annotate (Nonempty.class);
      final JVar jParts = jCtor.param (JMod.FINAL, IPeppolDocumentTypeIdentifierParts.class, "aParts");
      jParts.annotate (Nonnull.class);
      final JVar jProfileCode = jCtor.param (JMod.FINAL, String.class, "sProfileCode");
      jProfileCode.annotate (Nonnull.class);
      jProfileCode.annotate (Nonempty.class);
      final JVar jSince = jCtor.param (JMod.FINAL, Version.class, "aSince");
      jSince.annotate (Nonnull.class);
      final JVar jDeprecated = jCtor.param (JMod.FINAL, boolean.class, "bDeprecated");
      final JVar jDeprecatedSince = jCtor.param (JMod.FINAL, Version.class, "aDeprecatedSince");
      jDeprecatedSince.annotate (Nullable.class);
      final JVar jIssuedByOpenPEPPOL = jCtor.param (JMod.FINAL, boolean.class, "bIssuedByOpenPEPPOL");
      final JVar jBISVersion = jCtor.param (JMod.FINAL, int.class, "nBISVersion");
      final JVar jDomainCommunity = jCtor.param (JMod.FINAL, String.class, "sDomainCommunity");
      jDomainCommunity.annotate (Nonnull.class);
      jDomainCommunity.annotate (Nonempty.class);
      final JVar jProcIDs = jCtor.param (JMod.FINAL,
                                         s_aCodeModel.ref (ICommonsList.class).narrow (String.class),
                                         "aProcIDs");
      jProcIDs.annotate (Nonnull.class);
      jProcIDs.annotate (Nonempty.class);

      jCtor.body ()
           .assign (fScheme, jScheme)
           .assign (fParts, jParts)
           .assign (fProfileCode, jProfileCode)
           .assign (fID, fParts.invoke ("getAsDocumentTypeIdentifierValue"))
           .assign (fSince, jSince)
           .assign (fDeprecated, jDeprecated)
           .assign (fDeprecatedSince, jDeprecatedSince)
           .assign (fIssuedByOpenPEPPOL, jIssuedByOpenPEPPOL)
           .assign (fBISVersion, jBISVersion)
           .assign (fDomainCommunity, jDomainCommunity)
           .assign (fProcIDs,
                    s_aCodeModel.ref (CommonsArrayList.class)
                                .narrowEmpty ()
                                ._new ()
                                .arg (jProcIDs)
                                .arg (new JLambdaMethodRef (s_aCodeModel.ref (PeppolIdentifierFactory.class)
                                                                        .staticRef ("INSTANCE"),
                                                            "parseProcessIdentifier")));

      // public String getScheme ()
      JMethod m = jEnum.method (JMod.PUBLIC, String.class, "getScheme");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fScheme);

      // public String getValue ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getValue");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fID);

      // public String getRootNS ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getRootNS");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fParts.invoke (m.name ()));

      // public String getLocalName ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getLocalName");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fParts.invoke (m.name ()));

      // public String getSubTypeIdentifier ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getSubTypeIdentifier");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fParts.invoke (m.name ()));

      if (false)
      {
        // public String getTransactionID ()
        m = jEnum.method (JMod.PUBLIC, String.class, "getTransactionID");
        m.annotate (Nonnull.class);
        m.annotate (Nonempty.class);
        m.body ()._return (fParts.invoke (m.name ()));

        // public List<String> getExtensionIDs ()
        m = jEnum.method (JMod.PUBLIC, s_aCodeModel.ref (ICommonsList.class).narrow (String.class), "getExtensionIDs");
        m.annotate (Nonnull.class);
        m.annotate (ReturnsMutableCopy.class);
        m.body ()._return (fParts.invoke (m.name ()));
      }

      // public String getCustomizationID ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getCustomizationID");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fParts.invoke (m.name ()));

      // public String getVersion ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getVersion");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fParts.invoke (m.name ()));

      // public String getCommonName ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getCommonName");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fProfileCode);

      // public String getAsDocumentTypeIdentifierValue ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getAsDocumentTypeIdentifierValue");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fID);

      // public PeppolDocumentTypeIdentifier getAsDocumentTypeIdentifier ()
      m = jEnum.method (JMod.PUBLIC, PeppolDocumentTypeIdentifier.class, "getAsDocumentTypeIdentifier");
      m.annotate (Nonnull.class);
      m.body ()._return (JExpr._new (s_aCodeModel.ref (PeppolDocumentTypeIdentifier.class)).arg (fScheme).arg (fID));

      // public IPeppolDocumentTypeIdentifierParts getParts
      m = jEnum.method (JMod.PUBLIC, IPeppolDocumentTypeIdentifierParts.class, "getParts");
      m.annotate (Nonnull.class);
      m.body ()._return (fParts);

      // public Version getSince ()
      m = jEnum.method (JMod.PUBLIC, Version.class, "getSince");
      m.annotate (Nonnull.class);
      m.body ()._return (fSince);

      // public boolean isDeprecated ()
      m = jEnum.method (JMod.PUBLIC, s_aCodeModel.BOOLEAN, "isDeprecated");
      m.body ()._return (fDeprecated);

      // public Version getDeprecatedSince ()
      m = jEnum.method (JMod.PUBLIC, Version.class, "getDeprecatedSince");
      m.annotate (Nullable.class);
      m.body ()._return (fDeprecatedSince);

      // public boolean isIssuedByOpenPEPPOL ()
      m = jEnum.method (JMod.PUBLIC, s_aCodeModel.BOOLEAN, "isIssuedByOpenPEPPOL");
      m.body ()._return (fIssuedByOpenPEPPOL);

      // public int isIssuedByOpenPEPPOL ()
      m = jEnum.method (JMod.PUBLIC, s_aCodeModel.INT, "getBISVersion");
      m.annotate (CheckForSigned.class);
      m.body ()._return (fBISVersion);

      // public String getDomainCommunity ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getDomainCommunity");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fDomainCommunity);

      // public ICommonsList<IProcessIdentifier> getAllProcessIDs ()
      m = jEnum.method (JMod.PUBLIC,
                        s_aCodeModel.ref (ICommonsList.class).narrow (IProcessIdentifier.class),
                        "getAllProcessIDs");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.annotate (ReturnsMutableCopy.class);
      m.body ()._return (fProcIDs.invoke ("getClone"));

      // @Nullable
      // public static EPredefinedDocumentTypeIdentifier
      // getFromDocumentTypeIdentifierOrNull(@Nullable final
      // IDocumentTypeIdentifier aDocTypeID)
      m = jEnum.method (JMod.PUBLIC | JMod.STATIC, jEnum, "getFromDocumentTypeIdentifierOrNull");
      {
        m.annotate (Nullable.class);
        final JVar jValue = m.param (JMod.FINAL, IDocumentTypeIdentifier.class, "aDocTypeID");
        jValue.annotate (Nullable.class);
        final JBlock jIf = m.body ()._if (jValue.neNull ())._then ();
        final JForEach jForEach = jIf.forEach (jEnum, "e", jEnum.staticInvoke ("values"));
        jForEach.body ()
                ._if (jForEach.var ()
                              .invoke ("hasScheme")
                              .arg (jValue.invoke ("getScheme"))
                              .cand (jForEach.var ().invoke ("hasValue").arg (jValue.invoke ("getValue"))))
                ._then ()
                ._return (jForEach.var ());
        m.body ()._return (JExpr._null ());
      }
    }
    catch (final JClassAlreadyExistsException ex)
    {
      LOGGER.error ("Failed to create source", ex);
    }
  }

  private static void _writeValidationPartyIdFile (final Sheet aParticipantSheet) throws URISyntaxException
  {
    // Read excel file
    final ExcelReadOptions <UseType> aReadOptions = new ExcelReadOptions <UseType> ().setLinesToSkip (1)
                                                                                     .setLineIndexShortName (0);
    aReadOptions.addColumn (0, "code", UseType.REQUIRED, "string", true);
    aReadOptions.addColumn (2, "name", UseType.OPTIONAL, "string", false);

    // Convert to GeneriCode
    final CodeListDocument aCodeList = ExcelSheetToCodeList10.convertToSimpleCodeList (aParticipantSheet,
                                                                                       aReadOptions,
                                                                                       "Scheme Agency",
                                                                                       CODELIST_VERSION.getAsString (),
                                                                                       new URI ("PEPPOL"),
                                                                                       new URI ("PEPPOL-" +
                                                                                                CODELIST_VERSION.getAsString ()),
                                                                                       new URI ("PartyID.gc"));
    _writeGenericodeFile (aCodeList, RESULT_DIRECTORY + "PartyID" + FILENAME_SUFFIX + ".gc");
  }

  private static void _handleParticipantIdentifierSchemes (final Sheet aParticipantSheet) throws URISyntaxException
  {
    // Read excel file
    final ExcelReadOptions <UseType> aReadOptions = new ExcelReadOptions <UseType> ().setLinesToSkip (1)
                                                                                     .setLineIndexShortName (0);
    {
      int nCol = 0;
      aReadOptions.addColumn (nCol++, "schemeid", UseType.REQUIRED, "string", true);
      aReadOptions.addColumn (nCol++, "iso6523", UseType.REQUIRED, "string", true);
      aReadOptions.addColumn (nCol++, "country", UseType.REQUIRED, "string", true);
      aReadOptions.addColumn (nCol++, "schemename", UseType.REQUIRED, "string", true);
      aReadOptions.addColumn (nCol++, "issuingagency", UseType.OPTIONAL, "string", false);
      aReadOptions.addColumn (nCol++, "since", UseType.REQUIRED, "string", false);
      aReadOptions.addColumn (nCol++, "deprecated", UseType.REQUIRED, "boolean", false);
      aReadOptions.addColumn (nCol++, "deprecated-since", UseType.OPTIONAL, "string", false);
      aReadOptions.addColumn (nCol++, "structure", UseType.OPTIONAL, "string", false);
      aReadOptions.addColumn (nCol++, "display", UseType.OPTIONAL, "string", false);
      aReadOptions.addColumn (nCol++, "examples", UseType.OPTIONAL, "string", false);
      aReadOptions.addColumn (nCol++, "validation-rules", UseType.OPTIONAL, "string", false);
      aReadOptions.addColumn (nCol++, "usage", UseType.OPTIONAL, "string", false);
    }

    // Convert to GeneriCode
    final CodeListDocument aCodeList = ExcelSheetToCodeList10.convertToSimpleCodeList (aParticipantSheet,
                                                                                       aReadOptions,
                                                                                       "PeppolIdentifierIssuingAgencies",
                                                                                       CODELIST_VERSION.getAsString (),
                                                                                       new URI ("urn:peppol.eu:names:identifier:participantidentifierschemes"),
                                                                                       new URI ("urn:peppol.eu:names:identifier:participantidentifierschemes-2.0"),
                                                                                       null);
    _writeGenericodeFile (aCodeList, RESULT_DIRECTORY + "PeppolParticipantIdentifierSchemes" + FILENAME_SUFFIX + ".gc");

    _writeValidationPartyIdFile (aParticipantSheet);

    // Save data also as XML
    {
      final IMicroDocument aDoc = new MicroDocument ();
      aDoc.appendComment (DO_NOT_EDIT);
      final IMicroElement eRoot = aDoc.appendElement ("root");
      eRoot.setAttribute ("version", CODELIST_VERSION.getAsString ());
      for (final Row aRow : aCodeList.getSimpleCodeList ().getRow ())
      {
        final String sSchemeID = CodeGenerationHelper.getRowValue (aRow, "schemeid");
        final String sISO6523 = CodeGenerationHelper.getRowValue (aRow, "iso6523");
        final String sCountryCode = CodeGenerationHelper.getRowValue (aRow, "country");
        final String sSchemeName = CodeGenerationHelper.getRowValue (aRow, "schemename");
        final String sIssuingAgency = CodeGenerationHelper.getRowValue (aRow, "issuingagency");
        final String sSince = CodeGenerationHelper.getRowValue (aRow, "since");
        final boolean bDeprecated = CodeGenerationHelper.parseDeprecated (CodeGenerationHelper.getRowValue (aRow,
                                                                                                              "deprecated"));
        final String sDeprecatedSince = CodeGenerationHelper.getRowValue (aRow, "deprecated-since");
        final String sStructure = CodeGenerationHelper.getRowValue (aRow, "structure");
        final String sDisplay = CodeGenerationHelper.getRowValue (aRow, "display");
        final String sExamples = CodeGenerationHelper.getRowValue (aRow, "examples");
        final String sValidationRules = CodeGenerationHelper.getRowValue (aRow, "validation-rules");
        final String sUsage = CodeGenerationHelper.getRowValue (aRow, "usage");

        if (StringHelper.hasNoText (sSchemeID))
          throw new IllegalStateException ("schemeID");
        if (sSchemeID.indexOf (' ') >= 0)
          throw new IllegalStateException ("Scheme IDs are not supposed to contain spaces!");
        if (StringHelper.hasNoText (sISO6523))
          throw new IllegalStateException ("ISO6523Code");
        if (!RegExHelper.stringMatchesPattern ("[0-9]{4}", sISO6523))
          throw new IllegalStateException ("The ISO 6523 code '" + sISO6523 + "' does not consist of 4 numbers");
        if (bDeprecated && StringHelper.hasNoText (sDeprecatedSince))
          throw new IllegalStateException ("Code list entry is deprecated but there is no deprecated-since entry");

        final IMicroElement eAgency = eRoot.appendElement ("identifier-scheme");
        eAgency.setAttribute ("schemeid", sSchemeID);
        eAgency.setAttribute ("country", sCountryCode);
        eAgency.setAttribute ("schemename", sSchemeName);
        // legacy name
        eAgency.setAttribute ("agencyname", sIssuingAgency);
        eAgency.setAttribute ("iso6523", sISO6523);
        eAgency.setAttribute ("since", sSince);
        eAgency.setAttribute ("deprecated", bDeprecated);
        eAgency.setAttribute ("deprecated-since", sDeprecatedSince);
        if (StringHelper.hasText (sStructure))
          eAgency.appendElement ("structure").appendText (sStructure);
        if (StringHelper.hasText (sDisplay))
          eAgency.appendElement ("display").appendText (sDisplay);
        if (StringHelper.hasText (sExamples))
          eAgency.appendElement ("examples").appendText (sExamples);
        if (StringHelper.hasText (sValidationRules))
          eAgency.appendElement ("validation-rules").appendText (sValidationRules);
        if (StringHelper.hasText (sUsage))
          eAgency.appendElement ("usage").appendText (sUsage);
      }
      MicroWriter.writeToFile (aDoc,
                               new File (RESULT_DIRECTORY +
                                         "PeppolParticipantIdentifierSchemes" +
                                         FILENAME_SUFFIX +
                                         ".xml"));
    }

    // Create Java source
    try
    {
      final JDefinedClass jEnum = s_aCodeModel._package (RESULT_PACKAGE_PREFIX + "pidscheme")
                                              ._enum ("EPredefinedParticipantIdentifierScheme" + FILENAME_SUFFIX)
                                              ._implements (IParticipantIdentifierScheme.class);
      jEnum.annotate (CodingStyleguideUnaware.class);
      jEnum.javadoc ().add (DO_NOT_EDIT);

      // enum constants
      for (final Row aRow : aCodeList.getSimpleCodeList ().getRow ())
      {
        final String sSchemeID = CodeGenerationHelper.getRowValue (aRow, "schemeid");
        final String sISO6523 = CodeGenerationHelper.getRowValue (aRow, "iso6523");
        final String sCountryCode = CodeGenerationHelper.getRowValue (aRow, "country");
        final String sSchemeName = CodeGenerationHelper.getRowValue (aRow, "schemename");
        final String sIssuingAgency = CodeGenerationHelper.getRowValue (aRow, "issuingagency");
        final String sSince = CodeGenerationHelper.getRowValue (aRow, "since");
        final boolean bDeprecated = CodeGenerationHelper.parseDeprecated (CodeGenerationHelper.getRowValue (aRow,
                                                                                                              "deprecated"));
        final String sDeprecatedSince = CodeGenerationHelper.getRowValue (aRow, "deprecated-since");
        final String sStructure = CodeGenerationHelper.getRowValue (aRow, "structure");
        final String sDisplay = CodeGenerationHelper.getRowValue (aRow, "display");
        final String sExamples = CodeGenerationHelper.getRowValue (aRow, "examples");
        final String sValidationRules = CodeGenerationHelper.getRowValue (aRow, "validation-rules");
        final String sUsage = CodeGenerationHelper.getRowValue (aRow, "usage");

        final JEnumConstant jEnumConst = jEnum.enumConstant (RegExHelper.getAsIdentifier (sSchemeID));
        jEnumConst.arg (JExpr.lit (sSchemeID));
        jEnumConst.arg (JExpr.lit (sISO6523));
        jEnumConst.arg (JExpr.lit (sCountryCode));
        jEnumConst.arg (JExpr.lit (sSchemeName));
        jEnumConst.arg (sIssuingAgency == null ? JExpr._null () : JExpr.lit (sIssuingAgency));
        jEnumConst.arg (s_aCodeModel.ref (Version.class).staticInvoke ("parse").arg (sSince));
        jEnumConst.arg (JExpr.lit (bDeprecated));
        jEnumConst.arg (sDeprecatedSince == null ? JExpr._null ()
                                                 : s_aCodeModel.ref (Version.class)
                                                               .staticInvoke ("parse")
                                                               .arg (sDeprecatedSince));

        jEnumConst.javadoc ()
                  .add ("Prefix <code>" + sISO6523 + "</code>, scheme ID <code>" + sSchemeID + "</code><br>");
        if (bDeprecated)
        {
          jEnumConst.annotate (Deprecated.class);
          jEnumConst.javadoc ()
                    .add ("\n<b>This item is deprecated since version " +
                          sDeprecatedSince +
                          " and should not be used to issue new identifiers!</b><br>");
        }
        if (StringHelper.hasText (sStructure))
          jEnumConst.javadoc ()
                    .add ("\nStructure of the code: " + CodeGenerationHelper.maskHtml (sStructure) + "<br>");
        if (StringHelper.hasText (sDisplay))
          jEnumConst.javadoc ().add ("\nDisplay requirements: " + CodeGenerationHelper.maskHtml (sDisplay) + "<br>");
        if (StringHelper.hasText (sExamples))
          jEnumConst.javadoc ().add ("\nExample value: " + CodeGenerationHelper.maskHtml (sExamples) + "<br>");
        if (StringHelper.hasText (sValidationRules))
          jEnumConst.javadoc ()
                    .add ("\nValidation rules: " + CodeGenerationHelper.maskHtml (sValidationRules) + "<br>");
        if (StringHelper.hasText (sUsage))
          jEnumConst.javadoc ().add ("\nUsage information: " + CodeGenerationHelper.maskHtml (sUsage) + "<br>");
        jEnumConst.javadoc ().addTag (JDocComment.TAG_SINCE).add ("code list " + sSince);

        // Add a reference via the ISO 6523 value
        {
          final JFieldVar jEnumConstRef = jEnum.field (JMod.PUBLIC_STATIC_FINAL, jEnum, "_" + sISO6523, jEnumConst);
          if (bDeprecated)
            jEnumConstRef.annotate (Deprecated.class);
          // Just copy all javadocs
          jEnumConstRef.javadoc ().addAll (jEnumConst.javadoc ());
        }
      }

      // fields
      final JFieldVar fSchemeID = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sSchemeID");
      final JFieldVar fISO6523 = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sISO6523");
      final JFieldVar fCountryCode = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sCountryCode");
      final JFieldVar fSchemeName = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sSchemeName");
      final JFieldVar fIssuingAgency = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sIssuingAgency");
      final JFieldVar fSince = jEnum.field (JMod.PRIVATE | JMod.FINAL, Version.class, "m_aSince");
      final JFieldVar fDeprecated = jEnum.field (JMod.PRIVATE | JMod.FINAL, boolean.class, "m_bDeprecated");
      final JFieldVar fDeprecatedSince = jEnum.field (JMod.PRIVATE | JMod.FINAL, Version.class, "m_aDeprecatedSince");

      // Constructor
      final JMethod jCtor = jEnum.constructor (JMod.PRIVATE);
      final JVar jSchemeID = jCtor.param (JMod.FINAL, String.class, "sSchemeID");
      jSchemeID.annotate (Nonnull.class);
      jSchemeID.annotate (Nonempty.class);
      final JVar jISO6523 = jCtor.param (JMod.FINAL, String.class, "sISO6523");
      jISO6523.annotate (Nonnull.class);
      jISO6523.annotate (Nonempty.class);
      final JVar jCountryCode = jCtor.param (JMod.FINAL, String.class, "sCountryCode");
      jCountryCode.annotate (Nonnull.class);
      jCountryCode.annotate (Nonempty.class);
      final JVar jSchemeName = jCtor.param (JMod.FINAL, String.class, "sSchemeName");
      jSchemeName.annotate (Nonnull.class);
      jSchemeName.annotate (Nonempty.class);
      final JVar jIssuingAgency = jCtor.param (JMod.FINAL, String.class, "sIssuingAgency");
      jIssuingAgency.annotate (Nullable.class);
      final JVar jSince = jCtor.param (JMod.FINAL, Version.class, "aSince");
      jSince.annotate (Nonnull.class);
      final JVar jDeprecated = jCtor.param (JMod.FINAL, boolean.class, "bDeprecated");
      final JVar jDeprecatedSince = jCtor.param (JMod.FINAL, Version.class, "aDeprecatedSince");
      jDeprecatedSince.annotate (Nullable.class);
      jCtor.body ()
           .assign (fSchemeID, jSchemeID)
           .assign (fISO6523, jISO6523)
           .assign (fCountryCode, jCountryCode)
           .assign (fSchemeName, jSchemeName)
           .assign (fIssuingAgency, jIssuingAgency)
           .assign (fSince, jSince)
           .assign (fDeprecated, jDeprecated)
           .assign (fDeprecatedSince, jDeprecatedSince);

      // public String getSchemeID ()
      JMethod m = jEnum.method (JMod.PUBLIC, String.class, "getSchemeID");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fSchemeID);

      // public String getISO6523Code ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getISO6523Code");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fISO6523);

      // public String getCountryCode ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getCountryCode");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fCountryCode);

      // public String getSchemeName ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getSchemeName");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fSchemeName);

      // public String getSchemeAgency ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getSchemeAgency");
      m.annotate (Nullable.class);
      m.body ()._return (fIssuingAgency);

      // public Version getSince ()
      m = jEnum.method (JMod.PUBLIC, Version.class, "getSince");
      m.annotate (Nonnull.class);
      m.body ()._return (fSince);

      // public boolean isDeprecated ()
      m = jEnum.method (JMod.PUBLIC, boolean.class, "isDeprecated");
      m.body ()._return (fDeprecated);

      // public Version getDeprecatedSince ()
      m = jEnum.method (JMod.PUBLIC, Version.class, "getDeprecatedSince");
      m.annotate (Nullable.class);
      m.body ()._return (fDeprecatedSince);
    }
    catch (final Exception ex)
    {
      LOGGER.error ("Failed to create source", ex);
    }
  }

  private static void _handleProcessIdentifiers ()
  {
    // Create Java source
    try
    {
      final JDefinedClass jEnum = s_aCodeModel._package (RESULT_PACKAGE_PREFIX + "process")
                                              ._enum ("EPredefinedProcessIdentifier" + FILENAME_SUFFIX)
                                              ._implements (IProcessIdentifier.class)
                                              ._implements (IPeppolIdentifier.class);
      jEnum.annotate (CodingStyleguideUnaware.class);
      jEnum.javadoc ().add (DO_NOT_EDIT);

      // enum constants
      for (final Map.Entry <IProcessIdentifier, ICommonsList <String>> aEntry : KNOWN_PROCESS_IDS.entrySet ())
      {
        final String sScheme = aEntry.getKey ().getScheme ();
        final String sID = aEntry.getKey ().getValue ();

        // Prepend the scheme, if it is non-default
        final String sIDPrefix = (PeppolIdentifierHelper.DEFAULT_PROCESS_SCHEME.equals (sScheme) ? "" : sScheme + "-");
        final String sEnumConstName = RegExHelper.getAsIdentifier (sIDPrefix + sID);
        final JEnumConstant jEnumConst = jEnum.enumConstant (sEnumConstName);
        jEnumConst.arg (JExpr.lit (sScheme));
        jEnumConst.arg (JExpr.lit (sID));

        final JInvocation eDocTypeIDs = JExpr._new (s_aCodeModel.ref (CommonsArrayList.class).narrowEmpty ());
        for (final String sDocTypeID : aEntry.getValue ())
          eDocTypeIDs.arg (JExpr.lit (sDocTypeID));
        jEnumConst.arg (eDocTypeIDs);

        jEnumConst.javadoc ().add ("<code>" + sID + "</code><br>");
        for (final String sDocTypeID : aEntry.getValue ())
          jEnumConst.javadoc ().add ("\nUse with DocTypeID <code>" + sDocTypeID + "</code><br>");
      }

      // fields
      final JFieldVar fScheme = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sScheme");
      final JFieldVar fID = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sID");
      final JFieldVar fDocTypeIDs = jEnum.field (JMod.PRIVATE | JMod.FINAL,
                                                 s_aCodeModel.ref (ICommonsList.class)
                                                             .narrow (IDocumentTypeIdentifier.class),
                                                 "m_aDocTypeIDs");

      // Constructor
      final JMethod jCtor = jEnum.constructor (JMod.PRIVATE);
      final JVar jScheme = jCtor.param (JMod.FINAL, String.class, "sScheme");
      jScheme.annotate (Nonnull.class);
      jScheme.annotate (Nonempty.class);
      final JVar jID = jCtor.param (JMod.FINAL, String.class, "sID");
      jID.annotate (Nonnull.class);
      jID.annotate (Nonempty.class);
      final JVar jDocTypeIDs = jCtor.param (JMod.FINAL,
                                            s_aCodeModel.ref (ICommonsList.class).narrow (String.class),
                                            "aDocTypeIDs");
      jDocTypeIDs.annotate (Nonnull.class);
      jDocTypeIDs.annotate (Nonempty.class);
      jCtor.body ()
           .assign (fScheme, jScheme)
           .assign (fID, jID)
           .assign (fDocTypeIDs,
                    s_aCodeModel.ref (CommonsArrayList.class)
                                .narrowEmpty ()
                                ._new ()
                                .arg (jDocTypeIDs)
                                .arg (new JLambdaMethodRef (s_aCodeModel.ref (PeppolIdentifierFactory.class)
                                                                        .staticRef ("INSTANCE"),
                                                            "parseDocumentTypeIdentifier")));

      // public String getScheme ()
      JMethod m = jEnum.method (JMod.PUBLIC, String.class, "getScheme");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fScheme);

      // public String getValue ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getValue");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fID);

      // public ICommonsList<IDocumentTypeIdentifier> getAllDocTypeIDs ()
      m = jEnum.method (JMod.PUBLIC,
                        s_aCodeModel.ref (ICommonsList.class).narrow (IDocumentTypeIdentifier.class),
                        "getAllDocTypeIDs");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.annotate (ReturnsMutableCopy.class);
      m.body ()._return (fDocTypeIDs.invoke ("getClone"));

      // public boolean hasDefaultScheme()
      m = jEnum.method (JMod.PUBLIC, boolean.class, "hasDefaultScheme");
      m.body ()
       ._return (s_aCodeModel.ref (PeppolIdentifierHelper.class)
                             .staticRef ("DEFAULT_DOCUMENT_TYPE_SCHEME")
                             .invoke ("equals")
                             .arg (fScheme));

      // public PeppolProcessIdentifier getAsProcessIdentifier ()
      m = jEnum.method (JMod.PUBLIC, PeppolProcessIdentifier.class, "getAsProcessIdentifier");
      m.annotate (Nonnull.class);
      m.body ()._return (JExpr._new (s_aCodeModel.ref (PeppolProcessIdentifier.class)).arg (JExpr._this ()));

      // @Nullable public static EPredefinedProcessIdentifier
      // getFromProcessIdentifierOrNull(@Nullable final IProcessIdentifier
      // aProcessID)
      m = jEnum.method (JMod.PUBLIC | JMod.STATIC, jEnum, "getFromProcessIdentifierOrNull");
      {
        m.annotate (Nullable.class);
        final JVar jValue = m.param (JMod.FINAL, IProcessIdentifier.class, "aProcessID");
        jValue.annotate (Nullable.class);
        final JBlock jIf = m.body ()._if (jValue.neNull ())._then ();
        final JForEach jForEach = jIf.forEach (jEnum, "e", jEnum.staticInvoke ("values"));
        jForEach.body ()
                ._if (jForEach.var ()
                              .invoke ("hasScheme")
                              .arg (jValue.invoke ("getScheme"))
                              .cand (jForEach.var ().invoke ("hasValue").arg (jValue.invoke ("getValue"))))
                ._then ()
                ._return (jForEach.var ());
        m.body ()._return (JExpr._null ());
      }
    }
    catch (final JClassAlreadyExistsException ex)
    {
      LOGGER.warn ("Failed to create source", ex);
    }
  }

  private static void _handleTransportProfileIdentifiers (final Sheet aTPSheet) throws URISyntaxException
  {
    final ExcelReadOptions <UseType> aReadOptions = new ExcelReadOptions <UseType> ().setLinesToSkip (1)
                                                                                     .setLineIndexShortName (0);
    aReadOptions.addColumn (0, "protocol", UseType.REQUIRED, "string", false);
    aReadOptions.addColumn (1, "profileversion", UseType.REQUIRED, "string", false);
    aReadOptions.addColumn (2, "profileid", UseType.REQUIRED, "string", true);
    aReadOptions.addColumn (3, "since", UseType.REQUIRED, "string", false);
    aReadOptions.addColumn (4, "deprecated", UseType.REQUIRED, "boolean", false);
    aReadOptions.addColumn (5, "deprecated-since", UseType.OPTIONAL, "string", false);

    final CodeListDocument aCodeList = ExcelSheetToCodeList10.convertToSimpleCodeList (aTPSheet,
                                                                                       aReadOptions,
                                                                                       "PeppolTransportProfileIdentifier",
                                                                                       CODELIST_VERSION.getAsString (),
                                                                                       new URI ("urn:peppol.eu:names:identifier:transportprofile"),
                                                                                       new URI ("urn:peppol.eu:names:identifier:transportprofile-1.0"),
                                                                                       null);
    _writeGenericodeFile (aCodeList, RESULT_DIRECTORY + "PeppolTransportProfileIdentifier" + FILENAME_SUFFIX + ".gc");

    // Save as XML
    final IMicroDocument aDoc = new MicroDocument ();
    aDoc.appendComment (DO_NOT_EDIT);
    final IMicroElement eRoot = aDoc.appendElement ("root");
    eRoot.setAttribute ("version", CODELIST_VERSION.getAsString ());
    for (final Row aRow : aCodeList.getSimpleCodeList ().getRow ())
    {
      final String sProtocol = CodeGenerationHelper.getRowValue (aRow, "protocol");
      final String sProfileVersion = CodeGenerationHelper.getRowValue (aRow, "profileversion");
      final String sProfileID = CodeGenerationHelper.getRowValue (aRow, "profileid");
      final String sSince = CodeGenerationHelper.getRowValue (aRow, "since");
      final boolean bDeprecated = CodeGenerationHelper.parseDeprecated (CodeGenerationHelper.getRowValue (aRow,
                                                                                                            "deprecated"));
      final String sDeprecatedSince = CodeGenerationHelper.getRowValue (aRow, "deprecated-since");

      if (bDeprecated && StringHelper.hasNoText (sDeprecatedSince))
        throw new IllegalStateException ("Code list entry is deprecated but there is no deprecated-since entry");

      final IMicroElement eAgency = eRoot.appendElement ("process");
      eAgency.setAttribute ("protocol", sProtocol);
      eAgency.setAttribute ("profileversion", sProfileVersion);
      eAgency.setAttribute ("profileid", sProfileID);
      eAgency.setAttribute ("since", sSince);
      eAgency.setAttribute ("deprecated", bDeprecated);
      eAgency.setAttribute ("deprecated-since", sDeprecatedSince);
    }
    MicroWriter.writeToFile (aDoc,
                             new File (RESULT_DIRECTORY +
                                       "PeppolTransportProfileIdentifier" +
                                       FILENAME_SUFFIX +
                                       ".xml"));

    // Create Java source
    try
    {
      final JDefinedClass jEnum = s_aCodeModel._package (RESULT_PACKAGE_PREFIX + "transportprofile")
                                              ._enum ("EPredefinedTransportProfileIdentifier" + FILENAME_SUFFIX);
      jEnum._implements (s_aCodeModel.ref (IPredefinedTransportProfileIdentifier.class));
      jEnum.annotate (CodingStyleguideUnaware.class);
      jEnum.javadoc ().add (DO_NOT_EDIT);

      // enum constants
      final ICommonsSet <String> aAllShortcutNames = new CommonsHashSet <> ();
      for (final Row aRow : aCodeList.getSimpleCodeList ().getRow ())
      {
        final String sProtocol = CodeGenerationHelper.getRowValue (aRow, "protocol");
        final String sProfileVersion = CodeGenerationHelper.getRowValue (aRow, "profileversion");
        final String sProfileID = CodeGenerationHelper.getRowValue (aRow, "profileid");
        final String sSince = CodeGenerationHelper.getRowValue (aRow, "since");
        final boolean bDeprecated = CodeGenerationHelper.parseDeprecated (CodeGenerationHelper.getRowValue (aRow,
                                                                                                              "deprecated"));
        final String sDeprecatedSince = CodeGenerationHelper.getRowValue (aRow, "deprecated-since");

        // Prepend the scheme, if it is non-default
        final String sEnumConstName = RegExHelper.getAsIdentifier (sProfileID);
        final JEnumConstant jEnumConst = jEnum.enumConstant (sEnumConstName);
        jEnumConst.arg (JExpr.lit (sProtocol));
        jEnumConst.arg (JExpr.lit (sProfileVersion));
        jEnumConst.arg (JExpr.lit (sProfileID));
        jEnumConst.arg (s_aCodeModel.ref (Version.class).staticInvoke ("parse").arg (sSince));
        jEnumConst.arg (JExpr.lit (bDeprecated));
        if (bDeprecated)
        {
          jEnumConst.annotate (Deprecated.class);
          jEnumConst.javadoc ()
                    .add ("<b>This item is deprecated since version " +
                          sDeprecatedSince +
                          " and should not be used to issue new identifiers!</b><br>");
        }
        jEnumConst.javadoc ().add ("<code>" + sProfileID + "</code><br>");
        jEnumConst.javadoc ().addTag (JDocComment.TAG_SINCE).add ("code list " + sSince);

        // Emit shortcut name for better readability
        final String sShortcutName = CodeGenerationHelper.createShortcutTransportProtocolName (sProtocol +
                                                                                               "_" +
                                                                                               sProfileVersion);
        if (sShortcutName != null)
        {
          if (!aAllShortcutNames.add (sShortcutName))
            throw new IllegalStateException ("The Transport Profile shortcut '" +
                                             sShortcutName +
                                             "' is already used - please review the algorithm!");
          final JFieldVar aShortcut = jEnum.field (JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                                                   jEnum,
                                                   sShortcutName,
                                                   jEnumConst);
          if (bDeprecated)
            aShortcut.annotate (Deprecated.class);
          aShortcut.javadoc ().add ("Same as {@link #" + sEnumConstName + "}");
        }
      }

      // fields
      final JFieldVar fProtocol = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sProtocol");
      final JFieldVar fProfileVersion = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sProfileVersion");
      final JFieldVar fProfileID = jEnum.field (JMod.PRIVATE | JMod.FINAL, String.class, "m_sProfileID");
      final JFieldVar fSince = jEnum.field (JMod.PRIVATE | JMod.FINAL, Version.class, "m_aSince");
      final JFieldVar fDeprecated = jEnum.field (JMod.PRIVATE | JMod.FINAL, boolean.class, "m_bDeprecated");

      // Constructor
      final JMethod jCtor = jEnum.constructor (JMod.PRIVATE);
      final JVar jProtocol = jCtor.param (JMod.FINAL, String.class, "sProtocol");
      jProtocol.annotate (Nonnull.class);
      jProtocol.annotate (Nonempty.class);
      final JVar jProfileVersion = jCtor.param (JMod.FINAL, String.class, "sProfileVersion");
      jProfileVersion.annotate (Nonnull.class);
      jProfileVersion.annotate (Nonempty.class);
      final JVar jProfileID = jCtor.param (JMod.FINAL, String.class, "sProfileID");
      jProfileID.annotate (Nonnull.class);
      jProfileID.annotate (Nonempty.class);
      final JVar jSince = jCtor.param (JMod.FINAL, Version.class, "aSince");
      jSince.annotate (Nonnull.class);
      final JVar jDeprecated = jCtor.param (JMod.FINAL, boolean.class, "bDeprecated");
      jCtor.body ()
           .assign (fProtocol, jProtocol)
           .assign (fProfileVersion, jProfileVersion)
           .assign (fProfileID, jProfileID)
           .assign (fSince, jSince)
           .assign (fDeprecated, jDeprecated);

      // public String getProtocol()
      JMethod m = jEnum.method (JMod.PUBLIC, String.class, "getProtocol");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fProtocol);

      // public String getProfileVersion ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getProfileVersion");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fProfileVersion);

      // public String getProfileID ()
      m = jEnum.method (JMod.PUBLIC, String.class, "getProfileID");
      m.annotate (Nonnull.class);
      m.annotate (Nonempty.class);
      m.body ()._return (fProfileID);

      // public Version getSince ()
      m = jEnum.method (JMod.PUBLIC, Version.class, "getSince");
      m.annotate (Nonnull.class);
      m.body ()._return (fSince);

      // public boolean isDeprecated ()
      m = jEnum.method (JMod.PUBLIC, boolean.class, "isDeprecated");
      m.body ()._return (fDeprecated);
    }
    catch (final JClassAlreadyExistsException ex)
    {
      LOGGER.warn ("Failed to create source", ex);
    }
  }

  private static final class CodeListFile
  {
    private final File m_aFile;
    private final IThrowingConsumer <? super Sheet, Exception> m_aHandler;

    public CodeListFile (@Nonnull final String sFilenamePart,
                         @Nonnull final IThrowingConsumer <? super Sheet, Exception> aHandler)
    {
      m_aFile = new File ("src/test/resources/codelists/PEPPOL Code Lists - " +
                          sFilenamePart +
                          " v" +
                          CODELIST_VERSION.getAsString (false) +
                          CODELIST_FILE_SUFFIX +
                          ".xlsx").getAbsoluteFile ();
      ValueEnforcer.isTrue (m_aFile.exists (), () -> "File '" + m_aFile.getAbsolutePath () + "' does not exist!");
      m_aHandler = aHandler;
    }
  }

  public static void main (final String [] args) throws Exception
  {
    // DocumentType must be before Processes to fill the static list
    for (final CodeListFile aCLF : new CodeListFile [] { new CodeListFile ("Document types",
                                                                           MainCreatePredefinedEnumsFromExcel_v7::_handleDocumentTypes),
                                                         new CodeListFile ("Participant identifier schemes",
                                                                           MainCreatePredefinedEnumsFromExcel_v7::_handleParticipantIdentifierSchemes),
                                                         new CodeListFile ("Transport profiles",
                                                                           MainCreatePredefinedEnumsFromExcel_v7::_handleTransportProfileIdentifiers) })
    {
      // Where is the Excel?
      final IReadableResource aXls = new FileSystemResource (aCLF.m_aFile);
      if (!aXls.exists ())
        throw new IllegalStateException ("The Excel file '" +
                                         aCLF.m_aFile.getAbsolutePath () +
                                         "' could not be found!");

      // Interpret as Excel
      try (final Workbook aWB = new XSSFWorkbook (aXls.getInputStream ()))
      {
        // Check whether all required sheets are present
        final Sheet aSheet = aWB.getSheetAt (0);
        if (aSheet == null)
          throw new IllegalStateException ("The first sheet could not be found!");

        aCLF.m_aHandler.accept (aSheet);
      }
    }

    _handleProcessIdentifiers ();

    // Write all Java source files
    new JCMWriter (s_aCodeModel).build (new File ("src/main/java"), LOGGER::info);

    LOGGER.info ("Done creating code");
  }
}
