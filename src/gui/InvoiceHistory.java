/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static gui.Index.logger;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import static gui.Index.itemId;
import java.nio.file.FileSystems;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.MySQL;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Ayesh-PC
 */
public class InvoiceHistory extends javax.swing.JFrame {

    /**
     * Creates new form InvoiceHistory
     */
    public InvoiceHistory() {
        initComponents();
        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        ImageIcon imageIcon = new ImageIcon(Index.class.getResource("/resources/logo.png"));
        this.setIconImage(imageIcon.getImage());
        
        loadInvoice();
        loadInvoiceItem();
        loadInitialRows();
        setTableHeaderFontSize(jTable10, 16);
        setTableHeaderFontSize(jTable11, 16);     
    }
    
    private void setTableHeaderFontSize(JTable table, int value) {
        JTableHeader header = table.getTableHeader();
        header.setFont(new java.awt.Font("Segoe UI Semibold", 0, value));
    }
    
       
private int currentRow = 0;
private static final int ROW_LIMIT = 500;
private int totalResults = 0;
private int totalRows = 0;

private int getTotalInvoiceRowCount() {
    try {
        // Fetch the total number of results for the entire invoice table
        ResultSet countResultSet = MySQL.execute("SELECT COUNT(*) AS total FROM invoice");
        if (countResultSet.next()) {
            return countResultSet.getInt("total");
        }
    } catch (Exception e) {
        logger.log(Level.WARNING, "getTotalInvoiceRowCount", e);
    }
    return 0; // Return 0 in case of an exception
}


private void loadInvoice() {
    String invoiceID = jFormattedTextField12.getText().trim();
    String date = jTextField1.getText().trim();
    Double total = 0.00;

    try {
        // Fetch the total number of results
        ResultSet countResultSet = MySQL.execute("SELECT COUNT(*) AS total FROM invoice WHERE `id` LIKE '%" + invoiceID + "%'  "
                + "AND `date_time` LIKE '%" + date + "%'");
        if (countResultSet.next()) {
            totalResults = countResultSet.getInt("total");
        }

        // Fetch the current result set
        ResultSet resultSet = MySQL.execute("SELECT * FROM invoice WHERE `id` LIKE '%" + invoiceID + "%'  "
                + "AND `date_time` LIKE '%" + date + "%' LIMIT " + ROW_LIMIT + " OFFSET " + currentRow);

        DefaultTableModel model = (DefaultTableModel) jTable10.getModel();
        model.setRowCount(0);

        // Reset totalRows to 0 before entering the loop
        totalRows = 0;

        while (resultSet.next()) {
            Vector<String> vector = new Vector<>();

            vector.add(resultSet.getString("id"));
            vector.add(resultSet.getString("date_time"));
            Double discount = resultSet.getDouble("discount");
            vector.add(resultSet.getString("discount"));

            // Search items to calculate the total of each invoice
            double invoiceTotal = 0;

            ResultSet resultSet2 = MySQL.execute("SELECT * FROM invoice_item WHERE invoice_id='" + resultSet.getString("id") + "'");

            while (resultSet2.next()) {
                invoiceTotal += resultSet2.getDouble("qty") * resultSet2.getDouble("selling_price");
            }

            vector.add(String.valueOf(invoiceTotal - discount));
            vector.add(resultSet.getString("paid_amount"));

            totalRows++;
            total += invoiceTotal;

            model.addRow(vector);
        }

        int tableTotalRows = getTotalInvoiceRowCount();
        int displayedRows = model.getRowCount();
        int startResult = currentRow + 1;
        int endResult = currentRow + displayedRows;

        // Move the UI update outside of the loop to display totalResults all the time
        jLabel92.setText("Invoices (" + totalRows + " of " + tableTotalRows + ") - Showing results " + startResult + " to " + endResult);

        // Update jLabel87 with the calculated total
        jLabel87.setText(String.format("%.2f", total));

        // Disable the "Next" button if there are no more results
        if (currentRow + ROW_LIMIT >= totalResults) {
            jButton5.setEnabled(false);
        } else {
            jButton5.setEnabled(true);
        }
    } catch (Exception e) {
        logger.log(Level.WARNING, "loadInvoice_history", e);
        // e.printStackTrace();
    }
}


// Call this method when you want to navigate to the next set of results
private void navigateToNextSet() {
    currentRow += ROW_LIMIT;
    totalRows = 0;
    loadInvoice();
}

// Call this method when you want to navigate to the previous set of results
private void navigateToPreviousSet() {
    currentRow = Math.max(0, currentRow - ROW_LIMIT);
    totalRows = 0;
    loadInvoice();
}

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jLabel92 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jPanel95 = new javax.swing.JPanel();
        jPanel96 = new javax.swing.JPanel();
        jLabel79 = new javax.swing.JLabel();
        jPanel97 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jFormattedTextField12 = new javax.swing.JFormattedTextField();
        jPanel99 = new javax.swing.JPanel();
        jLabel83 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel100 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jPanel101 = new javax.swing.JPanel();
        jButton40 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel103 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable10.setAutoCreateRowSorter(true);
        jTable10.setFont(new java.awt.Font("Iskoola Pota", 0, 16)); // NOI18N
        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Invoice No", "Date_time", "Discount", "Total", "Paid_amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable10.setIntercellSpacing(new java.awt.Dimension(10, 0));
        jTable10.setRowHeight(40);
        jTable10.setSelectionBackground(new java.awt.Color(224, 224, 224));
        jTable10.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTable10.getTableHeader().setReorderingAllowed(false);
        jTable10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable10MouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(jTable10);

