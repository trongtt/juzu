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

package bridge.portlet.event.produce;

import juzu.bridge.portlet.JuzuPortlet;
import juzu.impl.bridge.portlet.ProduceEventTestCase;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.io.IOException;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class TestPortlet extends JuzuPortlet {

  @Override
  public void processEvent(EventRequest request, EventResponse response) throws PortletException, IOException {
    ProduceEventTestCase.eventNames.add(request.getEvent().getName());
    ProduceEventTestCase.eventQNames.add(request.getEvent().getQName());
    ProduceEventTestCase.eventPayloads.add(request.getEvent().getValue());
  }

  @Override
  protected String getApplicationName(PortletConfig config) {
    return "bridge.portlet.event.produce";
  }
}
