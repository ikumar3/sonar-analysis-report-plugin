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

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.sonar.api.Property;
import org.sonar.api.batch.PostJob;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.MetricFinder;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;



public final class AnalysisReportHelper implements PostJob {
	  
	  public static final String ENABLED_PROPERTY = "sonar.email.enabled";
	  public static final boolean ENABLED_DEFAULT_VALUE = true;

	  public static final String HOST_PROPERTY = "sonar.jd.smptp.host";
	  public static final String SMTP_HOST_DEFAULT_VALUE = "smtp.gmail.com";

	  public static final String PORT_PROPERTY = "sonar.jd.smptp.sslport";
	  public static final String PORT_DEFAULT_VALUE = "465";

	  public static final String USERNAME_PROPERTY = "sonar.jd.smptp.username";

	  public static final String PASSWORD_PROPERTY = "sonar.jd.smptp.password";

	  public static final String FROM_PROPERTY = "sonar.jd.smptp.from";

	  public static final String TO_PROPERTY = "sonar.jd.smptp.to";

	  public static final String TO_SUBJECT_PROPERTY = "sonar.jd.smptp.subject";
	  
	  public static final String TO_MESSAGE_PROPERTY = "sonar.jd.smptp.message";
	  
	  public static final String TO_SET_SSL_ON_CONNECT_PROPERTY = "sonar.jd.smptp.set_ssl_on_connect";

	  private static final String PROJECT_INDEX_URI = "/project/index/";
	  
	  static final String TO_SMS_PROPERTY = "sonar.jd.sms.to";
	            
	  static final String TO_SMS_PROVIDER_PROPERTY = "sonar.jd.sms.to.provider";
	  
	  private static final String SMS_ENABLED_PROPERTY = "sonar.jd.sms.enabled";
			  
	  private Settings settings;
	  private MetricFinder metricFinder;
	  
	  private Map<String,String> reportDataMap = new HashMap<String,String>();
	  
