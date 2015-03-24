/**
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
package com.helger.peppol.supplementary.tools;

import java.io.File;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.charset.CCharset;
import com.helger.commons.io.IReadableResource;
import com.helger.commons.io.file.SimpleFileIO;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.messagedigest.EMessageDigestAlgorithm;
import com.helger.commons.messagedigest.MessageDigestGeneratorHelper;
import com.helger.peppol.security.KeyStoreUtils;

/**
 * Utility class to create hash codes of the global trust store to verify if it
 * is valid or not.
 * 
 * @author PEPPOL.AT, BRZ, Philip Helger
 */
public final class MainCreateTrustStoreHashFiles {
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainCreateTrustStoreHashFiles.class);

  private static void _create (@Nonnull final String sTruststorePath) {
    final IReadableResource aTrustStore = new ClassPathResource (sTruststorePath);

    final byte [] aMD5 = MessageDigestGeneratorHelper.getDigestFromInputStream (aTrustStore.getInputStream (),
                                                                                EMessageDigestAlgorithm.MD5);
    SimpleFileIO.writeFile (new File ("src/main/resources/" + sTruststorePath + ".md5"),
                            MessageDigestGeneratorHelper.getHexValueFromDigest (aMD5),
                            CCharset.CHARSET_ISO_8859_1_OBJ);
    final byte [] aSHA1 = MessageDigestGeneratorHelper.getDigestFromInputStream (aTrustStore.getInputStream (),
                                                                                 EMessageDigestAlgorithm.SHA_1);
    SimpleFileIO.writeFile (new File ("src/main/resources/" + sTruststorePath + ".sha1"),
                            MessageDigestGeneratorHelper.getHexValueFromDigest (aSHA1),
                            CCharset.CHARSET_ISO_8859_1_OBJ);

    s_aLogger.info ("Done creating hash values for " + sTruststorePath);
  }

  public static void main (final String [] args) {
    _create (KeyStoreUtils.TRUSTSTORE_CLASSPATH);
    _create (KeyStoreUtils.TRUSTSTORE_CLASSPATH_PEPPOL);
    _create (KeyStoreUtils.TRUSTSTORE_CLASSPATH_OPENPEPPOL);
  }
}
