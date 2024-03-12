/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.nio.file.FileSystems;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.print.attribute.standard.MediaTray;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.InvoiceItem;
import model.MySQL;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.util.prefs.Preferences;
import javax.swing.plaf.FontUIResource;

/**
 *
 * @author Ayesh-PC
 */
public class Index extends javax.swing.JFrame {

    public static Logger logger = Logger.getLogger("pos1");

    private HashMap<String, InvoiceItem> invoiceItemMap = new HashMap<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH);

    private final Preferences prefs = Preferences.userNodeForPackage(getClass());
    private final Preferences prefs2 = Preferences.userNodeForPackage(getClass());

    public Index() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        GenerateInvoiceNumber();

        ImageIcon imageIcon = new ImageIcon(Index.class.getResource("/resources/logo.png"));
        this.setIconImage(imageIcon.getImage());

//        setIcon();
        jButton11.setEnabled(false);

        times();

        //logger
        try {
            FileHandler handler = new FileHandler("SenehasaTraders.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        } catch (Exception e) {
            logger.log(Level.WARNING, "logger", e);
        }

        JTableHeader tableHeader = jTable1.getTableHeader();
        Font headerFont = tableHeader.getFont();
        int newSize = 24; // Set the desired font size
        tableHeader.setFont(new Font(headerFont.getName(), Font.BOLD, newSize));

        // Load the saved state of the name checkbox
        boolean isChecked = prefs.getBoolean("checkbox_state", false);
        jCheckBox1.setSelected(isChecked);
        jTextField4.setEnabled(isChecked);
//        jTextField4.grabFocus();

        // Load the saved state of the qty checkbox
        boolean isChecked2 = prefs2.getBoolean("checkbox2_state", false);
        jCheckBox2.setSelected(isChecked2);
        jTextField1.setEnabled(isChecked2);
        if (isChecked) {
            jTextField4.grabFocus();
            if (!isChecked2) {
                jTextField1.setText("1");
            }
        } else if (isChecked2) {
            jTextField1.grabFocus();
        } else {
            jTextField1.setText("1");
            jTextField5.grabFocus();
        }

    }

    private void setIcon() {
        ImageIcon imageIcon = new ImageIcon(Index.class.getResource("/resources/logo.png"));
        this.setIconImage(imageIcon.getImage());
        setTableHeaderFontSize(jTable1, 16);
    }

    // time
    Timer t;
    SimpleDateFormat st;

    public void times() {

        t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

                Date dt = new Date();
                st = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a");

                String tt = st.format(dt);
                jLabel24.setText(tt);

            }
        });

        t.start();

    }

    private String invoiceNumber;

    private void GenerateInvoiceNumber() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
        LocalDateTime now = LocalDateTime.MAX.now();

        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("MM");
        LocalDateTime now1 = LocalDateTime.MAX.now();

        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd");
        LocalDateTime now2 = LocalDateTime.MAX.now();

        DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("HH");
        LocalDateTime now3 = LocalDateTime.MAX.now();

        DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("mm");
        LocalDateTime now4 = LocalDateTime.MAX.now();

        DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("ss");
        LocalDateTime now5 = LocalDateTime.MAX.now();

        DateTimeFormatter dtf6 = DateTimeFormatter.ofPattern("SSS");
        LocalDateTime now6 = LocalDateTime.MAX.now();

        String date = dtf.format(now);
        String date2 = dtf1.format(now1);
        String date3 = dtf2.format(now2);
        String date4 = dtf3.format(now3);
        String date5 = dtf4.format(now4);
        String date6 = dtf5.format(now5);
        String date7 = dtf6.format(now6);

        String date1 = date.substring(2);

        String curdate = "SINV-" + date1 + date2 + date3 + date4 + date5 + date6 + date7;
        String roundedTime = curdate.substring(0, curdate.length() - 2);
        jTextField2.setText(roundedTime);
        invoiceNumber = roundedTime;
    }

    private void setTableHeaderFontSize(JTable table, int value) {
        JTableHeader header = table.getTableHeader();
        header.setFont(new java.awt.Font("Segoe UI Semibold", 0, value));
    }

    private void loadInvoiceItem() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        double total = 0;
        int count = 1;
        for (InvoiceItem grn : invoiceItemMap.values()) {

            Vector<String> vector = new Vector<>();

            vector.add(String.valueOf(count));

            if (grn.getName().equals("Item")) {
                vector.add("Item_" + count);

            } else {

                vector.add(grn.getName());
            }
            count++;

            vector.add(grn.getQty());
            vector.add(grn.getSellingPrice());

            double itemTotal = Double.parseDouble(grn.getSellingPrice()) * Double.parseDouble(grn.getQty());
            vector.add(String.valueOf(itemTotal));
            vector.add(grn.getItemId());

            total += itemTotal;

            model.addRow(vector);
            calculate();
        }

        jLabel8.setText(String.valueOf(total));
        this.total = total;

    }

    private double total = 0;
    private double discount = 0;
    private double payment = 0;
    private double balance = 0;
    private boolean paymentEntered = false;

    private void calculate() {
        boolean inputError = false;
        if (!jTextField6.getText().matches("\\d+(\\.\\d+)?")) {
            if (jTextField6.getText().equals("")) {
                paymentEntered = false;
                jTextField6.setForeground(Color.BLACK);
            } else {
                inputError = true;
                jTextField6.setForeground(Color.RED);
            }
            paymentEntered = false;
        } else {
            paymentEntered = true;
            jTextField6.setForeground(Color.BLACK);

        }

        if (!jTextField7.getText().matches("\\d+(\\.\\d+)?")) {
            if (jTextField7.getText().equals("")) {
                this.discount = 0;
                jTextField7.setForeground(Color.BLACK);
            } else {
                inputError = true;
                jTextField7.setForeground(Color.RED);
            }
        } else {
            this.discount = Double.parseDouble(jTextField7.getText());
            jTextField7.setForeground(Color.BLACK);
        }

        if (inputError) {
            JOptionPane.showMessageDialog(this, "Invalid payment amount", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        if (balance < 0) {
            jButton11.setEnabled(false);
        } else if (invoiceItemMap.size() == 0) {
            jButton11.setEnabled(false);
        } else {
            jButton11.setEnabled(true);
        }

        double total = this.total;

        total -= discount;

        if (total < 0) {
            //discount error
        } else {

            if (total != 0) {
                jTextField6.setEditable(true);
                if (paymentEntered) {
                    this.payment = Double.parseDouble(jTextField6.getText());
                } else {
                    this.payment = 0;
                }

            }
            balance = payment - total;
            jLabel13.setText(String.valueOf(total));
        }

        jLabel14.setText(String.valueOf(balance));
        if (balance < 0) {
            jButton11.setEnabled(false);
        } else if (invoiceItemMap.size() == 0) {
            jButton11.setEnabled(false);
        } else {
            jButton11.setEnabled(true);
        }

    }

    private void clearProductEntry() {
        jTextField3.setText("");
        jTextField4.setText("");

        if (jCheckBox1.isSelected()) {
            jTextField4.grabFocus();
            if (jCheckBox2.isSelected()) {
                jTextField1.setText("");
            }

        } else if (jCheckBox2.isSelected()) {
            jTextField1.grabFocus();
            jTextField1.setText("");
        } else {
            jTextField5.grabFocus();
        }

        jTextField5.setText("");
    }

    private void resetInvoice() {

        clearProductEntry();
        jTextField7.setText("");
        jTextField6.setText("");
        jLabel8.setText("0.00");
        jLabel14.setText("0.00");
        jLabel13.setText("0.00");
        jLabel22.setText("Invoice items (0)");

        GenerateInvoiceNumber();
        invoiceItemMap.clear();
        loadInvoiceItem();
    }

    private void addToInvoice() {
        int min = 1000;
        int max = 5000;

        int itemId = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String gram = jTextField3.getText();
        String name = jTextField4.getText();
        String qty = jTextField1.getText();
        String sellingPrice = jTextField5.getText();

//        if (name.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please enter product name", "Warning", JOptionPane.WARNING_MESSAGE);
//        } else if (name.length() > 45) {
//            JOptionPane.showMessageDialog(this, "Name length cannot exceed 45 characters", "Warning", JOptionPane.WARNING_MESSAGE);
//        } else 
        if (qty.isEmpty()) {
            jTextField1.grabFocus();
            JOptionPane.showMessageDialog(this, "Please enter quantity", "Warning", JOptionPane.WARNING_MESSAGE);       
        }else if (!isNumeric(qty)) {
            jTextField1.grabFocus();
            JOptionPane.showMessageDialog(null, "Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (Double.parseDouble(qty) <= 0) {
            jTextField1.grabFocus();
            JOptionPane.showMessageDialog(this, "Please enter valid quantity", "Warning", JOptionPane.WARNING_MESSAGE);           
        } else if (sellingPrice.isEmpty()) {
            jTextField5.grabFocus();
            JOptionPane.showMessageDialog(this, "Please enter selling price", "Warning", JOptionPane.WARNING_MESSAGE);      
        }else if (!isNumeric(sellingPrice)) {
            jTextField5.grabFocus();
            JOptionPane.showMessageDialog(null, "Please enter a valid selling price.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (Double.parseDouble(sellingPrice) <= 0) {
            jTextField5.grabFocus();
            JOptionPane.showMessageDialog(this, "Please enter valid selling price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            if (name.trim().isEmpty()) {
                name = "Item";
//                count = count + 1;
            }
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setItemId(String.valueOf(itemId));
            invoiceItem.setName(name + gram);
            invoiceItem.setQty(qty);
            invoiceItem.setSellingPrice(sellingPrice);

            // Check if an item with the same itemId or the same sellingPrice already exists
            InvoiceItem existingItemName = findItemBySellingPrice(invoiceItemMap, invoiceItem.getName());

            if (existingItemName != null) {

                if (existingItemName.getSellingPrice().equals(invoiceItem.getSellingPrice())) {
                    int response = JOptionPane.showConfirmDialog(
                            this,
                            "Do you want to update the quantity of product: " + existingItemName.getName(),
                            "Warning",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (response == JOptionPane.YES_OPTION) {
                        double newQty = Double.parseDouble(existingItemName.getQty()) + Double.parseDouble(invoiceItem.getQty());
                        existingItemName.setQty(String.valueOf(newQty));
                    }
                } else {
                    int response2 = JOptionPane.showConfirmDialog(this, "You have already added this product under different selling price, Do you want to add continue?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (response2 == JOptionPane.YES_OPTION) {
                        invoiceItemMap.put(String.valueOf(itemId), invoiceItem);
                    }
                }
            } else {

                invoiceItemMap.put(String.valueOf(itemId), invoiceItem);
            }

            jLabel22.setText("Invoice items (" + invoiceItemMap.size() + ")");
            loadInvoiceItem();
            jButton11.setEnabled(true);
            calculate();
            clearProductEntry();

        }
    }
    
    private static boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void printInvoice() {
        Date date = new Date();
        String paymemt = jTextField6.getText();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//      
        String discount = jTextField7.getText();

        if (discount.isEmpty()) {
            discount = "0.00";
        }

        try {

            MySQL.execute("INSERT INTO `invoice` (`id`,"
                    + "`date_time`,"
                    + "`paid_amount`,`discount` ) "
                    + " VALUES ('" + jTextField2.getText() + "','" + dateTime + "',"
                    + "'" + paymemt + "','" + discount + "')");

//            System.out.println("invoice success");
            for (InvoiceItem item : invoiceItemMap.values()) {

                MySQL.execute("INSERT INTO `invoice_item` (`qty`,`name`,`selling_price`,`invoice_id`) VALUES "
                        + "('" + item.getQty() + "','" + item.getName() + "','" + item.getSellingPrice() + "', '" + invoiceNumber + "')");

            }

            //            WE CAN USE ONLY NEDBEANS IDE
//            
//            
//            String userDirectory = FileSystems.getDefault()
//                    .getPath("")
//                    .toAbsolutePath()
//                    .toString();
//
//            String url = userDirectory + "\\src\\reports\\invoice.jasper";

            String tempUrl = "src/reports/invoice.jasper";
            
//             WE CAN USE AFTER BUILD

            String userDirectory = FileSystems.getDefault()
                    .getPath("")
                    .toAbsolutePath()
                    .toString();
            String newpath = userDirectory.substring(0, userDirectory.lastIndexOf("\\"));
//               System.out.println(newpath);
            String url = newpath + "\\src\\reports\\invoice.jasper";

            String date1 = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String time = new SimpleDateFormat("hh:mm a").format(date);

            java.util.HashMap<String, Object> parameters = new HashMap<>();

            parameters.put("Parameter3", time);
            parameters.put("Parameter1", date1);
            parameters.put("Parameter2", invoiceNumber);
            parameters.put("Parameter4", jLabel8.getText() + "0");
            parameters.put("Parameter5", paymemt);
            parameters.put("Parameter6", jLabel14.getText() + "0");
            parameters.put("Parameter7", discount);

            JRTableModelDataSource datasource = new JRTableModelDataSource(jTable1.getModel());

            JasperPrint report = JasperFillManager.fillReport(url, parameters, datasource);
            JasperPrintManager.printReport(report, false); //print report dirrectly
//            JasperViewer.viewReport(report, false);
//
            resetInvoice();
            GenerateInvoiceNumber();

        } catch (Exception e) {
            logger.log(Level.WARNING, "Index", e);
//                e.printStackTrace();
        }

    }

    private void clearInvoice() {
        int response = JOptionPane.showConfirmDialog(this, "Do you want to clear the invoice?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            resetInvoice();
            jButton11.setEnabled(false);
        }
    }
    private boolean added = false;

    private void mannualTab(KeyEvent evt, int order) {
//        System.out.println(evt.getKeyCode());
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            switch (order) {
                case 1:
                    if (jTextField1.isEnabled()) {
                        jTextField1.grabFocus();
                    } else {
                        jTextField5.grabFocus();
                    }
                    break;
                case 2:
                    jTextField5.grabFocus();
                    break;
                case 3:
                    jTextField6.grabFocus();
                    break;
                case 4:
                    jTextField7.grabFocus();
                    break;
                case 5:
                    jTextField6.grabFocus();
                    break;

            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            switch (order) {
                case 1:
                    jTextField6.grabFocus();
                    break;
                case 2:
                    if (jTextField4.isEnabled()) {
                        jTextField4.grabFocus();
                    } else {
                        jTextField6.grabFocus();
                    }
                    break;
                case 3:
                    if (jTextField1.isEnabled()) {
                        jTextField1.grabFocus();
                    } else {
                        if (jTextField4.isEnabled()) {
                           jTextField4.grabFocus();
                        } else {
                            jTextField6.grabFocus();
                        }
                        
                    }
                    break;
                case 4:
                    jTextField5.grabFocus();
                    break;
                case 5:
                    jTextField5.grabFocus();
                    break;
                case 6:
                    jTextField7.grabFocus();
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            switch (order) {
                case 3:
                    addToInvoice();
                    break;
                case 6: ;
                    if (jButton11.isEnabled()) {
                        printInvoice();
                    }
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_CONTROL) {
            switch (order) {
                case 1:
                    clearInvoice();
                    break;
                case 2:
                    clearInvoice();
                    break;
                case 3:
                    clearInvoice();
                    break;
                case 4:
                    clearInvoice();
                    break;
                case 5:
                    clearInvoice();
                    break;
                case 6:
                    clearInvoice();
                    break;

            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            switch (order) {
                case 1:
                    jTextField3.setText("");
                    jTextField4.setText("");
                    added = false;
                    break;
                case 2:
                    jTextField1.setText("");
                    break;
                case 3:
                    jTextField5.setText("");
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ALT) {
            switch (order) {
                case 1:
                    if (!added) {
                        jTextField3.setText("g");
                        added = true;
                    } else {
                        jTextField3.setText("");
                        added = false;
                    }

            }
        }
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
        jPanel29 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jButton15 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel34 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1281, 740));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 10, 10));
        jPanel1.setMaximumSize(new java.awt.Dimension(1281, 740));
        jPanel1.setMinimumSize(new java.awt.Dimension(1281, 740));
        jPanel1.setPreferredSize(new java.awt.Dimension(1281, 740));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 10));

        jPanel29.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel22.setLayout(new java.awt.BorderLayout(0, 5));

        jTable1.setFont(new java.awt.Font("Iskoola Pota", 0, 24)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Name", "Quantity", "Selling price", "Total", "id"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(32);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(5).setMinWidth(5);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(5);
            jTable1.getColumnModel().getColumn(5).setMaxWidth(5);
        }

        jPanel22.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jLabel22.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel22.setText("Invoice items (0)");
        jPanel22.add(jLabel22, java.awt.BorderLayout.NORTH);

        jPanel29.add(jPanel22, java.awt.BorderLayout.CENTER);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(153, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 0), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        jPanel4.setPreferredSize(new java.awt.Dimension(385, 350));
        jPanel4.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel8.setOpaque(false);
        jPanel8.setLayout(new java.awt.BorderLayout());

        jLabel7.setBackground(new java.awt.Color(153, 204, 255));
        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel7.setText("Total");
        jLabel7.setOpaque(true);
        jPanel8.add(jLabel7, java.awt.BorderLayout.CENTER);

        jLabel8.setBackground(new java.awt.Color(153, 204, 255));
        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("0.0");
        jLabel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel8.setOpaque(true);
        jLabel8.setPreferredSize(new java.awt.Dimension(200, 25));
        jPanel8.add(jLabel8, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel8);

        jPanel12.setOpaque(false);
        jPanel12.setLayout(new java.awt.BorderLayout());

        jLabel9.setBackground(new java.awt.Color(153, 204, 255));
        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel9.setText("Discount");
        jLabel9.setMaximumSize(new java.awt.Dimension(42, 25));
        jLabel9.setMinimumSize(new java.awt.Dimension(42, 25));
        jLabel9.setOpaque(true);
        jLabel9.setPreferredSize(new java.awt.Dimension(30, 25));
        jPanel12.add(jLabel9, java.awt.BorderLayout.CENTER);

        jTextField7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextField7.setMinimumSize(new java.awt.Dimension(40, 29));
        jTextField7.setPreferredSize(new java.awt.Dimension(200, 31));
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField7KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
        });
        jPanel12.add(jTextField7, java.awt.BorderLayout.LINE_END);

        jPanel4.add(jPanel12);

        jPanel13.setOpaque(false);
        jPanel13.setLayout(new java.awt.BorderLayout());

        jLabel10.setBackground(new java.awt.Color(153, 204, 255));
        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel10.setText("Total payable");
        jLabel10.setOpaque(true);
        jPanel13.add(jLabel10, java.awt.BorderLayout.CENTER);

        jLabel13.setBackground(new java.awt.Color(153, 204, 255));
        jLabel13.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("0.0");
        jLabel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        jLabel13.setOpaque(true);
        jLabel13.setPreferredSize(new java.awt.Dimension(200, 16));
        jPanel13.add(jLabel13, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel13);

        jPanel14.setOpaque(false);
        jPanel14.setLayout(new java.awt.BorderLayout());

        jLabel11.setBackground(new java.awt.Color(153, 204, 255));
        jLabel11.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel11.setText("Payment");
        jLabel11.setOpaque(true);
        jPanel14.add(jLabel11, java.awt.BorderLayout.CENTER);

        jTextField6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextField6.setPreferredSize(new java.awt.Dimension(200, 31));
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField6KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });
        jPanel14.add(jTextField6, java.awt.BorderLayout.LINE_END);

        jPanel4.add(jPanel14);

        jPanel15.setOpaque(false);
        jPanel15.setLayout(new java.awt.BorderLayout());

        jLabel12.setBackground(new java.awt.Color(153, 204, 255));
        jLabel12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel12.setText("Balance");
        jLabel12.setOpaque(true);
        jPanel15.add(jLabel12, java.awt.BorderLayout.CENTER);

        jLabel14.setBackground(new java.awt.Color(153, 204, 255));
        jLabel14.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("0.0");
        jLabel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        jLabel14.setOpaque(true);
        jLabel14.setPreferredSize(new java.awt.Dimension(200, 16));
        jPanel15.add(jLabel14, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel15);

        jPanel17.setOpaque(false);
        jPanel17.setLayout(new java.awt.BorderLayout());

        jButton11.setBackground(new java.awt.Color(0, 153, 255));
        jButton11.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Print invoice");
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setPreferredSize(new java.awt.Dimension(200, 40));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel17.add(jButton11, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel17);

        jPanel5.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanel31.setPreferredSize(new java.awt.Dimension(199, 100));
        jPanel31.setLayout(new java.awt.GridLayout(2, 0, 0, 5));

        jButton15.setBackground(new java.awt.Color(0, 153, 255));
        jButton15.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText("Add to invoice");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jButton15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton15KeyPressed(evt);
            }
        });
        jPanel31.add(jButton15);

        jButton14.setBackground(new java.awt.Color(0, 153, 255));
        jButton14.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText("Clear invoice");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel31.add(jButton14);

        jPanel5.add(jPanel31, java.awt.BorderLayout.NORTH);

        jPanel29.add(jPanel5, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel29, java.awt.BorderLayout.CENTER);

        jPanel30.setPreferredSize(new java.awt.Dimension(1730, 240));
        jPanel30.setLayout(new java.awt.BorderLayout(0, 10));

        jPanel21.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
        jPanel21.setLayout(new java.awt.BorderLayout(40, 0));

        jPanel9.setPreferredSize(new java.awt.Dimension(500, 169));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel35.setPreferredSize(new java.awt.Dimension(620, 35));

        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Name Field");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Quantity Field");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(204, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel35, java.awt.BorderLayout.NORTH);

        jPanel34.setLayout(new java.awt.GridLayout(2, 0, 0, 5));

        jPanel16.setLayout(new java.awt.BorderLayout());

        jPanel11.setLayout(new java.awt.BorderLayout(5, 0));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel20.setText("<html> <p>(Kg/L)</p> <html>");
        jLabel20.setPreferredSize(new java.awt.Dimension(80, 0));
        jPanel11.add(jLabel20, java.awt.BorderLayout.EAST);

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        jPanel11.add(jTextField1, java.awt.BorderLayout.CENTER);

        jPanel16.add(jPanel11, java.awt.BorderLayout.CENTER);

        jButton12.setBackground(new java.awt.Color(0, 153, 255));
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/reload1.png"))); // NOI18N
        jButton12.setPreferredSize(new java.awt.Dimension(55, 40));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel16.add(jButton12, java.awt.BorderLayout.EAST);

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel21.setText("<html> <p>Quantity</p> <html>");
        jLabel21.setPreferredSize(new java.awt.Dimension(150, 0));
        jPanel16.add(jLabel21, java.awt.BorderLayout.WEST);

        jPanel34.add(jPanel16);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel19.setText("<html> <p>Selling Price</p> <html>");
        jLabel19.setPreferredSize(new java.awt.Dimension(150, 0));
        jPanel10.add(jLabel19, java.awt.BorderLayout.WEST);

        jPanel7.setLayout(new java.awt.BorderLayout(10, 0));

        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField5KeyPressed(evt);
            }
        });
        jPanel7.add(jTextField5, java.awt.BorderLayout.CENTER);

        jButton13.setBackground(new java.awt.Color(0, 153, 255));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/reload1.png"))); // NOI18N
        jButton13.setPreferredSize(new java.awt.Dimension(55, 40));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton13, java.awt.BorderLayout.EAST);

        jPanel10.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel34.add(jPanel10);

        jPanel9.add(jPanel34, java.awt.BorderLayout.CENTER);

        jPanel36.setPreferredSize(new java.awt.Dimension(0, 15));

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel36, java.awt.BorderLayout.SOUTH);

        jPanel21.add(jPanel9, java.awt.BorderLayout.EAST);

        jPanel37.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(600, 310));
        jPanel2.setLayout(new java.awt.GridLayout(3, 0, 0, 5));

        jPanel6.setLayout(new java.awt.BorderLayout());

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextField2.setPreferredSize(new java.awt.Dimension(220, 22));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField2, java.awt.BorderLayout.CENTER);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel17.setText("<html> <p>Invoice number</p> <html>");
        jLabel17.setPreferredSize(new java.awt.Dimension(180, 0));
        jPanel6.add(jLabel17, java.awt.BorderLayout.WEST);

        jPanel2.add(jPanel6);

        jPanel19.setLayout(new java.awt.BorderLayout(5, 0));

        jTextField4.setFont(new java.awt.Font("Iskoola Pota", 0, 24)); // NOI18N
        jTextField4.setPreferredSize(new java.awt.Dimension(220, 22));
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField4KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });
        jPanel19.add(jTextField4, java.awt.BorderLayout.CENTER);

        jPanel18.setPreferredSize(new java.awt.Dimension(115, 100));
        jPanel18.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        jTextField3.setEditable(false);
        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextField3.setPreferredSize(new java.awt.Dimension(220, 22));
        jPanel18.add(jTextField3);

        jButton10.setBackground(new java.awt.Color(0, 153, 255));
        jButton10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/reload1.png"))); // NOI18N
        jButton10.setPreferredSize(new java.awt.Dimension(81, 40));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel18.add(jButton10);

        jPanel19.add(jPanel18, java.awt.BorderLayout.EAST);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel18.setText("<html> <p>Name</p> <html>");
        jLabel18.setPreferredSize(new java.awt.Dimension(175, 0));
        jPanel19.add(jLabel18, java.awt.BorderLayout.WEST);

        jPanel2.add(jPanel19);

        jPanel20.setLayout(new java.awt.BorderLayout());

        jButton1.setBackground(new java.awt.Color(0, 153, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add g");
        jButton1.setPreferredSize(new java.awt.Dimension(300, 38));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel20.add(jButton1, java.awt.BorderLayout.CENTER);

        jPanel32.setPreferredSize(new java.awt.Dimension(180, 51));

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        jPanel20.add(jPanel32, java.awt.BorderLayout.WEST);

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        jPanel20.add(jPanel33, java.awt.BorderLayout.LINE_END);

        jPanel2.add(jPanel20);

        jPanel37.add(jPanel2, java.awt.BorderLayout.EAST);

        jPanel21.add(jPanel37, java.awt.BorderLayout.CENTER);

        jPanel30.add(jPanel21, java.awt.BorderLayout.CENTER);

        jPanel28.setLayout(new java.awt.BorderLayout());

        jPanel26.setPreferredSize(new java.awt.Dimension(500, 80));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jLabel24.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("<html> <p style=\"text-align: center;\">Thursday, 21 September 2023 00:00:00 AM</p> <html>");
        jPanel26.add(jLabel24, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.BorderLayout(10, 0));

        jPanel23.setLayout(new java.awt.GridLayout(2, 0));

        jLabel26.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jLabel26.setText("<html> <p style=\"text-align: center;\">nebulainfinite.com</p> <html>");
        jPanel23.add(jLabel26);

        jLabel27.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel27.setText("<html> <p style=\"text-align: center;\">+94783 233 760</p> <html>");
        jPanel23.add(jLabel27);

        jPanel3.add(jPanel23, java.awt.BorderLayout.CENTER);

        jPanel25.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel24.setLayout(new java.awt.BorderLayout());

        jLabel25.setFont(new java.awt.Font("Segoe UI Semibold", 0, 25)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 153, 255));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo_1.png"))); // NOI18N
        jPanel24.add(jLabel25, java.awt.BorderLayout.WEST);

        jLabel16.setFont(new java.awt.Font("Segoe UI Semibold", 0, 25)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 153, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("<html> <p style=\"text-align: center;\">Nebula Infinite</p> <html>");
        jPanel24.add(jLabel16, java.awt.BorderLayout.CENTER);

        jPanel25.add(jPanel24, java.awt.BorderLayout.CENTER);

        jLabel15.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("<html> <p style=\"text-align: center;\">Developed By :</p> <html>");
        jPanel25.add(jLabel15, java.awt.BorderLayout.WEST);

        jPanel3.add(jPanel25, java.awt.BorderLayout.WEST);

        jPanel26.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel28.add(jPanel26, java.awt.BorderLayout.WEST);

        jPanel27.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanel27.setLayout(new java.awt.BorderLayout());

        jButton7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/grnHis1.png"))); // NOI18N
        jButton7.setText("Grn History");
        jButton7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1));
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton7.setIconTextGap(10);
        jButton7.setPreferredSize(new java.awt.Dimension(200, 37));
        jButton7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButton7FocusGained(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel27.add(jButton7, java.awt.BorderLayout.EAST);

        jLabel23.setFont(new java.awt.Font("Segoe UI Semibold", 0, 48)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 153, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("<html> <p style=\"text-align: center;\">Senehasa Traders</p> <html>");
        jLabel23.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel23.setAutoscrolls(true);
        jPanel27.add(jLabel23, java.awt.BorderLayout.CENTER);

        jPanel28.add(jPanel27, java.awt.BorderLayout.CENTER);

        jPanel30.add(jPanel28, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel30, java.awt.BorderLayout.NORTH);

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

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        printInvoice();

    }//GEN-LAST:event_jButton11ActionPerformed

    public static int itemId = 1;
    public static int count = 1;

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed

        addToInvoice();

    }//GEN-LAST:event_jButton15ActionPerformed

    private InvoiceItem findItemBySellingPrice(HashMap<String, InvoiceItem> invoiceItemMap, String name) {
        for (InvoiceItem item : invoiceItemMap.values()) {

            if (!item.getName().equals("Item")) {
                if (item.getName().equals(name)) {
                    return item;
                }
            }

        }
        return null; // Not found
    }

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:

        int response = JOptionPane.showConfirmDialog(this, "Do you want to clear the invoice?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            resetInvoice();
            jButton11.setEnabled(false);
        }

    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField4.grabFocus();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        jTextField1.setText("");
        jTextField1.grabFocus();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        jTextField5.setText("");
        jTextField5.grabFocus();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton7FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton7FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7FocusGained

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

        InvoiceHistory grn = new InvoiceHistory();
        grn.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        int row = jTable1.getSelectedRow();

        if (evt.getClickCount() == 2) {

            long response = JOptionPane.showConfirmDialog(this, "Do you want to remove this product?", "Conformation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {

                invoiceItemMap.remove(String.valueOf(jTable1.getValueAt(row, 5)));
                jLabel22.setText("Invoice items (" + invoiceItemMap.size() + ")");
                loadInvoiceItem();
                calculate();

            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        jTextField3.setText("g");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyPressed
        // TODO add your handling code here:
        mannualTab(evt, 1);
    }//GEN-LAST:event_jTextField4KeyPressed

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped

    }//GEN-LAST:event_jTextField4KeyTyped

    private void jButton15KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton15KeyPressed
        // TODO add your handling code here:
        mannualTab(evt, 4);
    }//GEN-LAST:event_jButton15KeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:
        mannualTab(evt, 2);
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyPressed
        // TODO add your handling code here:
        mannualTab(evt, 3);
    }//GEN-LAST:event_jTextField5KeyPressed

    private void jTextField7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyPressed
        // TODO add your handling code here:
        mannualTab(evt, 5);
    }//GEN-LAST:event_jTextField7KeyPressed

    private void jTextField6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyPressed
        // TODO add your handling code here:
        mannualTab(evt, 6);

    }//GEN-LAST:event_jTextField6KeyPressed

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        // TODO add your handling code here:
        calculate();
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // TODO add your handling code here:
        calculate();
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:

        // Update the enabled state of the JTextField
        boolean isChecked = jCheckBox1.isSelected();
        jTextField4.setEnabled(isChecked);
        if (isChecked) {
            jTextField4.grabFocus();
            if (!jCheckBox2.isSelected()) {
                jTextField1.setEnabled(false);
            }
        } else {
            if (jCheckBox2.isSelected()) {
                jTextField1.grabFocus();
                jTextField5.setText("");
            } else {
                jTextField5.grabFocus();
            }
        }

        // Save the state of the checkbox
        prefs.putBoolean("checkbox_state", isChecked);
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:

        // Update the enabled state of the JTextField
        boolean isChecked = jCheckBox2.isSelected();
        jTextField1.setEnabled(isChecked);
        if (isChecked) {
            jTextField1.setText("");
            if (jCheckBox1.isSelected()) {
                jTextField4.grabFocus();
            } else {
                jTextField1.grabFocus();
            }
        } else {
            jTextField1.setText("1");
            jTextField5.grabFocus();
        }

        // Save the state of the checkbox
        prefs2.putBoolean("checkbox2_state", isChecked);
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatMacLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Index().setVisible(true);
            }
        });

        Font font = new Font("Iskoola Pota", Font.PLAIN, 14);

        UIManager.put("OptionPane.messageFont", font);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
