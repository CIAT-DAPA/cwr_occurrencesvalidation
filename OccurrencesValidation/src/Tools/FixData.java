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
import java.util.Arrays;
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
            arrLis.add(temp.trim());
        return arrLis;
    }
    
    /**
     * Method that split line by pattern
     * @param line line to split
     * @param pattern pattern with which you want to split line
     * @return
     */
    public static ArrayList<String> lineSplitLower(String line,String pattern)
    {
        ArrayList<String> arrLis = new ArrayList<String>();
        for(String temp: line.split(pattern))
            arrLis.add(temp.trim().toLowerCase());
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
        String ascii =    "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
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
        return values.contains(data) || (data==null && values.contains("null")) || (values.contains(""));
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
        if(field== null || value==null)
            return "";
        else
        {
            if(removeAccents && lower)
                return field + "='" + FixData.deleteAccent(value).toLowerCase().trim() + "',";
            else if(removeAccents && !lower)
                return field + "='" + FixData.deleteAccent(value).trim() + "',";
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
        return value== null || value.trim().equals("null") ? null : value.trim();
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
        return valueParameterSplit(line,",");
    }
    
    /**
     * Method that return all real values for parameters
     * @param line
     * @return 
     */
    public static String[] valueParameterSplit(String line, String pattern)
    {
        String[] values=line.split(pattern); 
        if(line.endsWith(pattern))
        {
            values=Arrays.copyOf(values, values.length+1);
            values[values.length-1]="";
        }
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
            return r.startsWith("subs") || r.equals("ssp") || r.equals("ssp.") || r.equals("subsp-") || r.equals("subsp.") ? "subsp." :
                    (r.equals("v") || r.equals("v.") || r.equals("var-") || r.equals("var.") ? "var." :
                    (r.startsWith("conv") || r.equals("conv.") || r.equals("convar-") || r.equals("convar.") ? "convar." :
                    (r.startsWith("for") || r.equals("forma.") || r.equals("for.") || r.equals("f-") || r.equals("f.") ? "f." : "")));
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
     * Method that delete the word sp. for some case in the taxon
     * @param taxon
     * @return 
     */
    public static String hideRankSP(String taxon)
    {
        return taxon.endsWith("_subsp.") ? taxon: (taxon.endsWith("_sp.") ? taxon.replaceAll("_sp.", "") : taxon);
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
    
    /**
     * Method that return the content field of empty if it is null
     * @param field
     * @return 
     */
    public static String getValueImaginary(String field)
    {
        return field == null ? "" : field;
    }
    
    /**
     * Method that return the content field of empty if it is null
     * @param field
     * @return 
     */
    public static String getValueImaginary(Object field)
    {
        return field == null ? "" : field.toString();
    }
    
    /**
     * Method that search a data in a specify list and return the value
     * @param type indicate the place to search
     * @param data value to search
     * @return String with the other value, on the other hand the same data
     */
    public static String translate(int type,String data)
    {
        String a=null;
        if(type==1)
            a=FixData.searchTranslate(Configuration.getParameter("validation_translate_origin"), data);
        return a==null ? data: a;
    }
    
    /**
     * Method that search value for change by other value
     * @param listValues List of values, splits by comma
     * @param value value to search
     * @return String with the other value, on the other hand null
     */
    public static String searchTranslate(String listValues,String value)
    {
        for(String d : listValues.split(","))
        {
            if(d.startsWith(value + "|"))
                return d.split("|")[1];
        }
        return null;
    }
    
    /**
     * Method that get distance between two coordinates
     * @param coord1 Coordinates Northeast 
     * @param coord2 Coordinates Southwest
     * @return 
     */
    public static double getDistance(double[] coord1, double[] coord2) {
        if (coord1.length == 2 && coord2.length == 2) {
            double LatA = (coord1[0] * Math.PI) / 180;
            double LatB = (coord2[0] * Math.PI) / 180;
            double LngA = (coord1[1] * Math.PI) / 180;
            double LngB = (coord2[1] * Math.PI) / 180;
            // Retorna la distancia en kilometros
            return 6371 * Math.acos(Math.cos(LatA) * Math.cos(LatB) * Math.cos(LngB - LngA) + Math.sin(LatA) * Math.sin(LatB));
        } else {
            System.out.println("Error: coord length is not correct");
            return -1;
        }
    }
    
    /**
     * Method that evaluate if current position is a gap
     * @param taxon
     * @param currentPos
     * @return 
     */
    public static String fixGapsInTaxon(String[] taxon,int currentPos)
    {
        String a=null;
        if(taxon.length > (currentPos + 1))
        {
            if(taxon[currentPos].equals("x"))
                a=taxon[currentPos] + taxon[currentPos+1];
            else if(currentPos > 0 && taxon[currentPos-1].equals("x"))
                a=null;
            else
                a=taxon[currentPos];
        }
        else if(taxon.length == (currentPos + 1))
        {
            if(currentPos > 0 && taxon[currentPos-1].equals("x"))
                a=null;
            else
                a=taxon[currentPos];
        }
        return a;
    }
}
