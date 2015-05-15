/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views.Desktop;

import Controllers.Occurrences.BaseController;
import Controllers.Occurrences.CMetadata;
import Controllers.Occurrences.CTempOccurrences;
import Controllers.Tools.TypeImports;
import Controllers.Tools.TypeUpdate;
import Models.DataBase.BaseUpdate;
import Tools.Configuration;
import Tools.FixData;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author HSOTELO
 */
public class FrmUpdate extends javax.swing.JDialog {

    private boolean exit;
    private BaseController cBase;
    private ArrayList<BaseUpdate> updates;
    private TypeUpdate typeUpdate;
    
    /**
     * Creates new form FrmUpdateFields
     */
    public FrmUpdate(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        generateGroupRBT();
        exit=false;
        for(TypeImports value: TypeImports.values())
            cboTypeImport.addItem(value);
        txtLog.setText(Configuration.DIRECTORY_DEFAULT);
        txtFile.setText(Configuration.DIRECTORY_DEFAULT);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rbtGroup = new javax.swing.ButtonGroup();
        cboTypeImport = new javax.swing.JComboBox();
        lblTypeImport = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUpdates = new javax.swing.JTable();
        cmdRunByField = new javax.swing.JButton();
        lblLog = new javax.swing.JLabel();
        txtLog = new javax.swing.JTextField();
        cmdLog = new javax.swing.JButton();
        lblFile = new javax.swing.JLabel();
        txtFile = new javax.swing.JTextField();
        cmdSearch = new javax.swing.JButton();
        cmdRunByFile = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        lblDesConfiguration = new javax.swing.JLabel();
        lblDesConfiguration1 = new javax.swing.JLabel();
        lblDesConfiguration2 = new javax.swing.JLabel();
        rbtClearQuery = new javax.swing.JRadioButton();
        rbtFields = new javax.swing.JRadioButton();
        chkTaxonFinal = new javax.swing.JCheckBox();
        txtSplit = new javax.swing.JTextField();
        lblSplit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update directly");
        setResizable(false);

        lblTypeImport.setText("Type:");

        tblUpdates.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Field", "Value", "Condition"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblUpdates);

        cmdRunByField.setText("Run");
        cmdRunByField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRunByFieldActionPerformed(evt);
            }
        });

        lblLog.setText("Log:");

        cmdLog.setText("Search");
        cmdLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLogActionPerformed(evt);
            }
        });

        lblFile.setText("File:");

        cmdSearch.setText("Search");
        cmdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSearchActionPerformed(evt);
            }
        });

        cmdRunByFile.setText("Run");
        cmdRunByFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRunByFileActionPerformed(evt);
            }
        });

        lblDesConfiguration.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDesConfiguration.setText("BY FILE");

        lblDesConfiguration1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDesConfiguration1.setText("CONFIGURATION");

        lblDesConfiguration2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDesConfiguration2.setText("BY FILEDS");

        rbtClearQuery.setSelected(true);
        rbtClearQuery.setText("Clear query");

        rbtFields.setText("Fields");

        chkTaxonFinal.setText("Taxon Final");

        txtSplit.setText("\\|");
        txtSplit.setToolTipText("");

        lblSplit.setText("Split:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblLog)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtLog)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdLog))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTypeImport)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboTypeImport, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(102, 102, 102)
                                .addComponent(cmdRunByFile, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(rbtClearQuery)
                                        .addGap(18, 18, 18)
                                        .addComponent(rbtFields)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblSplit)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(chkTaxonFinal)
                                        .addGap(0, 43, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtFile)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmdSearch))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(cmdRunByField, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblDesConfiguration1)
                .addGap(124, 124, 124))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(lblDesConfiguration2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(lblDesConfiguration)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDesConfiguration1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTypeImport)
                    .addComponent(cboTypeImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLog)
                    .addComponent(txtLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdLog))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDesConfiguration)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFile)
                    .addComponent(txtFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSplit)
                    .addComponent(txtSplit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbtClearQuery)
                    .addComponent(rbtFields)
                    .addComponent(chkTaxonFinal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdRunByFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDesConfiguration2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdRunByField)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdRunByFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRunByFieldActionPerformed
        // TODO add your handling code here:
        try
        {
            TypeImports tImport=(TypeImports)cboTypeImport.getSelectedItem();
            if(JOptionPane.showConfirmDialog(this, "confirm that you want to make changes in database?","Alert",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION)
            {
                typeUpdate = TypeUpdate.QUERY_FIELDS;
                cBase=tImport==TypeImports.TEMP_OCCURRENCES ? new CTempOccurrences() : 
                    (tImport==TypeImports.METADATA ? new CMetadata() : null);
                updates=new ArrayList<>();                 
                for(int i=0;i<tblUpdates.getRowCount();i++)
                {
                    String field=FixData.getValueImaginary(tblUpdates.getValueAt(i, 0)),value=FixData.getValueImaginary(tblUpdates.getValueAt(i, 1)),condition=FixData.getValueImaginary(tblUpdates.getValueAt(i, 2));
                    if(!field.equals("") && !value.equals(""))
                        updates.add( new BaseUpdate(field, value, condition) );
                }
                this.exit=true;
                this.setVisible(false);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error start");
            System.out.println(ex);
            System.out.println(ex.toString());
        }
    }//GEN-LAST:event_cmdRunByFieldActionPerformed

    private void cmdLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLogActionPerformed
        // TODO add your handling code here:
        try
        {
            JFileChooser fc=new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(new File(txtLog.getText()));
            if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                txtLog.setText(fc.getSelectedFile().getAbsolutePath());
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }//GEN-LAST:event_cmdLogActionPerformed

    private void cmdSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSearchActionPerformed
        // TODO add your handling code here:
        try
        {
            JFileChooser fc=new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setCurrentDirectory(new File(txtFile.getText()));
            if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            txtFile.setText(fc.getSelectedFile().getAbsolutePath());
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }//GEN-LAST:event_cmdSearchActionPerformed

    private void cmdRunByFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRunByFileActionPerformed
        // TODO add your handling code here:
        try
        {
            if(JOptionPane.showConfirmDialog(this, "confirm that you want to make changes in database?","Alert",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION)
            {
                TypeImports tImport=(TypeImports)cboTypeImport.getSelectedItem();
                cBase=tImport==TypeImports.TEMP_OCCURRENCES ? new CTempOccurrences() :
                    (tImport==TypeImports.METADATA ? new CMetadata() : null);
                this.exit=true;
                this.setVisible(false);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error start");
            System.out.println(ex);
        }
    }//GEN-LAST:event_cmdRunByFileActionPerformed

    /**
     * Method that add all radio buttons to a group
     */
    private void generateGroupRBT(){
        rbtGroup.add(rbtClearQuery);
        rbtGroup.add(rbtFields);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboTypeImport;
    private javax.swing.JCheckBox chkTaxonFinal;
    private javax.swing.JButton cmdLog;
    private javax.swing.JButton cmdRunByField;
    private javax.swing.JButton cmdRunByFile;
    private javax.swing.JButton cmdSearch;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDesConfiguration;
    private javax.swing.JLabel lblDesConfiguration1;
    private javax.swing.JLabel lblDesConfiguration2;
    private javax.swing.JLabel lblFile;
    private javax.swing.JLabel lblLog;
    private javax.swing.JLabel lblSplit;
    private javax.swing.JLabel lblTypeImport;
    private javax.swing.JRadioButton rbtClearQuery;
    private javax.swing.JRadioButton rbtFields;
    private javax.swing.ButtonGroup rbtGroup;
    private javax.swing.JTable tblUpdates;
    private javax.swing.JTextField txtFile;
    private javax.swing.JTextField txtLog;
    private javax.swing.JTextField txtSplit;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the exit
     */
    public boolean isExit() {
        return exit;
    }

    /**
     * @return the cBase
     */
    public BaseController getCBase() {
        return cBase;
    }

    /**
     * @return the updates
     */
    public ArrayList<BaseUpdate> getUpdates() {
        return updates;
    }
    
    /**
     * @return the path for log
     */
    public String getLog(){
        return txtLog.getText();
    }
    
    /**
     * @return Path the source file
     */
    public String getFile(){
        return txtFile.getText();
    }
    
    /**
     * @return Split pattern
     */
    public String getSplit(){
        return txtSplit.getText();
    }

    /**
     * @return the typeUpdate
     */
    public TypeUpdate getTypeUpdate() {
        return typeUpdate;
    }
    
    /**
     * @return If the update process is for taxon information
     */
    public boolean isTaxonUpdate(){
        return chkTaxonFinal.isSelected();
    }
}
