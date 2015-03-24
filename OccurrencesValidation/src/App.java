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
import Controllers.Tools.Reports.TypeReport;
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
                    if(frmImp.isExit())
                    {
                        changeDirectory(frmImp.getLog());
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
                    if(frmData.isExit())
                    {
                        changeDirectory(frmData.getLog());
                        cTempOccurrences=frmData.getCTO();
                        cTempOccurrences.crossCheck(frmData.getStep(), frmData.getPolicies(), frmData.getLog(),frmData.getReviewData());
                        message("Validation finished!!!");
                    }
                }
                else if(option == 3)
                {
                    frmUpdate=new FrmUpdate(new JFrame(),true);
                    frmUpdate.setVisible(true);                    
                    if(frmUpdate.isExit())
                    {
                        changeDirectory(frmUpdate.getLog());
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
                    if(frmUpdateFields.isExit())
                    {
                        changeDirectory(frmUpdateFields.getLog());
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
                    if(frmReport.isExit())
                    {
                        changeDirectory(frmReport.getDestination());
                        cTempOccurrences=new CTempOccurrences();
                        if(frmReport.getTypeReport()==TypeReport.SUMMARY)
                            cTempOccurrences.reportSummary(frmReport.getDestination());
                        else if(frmReport.getTypeReport()==TypeReport.COMPARE_TO)
                            cTempOccurrences.reportCompare(frmReport.getDestination(),frmReport.getCompare(),frmReport.getIgnore(),frmReport.getCondition());
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