        jTable11.setAutoCreateRowSorter(true);
        jTable11.setFont(new java.awt.Font("Iskoola Pota", 0, 16)); // NOI18N
        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Name", "Quantity", "Selling price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable11.setIntercellSpacing(new java.awt.Dimension(10, 0));
        jTable11.setRowHeight(40);
        jTable11.setSelectionBackground(new java.awt.Color(224, 224, 224));
        jTable11.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTable11.getTableHeader().setReorderingAllowed(false);
        jScrollPane11.setViewportView(jTable11);

        jLabel92.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel92.setText("Invoices (0)");

        jLabel91.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel91.setText("Invoice items (0)");

        jPanel95.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 0), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        jPanel95.setPreferredSize(new java.awt.Dimension(385, 0));
        jPanel95.setLayout(new java.awt.GridLayout(6, 1, 0, 10));

        jPanel96.setOpaque(false);
        jPanel96.setLayout(new java.awt.BorderLayout());

        jLabel79.setBackground(new java.awt.Color(153, 204, 255));
        jLabel79.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel79.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel79.setText("Search invoices");
        jPanel96.add(jLabel79, java.awt.BorderLayout.CENTER);

        jPanel95.add(jPanel96);

        jPanel97.setOpaque(false);
        jPanel97.setLayout(new java.awt.BorderLayout());

        jLabel80.setBackground(new java.awt.Color(153, 204, 255));
        jLabel80.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel80.setText("Invoice No");
        jPanel97.add(jLabel80, java.awt.BorderLayout.CENTER);

        jFormattedTextField12.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jFormattedTextField12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jFormattedTextField12.setPreferredSize(new java.awt.Dimension(200, 22));
        jFormattedTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField12KeyReleased(evt);
            }
        });
        jPanel97.add(jFormattedTextField12, java.awt.BorderLayout.EAST);

        jPanel95.add(jPanel97);

        jPanel99.setOpaque(false);

        jLabel83.setBackground(new java.awt.Color(153, 204, 255));
        jLabel83.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel83.setText("Date   ");
        jLabel83.setPreferredSize(new java.awt.Dimension(100, 25));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel99Layout = new javax.swing.GroupLayout(jPanel99);
        jPanel99.setLayout(jPanel99Layout);
        jPanel99Layout.setHorizontalGroup(
            jPanel99Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel99Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addGap(26, 26, 26)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel99Layout.setVerticalGroup(
            jPanel99Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel99Layout.createSequentialGroup()
                .addGroup(jPanel99Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel95.add(jPanel99);

        jPanel100.setOpaque(false);
        jPanel100.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("(Hint :  YYYY-MM-DD)");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel100.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jLabel87.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel87.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel87.setText("0");
        jPanel100.add(jLabel87, java.awt.BorderLayout.CENTER);

        jLabel86.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel86.setText("Total (Rs.)");
        jLabel86.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel100.add(jLabel86, java.awt.BorderLayout.LINE_START);

        jPanel95.add(jPanel100);

        jPanel101.setOpaque(false);
        jPanel101.setLayout(new java.awt.BorderLayout());

        jButton40.setBackground(new java.awt.Color(0, 153, 255));
        jButton40.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jButton40.setForeground(new java.awt.Color(255, 255, 255));
        jButton40.setText("Find Invoice");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });
        jPanel101.add(jButton40, java.awt.BorderLayout.CENTER);

        jButton1.setBackground(new java.awt.Color(0, 153, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/reload1.png"))); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(45, 31));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel101.add(jButton1, java.awt.BorderLayout.LINE_END);

        jPanel95.add(jPanel101);

        jPanel103.setOpaque(false);
        jPanel103.setLayout(new java.awt.BorderLayout());

        jButton2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jButton2.setText("Print report");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel103.add(jButton2, java.awt.BorderLayout.CENTER);

        jPanel95.add(jPanel103);

        jButton3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/stock1.png"))); // NOI18N
        jButton3.setText("Home");
        jButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1));
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton3.setIconTextGap(10);
        jButton3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButton3FocusGained(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setText("<");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton5.setText(">");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setText("<");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton7.setText(">");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel92)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton5)
                                .addGap(6, 6, 6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel91)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 909, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jPanel95, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton6)
                                .addGap(14, 14, 14)
                                .addComponent(jButton7)
                                .addGap(10, 10, 10)))))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel92)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel95, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel91)
                            .addComponent(jButton6)
                            .addComponent(jButton7))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

