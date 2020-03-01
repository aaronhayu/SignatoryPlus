/*
 * (c) Copyright 2008, 2010 Tenece Professional Services.
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

package org.tenece.web.common;

import java.text.DecimalFormat;

/**
 * The Source code was derived but was modified to accept amount with decimal
 * as well as being able to present the words in a readable format (including "and"
 * where required.
 * 
 * Tenece Professional Services Limited
 * @author amachree
 */
public class NumberToWords {

    private static final String[] tensNames = {
    "",
    " ten",
    " twenty",
    " thirty",
    " forty",
    " fifty",
    " sixty",
    " seventy",
    " eighty",
    " ninety"
    };

    private static final String[] numNames = {
    "",
    " one",
    " two",
    " three",
    " four",
    " five",
    " six",
    " seven",
    " eight",
    " nine",
    " ten",
    " eleven",
    " twelve",
    " thirteen",
    " fourteen",
    " fifteen",
    " sixteen",
    " seventeen",
    " eighteen",
    " nineteen"
    };

  private static String convertLessThanOneThousand(int number) {
    String soFar;

    if (number % 100 < 20){
      soFar = numNames[number % 100];
      number /= 100;
    }
    else {
      soFar = numNames[number % 10];
      number /= 10;

      soFar = tensNames[number % 10] + soFar;
      number /= 10;
    }
    if (number == 0) return soFar;
    //since we are converting less than a thousand... some words might include
    //and .... check the generated word so far and put and if appropriate
    if(soFar.trim().equals("")){
        return numNames[number] + " hundred" + soFar;
    }else{
        return numNames[number] + " hundred and" + soFar;
    }
  }


  public static String convert(long number) {
    // 0 to 999 999 999 999
    if (number == 0) { return "zero"; }

    String snumber = Long.toString(number);

    // pad with "0"
    String mask = "000000000000";
    DecimalFormat df = new DecimalFormat(mask);
    snumber = df.format(number);

    // XXXnnnnnnnnn
    int billions = Integer.parseInt(snumber.substring(0,3));
    // nnnXXXnnnnnn
    int millions  = Integer.parseInt(snumber.substring(3,6));
    // nnnnnnXXXnnn
    int hundredThousands = Integer.parseInt(snumber.substring(6,9));
    // nnnnnnnnnXXX
    int thousands = Integer.parseInt(snumber.substring(9,12));

    String tradBillions;
    switch (billions) {
    case 0:
      tradBillions = "";
      break;
    case 1 :
      tradBillions = convertLessThanOneThousand(billions)
      + " billion ";
      break;
    default :
      tradBillions = convertLessThanOneThousand(billions)
      + " billion ";
    }
    String result =  tradBillions;

    String tradMillions;
    switch (millions) {
    case 0:
      tradMillions = "";
      break;
    case 1 :
      tradMillions = convertLessThanOneThousand(millions)
      + " million ";
      break;
    default :
      tradMillions = convertLessThanOneThousand(millions)
      + " million ";
    }
    result =  result + tradMillions;

    String tradHundredThousands;
    switch (hundredThousands) {
    case 0:
      tradHundredThousands = "";
      break;
    case 1 :
      tradHundredThousands = "one thousand ";
      break;
    default :
      tradHundredThousands = convertLessThanOneThousand(hundredThousands)
      + " thousand ";
    }
    
    result =  result + tradHundredThousands;
    String tradThousand;
    tradThousand = convertLessThanOneThousand(thousands);
    //check if the thousand is empty or it already contains "and"...
    //this will enable us control the use of the word (and)
    if(tradThousand.trim().equals("") || tradThousand.indexOf("and") > -1){
        //dont put (and)
        result =  result + tradThousand;
    }else{
        if(result.trim().equals("")){
            result =  result + tradThousand;
        }else{
            //put (and) to the statement
            result =  result + " and" + tradThousand;
        }
    }
    
    // remove extra spaces!
    return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
  }

  public static String convertToMoney(float phrase){
      return convertToMoney(String.valueOf(phrase));
  }
  
  public static String convertToMoney(String phrase){
    //convert to float
    Float num = new Float( phrase ) ;
    //pick the exact figure with decimal
    long corAmount = (int)Math.floor( num ) ;
    //get the figure after the dot
    String strCent = phrase.substring(phrase.indexOf(".") + 1);
    long cent = Long.parseLong(strCent);

    //convert the core amount and figure after the dot to words
    String amount = "" + NumberToWords.convert( corAmount ) + " " + ConfigReader.getRegionCurrencyName() + " ";
    String koboPart = (cent <= 0) ? "" : NumberToWords.convert( cent ) + " " + ConfigReader.getRegionCurrencyDecimalName() + " only" ;
    
    return amount;
  }

  /**
   * testing
   * @param args
   */
  public static void main(String[] args) {
    System.out.println("*** " + NumberToWords.convertToMoney("10975518.30"));
    
  }
}