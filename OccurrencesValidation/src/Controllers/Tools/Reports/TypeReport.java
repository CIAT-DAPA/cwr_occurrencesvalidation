/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Tools.Reports;

/**
 *
 * @author HSOTELO
 */
public enum TypeReport {
    SUMMARY("Summary"),
    COMPARE_TO("Compare to"),
    COMPARE_GEOGRAPHIC("Compare Geographic"),
    COMPARE_TAXA("Compare Taxa");
    
    private final String name;       

    private TypeReport(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }
}