//    private void loadInvoiceItem() {
//        
//         int selectedRow = jTable10.getSelectedRow();
//         
//         String query = "SELECT * FROM invoice_item INNER JOIN invoice ON invoice_item.invoice_id = invoice.id";
//         
//         if(selectedRow != -1){
//             String InvoiceID = String.valueOf(jTable10.getValueAt(jTable10.getSelectedRow(), 0));
//             query += " WHERE invoice_id='" + InvoiceID + "'";
//         }        
//
//        try {
//            ResultSet resultset = MySQL.execute(query);
//
//            DefaultTableModel model = (DefaultTableModel) jTable11.getModel();
//            model.setRowCount(0);
//
//            int totalItems = 0;
//            int itemId = 1;
//
//            boolean found = false;
//
//            while (resultset.next()) {
//                Vector<String> vector = new Vector<>();
//
//                vector.add(String.valueOf(itemId));
//                vector.add(resultset.getString("name"));
//                vector.add(resultset.getString("qty"));
//                vector.add(resultset.getString("selling_price"));
//
//                double itemTotal = resultset.getDouble("qty") * resultset.getDouble("selling_price");
//
//                vector.add(String.valueOf(itemTotal));
//                
//                model.addRow(vector);
//
//                found = true;
//                
//                itemId++;
//            }
//            
//            if (!found) {
//                model.setRowCount(0);
//            }
//
//            jLabel91.setText("Invoice items (" + String.valueOf(totalItems) + ")");
//
//        } catch (Exception e) {
//            logger.log(Level.WARNING, "jTable1_mouseClick", e);
////            e.printStackTrace();
//        }
//    }
    
    private int currentRowItem = 0;
private static final int ROW_LIMIT_ITEM = 500;

// Add these variables to track the total number of results and the total number of rows in the current result set
private int totalResultsItem = 0;
private int totalRowsItem = 0;

private int getTotalRowCount() {
    int totalRowCount = 0;

    try {
        ResultSet resultSet = MySQL.execute("SELECT COUNT(*) AS total_rows FROM invoice_item");

        if (resultSet.next()) {
            totalRowCount = resultSet.getInt("total_rows");
        }
    } catch (Exception e) {
        // Handle exceptions appropriately
        e.printStackTrace();
    }

    return totalRowCount;
}

private void updateInvoiceItemsLabel() {
    int displayedRows = jTable11.getRowCount();
    int startResult = currentRowItem + 1;
    int endResult = currentRowItem + displayedRows;
    int totalRows = getTotalRowCount();

    jLabel91.setText("Invoice items (" + displayedRows + " of " + totalRows + " total) - Showing results " + startResult + " to " + endResult);
}

private void loadInvoiceItem() {
    int selectedRow = jTable10.getSelectedRow();

    String query = "SELECT * FROM invoice_item INNER JOIN invoice ON invoice_item.invoice_id = invoice.id";

    if (selectedRow != -1) {
        String invoiceID = String.valueOf(jTable10.getValueAt(jTable10.getSelectedRow(), 0));
        query += " WHERE invoice_id='" + invoiceID + "'";
    }

    // Adjust ROW_LIMIT_ITEM to the desired number of rows to load at a time
    query += " LIMIT " + ROW_LIMIT_ITEM + " OFFSET " + currentRowItem;

    try {
        ResultSet resultSet = MySQL.execute(query);

        DefaultTableModel model = (DefaultTableModel) jTable11.getModel();
        model.setRowCount(0);

        int totalItems = 0;
        int itemId = 1;

        boolean found = false;

        while (resultSet.next()) {
            Vector<String> vector = new Vector<>();

            vector.add(String.valueOf(itemId));
            vector.add(resultSet.getString("name"));
            vector.add(resultSet.getString("qty"));
            vector.add(resultSet.getString("selling_price"));

            double itemTotal = resultSet.getDouble("qty") * resultSet.getDouble("selling_price");

            vector.add(String.valueOf(itemTotal));

            model.addRow(vector);

            found = true;

            itemId++;
        }

        totalResultsItem = model.getRowCount(); // Update totalResultsItem
        updateInvoiceItemsLabel(); // Call the new method to update jLabel91

        // Update UI to disable the "Next" button if there are not more than ROW_LIMIT_ITEM rows
        jButton7.setEnabled(totalResultsItem >= ROW_LIMIT_ITEM);

    } catch (Exception e) {
        logger.log(Level.WARNING, "jTable1_mouseClick", e);
        // e.printStackTrace();
    }
}


