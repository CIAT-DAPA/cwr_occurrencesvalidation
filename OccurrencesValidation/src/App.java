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
import Tools.Configuration;
import Views.Desktop.FrmConf;
import Views.Desktop.FrmReport;
import Views.Desktop.FrmUpdate;
import Views.Desktop.FrmUpdateFields;
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
            FrmUpdateFields frmUpdateFields;
            FrmReport frmReport;
            do
            {   
                if(option==1)
                {
                    frmImp=new FrmImport(new JFrame(),true);
                    frmImp.setVisible(true);
                    changeDirectory(frmImp.getLog());
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
                    changeDirectory(frmData.getLog());
                    if(!frmData.isExit())
                    {
                        cTempOccurrences=frmData.getCTO();
                        cTempOccurrences.crossCheck(frmData.getStep(), frmData.getPolicies(), frmData.getLog(),frmData.getReviewData());
                        message("Validation finished!!!");
                    }
                }
                else if(option == 3)
                {
                    frmUpdate=new FrmUpdate(new JFrame(),true);
                    frmUpdate.setVisible(true);
                    changeDirectory(frmUpdate.getLog());
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
                else if(option == 5)
                {
                    frmUpdateFields=new FrmUpdateFields(new JFrame(),true);
                    frmUpdateFields.setVisible(true);
                    changeDirectory(frmUpdateFields.getLog());
                    if(!frmUpdateFields.isExit())
                    {
                        if(frmUpdateFields.getCBase() instanceof CTempOccurrences){
                            cTempOccurrences=(CTempOccurrences)frmUpdateFields.getCBase();
                            cTempOccurrences.updateFields(frmUpdateFields.getUpdates(), frmUpdateFields.getLog());
                        }
                        else
                            message("Type don't support");
                        message("Update finished!!!");
                    }
                }
                else if(option == 6)
                {
                    frmReport=new FrmReport(new JFrame(), true);
                    frmReport.setVisible(true);
                    changeDirectory(frmReport.getDestination());
                    if(!frmReport.isExit())
                    {
                        cTempOccurrences=new CTempOccurrences();
                        cTempOccurrences.generateReport(frmReport.getDestination());
                        message("Summary finished!!!");
                    }
                }
                frmOpt.setVisible(true);
                option=frmOpt.getOption();
            }while(option>=0 && option <7);
        }
        catch(Exception ex)
        {
            System.out.println("Error app");
            System.out.println(ex);
        }
    }
    
    /**
     * Method that change the value of default directory
     * @param log 
     */
    public static void changeDirectory(String log)
    {
        Configuration.DIRECTORY_DEFAULT = log;
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
