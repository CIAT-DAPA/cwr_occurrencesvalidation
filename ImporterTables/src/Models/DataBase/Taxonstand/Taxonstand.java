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

package Models.DataBase.Taxonstand;

import java.io.FileNotFoundException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.renjin.sexp.ListVector;



/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class Taxonstand {
    
    public static void query(String taxon) throws Exception
    {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("Renjin");
        if(engine == null)
            throw new Exception("Can't connect with R");        
        try 
        {
            ListVector res = (ListVector)engine.eval("require(Taxonstand); r1 <- TPL(\"" + taxon + "\", corr=TRUE);");
            String depthEffective  = res.getElementAsString(0);
            String organicMaterial = res.getElementAsString(1);
            String internalDrain   = res.getElementAsString(2);
            String externalDrain   = res.getElementAsString(3);
            String[] infoMaterials = organicMaterial.split(",");
        }
        catch (ScriptException ex) {
        }
    }
}
