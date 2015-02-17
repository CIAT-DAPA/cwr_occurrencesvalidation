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
import Controllers.Occurrences.CMetadata;
import Controllers.Occurrences.CTempOccurrences;
import Views.Desktop.FrmConf;
import Views.Desktop.FrmUpdate;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class App {
        
    public static CMetadata cMetadata;
    public static CTempOccurrences cTempOccurrences;
    
    public static void main(String[] args) throws IOException
    {
        try
        {
            int option=0;
            FrmOptions frmOpt=new FrmOptions(new JFrame(),true);            
            FrmImport frmImp;
            FrmDataValidation frmData;
            FrmConf frmConf;
            FrmUpdate frmUpdate;
            do
            {   
                if(option==1)
                {
                    frmImp=new FrmImport(new JFrame(),true);
                    frmImp.setVisible(true);
                    if(!frmImp.isExit())
                    {
                        if(frmImp.getCBase() instanceof CTempOccurrences){
                            cTempOccurrences=(CTempOccurrences)frmImp.getCBase();
                            cTempOccurrences.importFile(frmImp.getSource(), frmImp.getSplit(), frmImp.getClean(),frmImp.getLog());
                        }
                        else if(frmImp.getCBase() instanceof CMetadata){
                            cMetadata=(CMetadata)frmImp.getCBase();
                            cMetadata.importFile(frmImp.getSource(), frmImp.getSplit(), frmImp.getClean(),frmImp.getLog());
                        }
                        else
                            message("Type don't support");
                        message("Import finished!!!");
                    }
                }
                else if(option == 2)
                {
                    frmData=new FrmDataValidation(new JFrame(),true);
                    frmData.setVisible(true);
                    if(!frmData.isExit())
                    {
                        cTempOccurrences=frmData.getCTO();
                        cTempOccurrences.crossCheck(frmData.getStep(), frmData.getPolicies(), frmData.getLog());
                        message("Validation finished!!!");
                    }
                }
                else if(option == 3)
                {
                    frmUpdate=new FrmUpdate(new JFrame(),true);
                    frmUpdate.setVisible(true);
                    if(!frmUpdate.isExit())
                    {
                        if(frmUpdate.getCBase() instanceof CTempOccurrences){
                            cTempOccurrences=(CTempOccurrences)frmUpdate.getCBase();
                            cTempOccurrences.updateFileQuery(frmUpdate.getFile(), frmUpdate.getLog());
                        }
                        else
                            message("Type don't support");
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
