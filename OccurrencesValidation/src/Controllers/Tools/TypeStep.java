/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Tools;

/**
 *
 * @author HSOTELO
 */
public enum TypeStep {
    CUSTOM("Step 0: Custom"),
    CROSS_CHECK_DATA("Step 1: Cross check data"),
    TAXONOMY_1("Step 2: Taxonomy checks part 1"),
    TAXONOMY_2("Step 3: Taxonomy checks part 2"),
    TAXONOMY_3("Step 4: Taxonomy checks part 3"),
    GEOGRAPHIC_1("Step 5: Geographic checks part 1"),
    GEOGRAPHIC_2("Step 6: Geographic checks coords"),
    GEOGRAPHIC_3("Step 7: Geographic checks georef"),
    ORIGIN_STAT("Step 8: Compare origin stat"),
    QUALITY("Step 9: Quality register");
    
    private final String name;       

    private TypeStep(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }
    
}
