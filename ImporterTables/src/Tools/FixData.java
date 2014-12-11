 /**
  * Copyright 2014 International Center for Tropical Agriculture (CIAT).
  * This file is part of:
  * Crop Wild Relatives
  * It is free software: You can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * at your option) any later version.
  * It is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * See <http://www.gnu.org/licenses/>.
  */

package Tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class FixData {
    
    /*Static Method*/
    /**
     * Method that split line by pattern
     * @param line line to split
     * @param pattern pattern with which you want to split line
     * @return
     */
    public static ArrayList<String> lineSplit(String line,String pattern)
    {
        ArrayList<String> arrLis = new ArrayList<String>();
        for(String temp: line.split(pattern))
            arrLis.add(temp);
        return arrLis;
    }
    
    /**
     * Method that delete all '
     * @param input String to cleaner
     * @return
     */
    public static String fixToQuery(String input)
    {
        return input.replace("'", "");
    }
    
    /**
     * Method that delete all accent so leave the string only with ascii characters
     * @param input String to cleaner
     * @return
     */
    public static String deleteAccent(String input)
    {
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i=0; i<original.length(); i++)
            output = output.replace(original.charAt(i), ascii.charAt(i));
        return output;
    }
    
    /**
     * Method that return current date and time
     * @return 
     */
    public static String getDateTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format( Calendar.getInstance().getTime());
    }
    
    /**
     * Method
     * @param total
     * @param partial
     * @return 
     */
    public static double toPercent(double total, double partial)
    {
        return Math.rint(((partial/total)*100)*10)/10;
    }
    
    /**
     * Method that validate if a field contains the values allowed
     * @param data Data allowed
     * @param values Row values
     * @return 
     */
    public static boolean containsValue(String data,ArrayList<String> values)
    {
        return values.contains(data) || (data==null && values.contains("null"));
    }
    
    /**
     * Method that fix a word to format capital letter
     * @param data 
     * @return 
     */
    public static String toCapitalLetter(String data)
    {
        String d=String.valueOf(data.charAt(0)).toUpperCase() + data.substring(1,data.length()).toLowerCase();
        return d;
    }
    
    /**
     * Method that transform from array to array list
     * @param values to transform
     * @return 
     */
    public static ArrayList<String> toArrayList(String[] values)
    {
        ArrayList<String> a=new ArrayList<String>();
        for (String value : values)
            a.add(value);
        return a;
    }
    
    /**
     * Method that concatenate some values if they are different of empty and null
     * @param values values to concatenate
     * @param pattern pattern for concatenate
     * @return 
     */
    public static String concatenate(String[] values,String pattern)
    {
        String a="";
        for(String v:values)
            a+=v!=null && !v.equals("") ? v + pattern: "";
        return a.substring(0,a.length()-pattern.length());
    }
    
    /**
     * Method that create 
     * @param field
     * @param value
     * @param removeAccents
     * @param lower
     * @return 
     */
    public static String prepareUpdate(String field, String value, boolean removeAccents,boolean lower)
    {
        if(field== null)
            return "";
        else if(value==null)
            return "";
        else
        {
            if(removeAccents && lower)
                return field + "='" + FixData.deleteAccent(value).toLowerCase() + "',";
            else if(removeAccents && !lower)
                return field + "='" + FixData.deleteAccent(value) + "',";
            else
                return field + "='" + value + "',";
        }
    }
    
    /**
     * Method that return the real value for parameter
     * @param value
     * @return 
     */
    public static String valueParameter(String value)
    {
        return (value== null || value.trim().equals("null") ? null : value.trim());
    }
    
    /**
     * Method that delete pattern in the end of string
     * @param value value that you need evaluate
     * @param pattern pattern that you need delete
     * @return 
     */
    public static String removePatternEnd(String value,String pattern)
    {
        String a=value;
        while(a.endsWith(pattern))
            a=a.substring(0,a.length()-1);
        return a;
    }
    
    /**
     * Method that return all real values for parameters
     * @param line
     * @return 
     */
    public static String[] valueParameterSplit(String line)
    {
        String[] values=line.split(",");
        for(String v : values)
            v=FixData.valueParameter(v.trim());
        return values;
    }
    
    /**
     * Method that validate if field rank is correct
     * @param r rank to validate
     * @return
     */
    public static String validateRank(String rank)
    {
        if(rank==null)
            return "";
        else
        {
            String r=rank.toLowerCase().trim();
            return r.equals("ssp") || r.equals("ssp.") || r.equals("subsp-") ? "subsp." :
                    (r.equals("v") || r.equals("v.") || r.equals("var-") ? "var." :
                    (r.equals("conv") || r.equals("conv.") || r.equals("convar-") ? "convar." :
                    (r.equals("forma") || r.equals("forma.") || r.equals("for.") || r.equals("f-") ? "f." : "")));
        }
    }
    
    /**
     * Method that delete the rank from taxon
     * @param taxon
     * @return 
     */
    public static String hideRank(String taxon)
    {
        return taxon.replaceAll("_subsp.", "").replaceAll("_var.", "").replaceAll("_convar.", "");
    }
    
    /**
     * Method that convert degrees in decimal degrees
     * @param positive indicate if it should be positive or negative
     * @param grades 
     * @param minutes
     * @param seconds
     * @return 
     */
    public static double degreesToDecimalDegrees(boolean positive, double grades, double minutes, double seconds )
    {
        return (positive ? 1.0 : -1.0)*((seconds/3600)+(minutes/60)+grades) ;
    }
    
    /**
     * Method that validate if the field don't have value
     * @param field field to validate
     * @return 
     */
    public static String getValue(String field)
    {
        return field==null ? "null" : field;
    }
    
    /**
     * Method that validate if the field don't have value
     * @param field field to validate
     * @return 
     */
    public static String getValue(Object field)
    {
        return field==null ? "null" : FixData.getValue(field.toString());
    }
}
