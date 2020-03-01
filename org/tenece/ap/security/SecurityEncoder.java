/*
 * (c) Copyright 2008 Tenece Professional Services.
 *
 * Created on 27 May 2009, 13:28
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
 * even if Tenece Professional Services has been advised of the possibility of such damages.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package org.tenece.ap.security;
/*
 * SecurityEncoder.java
 *
 * Created on 13 April 2005, 08:51
 */
import java.security.MessageDigest;
import java.security.Security;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
/**
 *
 * @author  Administrator
 */
public class SecurityEncoder {
    
    private static int ASCII_DEC = 150;
    private static int ASCII_LIMIT = 255;
    private static int ASCII_MAX = 256;
    
    /** Creates a new instance of SecurityEncoder */
    public SecurityEncoder() {
    }
    public SecurityEncoder(int ascii_Dec, int ascii_Limit, int ascii_Max) {
        ASCII_DEC = ascii_Dec;
        ASCII_LIMIT = ascii_Limit;
        ASCII_MAX = ascii_Max;
    }
    
    public static String decrypt(String content) throws Exception{
        return decrypt(content.getBytes());
    }
    public static String decrypt(byte bt_[])throws Exception {
        String tmpStr = SecurityEncoder.decodeValue(bt_);
        bt_ = tmpStr.getBytes();
        
        String pwd = new String();
        //formatting...
        String tmpPwd = new String(bt_);
        tmpPwd = tmpPwd.substring(3);
        tmpPwd = tmpPwd.substring(0, tmpPwd.length() - 2);
        bt_ = tmpPwd.getBytes();
        //
        int len = bt_.length;
        int pwd_Size = bt_[0];
        byte byte_PWD[] = new byte[pwd_Size];
        int iStart = len - pwd_Size;
        for(int i = 0; i < bt_.length; i++)
            if(i >= iStart)
                byte_PWD[i - iStart] = bt_[i];

        for(int i = 0; i < byte_PWD.length; i++)
        {
            int val_Ascii = byte_PWD[i];
            val_Ascii = ASCII_MAX + val_Ascii;
            int real_Ascii = val_Ascii - ASCII_DEC;
            String tmp = String.valueOf((char)real_Ascii);
            pwd = String.valueOf(tmp) + String.valueOf(pwd);
        }
        
        return pwd;
    }
    public static byte[] encrypt(byte[] content)throws Exception{
        return encrypt(new String(content));
    }
    public static byte[] encrypt(String content)throws Exception {
        String chrPos = new String();
        String encryptAscii_text = new String();
        String pWord = new String();
        int txtIt = 0;
        int newCharVal = 0;
        int pwdLen = content.length();
        //encrypting real password
        for(int txtLen = pwdLen; txtLen > 0; txtLen--)
        {
            chrPos = String.valueOf((txtLen - pwdLen) + 1);
            newCharVal = content.charAt(txtLen - 1) + ASCII_DEC;
            if(newCharVal > ASCII_LIMIT)
                newCharVal -= ASCII_MAX;
            encryptAscii_text += String.valueOf((char)newCharVal);
        }
        //getting junks..
        int nTimes = 0;
        nTimes = (12 - pwdLen) / 2;
        pWord = String.valueOf((char)pwdLen);
        for(txtIt = 1; txtIt <= nTimes; txtIt++)
        {
            int num = pwdLen + txtIt * 3;
            pWord = String.valueOf(pWord) + String.valueOf((char)(pwdLen + txtIt * 3));
        }

        int iText = encryptAscii_text.length();
        iText -= pwdLen;
        if(iText < 0)
            iText = 0;
        pWord = String.valueOf(pWord) + String.valueOf(encryptAscii_text.substring(iText, pwdLen));
        
        long lHowMany = Math.abs(Math.round(Math.random() * 3));
        int howMany = (int)lHowMany;
        
        String frontLiner = "";
        String endLiner = "";
        for(int k = 0; k < 3; k++){
            long pas = Math.abs(Math.round(Math.random() * ASCII_LIMIT));
            int iPos = (int)pas;
            frontLiner += (char)iPos;
        }
        for(int k = 0; k < 2; k++){
            long pas = Math.abs(Math.round(Math.random() * ASCII_LIMIT));
            int iPos = (int)pas;
            endLiner += (char)iPos;
        }
        
        pWord = frontLiner + pWord + endLiner;
        pWord = encodeValue(pWord.getBytes());
        return pWord.getBytes();
    }
    public static String encodeValue(byte[] bt) throws Exception{
        return new BASE64Encoder().encodeBuffer(bt);
    }
    public static String decodeValue(byte[] bt) throws Exception{
        byte[] newVal = new BASE64Decoder().decodeBuffer(new String(bt));
        return new String(newVal);
    }

    public static byte[] digestKey(String password)throws Exception {
        Security.addProvider( new com.sun.crypto.provider.SunJCE() );
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        return md.digest();

    }
    public static void main(String[] args){
        try{
           byte[] bt = SecurityEncoder.encrypt("batubo");
           System.out.println(new String(bt));
           String pwd = SecurityEncoder.decrypt(bt);
           System.out.println(pwd);
        }catch(Exception er){
            System.out.println(er);
        }
    }
    
}
