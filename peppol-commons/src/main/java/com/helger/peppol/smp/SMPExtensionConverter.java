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
package com.helger.peppol.smp;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.string.StringHelper;
import com.helger.xml.serialize.read.DOMReader;
import com.helger.xml.serialize.write.EXMLSerializeDocType;
import com.helger.xml.serialize.write.EXMLSerializeIndent;
import com.helger.xml.serialize.write.XMLWriter;
import com.helger.xml.serialize.write.XMLWriterSettings;

/**
 * This class is used for converting between a String representation of the
 * extension element and the "ExtensionType" complex type used in the PEPPOL
 * SMP.
 *
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
@Immutable
public final class SMPExtensionConverter
{
  private static final XMLWriterSettings s_aXWS = new XMLWriterSettings ().setSerializeDocType (EXMLSerializeDocType.IGNORE)
                                                                          .setIndent (EXMLSerializeIndent.NONE);

  @PresentForCodeCoverage
  private static final SMPExtensionConverter s_aInstance = new SMPExtensionConverter ();

  private SMPExtensionConverter ()
  {}

  /**
   * Convert the passed extension type to a string representation.
   *
   * @param aExtension
   *        The extension to be converted. May be <code>null</code>.
   * @return <code>null</code> if no extension was passed - the XML
   *         representation of the extension otherwise.
   * @throws IllegalArgumentException
   *         If the Extension cannot be converted to a String
   */
  @Nullable
  public static String convertToString (@Nullable final ExtensionType aExtension)
  {
    // If there is no extension present, nothing to convert
    if (aExtension != null && aExtension.getAny () != null)
    {
      // Get the extension content
      return XMLWriter.getNodeAsString (aExtension.getAny (), s_aXWS);
    }
    return null;
  }

  /**
   * Convert the passed XML string to an SMP extension type.
   *
   * @param sXML
   *        the XML representation to be converted.
   * @return <code>null</code> if the passed string is empty.
   * @throws IllegalArgumentException
   *         If the String cannot be converted to a XML node
   */
  @Nullable
  public static ExtensionType convert (@Nullable final String sXML)
  {
    if (StringHelper.hasText (sXML))
    {
      try
      {
        // Try to interpret as XML
        final Document aDoc = DOMReader.readXMLDOM (sXML);
        if (aDoc != null)
        {
          final ExtensionType aExtension = new ExtensionType ();
          aExtension.setAny (aDoc.getDocumentElement ());
          return aExtension;
        }
      }
      catch (final SAXException ex)
      {
        throw new IllegalArgumentException ("Error in parsing extension XML '" + sXML + "'", ex);
      }
    }

    return null;
  }

  /**
   * Convert the passed XML string to an SMP extension type.
   *
   * @param sXML
   *        the XML representation to be converted.
   * @return <code>null</code> if the passed string is empty or cannot be
   *         converted to a XML node
   */
  @Nullable
  public static ExtensionType convertOrNull (@Nullable final String sXML)
  {
    try
    {
      return convert (sXML);
    }
    catch (final IllegalArgumentException ex)
    {
      // Invalid XML passed
      return null;
    }
  }
}
