/**
 * Copyright (C) 2015-2017 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Note: some files, that were not part of the original package are currently
 *   licensed under Apache 2.0 license - https://www.apache.org/licenses/LICENSE-2.0
 *   The respective files contain a special class header!
 *
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
package com.helger.peppol.smpclient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.httpclient.HttpClientFactory;
import com.helger.peppol.utils.PeppolKeyStoreHelper;
import com.helger.settings.exchange.configfile.ConfigFile;
import com.helger.settings.exchange.configfile.ConfigFileBuilder;

/**
 * This class manages the configuration properties of the SMP client. The order
 * of the properties file resolving is as follows:
 * <ol>
 * <li>Check for the value of the system property
 * <code>peppol.smp.client.properties.path</code></li>
 * <li>Check for the value of the system property
 * <code>smp.client.properties.path</code></li>
 * <li>The filename <code>private-smp-client.properties</code> in the root of
 * the classpath</li>
 * <li>The filename <code>smp-client.properties</code> in the root of the
 * classpath</li>
 * </ol>
 * <p>
 * Note: this class is also licensed under Apache 2 license, as it was not part
 * of the original implementation
 * </p>
 *
 * @author Philip Helger
 */
@Immutable
public final class SMPClientConfiguration
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SMPClientConfiguration.class);
  private static final ConfigFile s_aConfigFile;

  static
  {
    final ConfigFileBuilder aCFB = new ConfigFileBuilder ().addPathFromSystemProperty ("peppol.smp.client.properties.path")
                                                           .addPathFromSystemProperty ("smp.client.properties.path")
                                                           .addPath ("private-smp-client.properties")
                                                           .addPath ("smp-client.properties");

    s_aConfigFile = aCFB.build ();
    if (s_aConfigFile.isRead ())
      s_aLogger.info ("Read PEPPOL SMP client properties from " + s_aConfigFile.getReadResource ().getPath ());
    else
      s_aLogger.warn ("Failed to read PEPPOL SMP client properties from " + aCFB.getAllPaths ());
  }

  private SMPClientConfiguration ()
  {}

  /**
   * @return The global config file for the SMP client.
   */
  @Nonnull
  public static ConfigFile getConfigFile ()
  {
    return s_aConfigFile;
  }

  /**
   * @return The truststore location as specified in the configuration file by
   *         the key <code>truststore.location</code>. If none is present
   *         {@link PeppolKeyStoreHelper#TRUSTSTORE_COMPLETE_CLASSPATH} is
   *         returned as a default.
   */
  @Nonnull
  public static String getTruststoreLocation ()
  {
    return s_aConfigFile.getAsString ("truststore.location", PeppolKeyStoreHelper.TRUSTSTORE_COMPLETE_CLASSPATH);
  }

  /**
   * @return The truststore password as specified in the configuration file by
   *         the key <code>truststore.password</code>. If none is present
   *         {@link PeppolKeyStoreHelper#TRUSTSTORE_PASSWORD} is returned as a
   *         default.
   */
  @Nonnull
  public static String getTruststorePassword ()
  {
    return s_aConfigFile.getAsString ("truststore.password", PeppolKeyStoreHelper.TRUSTSTORE_PASSWORD);
  }

  /**
   * @return The HttpProxy object to be used by SMP clients based on the Java
   *         System properties "http.proxyHost" and "http.proxyPort". Note:
   *         https is not needed, because SMPs must run on http only.
   */
  @Nullable
  public static HttpHost getHttpProxy ()
  {
    final String sProxyHost = s_aConfigFile.getAsString ("http.proxyHost");
    final int nProxyPort = s_aConfigFile.getAsInt ("http.proxyPort", 0);
    if (sProxyHost != null && nProxyPort > 0)
      return new HttpHost (sProxyHost, nProxyPort);

    return null;
  }

  /**
   * @return The {@link UsernamePasswordCredentials} object to be used for proxy
   *         server authentication.
   * @since 5.2.5
   */
  @Nullable
  public static UsernamePasswordCredentials getHttpProxyCredentials ()
  {
    final String sProxyUsername = s_aConfigFile.getAsString ("http.proxyUsername");
    final String sProxyPassword = s_aConfigFile.getAsString ("http.proxyPassword");
    if (sProxyUsername != null && sProxyPassword != null)
      return new UsernamePasswordCredentials (sProxyUsername, sProxyPassword);

    return null;
  }

  /**
   * Get the content of the property "http.useSystemProperties" or
   * <code>false</code> if undefined.
   *
   * @return <code>true</code> if the SMP client proxy configuration should be
   *         take from the system properties, or <code>false</code> if not. The
   *         default behavior is to return <code>false</code>.
   * @since 5.2.4
   */
  public static boolean isUseProxySystemProperties ()
  {
    return s_aConfigFile.getAsBoolean ("http.useSystemProperties", HttpClientFactory.DEFAULT_USE_SYSTEM_PROPERTIES);
  }

  /**
   * Get the content of the property "http.useDNSClientCache" or
   * <code>true</code> if undefined.
   *
   * @return <code>true</code> if the SMP client should use DNS client caching
   *         (default) or <code>false</code> if DNS caching should be disabled.
   *         The default behavior is to return <code>true</code>.
   * @since 5.2.5
   */
  public static boolean isUseDNSClientCache ()
  {
    return s_aConfigFile.getAsBoolean ("http.useDNSClientCache", HttpClientFactory.DEFAULT_USE_DNS_CACHE);
  }
}
