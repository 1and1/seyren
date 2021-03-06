/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seyren.core.util.velocity;

import java.io.StringWriter;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.seyren.core.domain.Alert;
import com.seyren.core.domain.Check;
import com.seyren.core.domain.Subscription;
import com.seyren.core.util.config.SeyrenConfig;
import com.seyren.core.util.email.EmailHelper;

@Named
public class VelocityEmailHelper extends AbstractHelper implements EmailHelper {

    // Will first attempt to load from classpath then fall back to loading from the filesystem.
    private final String TEMPLATE_CONTENT;

    /**
     * Loads content of configurable templated email message at creation time.
     *
     * @param seyrenConfig Used for both email template file name and the seyren URL.
     */
    @Inject
    public VelocityEmailHelper(SeyrenConfig seyrenConfig) {
        super(seyrenConfig);
        TEMPLATE_CONTENT = getTemplateAsString(seyrenConfig.getEmailTemplateFileName());
    }
    
    public String createSubject(Check check) {
        return "Seyren alert: " + check.getName();
    }
    
    @Override
    public String createBody(Check check, Subscription subscription, List<Alert> alerts) {
        VelocityContext context = createVelocityContext(check, subscription, alerts);
        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context, stringWriter, "EmailNotificationService", TEMPLATE_CONTENT);
        return stringWriter.toString();
    }


    
}
