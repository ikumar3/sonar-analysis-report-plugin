/**
 * Copyright (C) 2013 JiteshDundas <jbdundas@gmail.com>
 *
 * Licensed under the RPL License, Version 1.5 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://opensource.org/licenses/RPL-1.5
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jnd.sonar.analysisreport;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.Plugin;
import org.sonar.api.Properties;
import org.sonar.api.Property;


/**
 * @author Jitesh Dundas
 * @date 27-May-2013
 * 
 */
@Properties({
    @Property(
        key = AnalysisReportHelper.ENABLED_PROPERTY,
        name = "Enabled",
        defaultValue = AnalysisReportHelper.ENABLED_DEFAULT_VALUE + "",
        global = true, project = true, module = false),
    @Property(
        key = AnalysisReportHelper.HOST_PROPERTY,
        name = "SMTP host",
        defaultValue = AnalysisReportHelper.SMTP_HOST_DEFAULT_VALUE,
        global = true, project = false, module = false),
    @Property(
        key = AnalysisReportHelper.PORT_PROPERTY,
        name = "SMTP port",
        defaultValue = AnalysisReportHelper.PORT_DEFAULT_VALUE,
        global = true, project = false, module = false),
    @Property(
        key = AnalysisReportHelper.USERNAME_PROPERTY,
        name = "SMTP username",
        global = true, project = false, module = false),
    @Property(
        key = AnalysisReportHelper.PASSWORD_PROPERTY,
        name = "SMTP password",
        global = true, project = false, module = false),
    @Property(
        key = AnalysisReportHelper.FROM_PROPERTY,
        name = "From",
        global = true, project = false, module = false),
    @Property(
            key = AnalysisReportHelper.TO_SUBJECT_PROPERTY,
            name = "Subject",
            global = true, project = false, module = false),
    @Property(
            key = AnalysisReportHelper.TO_MESSAGE_PROPERTY,
            name = "Message",
            global = true, project = false, module = false),
    @Property(
            key = AnalysisReportHelper.TO_SET_SSL_ON_CONNECT_PROPERTY,
            name = "Set SSL On Connect",
            global = true, project = false, module = false),
    @Property(
            key = AnalysisReportHelper.TO_SMS_PROPERTY,
            name = "Message",
            global = true, project = false, module = false),
    @Property(
            key = AnalysisReportHelper.TO_SMS_PROVIDER_PROPERTY,
            name = "Set SSL On Connect",
            global = true, project = false, module = false),
    @Property(
        key = AnalysisReportHelper.TO_PROPERTY,
        name = "To",
        global = false, project = true, module = false) })
public class AnalysisReportPlugin implements Plugin {

  public String getKey() {
    return "AnalysisReport";
  }

  public String getName() {
    return "AnalysisReport";
  }

  public String getDescription() {
    return "AnalysisReport";
  }

  public List getExtensions() {
    return Arrays.asList(AnalysisReportHelper.class);
  }

}