	  String from = null;
      String to_email = null;
      String to_email_name = null;
      String username = null;
      String password = null;
      String hostname = null;
      String portno = null;
      boolean setSSLOnConnectFlag ;
      String subject = null;
      String message = null;
      String send_sms_to = null;
      String send_sms_to_provider = null;
	  //private String[] metricKeys = {ABSTRACTNESS,ABSTRACTNESS_KEY,ACCESSORS,ACCESSORS_KEY,AFFERENT_COUPLINGS,AFFERENT_COUPLINGS_KEY,ALERT_STATUS,ALERT_STATUS_KEY,BLOCKER_VIOLATIONS,BLOCKER_VIOLATIONS_KEY,BRANCH_COVERAGE,BRANCH_COVERAGE_HITS_DATA,BRANCH_COVERAGE_HITS_DATA_KEY,BRANCH_COVERAGE_KEY,CLASS_COMPLEXITY,CLASS_COMPLEXITY_DISTRIBUTION,CLASS_COMPLEXITY_DISTRIBUTION_KEY,CLASS_COMPLEXITY_KEY,CLASSES,CLASSES_KEY,COMMENT_BLANK_LINES,COMMENT_BLANK_LINES_KEY,COMMENT_LINES,COMMENT_LINES_DENSITY,COMMENT_LINES_DENSITY_KEY,COMMENT_LINES_KEY,COMMENTED_OUT_CODE_LINES,COMMENTED_OUT_CODE_LINES_KEY,COMPLEXITY,COMPLEXITY_KEY,CONDITIONS_BY_LINE,CONDITIONS_BY_LINE_KEY,CONDITIONS_TO_COVER,CONDITIONS_TO_COVER_KEY,COVERAGE,COVERAGE_KEY,COVERAGE_LINE_HITS_DATA,COVERAGE_LINE_HITS_DATA_KEY,COVERED_CONDITIONS_BY_LINE,COVERED_CONDITIONS_BY_LINE_KEY,CRITICAL_VIOLATIONS,CRITICAL_VIOLATIONS_KEY,DEPENDENCY_MATRIX,DEPENDENCY_MATRIX_KEY,DEPTH_IN_TREE,DEPTH_IN_TREE_KEY,DIRECTORIES,DIRECTORIES_KEY,DISTANCE,DISTANCE_KEY,DOMAIN_COMPLEXITY,DOMAIN_DESIGN,DOMAIN_DOCUMENTATION,DOMAIN_DUPLICATION,DOMAIN_GENERAL,DOMAIN_RULE_CATEGORIES,DOMAIN_RULES,DOMAIN_SCM,DOMAIN_SIZE,DOMAIN_TESTS,DUPLICATED_BLOCKS,DUPLICATED_BLOCKS_KEY,DUPLICATED_FILES,DUPLICATED_FILES_KEY,DUPLICATED_LINES,DUPLICATED_LINES_DENSITY,DUPLICATED_LINES_DENSITY_KEY,DUPLICATED_LINES_KEY,DUPLICATIONS_DATA,DUPLICATIONS_DATA_KEY,EFFERENT_COUPLINGS,EFFERENT_COUPLINGS_KEY,EFFICIENCY,EFFICIENCY_KEY,FILE_COMPLEXITY,FILE_COMPLEXITY_DISTRIBUTION,FILE_COMPLEXITY_DISTRIBUTION_KEY,FILE_COMPLEXITY_KEY,FILE_CYCLES,FILE_CYCLES_KEY,FILE_EDGES_WEIGHT,FILE_EDGES_WEIGHT_KEY,FILE_FEEDBACK_EDGES,FILE_FEEDBACK_EDGES_KEY,FILE_TANGLE_INDEX,FILE_TANGLE_INDEX_KEY,FILE_TANGLES,FILE_TANGLES_KEY,FILES,FILES_KEY,FUNCTION_COMPLEXITY,FUNCTION_COMPLEXITY_DISTRIBUTION,FUNCTION_COMPLEXITY_DISTRIBUTION_KEY,FUNCTION_COMPLEXITY_KEY,FUNCTIONS,FUNCTIONS_KEY,GENERATED_LINES,GENERATED_LINES_KEY,GENERATED_NCLOC,GENERATED_NCLOC_KEY,INFO_VIOLATIONS,INFO_VIOLATIONS_KEY,INSTABILITY,INSTABILITY_KEY,LCOM4,LCOM4_BLOCKS,LCOM4_BLOCKS_KEY,LCOM4_DISTRIBUTION,LCOM4_DISTRIBUTION_KEY,LCOM4_KEY,LINE_COVERAGE,LINE_COVERAGE_KEY,LINES,LINES_KEY,LINES_TO_COVER,LINES_TO_COVER_KEY,MAINTAINABILITY,MAINTAINABILITY_KEY,MAJOR_VIOLATIONS,MAJOR_VIOLATIONS_KEY,MINOR_VIOLATIONS,MINOR_VIOLATIONS_KEY,NCLOC,NCLOC_KEY,NEW_BLOCKER_VIOLATIONS,NEW_BLOCKER_VIOLATIONS_KEY,NEW_BRANCH_COVERAGE,NEW_BRANCH_COVERAGE_KEY,NEW_CONDITIONS_TO_COVER,NEW_CONDITIONS_TO_COVER_KEY,NEW_COVERAGE,NEW_COVERAGE_KEY,NEW_CRITICAL_VIOLATIONS,NEW_CRITICAL_VIOLATIONS_KEY,NEW_INFO_VIOLATIONS,NEW_INFO_VIOLATIONS_KEY,NEW_LINE_COVERAGE,NEW_LINE_COVERAGE_KEY,NEW_LINES_TO_COVER,NEW_LINES_TO_COVER_KEY,NEW_MAJOR_VIOLATIONS,NEW_MAJOR_VIOLATIONS_KEY,NEW_MINOR_VIOLATIONS,NEW_MINOR_VIOLATIONS_KEY,NEW_UNCOVERED_CONDITIONS,NEW_UNCOVERED_CONDITIONS_KEY,NEW_UNCOVERED_LINES,NEW_UNCOVERED_LINES_KEY,NEW_VIOLATIONS,NEW_VIOLATIONS_KEY,NUMBER_OF_CHILDREN,NUMBER_OF_CHILDREN_KEY,PACKAGE_CYCLES,PACKAGE_CYCLES_KEY,PACKAGE_EDGES_WEIGHT_KEY,PACKAGE_FEEDBACK_EDGES,PACKAGE_FEEDBACK_EDGES_KEY,PACKAGE_TANGLE_INDEX,PACKAGE_TANGLE_INDEX_KEY,PACKAGE_TANGLES,PACKAGE_TANGLES_KEY,PACKAGES,PACKAGES_KEY,PARAGRAPH_COMPLEXITY,PARAGRAPH_COMPLEXITY_DISTRIBUTION,PARAGRAPH_COMPLEXITY_DISTRIBUTION_KEY,PARAGRAPH_COMPLEXITY_KEY,PARAGRAPHS,PARAGRAPHS_KEY,PORTABILITY,PORTABILITY_KEY,PROFILE,PROFILE_KEY,PROFILE_VERSION,PROFILE_VERSION_KEY,PUBLIC_API,PUBLIC_API_KEY,PUBLIC_DOCUMENTED_API_DENSITY,PUBLIC_DOCUMENTED_API_DENSITY_KEY,PUBLIC_UNDOCUMENTED_API,PUBLIC_UNDOCUMENTED_API_KEY,RELIABILITY,RELIABILITY_KEY,RFC,RFC_DISTRIBUTION,RFC_DISTRIBUTION_KEY,RFC_KEY,SCM_AUTHORS_BY_LINE,SCM_AUTHORS_BY_LINE_KEY,SCM_COMMITS,SCM_COMMITS_KEY,SCM_LAST_COMMIT_DATE,SCM_LAST_COMMIT_DATE_KEY,SCM_LAST_COMMIT_DATETIMES_BY_LINE,SCM_LAST_COMMIT_DATETIMES_BY_LINE_KEY,SCM_REVISION,SCM_REVISION_KEY,SCM_REVISIONS_BY_LINE,SCM_REVISIONS_BY_LINE_KEY,SKIPPED_TESTS,SKIPPED_TESTS_KEY,STATEMENTS,STATEMENTS_KEY,SUSPECT_LCOM4_DENSITY,SUSPECT_LCOM4_DENSITY_KEY,TEST_DATA,TEST_DATA_KEY,TEST_ERRORS,TEST_ERRORS_KEY,TEST_EXECUTION_TIME,TEST_EXECUTION_TIME_KEY,TEST_FAILURES,TEST_FAILURES_KEY,TEST_SUCCESS_DENSITY,TEST_SUCCESS_DENSITY_KEY,TESTS,TESTS_KEY,UNCOVERED_CONDITIONS,UNCOVERED_CONDITIONS_KEY,UNCOVERED_LINES,UNCOVERED_LINES_KEY,USABILITY,USABILITY_KEY,VIOLATIONS,VIOLATIONS_DENSITY,VIOLATIONS_DENSITY_KEY,VIOLATIONS_KEY,WEIGHTED_VIOLATIONS,WEIGHTED_VIOLATIONS_KEY }; 
	  
