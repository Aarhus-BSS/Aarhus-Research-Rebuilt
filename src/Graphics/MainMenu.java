/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Common.Logging.ILogManager;
import Common.MsgBoxer.MBox;
import Environment.RoundManager;
import Environment.graphStatsExport;
import Graphics.CustomEvents.TreeListener;
import Graphics.GraphicManager.GraphTypeHandler;
import Graphics.GraphicManager.ReportManager.ReportCompiler;
import auresearch.FactoryHolder;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.YIntervalSeries;

/**
 *
 * @author d3vil401
 */
public class MainMenu extends javax.swing.JDialog implements Runnable {

    /**
     * Creates new form GraphTest
     */
    private TreeListener _tListener = null;
    private RoundManager _rm = null;
    
    private DefaultMutableTreeNode _root = new DefaultMutableTreeNode("Rounds List");
    
    public void clearEverything()
    {
        this.roundList.removeAll();
    }
    
    public MainMenu(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setAlwaysOnTop(false);
        this.setAutoRequestFocus(false);
        
        this._tListener = new TreeListener(this.roundList);
        
        this.roundList.setEditable(true);
        this.roundList.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.roundList.setShowsRootHandles(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Sep1 = new javax.swing.JSeparator();
        GraphTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        Graph_ActivityMonitor = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        Graph_ActivityMonitor5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        Graph_ActivityMonitor6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        Graph_ActivityMonitor7 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        Graph_ActivityMonitor8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        roundList = new javax.swing.JTree();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        jMenu = new javax.swing.JMenuBar();
        Menu_File = new javax.swing.JMenu();
        File_NewSession = new javax.swing.JMenuItem();
        File_CloseSession = new javax.swing.JMenuItem();
        Menu_Sep1 = new javax.swing.JPopupMenu.Separator();
        Menu_ExportMenu = new javax.swing.JMenu();
        ExportMenu_XLS = new javax.swing.JMenuItem();
        ExportMenu_PDF = new javax.swing.JMenuItem();
        ExportMenu_PNG = new javax.swing.JMenuItem();
        Menu_Sep2 = new javax.swing.JPopupMenu.Separator();
        Menu_Quit = new javax.swing.JMenuItem();
        Menu_Options = new javax.swing.JMenu();
        Options_Graphics = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Aarhus University - ABM Simulator");
        setMaximumSize(new java.awt.Dimension(1110, 653));
        setMinimumSize(new java.awt.Dimension(1110, 653));
        setName("DialogMain"); // NOI18N
        setResizable(false);

        Sep1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        GraphTab.setToolTipText("");
        GraphTab.setName(""); // NOI18N

        Graph_ActivityMonitor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Graph_ActivityMonitor.setMaximumSize(new java.awt.Dimension(60, 283));
        Graph_ActivityMonitor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Graph_ActivityMonitorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Graph_ActivityMonitorLayout = new javax.swing.GroupLayout(Graph_ActivityMonitor);
        Graph_ActivityMonitor.setLayout(Graph_ActivityMonitorLayout);
        Graph_ActivityMonitorLayout.setHorizontalGroup(
            Graph_ActivityMonitorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 674, Short.MAX_VALUE)
        );
        Graph_ActivityMonitorLayout.setVerticalGroup(
            Graph_ActivityMonitorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 429, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Graph_ActivityMonitor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Graph_ActivityMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GraphTab.addTab("AVG Problem Solve pR", jPanel1);

        Graph_ActivityMonitor5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Graph_ActivityMonitor5.setMaximumSize(new java.awt.Dimension(60, 283));
        Graph_ActivityMonitor5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Graph_ActivityMonitor5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Graph_ActivityMonitor5Layout = new javax.swing.GroupLayout(Graph_ActivityMonitor5);
        Graph_ActivityMonitor5.setLayout(Graph_ActivityMonitor5Layout);
        Graph_ActivityMonitor5Layout.setHorizontalGroup(
            Graph_ActivityMonitor5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 674, Short.MAX_VALUE)
        );
        Graph_ActivityMonitor5Layout.setVerticalGroup(
            Graph_ActivityMonitor5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 429, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Graph_ActivityMonitor5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Graph_ActivityMonitor5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GraphTab.addTab("Agents pR", jPanel2);

        Graph_ActivityMonitor6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Graph_ActivityMonitor6.setMaximumSize(new java.awt.Dimension(60, 283));
        Graph_ActivityMonitor6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Graph_ActivityMonitor6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Graph_ActivityMonitor6Layout = new javax.swing.GroupLayout(Graph_ActivityMonitor6);
        Graph_ActivityMonitor6.setLayout(Graph_ActivityMonitor6Layout);
        Graph_ActivityMonitor6Layout.setHorizontalGroup(
            Graph_ActivityMonitor6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 674, Short.MAX_VALUE)
        );
        Graph_ActivityMonitor6Layout.setVerticalGroup(
            Graph_ActivityMonitor6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 429, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Graph_ActivityMonitor6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Graph_ActivityMonitor6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GraphTab.addTab("PAgents pR", jPanel3);

        Graph_ActivityMonitor7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Graph_ActivityMonitor7.setMaximumSize(new java.awt.Dimension(60, 283));
        Graph_ActivityMonitor7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Graph_ActivityMonitor7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Graph_ActivityMonitor7Layout = new javax.swing.GroupLayout(Graph_ActivityMonitor7);
        Graph_ActivityMonitor7.setLayout(Graph_ActivityMonitor7Layout);
        Graph_ActivityMonitor7Layout.setHorizontalGroup(
            Graph_ActivityMonitor7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 674, Short.MAX_VALUE)
        );
        Graph_ActivityMonitor7Layout.setVerticalGroup(
            Graph_ActivityMonitor7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 429, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Graph_ActivityMonitor7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Graph_ActivityMonitor7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GraphTab.addTab("Composite EXP", jPanel4);

        Graph_ActivityMonitor8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Graph_ActivityMonitor8.setMaximumSize(new java.awt.Dimension(60, 283));
        Graph_ActivityMonitor8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Graph_ActivityMonitor8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Graph_ActivityMonitor8Layout = new javax.swing.GroupLayout(Graph_ActivityMonitor8);
        Graph_ActivityMonitor8.setLayout(Graph_ActivityMonitor8Layout);
        Graph_ActivityMonitor8Layout.setHorizontalGroup(
            Graph_ActivityMonitor8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 674, Short.MAX_VALUE)
        );
        Graph_ActivityMonitor8Layout.setVerticalGroup(
            Graph_ActivityMonitor8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 429, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Graph_ActivityMonitor8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Graph_ActivityMonitor8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GraphTab.addTab("Composite Problems", jPanel5);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Rounds");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Round 1");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("PAgents");
        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("PAgent#1");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("SAgents");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SAgent#1");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SAgent#2");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SAgent#3");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Round 2");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("PAgents");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("PAgent#1");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("PAgent#2");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("SAgents");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SAgent#1");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SAgent#2");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SAgent#3");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        roundList.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        roundList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                roundListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(roundList);

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Use same agents every Round");

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Enable mortality");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel1.setText("Simulation Status:");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(168, 162, 0));
        jLabel2.setText("Idle");
        jLabel2.setToolTipText("");

        jProgressBar1.setToolTipText("");

        jLabel3.setText("0 Years / 0 Years");
        jLabel3.setDoubleBuffered(true);

        Menu_File.setText("File");

        File_NewSession.setText("New Session");
        File_NewSession.setToolTipText("Creates a new session.");
        File_NewSession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                File_NewSessionActionPerformed(evt);
            }
        });
        Menu_File.add(File_NewSession);

        File_CloseSession.setText("Close Session");
        File_CloseSession.setToolTipText("Close current session.");
        Menu_File.add(File_CloseSession);
        Menu_File.add(Menu_Sep1);

        Menu_ExportMenu.setText("Export");
        Menu_ExportMenu.setToolTipText("");

        ExportMenu_XLS.setText("Export as Excel Sheet");
        ExportMenu_XLS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportMenu_XLSActionPerformed(evt);
            }
        });
        Menu_ExportMenu.add(ExportMenu_XLS);

        ExportMenu_PDF.setText("Export as PDF");
        Menu_ExportMenu.add(ExportMenu_PDF);

        ExportMenu_PNG.setText("Export as PNG");
        Menu_ExportMenu.add(ExportMenu_PNG);

        Menu_File.add(Menu_ExportMenu);
        Menu_File.add(Menu_Sep2);

        Menu_Quit.setText("Quit");
        Menu_Quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_QuitActionPerformed(evt);
            }
        });
        Menu_File.add(Menu_Quit);

        jMenu.add(Menu_File);

        Menu_Options.setText("Options");

        Options_Graphics.setText("Graphics");
        Menu_Options.add(Options_Graphics);

        jMenu.add(Menu_Options);

        setJMenuBar(jMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Sep1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(GraphTab)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1)
                            .addComponent(jCheckBox2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addComponent(jLabel2))
                                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(47, 47, 47))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Sep1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(GraphTab, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jCheckBox1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCheckBox2))
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );

        GraphTab.getAccessibleContext().setAccessibleName("AVG Problem Solve pR");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Menu_QuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_QuitActionPerformed
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_NORMAL, "Quitting...");
        FactoryHolder.destroy();
        System.exit(0);
    }//GEN-LAST:event_Menu_QuitActionPerformed

    private void Graph_ActivityMonitorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Graph_ActivityMonitorMouseClicked
        
        this.Graph_ActivityMonitor.removeAll();
        this.Graph_ActivityMonitor.setLayout(new FlowLayout(FlowLayout.LEFT));
        /*
        FactoryHolder._graphicManager.setGraphProperties(GraphTypeHandler._GRAPH_TYPE.TYPE_XYLINE, GraphTypeHandler._GRAPH_ORIENTATION.ORIENT_VERTICAL);
        FactoryHolder._graphicManager.setName("AVG Problems per Round");
        FactoryHolder._graphicManager.setSeriesName("Test Series");
        
        FactoryHolder._graphicManager.addXYData(1, 1);
        FactoryHolder._graphicManager.addXYData(2, 2);
        FactoryHolder._graphicManager.addXYData(3, 3);
        FactoryHolder._graphicManager.addXYData(4, 4);
        FactoryHolder._graphicManager.addXYData(5, 5);
        FactoryHolder._graphicManager.addXYData(6, 6);
        
        FactoryHolder._graphicManager.assignData();
        */
        ChartPanel _chart = null;
        
        if (FactoryHolder._graphsRender != null)
            _chart = FactoryHolder._graphsRender[graphStatsExport._GRAPH_PAGENTS_PER_ROUND];
        else {
            MBox.showBox("Renders not available.", "Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
            return;
        }
        //new ChartPanel(FactoryHolder._graphicManager.render()); 
        if (_chart != null)
        {
            _chart = FactoryHolder._graphsRender[graphStatsExport._GRAPH_PAGENTS_PER_ROUND];
            this.Graph_ActivityMonitor.add(_chart);
            this.Graph_ActivityMonitor.validate();
        } else 
            MBox.showBox("Graphic has not been rendered yet.", "Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
    }//GEN-LAST:event_Graph_ActivityMonitorMouseClicked

    private void ExportMenu_XLSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportMenu_XLSActionPerformed
        ReportCompiler _report = new ReportCompiler("testReport");
        
        _report.createReport(new Object[] {"ID", "Agent Type", "Experience"});
        _report.addContent(new Object[] {"1", "SolverAgent#1", 2100});
        _report.addContent(new Object[] {"2", "SolverAgent#2", 2000});
        _report.addContent(new Object[] {"3", "SolverAgent#3", 1800});
        _report.addContent(new Object[] {"4", "SolverAgent#4", 100});
        _report.addContent(new Object[] {"5", "SolverAgent#5", 5900});
        _report.addContent(new Object[] {"6", "SolverAgent#6", 0});
        _report.addContent(new Object[] {"7", "SolverAgent#7", 10});
        _report.addContent(new Object[] {"8", "SolverAgent#8", 2300});
        _report.addContent(new Object[] {"9", "SolverAgent#9", 11100});
        _report.end();
        MBox.showBox("Report has been compiled into " + FactoryHolder._configManager.getStringValue("REPORT_OUTPUT_FOLDER") + File.separator + "testReport.xls", "Report compiled", MBox._MSGBOX_TYPE.TYPE_INFORMATION);
    }//GEN-LAST:event_ExportMenu_XLSActionPerformed

    private void Graph_ActivityMonitor5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Graph_ActivityMonitor5MouseClicked
         this.Graph_ActivityMonitor5.removeAll();
        this.Graph_ActivityMonitor5.setLayout(new FlowLayout(FlowLayout.LEFT));
        
         ChartPanel _chart = null;
        
        if (FactoryHolder._graphsRender != null)
            _chart = FactoryHolder._graphsRender[graphStatsExport._GRAPH_SAGENTS_PER_ROUND];
        else {
            MBox.showBox("Renders not available.", "Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
            return;
        }
        //new ChartPanel(FactoryHolder._graphicManager.render()); 
        if (_chart != null)
        {
            _chart = FactoryHolder._graphsRender[graphStatsExport._GRAPH_SAGENTS_PER_ROUND];
            this.Graph_ActivityMonitor5.add(_chart);
            this.Graph_ActivityMonitor5.validate();
        } else 
            MBox.showBox("Graphic has not been rendered yet.", "Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
    }//GEN-LAST:event_Graph_ActivityMonitor5MouseClicked

    private void Graph_ActivityMonitor6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Graph_ActivityMonitor6MouseClicked
         this.Graph_ActivityMonitor6.removeAll();
        this.Graph_ActivityMonitor6.setLayout(new FlowLayout(FlowLayout.LEFT));
        
         ChartPanel _chart = null;
        
        if (FactoryHolder._graphsRender != null)
            _chart = FactoryHolder._graphsRender[graphStatsExport._GRAPH_PAGENTS_PER_ROUND];
        else {
            MBox.showBox("Renders not available.", "Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
            return;
        }
        //new ChartPanel(FactoryHolder._graphicManager.render()); 
        if (_chart != null)
        {
            _chart = FactoryHolder._graphsRender[graphStatsExport._GRAPH_PAGENTS_PER_ROUND];
            this.Graph_ActivityMonitor6.add(_chart);
            this.Graph_ActivityMonitor6.validate();
        } else 
            MBox.showBox("Graphic has not been rendered yet.", "Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
    }//GEN-LAST:event_Graph_ActivityMonitor6MouseClicked

    private void Graph_ActivityMonitor7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Graph_ActivityMonitor7MouseClicked
        this.Graph_ActivityMonitor7.removeAll();
        this.Graph_ActivityMonitor7.setLayout(new FlowLayout(FlowLayout.LEFT));
        
         ChartPanel _chart = null;
        
        if (FactoryHolder._graphsRender != null)
            _chart = FactoryHolder._graphsRender[graphStatsExport._GRAPH_COMPOSITEXP];
        else {
            MBox.showBox("Renders not available.", "Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
            return;
        }
        //new ChartPanel(FactoryHolder._graphicManager.render()); 
        if (_chart != null)
        {
            _chart = FactoryHolder._graphsRender[graphStatsExport._GRAPH_COMPOSITEXP];
            this.Graph_ActivityMonitor7.add(_chart);
            this.Graph_ActivityMonitor7.validate();
        } else 
            MBox.showBox("Graphic has not been rendered yet.", "Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
    }//GEN-LAST:event_Graph_ActivityMonitor7MouseClicked

    private void Graph_ActivityMonitor8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Graph_ActivityMonitor8MouseClicked
        this.Graph_ActivityMonitor8.removeAll();
        this.Graph_ActivityMonitor8.setLayout(new FlowLayout(FlowLayout.LEFT));
        
         ChartPanel _chart = null;
        
        if (FactoryHolder._graphsRender != null)
            _chart = FactoryHolder._graphsRender[graphStatsExport._GRAPH_PAGENTS_PER_ROUND];
        else {
            MBox.showBox("Renders not available.", "Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
            return;
        }
        //new ChartPanel(FactoryHolder._graphicManager.render()); 
        if (_chart != null)
        {
            _chart = FactoryHolder._graphsRender[graphStatsExport._GRAPH_PAGENTS_PER_ROUND];
            this.Graph_ActivityMonitor8.add(_chart);
            this.Graph_ActivityMonitor8.validate();
        } else 
            MBox.showBox("Graphic has not been rendered yet.", "Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
    }//GEN-LAST:event_Graph_ActivityMonitor8MouseClicked

    private void roundListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundListMouseClicked
        this._tListener.mouseClicked(evt);
    }//GEN-LAST:event_roundListMouseClicked

    private void File_NewSessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_File_NewSessionActionPerformed
        try {
            FactoryHolder._configManager.reload();
            _rm = new RoundManager(
                    FactoryHolder._configManager.getNumberValue("MAX_ROUNDS"),
                    FactoryHolder._configManager.getNumberValue("SOLVER_AGENTS_AMOUNT"),
                    FactoryHolder._configManager.getNumberValue("PROPOSER_AGENTS_AMOUNT")
            );
            this.jLabel2.setText("Running...");
            this.jLabel2.setForeground(Color.RED);
            this.jProgressBar1.setMaximum(FactoryHolder._configManager.getNumberValue("MAX_ROUNDS"));
            this.jProgressBar1.setValue(0);
            this.jLabel3.setText("0 Years / " + FactoryHolder._configManager.getNumberValue("MAX_ROUNDS") + " Years");
            new Thread(this).start();
        } catch (IOException ex) {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Couldn't reload configuration: " + ex.getMessage());
        }
    }//GEN-LAST:event_File_NewSessionActionPerformed

    @Override
    public void run()
    {
        int _counter = 0;
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "Running simulation now...");
        while (this._rm.hasNext())
        {
            _counter++;
            this._rm.runNextRound();
            this.jProgressBar1.setValue(_counter);
            
            this.jLabel3.setText((_counter) + " Rounds / " + FactoryHolder._configManager.getNumberValue("MAX_ROUNDS") + " Rounds");
        }
        this._rm.end();
        this.jLabel2.setText("Ended");
        this.jLabel2.setForeground(Color.GREEN);
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "Simulation ended!");
        Toolkit.getDefaultToolkit().beep();
        
        ChartPanel _chart = null;
        
        if (FactoryHolder._graphsRender != null) 
        {
        
            //new ChartPanel(FactoryHolder._graphicManager.render()); 
            this.Graph_ActivityMonitor.removeAll();
            this.Graph_ActivityMonitor.setLayout(new FlowLayout(FlowLayout.LEFT));
            this.Graph_ActivityMonitor5.removeAll();
            this.Graph_ActivityMonitor5.setLayout(new FlowLayout(FlowLayout.LEFT));
            this.Graph_ActivityMonitor7.removeAll();
            this.Graph_ActivityMonitor7.setLayout(new FlowLayout(FlowLayout.LEFT));
            this.Graph_ActivityMonitor8.removeAll();
            this.Graph_ActivityMonitor8.setLayout(new FlowLayout(FlowLayout.LEFT));
            
            this.Graph_ActivityMonitor.add(FactoryHolder._graphsRender[graphStatsExport._GRAPH_PAGENTS_PER_ROUND]);
            this.Graph_ActivityMonitor5.add(FactoryHolder._graphsRender[graphStatsExport._GRAPH_SAGENTS_PER_ROUND]);
            this.Graph_ActivityMonitor7.add(FactoryHolder._graphsRender[graphStatsExport._GRAPH_COMPOSITEXP]);
            this.Graph_ActivityMonitor8.add(FactoryHolder._graphsRender[graphStatsExport._GRAPH_PAGENTS_PER_ROUND]);
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            this.Graph_ActivityMonitor.validate();
            this.Graph_ActivityMonitor5.validate();
            this.Graph_ActivityMonitor7.validate();
            this.Graph_ActivityMonitor8.validate();
                
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ExportMenu_PDF;
    private javax.swing.JMenuItem ExportMenu_PNG;
    private javax.swing.JMenuItem ExportMenu_XLS;
    private javax.swing.JMenuItem File_CloseSession;
    private javax.swing.JMenuItem File_NewSession;
    private javax.swing.JTabbedPane GraphTab;
    private javax.swing.JPanel Graph_ActivityMonitor;
    private javax.swing.JPanel Graph_ActivityMonitor5;
    private javax.swing.JPanel Graph_ActivityMonitor6;
    private javax.swing.JPanel Graph_ActivityMonitor7;
    private javax.swing.JPanel Graph_ActivityMonitor8;
    private javax.swing.JMenu Menu_ExportMenu;
    private javax.swing.JMenu Menu_File;
    private javax.swing.JMenu Menu_Options;
    private javax.swing.JMenuItem Menu_Quit;
    private javax.swing.JPopupMenu.Separator Menu_Sep1;
    private javax.swing.JPopupMenu.Separator Menu_Sep2;
    private javax.swing.JMenuItem Options_Graphics;
    private javax.swing.JSeparator Sep1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuBar jMenu;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTree roundList;
    // End of variables declaration//GEN-END:variables
}
