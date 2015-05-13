/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.DataBase;

import Tools.FixData;
import java.util.HashMap;

/**
 *
 * @author HSOTELO
 */
public class UpdateQuery {
    
    private HashMap content;
    private String header;
    private String where;
    
    public UpdateQuery(String header)
    {
        content=new HashMap();
        this.header=header;
        this.where="";
    }
    
    /**
     * Method that add key and value
     * @param key
     * @param value 
     */
    public void add(String key,String value)
    {
        content.put(key, value);
    }
    
    /**
     * Method that return count of inputs
     * @return 
     */
    public int count()
    {
        return content.size();
    }
    
    /**
     * Set Clause where for query
     * @param value 
     */
    public void setWhere(String value){
        this.where=value;
    }
    
    /**
     * Method that return all keys and values in format for query
     * @return 
     */
    public String toString(){
        String a="";
        String value;
        Object[] keys=content.keySet().toArray();
        for(Object key:keys){
            value=content.get(key.toString()).toString().trim();
            a+=key.toString() + "=" + (value.equals("null") ? "null" : "'" + FixData.fixToQuery(FixData.deleteAccent(value)) + "'") + ",";
        }
        return header + " " + a.substring(0, a.length()-1) + " " + where + ";";
    }
}