	  public AnalysisReportHelper(Settings settings,MetricFinder metricFinder) {
	      this.settings = settings;
	      this.metricFinder = metricFinder;
	  }
	  
	  public void executeOn(Project project, SensorContext context) {
				  
		  ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		    Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		    
		  if (!project.getConfiguration().getBoolean(ENABLED_PROPERTY, ENABLED_DEFAULT_VALUE)) {
			  return;
		  }
		  
			  try {
				reportDataMap = getAnalysisDetails(context);
				String reportname = createMetricReport(reportDataMap);
				sendEmail(reportname,reportDataMap);
				//check if sms notification is enabled. If yes, then send it.
				boolean flag_sms_enabled = Boolean.valueOf(settings.getString(SMS_ENABLED_PROPERTY));
				if ( flag_sms_enabled == true )	{
					System.out.println("SMS notification enabled. sending sms now.");
					sendNotificationSMS(send_sms_to_provider,send_sms_to,from, username,password,hostname,portno,setSSLOnConnectFlag,subject,message);
				}
			} catch (COSVisitorException cose) {
				// TODO Auto-generated catch block
				cose.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			} 
			 
	  }
	  
	
	  @SuppressWarnings("unused")
	private Map<String, String> getAnalysisDetails(SensorContext context)
	  {
		    System.out.println("Line 232.getAnalysisDetails..");
		    
			  System.out.println("JD Line 110 CLASSES_KEY");	
			  Metric metric = metricFinder.findByKey(CoreMetrics.CLASSES_KEY);
			  System.out.println("JD Line 112 ");
			  Measure measure = context.getMeasure(metric);
			  reportDataMap.put(CoreMetrics.CLASSES_KEY,measure.getIntValue().toString());
		  	  System.out.println("Metric =>"+CoreMetrics.CLASSES_KEY + " Has Value="+measure.getIntValue().toString());
		      
		  	  //Metric for CoreMetrics.BLOCKER_VIOLATIONS_KEY
		  	  System.out.println("JD Line 110 BlockerViolations");	
			  metric = metricFinder.findByKey(CoreMetrics.BLOCKER_VIOLATIONS_KEY);
			  System.out.println("JD Line 112 ");
			  measure = context.getMeasure(metric);
			  reportDataMap.put(CoreMetrics.BLOCKER_VIOLATIONS_KEY,measure.getIntValue().toString());
		  	  System.out.println("Metric =>"+CoreMetrics.BLOCKER_VIOLATIONS_KEY + " Has Value="+measure.getIntValue().toString());
		    
		  	//Metric for CoreMetrics.CRITICAL_VIOLATIONS_KEY
		  	  System.out.println("JD Line 110 CriticalViolations");	
			  metric = metricFinder.findByKey(CoreMetrics.CRITICAL_VIOLATIONS_KEY);
			  System.out.println("JD Line 112 ");
			  measure = context.getMeasure(metric);
			  reportDataMap.put(CoreMetrics.CRITICAL_VIOLATIONS_KEY,measure.getIntValue().toString());
		  	  System.out.println("Metric =>"+CoreMetrics.CRITICAL_VIOLATIONS_KEY + " Has Value="+measure.getIntValue().toString());
		      
		  	 //Metric for CoreMetrics.MAJOR_VIOLATIONS_KEY
		  	  System.out.println("JD Line 110 MajorViolations");	
			  metric = metricFinder.findByKey(CoreMetrics.MAJOR_VIOLATIONS_KEY);
			  System.out.println("JD Line 112 ");
			  measure = context.getMeasure(metric);
			  reportDataMap.put(CoreMetrics.MAJOR_VIOLATIONS_KEY,measure.getIntValue().toString());
		  	  System.out.println("Metric =>"+CoreMetrics.MAJOR_VIOLATIONS_KEY + " Has Value="+measure.getIntValue().toString());
		      
		  	  //Metric for CoreMetrics.MINOR_VIOLATIONS_KEY
		  	  System.out.println("JD Line 110 MinorViolations");	
			  metric = metricFinder.findByKey(CoreMetrics.MINOR_VIOLATIONS_KEY);
			  System.out.println("JD Line 112 ");
			  measure = context.getMeasure(metric);
			  reportDataMap.put(CoreMetrics.MINOR_VIOLATIONS_KEY,measure.getIntValue().toString());
		  	  System.out.println("Metric =>"+CoreMetrics.MINOR_VIOLATIONS_KEY + " Has Value="+measure.getIntValue().toString());
		      
		  	//Metric for CoreMetrics.INFO_VIOLATIONS_KEY
		  	  System.out.println("JD Line 110 InfoViolations");	
			  metric = metricFinder.findByKey(CoreMetrics.INFO_VIOLATIONS_KEY);
			  System.out.println("JD Line 112 ");
			  measure = context.getMeasure(metric);
			  reportDataMap.put(CoreMetrics.INFO_VIOLATIONS_KEY,measure.getIntValue().toString());
		  	  System.out.println("Metric =>"+CoreMetrics.INFO_VIOLATIONS_KEY + " Has Value="+measure.getIntValue().toString());
		    
		  	//Metric for CLASS_COMPLEXITY
		  	  System.out.println("JD Line 110 Class Complexity");	
			  //metric = metricFinder.findByKey(CoreMetrics.ABSTRACTNESS_KEY);
			  //System.out.println("JD Line 112 ");
			  measure = context.getMeasure(CoreMetrics.CLASS_COMPLEXITY);
			  reportDataMap.put(CoreMetrics.CLASS_COMPLEXITY_KEY,measure.getIntValue().toString());
		  	  System.out.println("Metric =>"+CoreMetrics.CLASS_COMPLEXITY_KEY + " Has Value="+measure.getIntValue().toString());
			
		  	return reportDataMap;
		 }
	  
