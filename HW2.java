//Areej Afzaal
//l174189@lhr.nu.edu.pk
//AP-6A
//This program is revolves around the concepts of GUI, Action Listeners and Database Connectivity
/*This program creates an address book that uses a GUI to interact with the user. The user can add, delete, update and search
(by name or cnic). The program is connectced at the nbackend with a sql database*/

package hw2;

import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;


public class HW2 extends JFrame implements ActionListener{

    boolean addFlag = false, updateFlag = false, deleteFlag = false, searchNameFlag = false, searchCnicFlag = false;
    JButton add, delete, update, searchName, searchCNIC, enterButtonTemp, enterButtonTemp1, enterButton, enterButton1, enterButton2, enterButton3, enterButton4;
    JLabel label1, addLabel, deleteLabel, snLabel, cnicLabel, updateLabel, cnic, nameLabel, addressLabel, numLabel;
    JPanel p, p1;
    JTextField nameText, cnicText, addressText, numText;
    Border thickBorder;
    static JFrame f = new JFrame("Address Book"), dialogFrame = new JFrame("Message");


    //designs the main screen of the address book
    public void initMethod() {

        //making the label components
        label1=new JLabel("******ADDRESS BOOK******");
        addLabel = new JLabel("Add Record");
        deleteLabel = new JLabel("Delete Record");
        updateLabel = new JLabel("Update Record");
        snLabel = new JLabel("Search by Name");
        cnicLabel = new JLabel("Search by CNIC");

        //making the button components
        add=new JButton("Add");
        delete=new JButton("Delete");
        update = new JButton("Update");
        searchName = new JButton("Search");
        searchCNIC = new JButton("Search");

        //attaching the action listeners of the buttons
        add.addActionListener(this);
        delete.addActionListener(this);
        update.addActionListener(this);
        searchName.addActionListener(this);
        searchCNIC.addActionListener(this);

        //Get the window toolkit
        Toolkit theKit = f.getToolkit();

        //Get the screen size and set window size to half and the position to approximately center of the screen
        Dimension wndSize = theKit.getScreenSize();
        System.out.println(wndSize.height + " " + wndSize.width);
        f.setBounds(wndSize.width/3, wndSize.height/3, wndSize.width/2, wndSize.height/2);  // Position and Size


        //**************Creating 2 JPanels using GridLayout*******************//
        //One to set the labels on the right side and the other to set buttons on the left side

        //Creating first JPanel (for labels) and adding the labels to it
        p = new JPanel(new GridLayout(5,1,2,2));

        p.add(addLabel);
        p.add(deleteLabel);
        p.add(updateLabel);
        p.add(snLabel);
        p.add(cnicLabel);

        //setting the colour of the text labels
        addLabel.setForeground(Color.GRAY);
        deleteLabel.setForeground(Color.GRAY);
        updateLabel.setForeground(Color.GRAY);
        snLabel.setForeground(Color.GRAY);
        cnicLabel.setForeground(Color.GRAY);

        //setting the font style and font size of the text label
        Font font1 = new Font(Font.MONOSPACED, Font.BOLD , 18);
        label1.setFont(font1);
        addLabel.setFont(font1);
        deleteLabel.setFont(font1);
        updateLabel.setFont(font1);
        snLabel.setFont(font1);
        cnicLabel.setFont(font1);

        //setting a custom border around the title of the frame
        Border titleBorder = new MatteBorder(new Insets(10, 4, 10, 4), new ImageIcon("C:\\Users\\Dell\\OneDrive\\Documents\\NetBeansProjects\\HW2\\src\\hw2\\bg.png"));
        label1.setBorder(titleBorder);

        //Creating the second JPanel (for buttons) and addinng buttons to it
        p1 = new JPanel(new GridLayout(5, 5, 1, 2));
        p1.add(add);
        p1.add(delete);
        p1.add(update);
        p1.add(searchName);
        p1.add(searchCNIC);

        //setting the font style and font size for the text on the buttonss
        Font font2 = new Font(Font.SERIF, Font.PLAIN ,16);
        add.setFont(font2);
        delete.setFont(font2);
        update.setFont(font2);
        searchName.setFont(font2);
        searchCNIC.setFont(font2);

        //setting the border of the buttons
        thickBorder = new LineBorder(Color.PINK , 2);
        add.setBorder(thickBorder);
        delete.setBorder(thickBorder);
        update.setBorder(thickBorder);
        searchName.setBorder(thickBorder);
        searchCNIC.setBorder(thickBorder);

        //************Creating a BorderLayout and adding both the grid layout and title to it***************//
        Container con=f.getContentPane();
        con.setLayout(new BorderLayout());

        //adding the title to the top
        con.add(label1,BorderLayout.NORTH);
        //adding he labels to the left and buttons to right
        con.add(p, BorderLayout.WEST);
        con.add(p1,BorderLayout.EAST);

        //setting some other properties of the frame
        f.setSize(300,300);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setResizable(false);
    }


