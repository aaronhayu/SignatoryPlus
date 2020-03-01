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


import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Base64;

/**
 *
 * @author Jeffry Amachree
 */
public class AES_SecurityEncoder {

    public String defaultKey = "5pZQ31yYLv8k/D234ocpLQ==";

    public String encryptPassword(String strKey, byte[] input) throws Exception{
        return encryptPassword(strKey, new String(input));
    }
    public String encryptPassword(String strKey, String input) throws Exception{
        
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        //decode key which will be used to encrypt input
        byte[] decodedKey = Base64.decode(strKey);
        //specify the type of algorithm
        SecretKeySpec key = new SecretKeySpec(decodedKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        System.out.println(new String(input));

        // encryption pass
        cipher.init(Cipher.ENCRYPT_MODE, key);

        //convert input to byte array
        byte[] convertedKey = input.getBytes();
        //pass the input to the cipher and encrypt
        byte[] cipherText = cipher.doFinal(convertedKey);
        //encode the input
        String encodedString = new String(Base64.encode(cipherText));
        System.out.println("encoded password:" + encodedString);
        //returning encoded input
        return encodedString;
    }

    public String decryptPassword(String strKey, byte[] encodedInput) throws Exception{
        return encryptPassword(strKey, new String(encodedInput));
    }
    public String decryptPassword(String strKey, String encodedInput) throws Exception{
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        //decode key which will be used to encrypt input
        byte[] decodedKey = Base64.decode(strKey);
        //specify the type of algorithm
        SecretKeySpec key = new SecretKeySpec(decodedKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        // decode input using base64
        byte[] decodedPwd = Base64.decode(encodedInput);

        //pass key to algorithm
        cipher.init(Cipher.DECRYPT_MODE, key);
        //decryption of input
        byte[] plainText = cipher.doFinal(decodedPwd);
        //return plainText (which is the input)
        System.out.println(new String(plainText));

        //return string as plain text
        return new String(plainText);
    }
}
