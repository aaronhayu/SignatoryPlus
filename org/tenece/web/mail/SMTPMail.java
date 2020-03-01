/*
 * (c) Copyright 2008 Tenece Professional Services.
 *
 * ============================================================
 * Project Info:  http://www.tenece.com
 * Project Lead:  Amachree Jeffry (info@tenece.com);
 * ============================================================
 *
 *
 * Licensed under the Tenece Professional Services;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.tenece.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Strategiex. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "as is," without a warranty of any
 * kind. All express or implied conditions, representations and
 * warranties, including any implied warranty of merchantability,
 * fitness for a particular purpose or non-infringement, are hereby
 * excluded.
 * Tenece and its licensors shall not be liable for any damages
 * suffered by licensee as a result of using, modifying or
 * distributing the software or its derivatives. In no event will Strategiex
 * or its licensors be liable for any lost revenue, profit or data, or
 * for direct, indirect, special, consequential, incidental or
 * punitive damages, however caused and regardless of the theory of
 * liability, arising out of the use of or inability to use software,
 * even if Strategiex has been advised of the possibility of such damages.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package org.tenece.web.mail;

/**
 *
 * @author jeffry.amachree
 */

import com.sun.mail.smtp.SMTPTransport;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Multipart;
import javax.swing.JOptionPane;

public class SMTPMail
{
    private int sent;
    private int smtpPort;
    private String smtpAddress;
    private String userName;
    private String password;
    private boolean useAuthentication;
    private String mailer = "StrategieX";

    private static String PROTOCOL = "smtp";

    public SMTPMail()
    {
        sent = 0;
        smtpPort = 26;
        smtpAddress = new String("127.0.0.1");
        userName = new String("administrator");
        password = new String();
        useAuthentication = false;
        mailer = "Strategiex";
    }

    public SMTPMail(String smtpAddr, int smtpPort, String userName, String password, boolean useAuthentication)
    {
        sent = 0;
        this.smtpAddress = smtpAddr;
        this.smtpPort = smtpPort;
        this.userName = userName;
        this.password = password;
        this.useAuthentication = useAuthentication;
    }

    /**
     * Set all required properties
     * @return
     */
    private Properties getConfiguredProperties(){
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", SMTPMail.PROTOCOL);
        props.setProperty("mail.host",getSmtpAddress());
        props.setProperty("mail.user", getUserName()==null?"":getUserName());
        props.setProperty("mail.password", getPassword()==null?"":getPassword());
        props.setProperty("mail.smtp.auth", isUseAuthentication() == true ? "true" : "false");
        return props;
    }

    private InternetAddress getInternetAddress(String emailAccount, String personName) throws Exception{
        if(personName == null){
            return new InternetAddress(emailAccount);
        }else{
            return new InternetAddress(emailAccount, personName);
        }
    }
    
    private InternetAddress[] getAddressList(String emailAccounts) throws Exception{

        String[] emails = emailAccounts.split(";");
        InternetAddress[] addressList = new InternetAddress[emails.length];
        for(int i = 0; i < emails.length; i++){
            InternetAddress address = getInternetAddress(emails[i], null);
            addressList[i] = address;
        }
        //return instance
        return addressList;
    }

    public int sendMail(String toClient, String fromClient, String subject, String body)
    {
//        setSent(1);
//        java.util.Properties prop = getConfiguredProperties();//System.getProperties();
//        Session s1 = Session.getDefaultInstance(prop, null);
//        Message msg = new MimeMessage(s1);
//        InternetAddress addr[] = null;
//        try
//        {
//            addr = InternetAddress.parse(toClient, true);
//        }
//        catch(Exception e)
//        {
//            setSent(0);
//        }
//        try
//        {
//            msg.setRecipients(javax.mail.Message.RecipientType.TO, addr);
//            msg.setFrom(new InternetAddress(fromClient));
//            msg.setSentDate(new Date());
//            msg.setSubject(subject);
//            //msg.setHeader("X-Mailer", "Stedy Strategics WebMailer 1.0");
//            //msg.setHeader("X-Priority", "3");
//            msg.setDataHandler(new DataHandler(body, "text/html"));
//            SMTPTransport tSend = new SMTPTransport(s1, new URLName(SMTPMail.PROTOCOL, getSmtpAddress(), getSmtpPort(), null,getUserName(), getPassword()));
//            tSend.connect();
//            tSend.sendMessage(msg, addr);
//            tSend.close();
//            setSent(1);
//        }
//        catch(Exception e)
//        {
//            setSent(0);
//            System.out.println("Error: " + e);
//        }
        return getSent();
    }