    //a function to set JFormattedText field (cnic and phone number) to take input in a specific way
    protected MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }


    //Called in init method of add and update. It displays all the four fields necessary to add or update.
    public JButton showFullPanel(){

        //cretaing a container and setting its layout to flow layout
        Container con =getContentPane();
        con.removeAll();
        con.setLayout(new FlowLayout());

        //creating text fields and formatted text fields
        cnicText = new JFormattedTextField(createFormatter("#####-#######-#"));
        cnicText.setColumns(15);
        nameText = new JTextField(15);
        addressText = new JTextField(15);
        numText = new JFormattedTextField(createFormatter("#### #######"));
        numText.setColumns(15);

        //implementing the text listeners for all the text fields in this method
        cnicText.getDocument().addDocumentListener(new DocumentListener() {
            
            //if the boxes text fields are in any of the following states than changed() method is called
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            //checks if any of the field is empty keeps the button disabled else enables it
            public void changed() {

                if (cnicText.getText().equals("     -       - ") || nameText.getText().equals("")
                        ||addressText.getText().equals("") || numText.getText().equals("            ")) {
                    
                    //checking addFlag if called in initAdd
                    if(addFlag)
                        enterButton.setEnabled(false);
                    //if called in initUpdate
                    else
                        enterButton1.setEnabled(false);
                }
                else {
                    if(addFlag)
                        enterButton.setEnabled(true);
                    else
                        enterButton1.setEnabled(true);
                }
            }
        });

        nameText.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {

                if (cnicText.getText().equals("     -       - ") || nameText.getText().equals("")
                        ||addressText.getText().equals("") || numText.getText().equals("            ")) {

                    if(addFlag)
                        enterButton.setEnabled(false);
                    else
                        enterButton1.setEnabled(false);
                }
                else {

                    if(addFlag)
                        enterButton.setEnabled(true);
                    else
                        enterButton1.setEnabled(true);
                }
            }
        });

        addressText.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {

                if (cnicText.getText().equals("     -       - ") || nameText.getText().equals("")
                        ||addressText.getText().equals("") || numText.getText().equals("            ")) {

                    if(addFlag)
                        enterButton.setEnabled(false);
                    else
                        enterButton1.setEnabled(false);
                }
                else {

                    if(addFlag)
                        enterButton.setEnabled(true);
                    else
                        enterButton1.setEnabled(true);
                }
            }

        });

        numText.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {

                if (cnicText.getText().equals("     -       - ") || nameText.getText().equals("")
                        ||addressText.getText().equals("") || numText.getText().equals("            ")) {
                    //System.out.println("Phone Flag");
                    if(addFlag)
                        enterButton.setEnabled(false);
                    else
                        enterButton1.setEnabled(false);
                }
                else {

                    if(addFlag)
                        enterButton.setEnabled(true);
                    else
                        enterButton1.setEnabled(true);
                }
            }

        });

        //creating all the field labels
        cnicLabel = new JLabel("Enter CNIC: ");
        nameLabel = new JLabel("Enter Name: ");
        addressLabel = new JLabel("Enter Address: ");
        numLabel = new JLabel("Enter Phone Number: ");

        //creating the enter button and setting its properties
        enterButtonTemp = new JButton("Enter");
        enterButtonTemp.setPreferredSize(new Dimension(70,25));
        enterButtonTemp.setBorder(thickBorder);

        //adding the component to container
        con.add(cnicLabel);
        con.add(cnicText);
        con.add(nameLabel);
        con.add(nameText);
        con.add(addressLabel);
        con.add(addressText);
        con.add(numLabel);
        con.add(numText);
        con.add(enterButtonTemp);

        //getting the window toolkit to set the size and position of the container accordingly
        Toolkit theKit = con.getToolkit();
        Dimension wndSize = theKit.getScreenSize();
        int p1 = wndSize.width/3;
        int p2 = wndSize.height/3;
        setLocation(p1, p2);

        //setting some other basic properties on the container
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setMinimumSize(new Dimension(1000/4, 1200/4));
        setResizable(false);

        //disabling the enter button will only be enabled when fields are filled
        enterButtonTemp.setEnabled(false);

        return enterButtonTemp;
    }


    //called in initMethod of Delete and CNIC search and displays only CNIC field
    public JButton showCnicPanel(){

        //cretaing a container and setting its layout to flow layout
        Container con =getContentPane();
        con.removeAll();
        con.setLayout(new FlowLayout());

        //creating text fields and formatted text fields
        cnicText = new JFormattedTextField(createFormatter("#####-#######-#"));
        cnicText.setColumns(15);

        //implementing the text listener for cnic text field
        cnicText.getDocument().addDocumentListener(new DocumentListener() {

            //if cnic field is any of the three states then the changed() method is called
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            //checks if cnic field empty keeps button disabled else enables it
            public void changed() {

                if (cnicText.getText().equals("     -       - ")) {

                    //checks if method called in initDelete
                    if(deleteFlag)
                        enterButton2.setEnabled(false);
                    //checks if method called in initSearchByCNIC
                    else if(searchCnicFlag)
                        enterButton3.setEnabled(false);
                }
                else {

                    if(deleteFlag)
                        enterButton2.setEnabled(true);
                    else if(searchCnicFlag)
                        enterButton3.setEnabled(true);
                }
            }

        });

        //creating CNIC label and button and setting button properties and adding to container
        cnicLabel = new JLabel("Enter CNIC: ");

        enterButtonTemp1 = new JButton("Enter");
        enterButtonTemp1.setPreferredSize(new Dimension(70,25));
        enterButtonTemp1.setBorder(thickBorder);

        con.add(cnicLabel);
        con.add(cnicText);
        con.add(enterButtonTemp1);

        //getting windows toolkit to set the container ssize and position according to screen
        Toolkit theKit = con.getToolkit();
        Dimension wndSize = theKit.getScreenSize();
        int p1 = wndSize.width/3;
        int p2 = wndSize.height/3;
        setLocation(p1, p2);

        //setting some other basic properties of the container
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setMinimumSize(new Dimension(1000/4, 1200/4));
        setResizable(false);

        return enterButtonTemp1;
    }


    //shows the full panel and calls the button listener to add record to DB
    public void initMethodAdd(){

        addFlag = true;
        enterButton = showFullPanel();
        enterButton.addActionListener(this);

    }


    //shows the full panel and calls the button listener to update record in DB
    public void initMethodUpdate(){

        updateFlag=true;
        enterButton1 = showFullPanel();
        enterButton1.addActionListener(this);

    }


    //shows the cnic panel and calls the button listener to delete record in DB
    public void initMethodDelete(){

        deleteFlag = true;
        enterButton2 = showCnicPanel();
        enterButton2.addActionListener(this);

    }


    //shows the cnic panel and calls the button listener to search record in DB
    public void initMethodSearchCNIC(){

        searchCnicFlag = true;
        enterButton3 = showCnicPanel();
        enterButton3.addActionListener(this);

    }

    //shows the name panel and calls the button listener to search record in DB
    public void initMethodSearchName(){

        //creating the container and setting its layoout to flow layout
        searchNameFlag = true;
        Container con =getContentPane();
        con.removeAll();
        con.setLayout(new FlowLayout());

        //creating the name text field and button and setting button properties
        nameText=new JTextField(15);
        nameLabel = new JLabel("Enter Name: ");

        enterButton4 = new JButton("Enter");
        enterButton4.setPreferredSize(new Dimension(70,25));
        enterButton4.setBorder(thickBorder);

        //attaching the action listener
        enterButton4.addActionListener(this);
        enterButton4.setEnabled(false);

        //implementing text listener for the name field
        nameText.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            //if naame field empty then keep button disabled else enable
            public void changed() {

                if (nameText.getText().equals("")) {

                    if(searchNameFlag)
                        enterButton4.setEnabled(false);

                } else {

                    if(searchNameFlag)//To be changed here
                        enterButton4.setEnabled(true);
                }
            }

        });

        //adding components to container
        con.add(nameLabel);
        con.add(nameText);
        con.add(enterButton4);

        //getting toolkit and setting the size and position of the container according to the windows screen
        Toolkit theKit = con.getToolkit();
        Dimension wndSize = theKit.getScreenSize();
        int p1 = wndSize.width/3;
        int p2 = wndSize.height/3;
        setLocation(p1, p2);

        //setting some other basic properties of container
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setMinimumSize(new Dimension(768/4, 500/4));
        setResizable(false);

    }


    //Method is called in actionPerformed method when the user enters information for add or update and then presses enter
    //button, this function will get the information from text fields and return it in the form of array to be stored in DB
    public String[] getInformation(){

        String cnicStr, phoneStr, nameStr, addressStr;

        cnicStr = cnicText.getText();
        nameStr  = nameText.getText();
        phoneStr = numText.getText();
        addressStr = addressText.getText();

        String[] arr = {cnicStr, nameStr, addressStr, phoneStr};
        setVisible(false);
        return arr;
    }


    //Method is called in actionPerformed method when the user enters information for delete or search by CNIC and then presses
    //enterbutton, this function will get the information from text field and return it in the form of array to be stored in DB
    public String getCNIC(){

        String cnicStr;

        cnicStr =cnicText.getText();

        setVisible(false);
        return cnicStr;
    }


    //Method is called in actionPerformed method when the user enters information for search by name and then presses
    //enterbutton, this function will get the information from text field and return it in the form of array to be stored in DB
    public String getName(){

        String name;
        name =nameText.getText();
        setVisible(false);
        return name;
    }


    //Adds the information in the array to DB
    public void addToDB(String []arr){

        try{

            //establishing the connection
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            String dbURL = "jdbc:sqlserver://localhost; databaseName = AddressBook";
            String user = "sa";
            String pass = "12345678";

            Connection con=DriverManager.getConnection(dbURL,user,pass);

            //creating the insert query and prepared statement
            String sql = "insert into Person values (?,?,?,?)";
            PreparedStatement pSt=con.prepareStatement(sql);

            //inserting the row values
            pSt.setString(1, arr[0]);
            pSt.setString(2, arr[1]);
            pSt.setString(3, arr[2]);
            pSt.setString(4, arr[3]);

            //executing the prepared statement
            int count = pSt.executeUpdate();
            System.out.println("Number of Rows effected = " + count);

            con.close();
            JOptionPane.showMessageDialog(dialogFrame,
                    "Successfully Added");
            
        }catch(Exception e) {

            System.out.println(e);
            JOptionPane.showMessageDialog(dialogFrame,
                    e.getMessage().toString(),
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }

    }


    //Updates the information in the array in DB
    public void updateDB(String []arr){

        try{

            //establishing the connection
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            String dbURL = "jdbc:sqlserver://localhost; databaseName = AddressBook";
            String user = "sa";
            String pass = "12345678";
            Connection con=DriverManager.getConnection(dbURL,user,pass);

            //creating the update query and prepared statement
            String sql = "update Person set Name = ?, Address=?, PhoneNumber=? where CNIC = ?";
            PreparedStatement pSt=con.prepareStatement(sql);

            //updating the new row values
            pSt.setString(1, arr[1]);
            pSt.setString(2, arr[2]);
            pSt.setString(3, arr[3]);
            pSt.setString(4, arr[0]);

            //executing the prepared statement
            int count = pSt.executeUpdate();
            System.out.println("Number of Rows effected = " + count);

            con.close();
            if(count<1){
                JOptionPane.showMessageDialog(dialogFrame,
                        "Record does not exist in DB",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(dialogFrame,
                        "Successfully Updated");
            }
        }catch(Exception e) {

            System.out.println(e);
            JOptionPane.showMessageDialog(dialogFrame,
                    e.getMessage().toString(),
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }

    }

    //Deletes the record from the DB containing the CNIC passed
    public void deleteFromDB(String cnic){

        try{

            //establishing the connection
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            String dbURL = "jdbc:sqlserver://localhost; databaseName = AddressBook";
            String user = "sa";
            String pass = "12345678";
            Connection con = DriverManager.getConnection(dbURL,user,pass);

            //creating the delete query and prepared statement
            String sql = "delete from Person where CNIC = ?";
            PreparedStatement pSt=con.prepareStatement(sql);

            //setting the cnic
            pSt.setString(1, cnic);

            //executing the prepared statement
            int count = pSt.executeUpdate();
            System.out.println("Number of Rows effected = " + count);
            con.close();
            if(count<1){
                JOptionPane.showMessageDialog(dialogFrame,
                        "CNIC does not exist in DB",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(dialogFrame,
                        "Successfully Deleted");
            }


        }catch(Exception e) {

            System.out.println(e);
            JOptionPane.showMessageDialog(dialogFrame,
                    e.getMessage().toString(),
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }

    }


    //Search the record from the DB containing the CNIC passed
    public void searchByCNIC(String cnic){

        try{

            //establishing the connection
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            String dbURL = "jdbc:sqlserver://localhost; databaseName = AddressBook";
            String user = "sa";
            String pass = "12345678";
            Connection con=DriverManager.getConnection(dbURL,user,pass);

            //creating the select query and prepared statement
            String sql = "select * from Person where CNIC = ?";
            PreparedStatement pSt=con.prepareStatement(sql);
            pSt.setString(1,cnic);

            //executing the query and displaying the result set
            ResultSet rs = pSt.executeQuery();
            String name, address, phoneNum;
            if(!rs.wasNull()){
                    JOptionPane.showMessageDialog(dialogFrame,
                            "CNIC does not exist in DB",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);

            }
            while(rs.next()){

                cnic = rs.getString("CNIC");
                name = rs.getString("Name");
                address = rs.getString("Address");
                phoneNum = rs.getString("PhoneNumber");

                System.out.println(cnic + " " + name + " " + address + " " + phoneNum);

            }

            con.close();

        }catch(Exception e) {

            JOptionPane.showMessageDialog(dialogFrame,
                    e.getMessage().toString(),
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }

    }


    //Search the record from the DB containing the name passed
    public void searchByName(String name){

        try{

            //establishing the connection
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            String dbURL = "jdbc:sqlserver://localhost; databaseName = AddressBook";
            String user = "sa";
            String pass = "12345678";
            Connection con = DriverManager.getConnection(dbURL,user,pass);

            //creating the select query and prepared statement
            String sql = "select * from Person where Name = ?";
            PreparedStatement pSt=con.prepareStatement(sql);
            pSt.setString(1,name);

            //executing the query and displaying the result set
            ResultSet rs = pSt.executeQuery();
            String cnic,address, phoneNum;
            if(!rs.wasNull()){
                JOptionPane.showMessageDialog(dialogFrame,
                        "Name does not exist in DB",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);

            }
            while(rs.next()){

                cnic = rs.getString("CNIC");
                name = rs.getString("Name");
                address = rs.getString("Address");
                phoneNum = rs.getString("PhoneNumber");

                System.out.println(cnic + " " + name + " " + address + " " + phoneNum);

            }

            con.close();

        }catch(Exception e) {
            JOptionPane.showMessageDialog(dialogFrame,
                    e.getMessage().toString(),
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }

    }


    //Action Listeners of all the buttons implemented here
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()== add){

            f.setVisible(false);
            initMethodAdd();

        }
        else if(e.getSource()== update){

            f.setVisible(false);
            initMethodUpdate();

        }
        else if(e.getSource()== delete){

            f.setVisible(false);
            initMethodDelete();

        }
        else if(e.getSource()== searchName){

            f.setVisible(false);
            initMethodSearchName();

        }
        else if(e.getSource()== searchCNIC){

            f.setVisible(false);
            initMethodSearchCNIC();

        }
        else if(e.getSource()== enterButton){

            //upon pressing the enter button the information entered by user is picked and sent to DB
            String cnicStr, nameStr, phoneStr, addressStr;

            String []arr = getInformation();
            cnicStr = arr[0];
            nameStr = arr[1];
            addressStr = arr[2];
            phoneStr = arr[3];

            System.out.println("Name: " + nameStr + "\nCNIC: " + cnicStr + "\nAddress: " + addressStr + "\nPhone: " + phoneStr);

            addToDB(arr);

            addFlag = false;
            updateFlag = false;
            deleteFlag = false;
            searchNameFlag = false;
            searchCnicFlag = false;

            f.setVisible(true);

        }

        else if(e.getSource()== enterButton1){

            //upon pressing the enter button the information entered by user is picked and sent to be updated in DB
            String cnicStr, nameStr, phoneStr, addressStr;

            String []arr = getInformation();
            cnicStr = arr[0];
            nameStr = arr[1];
            addressStr = arr[2];
            phoneStr = arr[3];

            System.out.println("Name: " + nameStr + "\nCNIC: " + cnicStr + "\nAddress: " + addressStr + "\nPhone: " + phoneStr);

            updateDB(arr);

            addFlag = false;
            updateFlag = false;
            deleteFlag = false;
            searchNameFlag = false;
            searchCnicFlag = false;

            f.setVisible(true);
        }

        else if(e.getSource()== enterButton2){

            //upon pressing the enter button the cnic entered by user is picked and sent to be deleted in DB
            String cnicStr;

            cnicStr = getCNIC();
            System.out.println("CNIC: " + cnicStr);

            deleteFromDB(cnicStr);

            addFlag = false;
            updateFlag = false;
            deleteFlag = false;
            searchNameFlag = false;
            searchCnicFlag = false;

            f.setVisible(true);
        }
        else if(e.getSource()== enterButton3){

            //upon pressing the enter button the cnic entered by user is picked and sent to be searched in DB
            String cnicStr;

            cnicStr = getCNIC();
            System.out.println("CNIC: " + cnicStr);

            searchByCNIC(cnicStr);

            addFlag = false;
            updateFlag = false;
            deleteFlag = false;
            searchNameFlag = false;
            searchCnicFlag = false;

            f.setVisible(true);
        }

        else if(e.getSource()== enterButton4){

            //upon pressing the enter button the name entered by user is picked and sent to be searched in DB
            String name;

            name = getName();
            System.out.println("Name: " + name);

            searchByName(name);

            addFlag = false;
            updateFlag = false;
            deleteFlag = false;
            searchNameFlag = false;
            searchCnicFlag = false;

            f.setVisible(true);
        }
    }

    public HW2(){

        initMethod();

    }

    public static void main(String args[]) {

        HW2 c=new HW2();
    }

}