	  /**
	     * create the second sample document from the PDF file format specification.
	 * @param reportDataMap 
	     *
	     * @param file The file to write the PDF to.
	     * @param message The message to write in the file.
	     *
	     * @throws IOException If there is an error writing the data.
	     * @throws COSVisitorException If there is an error writing the PDF.
	     */
	    public String createMetricReport(Map<String, String> reportDataMap) throws IOException, COSVisitorException
	    {
	        // the document
	        PDDocument doc = null;
	        StringBuilder strRow = new StringBuilder();
	        
	        String reportname = null;
	        java.util.Random R = new java.util.Random();
	        try
	        {
	            doc = new PDDocument();
	            System.out.println("Creating PDF Line 264");
	            PDPage page = new PDPage();
	            doc.addPage( page );
	            PDFont font = PDType1Font.HELVETICA_BOLD;

	            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
	            
	            contentStream.beginText();
	            contentStream.setFont( font, 12 );
            	contentStream.moveTextPositionByAmount( 100, 700 - 0);
	            contentStream.drawString( " Sonar Analysis Report " );
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont( font, 12 );
            	contentStream.moveTextPositionByAmount( 100, 700 - 20 );
	            contentStream.drawString( "==================================================== " );
	            contentStream.endText();
	            	            
	            System.out.println("Print Entries from Analysis Data Map.");
	            int i = 2;
	            for ( Map.Entry<String,String> entry : reportDataMap.entrySet())
	            {
	            	contentStream.beginText();
		            contentStream.setFont( font, 12 );
	            	contentStream.moveTextPositionByAmount( 100, 700 - (i * 20) );
		            contentStream.drawString( i + ") " + entry.getKey() + " = " + entry.getValue() );
		            contentStream.endText();
		            
	            	//strRow.append(i + ") " + entry.getKey() + " = " + entry.getValue() + "\n ");
	                i++;
	            }
	            //System.out.println(strRow);	            
	            
	            contentStream.close();
	            System.out.println("Done Writing Text.Save Doc.");
	            
	            reportname = "sonarreport"+ java.util.Calendar.getInstance().getTimeInMillis() + "_" + R.nextInt() + ".pdf" ;
	            doc.save(reportname);
	            System.out.println("Report Created.Exit to email sending. name=>"+reportname);
	        }
	        finally
	        {	
	            if( doc != null )
	            {
	                doc.close();
	            }
	        }
			return reportname;
	    }	    
	    