    public void sendHTMLMail(String toClient, String fromClient, String subject, String body)
    {
        try{
            Properties props = getConfiguredProperties();

            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(false);
            Transport transport
                = mailSession.getTransport(new URLName(SMTPMail.PROTOCOL, getSmtpAddress(), getSmtpPort(), null,getUserName(), getPassword()));



            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(subject);
            message.setFrom(new InternetAddress(fromClient));
            message.setContent(body, "text/html");
            if (toClient.indexOf(",") != -1) {
                java.util.StringTokenizer token = new java.util.StringTokenizer(toClient,",");
                while(token.hasMoreElements()){
                    message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress((String)token.nextElement()));
                }
            }else{
                message.addRecipient(Message.RecipientType.TO,
                 new InternetAddress(toClient));
            }
            transport.connect();
            transport.sendMessage(message,
            message.getRecipients(Message.RecipientType.TO));
            transport.close();
        }catch(Exception e){
            System.out.println("MAIL Error: " + e);
        }
    }

    public int sendAttachedMail(String toClient, String fromClient, String BCC, String CC, String subject, String body, Vector fileName)
    {
        setSent(0);
        java.util.Properties prop = getConfiguredProperties();//System.getProperties();
        Session s1 = Session.getDefaultInstance(prop, null);
        MimeMessage msg = new MimeMessage(s1);
        InternetAddress addr[] = null;
        InternetAddress addrBCC[] = null;
        InternetAddress addrCC[] = null;
        try
        {
            addr = getAddressList(toClient.trim()); //InternetAddress.parse(toClient.trim());
            if(!BCC.trim().equals(""))
                addrBCC = getAddressList(BCC.trim()); //InternetAddress.parse(BCC.trim());
            if(!CC.trim().equals(""))
                addrCC = getAddressList(CC.trim()); //InternetAddress.parse(CC.trim());
        }
        catch(Exception e)
        {
            setSent(0);
        }
        try
        {
            msg.setRecipients(javax.mail.Message.RecipientType.TO, addr);
            if(!BCC.trim().equals(""))
                msg.setRecipients(javax.mail.Message.RecipientType.BCC, addrBCC);
            if(!CC.trim().equals(""))
                msg.setRecipients(javax.mail.Message.RecipientType.CC, addrCC);
            msg.setFrom(new InternetAddress(fromClient));
            msg.setSentDate(new Date());
            msg.setSubject(subject);
            msg.setHeader("X-Mailer", mailer);
            msg.setHeader("X-Priority", "3");
            MimeBodyPart msgBody = new MimeBodyPart();
            msgBody.setDataHandler(new DataHandler(body, "text/html"));
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(msgBody);
            for(int i = 0; i < fileName.size(); i++)
            {
                String tmpName = String.valueOf(fileName.elementAt(i));
                File f_tmp = new File(tmpName);
                if(!f_tmp.exists())
                    throw new Exception("Attached file does not exist or have been moved.");
                MimeBodyPart msgBody_File = new MimeBodyPart();
                FileDataSource fSrc = new FileDataSource(tmpName);
                File f = fSrc.getFile();
                System.out.println(f.getAbsoluteFile());
                msgBody_File.setDataHandler(new DataHandler(fSrc));
                msgBody_File.setFileName(fSrc.getName());
                mp.addBodyPart(msgBody_File);
            }

            msg.setContent(mp);
            SMTPTransport tSend = new SMTPTransport(s1, new URLName(SMTPMail.PROTOCOL, getSmtpAddress(), getSmtpPort(), null,getUserName(), getPassword()));
            tSend.connect();
            tSend.sendMessage(msg, addr);
            tSend.close();
            setSent(1);
        }
        catch(Exception e)
        {
            setSent(0);
            System.out.println(e);
        }
        return getSent();
    }

    public int mailSent()
    {
        return getSent();
    }

    public static void main(String args[])
    {
        SMTPMail mail = new SMTPMail();

        mail.setSmtpAddress("smtp.tenece.com");
        mail.setSmtpPort(587);
        mail.setPassword("password");
        mail.setUserName("info@tenece.com");
        mail.setUseAuthentication(true);
        
        JOptionPane.showMessageDialog(null, "Send Mail...", "mail", 2);
        Vector vFiles = new Vector();
        vFiles.addElement("c:\\Tenece\\logo.gif");
        //vFiles.addElement("c:\\newxml.xml");
        //int num = mail.sendMail("jeffrillino@jeffrillino.com,boma@jeffrillino.com", "postmaster@jeffrillino.com","Whaz Up", "Hello\r\nThis mail Got ur back from community edition\r\nRegards");
        int num = mail.sendAttachedMail("jeffry.amachree@gmail.com;strategiex@yahoo.it;", "info@tenece.com","","","Whaz Up", "<b>Hello</b><br/>This mail Got ur back from XtremeHR<br/><br/>Regards", vFiles);
        JOptionPane.showMessageDialog(null, "Sent : " + num, "Details", 2);
        System.exit(1);
    }

    public String toString()
    {
        return "SMTP mail class from Jeffry Amachree.";
    }

    /**
     * @return the sent
     */
    public int getSent() {
        return sent;
    }

    /**
     * @param sent the sent to set
     */
    public void setSent(int sent) {
        this.sent = sent;
    }

    /**
     * @return the smtpPort
     */
    public int getSmtpPort() {
        return smtpPort;
    }

    /**
     * @param smtpPort the smtpPort to set
     */
    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    /**
     * @return the smtpAddress
     */
    public String getSmtpAddress() {
        return smtpAddress;
    }

    /**
     * @param smtpAddress the smtpAddress to set
     */
    public void setSmtpAddress(String smtpAddress) {
        this.smtpAddress = smtpAddress;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the useAuthentication
     */
    public boolean isUseAuthentication() {
        return useAuthentication;
    }

    /**
     * @param useAuthentication the useAuthentication to set
     */
    public void setUseAuthentication(boolean useAuthentication) {
        this.useAuthentication = useAuthentication;
    }

    /**
     * @return the mailer
     */
    public String getMailer() {
        return mailer;
    }

    /**
     * @param mailer the mailer to set
     */
    public void setMailer(String mailer) {
        this.mailer = mailer;
    }

}
