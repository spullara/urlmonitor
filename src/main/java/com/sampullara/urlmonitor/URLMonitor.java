package com.sampullara.urlmonitor;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: Mar 25, 2010
 * Time: 10:40:08 AM
 */
public class URLMonitor {
  @Argument(required = true)
  private static String url;

  @Argument(alias = "f")
  private static String from = "spullara@yahoo.com";

  @Argument(required = true, alias = "t")
  private static String to;

  @Argument(alias = "u")
  private static String user = "spullara";

  @Argument(required = true, alias = "p")
  private static String password;

  @Argument
  private static String protocol = "smtps";

  @Argument
  private static String server = "smtp.mail.yahoo.com";

  @Argument
  private static String port = "465";

  // Some random numbers
  private static final int ONE_MINUTE = 60000;
  private static final int FIVE_SECONDS = 5000;
  private static final int TEN_MINUTES = 600000;

  public static void main(String[] args) throws IOException, InterruptedException, MessagingException {
    try {
      Args.parse(URLMonitor.class, args);
    } catch (IllegalArgumentException iae) {
      Args.usage(URLMonitor.class);
      System.exit(1);
    }

    while (true) {
      try {
        URL u = new URL(url);
        URLConnection urlc = u.openConnection();
        urlc.setReadTimeout(FIVE_SECONDS);
        urlc.getContent();
        Thread.sleep(ONE_MINUTE);
      } catch (IOException ioe) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ioe.printStackTrace(pw);
        pw.flush();
        sendMail(to, from, "[URLMonitor] " + url + " " + ioe.getMessage(), "Failed.\n\n URLMonitor", server, port, user, password);
        Thread.sleep(TEN_MINUTES);
      }
    }
  }

  public static void sendMail(String to, String from, String subject, String text, String host, String port, String user, String password) throws MessagingException {
    Properties props = new Properties();
    props.put("mail.smtps.host", host);
    props.put("mail.smtps.port", port);
    props.put("mail.smtps.user", user);

    Session mailSession = Session.getDefaultInstance(props);
    Message simpleMessage = new MimeMessage(mailSession);

    InternetAddress fromAddress = null;
    InternetAddress toAddress = null;
    try {
      fromAddress = new InternetAddress(from);
      toAddress = new InternetAddress(to);
    } catch (AddressException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try {
      simpleMessage.setFrom(fromAddress);
      simpleMessage.setRecipient(Message.RecipientType.TO, toAddress);
      simpleMessage.setSubject(subject);
      simpleMessage.setText(text);

      Transport tr = mailSession.getTransport("smtps");
      tr.connect(host, user, password);
      simpleMessage.saveChanges();      // don't forget this
      tr.sendMessage(simpleMessage, simpleMessage.getAllRecipients());
      tr.close();
    } catch (MessagingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