// Call this method when you want to navigate to the next set of results for invoice items
private void navigateToNextSetItem() {
    currentRowItem += ROW_LIMIT_ITEM;
    loadInvoiceItem();

    // Enable the "Previous" button since navigating to the next set
    jButton6.setEnabled(true);
}

// Call this method when you want to navigate to the previous set of results for invoice items
private void navigateToPreviousSetItem() {
    currentRowItem = Math.max(0, currentRowItem - ROW_LIMIT_ITEM);
    loadInvoiceItem();

    // Enable the "Next" button if there are more results
    jButton7.setEnabled(totalResultsItem > ROW_LIMIT_ITEM);
}

private void loadInitialRows() {
    // Perform a separate query to check the initial number of rows
    try {
        ResultSet resultSet = MySQL.execute("SELECT COUNT(*) AS row_count FROM invoice_item");

        if (resultSet.next()) {
            int rowCount = resultSet.getInt("row_count");

            // Check the number of rows loaded
            if (rowCount > ROW_LIMIT_ITEM) {
                jButton7.setEnabled(true);
            } else {
                jButton7.setEnabled(false);
            }
        }
    } catch (Exception e) {
        // Handle exceptions appropriately
        e.printStackTrace();
    }
}

    
    private void jTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable10MouseClicked
        // TODO add your handling code here:

         loadInvoiceItem();

    }//GEN-LAST:event_jTable10MouseClicked

    private void jFormattedTextField12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField12KeyReleased
        // TODO add your handling code here:
        loadInvoice();
        
    }//GEN-LAST:event_jFormattedTextField12KeyReleased

    private void jButton3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton3FocusGained
        // TODO add your handling code here:

        
    }//GEN-LAST:event_jButton3FocusGained

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        Index grn = new Index();
        grn.setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        loadInvoice();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            int row = jTable10.getSelectedRow();
            
            double netTotal = 0;
            for (int i = 0; i < jTable10.getRowCount(); i++) {
                netTotal += Double.parseDouble(String.valueOf(jTable10.getValueAt(i, 3)));
            }

           //            WE CAN USE ONLY NEDBEANS IDE
            
            
            
//               String userDirectory = FileSystems.getDefault()
//                    .getPath("")
//                    .toAbsolutePath()
//                    .toString();
//
//            
//            
//            String tempUrl = "src/reports/invoice_report.jasper"; // for testing

//            
            

//             WE CAN USE AFTER BUILD

            String userDirectory = FileSystems.getDefault()
                    .getPath("")
                    .toAbsolutePath()
                    .toString();

//            String newpath = userDirectory.substring(0, userDirectory.lastIndexOf("\\"));
//            System.out.println(newpath);

            String url = userDirectory + "\\src\\reports\\invoice_report.jasper";

            java.util.HashMap<String, Object> parameters = new HashMap<>();
     
          
            parameters.put("Parameter5", String.valueOf(netTotal));            
            parameters.put("Parameter7", date);

            JRTableModelDataSource datasource = new JRTableModelDataSource(jTable10.getModel());

            JasperPrint report = JasperFillManager.fillReport(url, parameters, datasource);
//            JasperPrintManager.printReport(report, false); //prirent report dirrectly
            JasperViewer.viewReport(report, false); //for testing

            
            

        } catch (Exception e) {
            logger.log(Level.WARNING, "print_invoice_report_btn", e);
//            e.printStackTrace();
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        // TODO add your handling code here:
        loadInvoice();
        
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        jFormattedTextField12.setText("");
        jTextField1.setText("");
        loadInvoice();
        loadInvoiceItem();
        jTable11.clearSelection();
        jTable10.clearSelection();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        navigateToPreviousSet();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        navigateToNextSet();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        navigateToPreviousSetItem();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        navigateToNextSetItem();
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
//        FlatMacLightLaf.setup();

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new InvoiceHistory().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JFormattedTextField jFormattedTextField12;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel100;
    private javax.swing.JPanel jPanel101;
    private javax.swing.JPanel jPanel103;
    private javax.swing.JPanel jPanel95;
    private javax.swing.JPanel jPanel96;
    private javax.swing.JPanel jPanel97;
    private javax.swing.JPanel jPanel99;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable11;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
