/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.DataBase;

/**
 *
 * @author HSOTELO
 */
public class Field {
    /*Members Class*/
    private String name;
    private String type;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /*Methods*/
    
    /**
     * Method Construct
     * @param name
     * @param type
     */
    public Field(String name,String type){
        this.name=name;
        this.type=type;
    }
    
    /**
     * Method Construct
     */
    public Field(){
        this("","");
    }
}
