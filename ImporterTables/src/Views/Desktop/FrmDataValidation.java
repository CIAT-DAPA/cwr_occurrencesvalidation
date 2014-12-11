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

package Views.Desktop;

import Controllers.Validation.Policies.Policy;
import Controllers.Validation.Policies.TypePolicy;
import Controllers.Validation.TypeDataValidation;
import Controllers.Validation.ValidationBase;
import Controllers.Validation.ValidationTempOccurrence;
import Tools.Log;
import Tools.TypeLog;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class FrmDataValidation extends javax.swing.JDialog {
    private boolean exit;
    private ValidationBase validation;
    
    /**
     * @return the exit
     */
    public boolean isExit() {
        return exit;
    }
    
    /**
     * @return the validation
     */
    public ValidationBase getValidation() {
        return validation;
    }
    
    /**
     * Creates new form FrmDataValidation
     */
    public FrmDataValidation(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        exit=false;
        txtLog.setText(System.getProperty("user.home"));
        for(TypeDataValidation value: TypeDataValidation.values())
            cboSource.addItem(value);
        //Fill table
        TypePolicy[] types=TypePolicy.values();
        DefaultTableModel model = (DefaultTableModel) tblPolicies.getModel();
        for(int i=0;i<types.length;i++)
            model.addRow(new Object[]{false,types[i],""});
        //Steps
        setPoliciesByStep(cboStep.getSelectedIndex());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblSource = new javax.swing.JLabel();
        cboSource = new javax.swing.JComboBox();
        lblStep = new javax.swing.JLabel();
        cboStep = new javax.swing.JComboBox();
        lblLog = new javax.swing.JLabel();
        txtLog = new javax.swing.JTextField();
        cmdLog = new javax.swing.JButton();
        cmdCountries = new javax.swing.JButton();
        scpPolicies = new javax.swing.JScrollPane();
        tblPolicies = new javax.swing.JTable();
        cmdStart = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        cmdSelect = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data Validation");
        setResizable(false);

        lblSource.setText("Source:");

        lblStep.setText("Step:");

        cboStep.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Step 1: Cross check data", "Step 2: Taxonomy checks part 1", "Step 4: Geographic checks part 1", "Step 5: Taxonomy checks part 2", "Step 6: Taxonomy checks part 3", "Step 7: Geographic checks coords", "Step 8: Geographic checks georef" }));
        cboStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStepActionPerformed(evt);
            }
        });

        lblLog.setText("Log:");

        cmdLog.setText("Search");
        cmdLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLogActionPerformed(evt);
            }
        });

        cmdCountries.setText("List Countries");
        cmdCountries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCountriesActionPerformed(evt);
            }
        });

        tblPolicies.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Check", "Policy", "Values"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scpPolicies.setViewportView(tblPolicies);

        cmdStart.setText("Start");
        cmdStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdStartActionPerformed(evt);
            }
        });

        cmdExit.setText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        cmdSelect.setText("Unselect all");
        cmdSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scpPolicies, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblLog)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLog)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdLog)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdCountries))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblSource)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboSource, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStep)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboStep, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdSelect)
                        .addGap(102, 102, 102)
                        .addComponent(cmdStart, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdExit, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSource)
                    .addComponent(cboSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStep)
                    .addComponent(cboStep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLog)
                    .addComponent(txtLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdLog)
                    .addComponent(cmdCountries))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpPolicies, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdSelect)
                    .addComponent(cmdStart)
                    .addComponent(cmdExit))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLogActionPerformed
        // TODO add your handling code here:
        try
        {
            JFileChooser fc=new JFileChooser();            
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(new File(System.getProperty("user.home")));
            if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                txtLog.setText(fc.getSelectedFile().getAbsolutePath());
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }//GEN-LAST:event_cmdLogActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        exit=true;
        this.setVisible(false);
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdStartActionPerformed
        // TODO add your handling code here:
        try
        {
            if(Log.existLog(txtLog.getText(), TypeLog.REGISTER_OK) || Log.existLog(txtLog.getText(), TypeLog.REGISTER_ERROR))
                JOptionPane.showMessageDialog(this, "Already exist file log. Please delete it and try again.\n" + txtLog.getText());
            else if((TypeDataValidation)cboSource.getSelectedItem()==TypeDataValidation.TEMP_OCCURRENCES)
            {
                ArrayList<Policy> policies=new ArrayList<Policy>();
                for(int i=0;i<tblPolicies.getRowCount();i++)
                {
                    if((boolean)tblPolicies.getValueAt(i, 0))
                        policies.add( new Policy((TypePolicy)tblPolicies.getValueAt(i, 1),null) );
                }
                validation = new ValidationTempOccurrence(policies, txtLog.getText());
                this.exit=false;            
                this.setVisible(false);
            }   
        }
        catch(Exception ex)
        {
            System.out.println("Error start");
            System.out.println(ex);
        }
    }//GEN-LAST:event_cmdStartActionPerformed

    private void cmdSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectActionPerformed
        // TODO add your handling code here:
        setPoliciesTable(cmdSelect.getText().equals("Select all"));
    }//GEN-LAST:event_cmdSelectActionPerformed

    private void cboStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStepActionPerformed
        // TODO add your handling code here:
        setPoliciesByStep(cboStep.getSelectedIndex());
    }//GEN-LAST:event_cboStepActionPerformed

    private void cmdCountriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCountriesActionPerformed
        // TODO add your handling code here:
        FrmCountries frmCountries =new FrmCountries(new JFrame(), true);
        frmCountries.setVisible(true);
    }//GEN-LAST:event_cmdCountriesActionPerformed

    /**
     * Method that establish
     * @param step 
     */
    private void setPoliciesByStep(int step)
    {
        setPoliciesTable(false);
        switch(step)
        {
            //Step 1: Cross check data
            case 0:
                setStatusTable(0, 8, true);
                break;
            //Step 2: Taxonomy checks part 1
            case 1:
                setStatusTable(12, 13, true);
                break;
            //Step 4: Geographic checks part 1
            case 2:
                setStatusTable(14, 19, true);
                break;
            //Step 5: Taxonomy checks part 2
            case 3:
                setStatusTable(20, 20, true);
                break;
            //Step 6: Taxonomy checks part 3
            case 4:
                setStatusTable(21, 21, true);
                break;
            //Step 7: Geographic checks coords
            case 5:
                setStatusTable(22, 22, true);
                break;
            //Step 8: Geographic checks georef
            case 6:
                setStatusTable(23, 23, true);
                break;
            default:
                break;
        }
    }
    
    /**
     * Method that clear or put values check in the table
     * @param val status check
     */
    private void setPoliciesTable(boolean val)
    {        
        setStatusTable(0,tblPolicies.getRowCount()-1,val);
        cmdSelect.setText(val?"Unselect all":"Select all");
    }
    
    
    private void setStatusTable(int init,int end, boolean val)
    {
        for(int i=init;i<=end;i++)
            tblPolicies.setValueAt(val, i, 0);
    }
            
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboSource;
    private javax.swing.JComboBox cboStep;
    private javax.swing.JButton cmdCountries;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdLog;
    private javax.swing.JButton cmdSelect;
    private javax.swing.JButton cmdStart;
    private javax.swing.JLabel lblLog;
    private javax.swing.JLabel lblSource;
    private javax.swing.JLabel lblStep;
    private javax.swing.JScrollPane scpPolicies;
    private javax.swing.JTable tblPolicies;
    private javax.swing.JTextField txtLog;
    // End of variables declaration//GEN-END:variables

    
}
