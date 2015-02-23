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
public class BaseUpdate {
    /*Members Class*/
    private String field;
    private String value;
    private String condition;

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    /**
     * Method Construct
     * @param field
     * @param value
     * @param condition 
     */
    public BaseUpdate(String field,String value,String condition)
    {
        this.field=field;
        this.value=value;
        this.condition=condition;
    }
    
    @Override
    public String toString(){
        return field + "|" + value +"|" + condition;
    }
}
