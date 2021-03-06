/*
 * Copyright 2013 eXo Platform SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package juzu.impl.bridge.context;

import juzu.test.AbstractWebTestCase;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public abstract class AbstractContextUserTestCase extends AbstractWebTestCase {

  /** . */
  public static Locale locale;

  protected void test(URL initialURL) throws Exception {
    locale = null;
    HttpURLConnection conn = (HttpURLConnection)initialURL.openConnection();
    conn.addRequestProperty("Accept-Language", "fr-FR");
    assertEquals(200, conn.getResponseCode());
    assertEquals(Locale.FRANCE, locale);
  }
}
