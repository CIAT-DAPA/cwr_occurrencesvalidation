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

import Views.Desktop.FrmImport;
import Views.Desktop.FrmOptions;
import Views.Desktop.FrmDataValidation;
import Controllers.Importers.ImporterBase;
import Controllers.Update.UpdateBase;
import Controllers.Validation.ValidationBase;
import Views.Desktop.FrmConf;
import Views.Desktop.FrmQuery;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class Importer {
    
    public static ImporterBase importer;
    public static ValidationBase validation;
    public static UpdateBase update;
    
    public static void main(String[] args) throws IOException
    {
        try
        {
            int option=0;
            FrmOptions frmOpt=new FrmOptions(new JFrame(),true);            
            FrmImport frmImp;
            FrmDataValidation frmData;
            FrmConf frmConf;
            FrmQuery frmQuery;
            do
            {   
                if(option==1)
                {
                    frmImp=new FrmImport(new JFrame(),true);
                    frmImp.setVisible(true);
                    if(!frmImp.isExit())
                    {
                        Importer.importer=frmImp.getImporter();
                        Importer.importer.start();
                        message("Import finished!!!");
                    }
                }
                else if(option == 2)
                {
                    frmData=new FrmDataValidation(new JFrame(),true);
                    frmData.setVisible(true);
                    if(!frmData.isExit())
                    {
                        validation=frmData.getValidation();
                        validation.review(frmData.getStep());
                        message("Validation finished!!!");
                    }
                }
                else if(option == 3)
                {
                    frmQuery=new FrmQuery(new JFrame(),true);
                    frmQuery.setVisible(true);
                    if(!frmQuery.isExit())
                    {
                        update=frmQuery.getUpdate();
                        update.start();
                        message("Update finished!!!");
                    }
                }
                else if(option == 4)
                {
                    frmConf=new FrmConf(new JFrame(),true);
                    frmConf.setVisible(true);
                }
                frmOpt.setVisible(true);
                option=frmOpt.getOption();
            }while(option>=0 && option <5);
        }
        catch(Exception ex)
        {
            System.out.println("Error app");
            System.out.println(ex);
        }
    }
    
    /**
     * Method that show message in the window
     * @param msg message to show
     */
    public static void message(String msg)
    {
        JOptionPane.showMessageDialog(null, msg);
    }
}
