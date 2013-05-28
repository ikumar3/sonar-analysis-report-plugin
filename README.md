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

Analysis-Report Plugin:-
=====================

This plugin will send an email/sms after every analysis is completed.<br>

Added the following functionalities to the plugin:-<br>
=====================================================================<br>
1) PDF Report<br>
2) HTML based content with violations summary<br>
3) Image Logo<br>


You need to add the values of the email-related properties with their values in the General Settings:-<br>
=====================================================================
Set the value of the property - <br>
1)  sonar.jd.smptp.to (suggested to set this property from the project).<br>
2)  sonar.email.enabled(true if you want the email to be sent, else set to false) <br>
3)  sonar.jd.smptp.host<br>
4)  sonar.jd.smptp.set_ssl_on_connect<br>
5)  sonar.jd.smptp.sslport<br>
6)  sonar.jd.smptp.username<br>
7)  sonar.jd.smptp.password<br>
8)  sonar.jd.smptp.from<br>
9)  sonar.jd.smptp.subject<br>
10) sonar.jd.smptp.message<br>

Steps to Install:-<br>
=====================================================================<br>
1) You can get the JAR from the zip version attached with the previous email on this thread or <br>
from  target  folder of this project's root directory in github. <br>
2) Download it into the SONAR_HOME/extensions/plugins directory<br>
3) Restart the Sonar server<br>
4) You will then have to define the properties as mentioned in the <br>
ReadMe.md above<br>
5) Note that you can define multiple To addresses from the project configuration <br>
file ( sonar-project.properties for Sonar Runner, build.xml for ant or pom.xml for maven ).<br>
 This will supersede the value put for the same field from the global settings page and it will<br>
  send an email accordingly.<br>
  
 Note:- A sample report can be seen at:- <br>
 sonarreport1368751371210_-847661506.pdf <br>


SMS Notification:-<br>
============================================================================
You will need the following proeprties too:-
1) sonar.jd.sms.to - The phone number to which you wish to send the sms ( with country code ).
2) sonar.jd.sms.to.provider - The name of the carrier for the sms . For e.g. AT&T, Verizon, etc.
3) sonar.jd.sms.enabled - Is SMS sending enabled? True or False

You can find the exact name of your provider and other details on this page:-
http://en.wikipedia.org/wiki/List_of_SMS_gateways

Note that the first and third property are mandatory while the second is optional if you have the entire string of 
mobile number as an email from the above link.