		private void sendEmail(String reportname, Map<String, String> reportDataMap2)
		{
			try {
			 
			  // Create the email message
			  //MultiPartEmail email = new MultiPartEmail();	
   	          StringBuilder strHtmlContentSummary = new StringBuilder();
			  System.out.println("Analysis -  Sonar Email Notification");
	  		  from = settings.getString("sonar.jd.smptp.username");
	  		  System.out.println("from=>"+from);
	  		  to_email = settings.getString("sonar.jd.smptp.to");
	  		  System.out.println("to_email=>"+to_email);
	  		  
	  		  to_email_name = settings.getString("sonar.jd.smptp.to_name");
	  		  System.out.println("to_email_name=>"+to_email_name);
	  		
	  		  username = settings.getString("sonar.jd.smptp.username");
	  		  System.out.println("username=>"+username);
	  		  password = settings.getString("sonar.jd.smptp.password");
	  		  System.out.println("password=>"+password);
			  hostname = settings.getString("sonar.jd.smptp.host");
			  System.out.println("hostname=>"+hostname);
			  portno = settings.getString("sonar.jd.smptp.sslport");
			  System.out.println("portno=>"+portno);
			  setSSLOnConnectFlag = settings.getBoolean("sonar.jd.smptp.set_ssl_on_connect");
			  System.out.println("setSSLOnConnectFlag=>"+String.valueOf(setSSLOnConnectFlag));
			  subject = settings.getString("sonar.jd.smptp.subject");
			  System.out.println("subject=>"+subject);
			  message = settings.getString("sonar.jd.smptp.message");
			  System.out.println("message=>"+message);
			  
			  
			  // Create the email message
			  HtmlEmail email = new HtmlEmail();
			  email.setHostName(hostname);
			  email.setSslSmtpPort(portno);
			  if (!StringUtils.isBlank(username) || !StringUtils.isBlank(password)) {
			      email.setAuthentication(username, password);
			    }
			  //email.setSSLOnConnect(setSSLOnConnectFlag);
	 		  email.setSSL(setSSLOnConnectFlag);
			  String[] addrs = StringUtils.split(to_email, "\t\r\n;, ");
			  for (String addr : addrs) {
			      email.addTo(addr);
			  }
			  //email.addTo(to_email,to_email_name);
			  email.setFrom(from);
			  email.setSubject(subject);
			  //email.setMsg(message);
			  
			  // embed the image and get the content id	
			  URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
			  String cid = email.embed(url, "Apache logo - 1");
			  
			  System.out.println("Print Entries from Analysis Data Map.");
	            int i = 1;
	            strHtmlContentSummary.append("<html><body>The apache logo - <img src=\"cid:"+cid+"\"><br><br><br><b><center>Metric Information:-</center></b><br><table border='2'>");
	            for ( Map.Entry<String,String> entry : reportDataMap.entrySet())
	            {
	            	strHtmlContentSummary.append("<tr><td>" + entry.getKey() + ":</td><td>" + entry.getValue() + "</td></tr>");
	                i++;
	            }
	            strHtmlContentSummary.append("</table></body></html>");
	            System.out.println(strHtmlContentSummary.toString());
			  // set the html message
	            email.setHtmlMsg(strHtmlContentSummary.toString());
			  // set the alternative message
			  email.setTextMsg("Your email client does not support HTML messages");
			   
			  // Create the attachment
			  EmailAttachment attachment = new EmailAttachment();
			  attachment.setPath(reportname);
		
			  attachment.setDisposition(EmailAttachment.ATTACHMENT);
			  attachment.setDescription("Sonar Analysis Report"+reportname);
			  attachment.setName(reportname);
			  email.attach(attachment);
			  // send the email
			  System.out.println("Sending the Email");
			  email.send();					  			  
	    	} catch (EmailException e) {
	    			throw new SonarException("Unable to send email", e);
	    	}
		  catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		 	  
		}
	
	//send sms notification via email based SMS.
	public void sendNotificationSMS(String send_sms_to_provider,String send_sms_to, String from, String username, String password, String hostname, String portno, boolean setSSLOnConnectFlag, String subject, String message)
	{
		try {
				send_sms_to_provider = settings.getString(TO_SMS_PROVIDER_PROPERTY);
				send_sms_to = settings.getString(TO_SMS_PROPERTY);
				
				Email smsObject = new SimpleEmail();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				String dateStr = dateFormat.format(cal.getTime()) ;
				
				smsObject.setSmtpPort(Integer.parseInt(portno));
				smsObject.setAuthenticator(new DefaultAuthenticator(username,password));
				smsObject.setSSL(true);
				smsObject.setFrom(from);
				smsObject.setSubject("");
				smsObject.setMsg("Sonar analysis completed successfully at "+ dateStr + " . Please visit " + settings.getString("sonar.host.url")  + " for more details!");
				smsObject.addTo(send_sms_to);
				smsObject.send();
			} 
		catch (EmailException e) {
				throw new SonarException("Unable to send sms", e);
		}
		catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}//sendNotificationSMS method ends here.
}
