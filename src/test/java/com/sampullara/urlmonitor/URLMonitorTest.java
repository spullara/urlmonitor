package com.sampullara.urlmonitor;

import junit.framework.TestCase;

import javax.mail.MessagingException;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: Mar 25, 2010
 * Time: 11:00:16 AM
 */
public class URLMonitorTest extends TestCase {
  public void testSendMail() throws MessagingException {
    URLMonitor.sendMail("4156094298@txt.att.net", "spullara@yahoo.com", "Test Message", "This is a test message from Java.", "smtp.mail.yahoo.com", "465", "spullara", "jj22blake");
  }
}
