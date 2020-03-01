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

/**
 *
 * @author jeffry.amachree
 */
public class SecurityEncoderImpl {

    public static String encryptPassword(byte[] userPassword) throws Exception{
        return encryptPassword(new String(userPassword));
    }
    public static String encryptPassword(String userPassword) throws Exception{
        //instantiate tenece security encoder
        SecurityEncoder encoder = new SecurityEncoder();
        
        //encode password using self-generated algorith
        String encryptedPassword = new String(encoder.encrypt(userPassword));
        
        //encrypt 2nd phase
        String encryptedPassword2 = new String(encoder.encrypt(encryptedPassword));
        
        String encodedPassword = new String(SecurityEncoder.encodeValue(encryptedPassword2.getBytes()));
        
        return encodedPassword;
    }

    public static String decryptPassword(byte[] encodedPassword) throws Exception {
        return decryptPassword(new String(encodedPassword));
    }
    public static String decryptPassword(String encodedPassword) throws Exception {
        //instantiate tenece security encoder
        SecurityEncoder encoder = new SecurityEncoder();

        //decode password to the exact value after decrypt 2
        String decodedPassword = SecurityEncoder.decodeValue(encodedPassword.getBytes());

        //decrypt the 2nd phase password
        String decryptedPasssword2 = encoder.decrypt(decodedPassword);
        
        //decrypt 1st phase password
        String decryptedPasssword = encoder.decrypt(decryptedPasssword2);

        return decryptedPasssword;
    }

    public static String encryptPasswordWithAES(byte[] input) throws Exception{
        AES_SecurityEncoder aesAlgorithm = new AES_SecurityEncoder();
        return aesAlgorithm.encryptPassword(aesAlgorithm.defaultKey, input);
    }
    public static String encryptPasswordWithAES(String input) throws Exception{
        AES_SecurityEncoder aesAlgorithm = new AES_SecurityEncoder();
        return aesAlgorithm.encryptPassword(aesAlgorithm.defaultKey, input);
    }
    
    public static String decryptPasswordWithAES(byte[] input) throws Exception{
        AES_SecurityEncoder aesAlgorithm = new AES_SecurityEncoder();
        return aesAlgorithm.decryptPassword(aesAlgorithm.defaultKey, input);
    }
    public static String decryptPasswordWithAES(String input) throws Exception{
        AES_SecurityEncoder aesAlgorithm = new AES_SecurityEncoder();
        return aesAlgorithm.decryptPassword(aesAlgorithm.defaultKey, input);
    }

    
    public static void main(String[] args){
        try{
            AES_SecurityEncoder aesAlgorithm = new AES_SecurityEncoder();
            //System.out.println(aesAlgorithm.encryptPassword(aesAlgorithm.defaultKey, "advanceplus"));
            //System.out.println(aesAlgorithm.encryptPassword(aesAlgorithm.defaultKey, "advanceplus"));
            //System.out.println(aesAlgorithm.encryptPassword(aesAlgorithm.defaultKey, "admin"));
            
            //System.out.println(SecurityEncoderImpl.encryptPassword("admin"));
            //System.out.println(SecurityEncoderImpl.decryptPassword("nH1qijAkaNa2JrGIpLoT7A=="));
             System.out.println(SecurityEncoderImpl.decryptPasswordWithAES("VsOJkURtAGb7ju2vTSiYlQ=="));
             //System.out.println(SecurityEncoderImpl.encryptPasswordWithAES("password2$"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
