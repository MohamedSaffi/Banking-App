package BankApp;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.*;
class display extends Frame {
  Button b2;
  String name;
  Label l1, l2;
  BufferedImage backgroundImage;
  display(String num, String num2, int x) {
    Font font1 = new Font("SansSerif", Font.BOLD, 50);
    Font font2 = new Font("SansSerif", Font.BOLD, 20);
    l1 = new Label("Balance amount:");
    l2 = new Label("Rs " + num2);
    l1.setFont(font1);
    l2.setFont(font1);
    l1.setBounds(100, 300, 450, 100);
    l1.setForeground(Color.WHITE);
    l1.setBackground(Color.MAGENTA);
    l2.setBounds(600, 300, 450, 100);
    l2.setForeground(Color.WHITE);
    Color c1 = new Color(0, 0, 153);
    l2.setBackground(c1);
    name = num;
    b2 = new Button("Back");
    b2.setBounds(100, 600, 100, 50);
    b2.setBackground(Color.BLUE);
    b2.setForeground(Color.WHITE);
    b2.setFont(font2);
    b2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dispose();
          if (x == 1) 
            new savings(name);
          else if (x == 2) 
            new loan(name);
        }
      }
    );
    add(l1);
    add(l2);
    add(b2);
    try {
      backgroundImage =ImageIO.read(new File("D:\\Java\\Mini-Project\\BankApp\\src\\background.jpg"));
    } catch (IOException ex) {
      System.out.println("Error");
    }
    addWindowListener (new WindowAdapter() {    
        public void windowClosing (WindowEvent e) {    
            dispose();    
        }    
    });
    setSize(1300, 700);
    setLayout(null);
    setVisible(true);
  }
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
  }
}

class nloan extends Frame {
  String name, a, temp;
  TextField t1;
  Label l1;
  int t, amount_L, limit = 2500000;
  double r, interest, amount, amount1, amt;
  Button done, b2;
  Font font1 = new Font("SansSerif", Font.BOLD, 20);
  BufferedImage backgroundImage;
  nloan(String num) {
    name = num;
    l1 = new Label("Loan amount(Limit Rs2500000)");
    l1.setFont(font1);
    l1.setBounds(380, 250, 300, 30);
    l1.setForeground(Color.WHITE);
    Color c1 = new Color(0, 0, 153);
    l1.setBackground(c1);
    t1 = new TextField();
    t1.setFont(font1);
    t1.setBounds(800, 250, 250, 40);
    t1.setBackground(Color.LIGHT_GRAY);
    CheckboxGroup cbg = new CheckboxGroup();
    Checkbox checkBox1 = new Checkbox("3 years scheme(6% interest)",cbg,false);
    checkBox1.setBounds(800, 350, 250, 50);
    checkBox1.setForeground(Color.WHITE);
    checkBox1.setBackground(c1);
    Checkbox checkBox2 = new Checkbox("6 years scheme(3% interest)",cbg,false);
    checkBox2.setBounds(800, 400, 250, 50);
    checkBox2.setForeground(Color.WHITE);
    checkBox2.setBackground(c1);
    checkBox1.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          t = 3;
          r = 0.06;
        }
      }
    );
    checkBox2.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          t = 6;
          r = 0.03;
        }
      }
    );
    b2 = new Button("Back");
    b2.setBounds(100, 600, 100, 50);
    b2.setBackground(Color.BLUE);
    b2.setForeground(Color.WHITE);
    b2.setFont(font1);
    b2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dispose();
          new loan(name);
        }
      }
    );
    done = new Button("Submit");
    done.setBounds(800, 600, 100, 50);
    done.setBackground(Color.BLUE);
    done.setForeground(Color.WHITE);
    done.setFont(font1);
    done.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          a = t1.getText();
          if (t > 0 && a != null) {
            amount_L = Integer.parseInt(a);
            if (amount_L % 100000 == 0) {
              if (amount_L <= limit) {
                interest = r * amount_L;
                amount1 = (amount_L / t) + interest;
                amount = (amount_L + (interest * t));
                try {
                  Class.forName("oracle.jdbc.driver.OracleDriver");
                  Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","guhan");
                  Statement stmt = con.createStatement();
                  ResultSet rs = stmt.executeQuery("select SAVINGS_AMOUNT from Accounts where USERNAME LIKE '%" +name +"%'");
                  rs.next();
                  amt =rs.getDouble(1);
                  amt += amount;
                  stmt.executeUpdate("UPDATE ACCOUNTS SET LOAN_AMOUNT="+amount+" WHERE USERNAME LIKE '%" +name +"%'");
                  stmt.executeUpdate("UPDATE ACCOUNTS SET SAVINGS_AMOUNT="+amt+" WHERE USERNAME LIKE '%" +name +"%'");
                  stmt.executeUpdate("UPDATE ACCOUNTS SET INTEREST="+amount1+" WHERE USERNAME LIKE '%" +name +"%'");
                  JOptionPane.showMessageDialog(null,"Loan Sucessfully received","Success",JOptionPane.INFORMATION_MESSAGE);
                  dispose();
                  con.close();
                  new loan(name);
                } catch (Exception event) {
                  System.out.println("Exception Occurred" + event);
                }
              } 
              else 
                JOptionPane.showMessageDialog(null,"Limit Exceeded!!!!!","Alert",JOptionPane.INFORMATION_MESSAGE);
            } 
            else 
              JOptionPane.showMessageDialog(null,"Invalid amount\nEnter amount in lakhs","Alert",JOptionPane.INFORMATION_MESSAGE);
          } 
          else
              JOptionPane.showMessageDialog(null,"Select a year policy and Enter loan amount","Alert",JOptionPane.INFORMATION_MESSAGE);
        }
      }
    );
    add(t1);
    add(l1);
    add(checkBox1);
    add(checkBox2);
    add(done);
    add(b2);
    try {
      backgroundImage =ImageIO.read(new File("D:\\Java\\Mini-Project\\BankApp\\src\\background.jpg"));
    } catch (IOException ex) {
      System.out.println("Error");
    }
    addWindowListener (new WindowAdapter() {    
        public void windowClosing (WindowEvent e) {    
            dispose();    
        }    
    });
    setSize(1300, 700);
    setLayout(null);
    setVisible(true);
  }
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
  }
}

class savings extends Frame {
  Button s_acc, tran, b2;
  String name, temp;
  double amt;
  BufferedImage backgroundImage;
  savings(String num) {
    Font font1 = new Font("SansSerif", Font.BOLD, 20);
    name = num;
    s_acc = new Button("View Balance");
    s_acc.setBounds(800, 200, 250, 100);
    tran = new Button("Transaction");
    tran.setBounds(800, 500, 250, 100);
    s_acc.setBackground(Color.BLUE);
    s_acc.setForeground(Color.WHITE);
    tran.setBackground(Color.BLUE);
    tran.setForeground(Color.WHITE);
    s_acc.setFont(font1);
    tran.setFont(font1);
    b2 = new Button("Back");
    b2.setBounds(100, 600, 100, 50);
    b2.setBackground(Color.BLUE);
    b2.setForeground(Color.WHITE);
    b2.setFont(font1);
    b2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dispose();
          new home(name);
        }
      }
    );
    s_acc.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","guhan");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select SAVINGS_AMOUNT from Accounts where USERNAME LIKE '%" +name +"%'");
            rs.next();
            amt=rs.getDouble(1);
            con.close();
          } catch (Exception ex) {
            System.out.println("Error");
          }
          new display(name, String.format("%.02f", amt), 1);
          dispose();
        }
      }
    );
    tran.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          new transaction(name);
          dispose();
        }
      }
    );
    add(s_acc);
    add(tran);
    add(b2);
    try {
      backgroundImage =ImageIO.read(new File("D:\\Java\\Mini-Project\\BankApp\\src\\background.jpg"));
    } catch (IOException ex) {
      System.out.println("Error");
    }
    addWindowListener (new WindowAdapter() {    
        public void windowClosing (WindowEvent e) {    
            dispose();    
        }    
    });
    setSize(1300, 700);
    setLayout(null);
    setVisible(true);
  }
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
  }
}

class loan extends Frame {
  Button l_new, l_bal, l_pay, b2;
  String name, loan1, loan2, loan3;
  int loan_amt;
  double amount, interest, amount1,amt;
  BufferedImage backgroundImage;
  loan(String num) {
    Font font1 = new Font("SansSerif", Font.BOLD, 20);
    name = num;
    l_new = new Button("New Loan");
    l_new.setBounds(800, 200, 250, 40);
    l_bal = new Button("Loan balance");
    l_bal.setBounds(800, 400, 250, 40);
    l_pay = new Button("Pay Loan");
    l_pay.setBounds(800, 600, 250, 40);
    l_new.setBackground(Color.BLUE);
    l_new.setForeground(Color.WHITE);
    l_bal.setBackground(Color.BLUE);
    l_pay.setBackground(Color.BLUE);
    l_pay.setForeground(Color.WHITE);
    l_bal.setForeground(Color.WHITE);
    l_new.setFont(font1);
    l_bal.setFont(font1);
    l_pay.setFont(font1);
    b2 = new Button("Back");
    b2.setBounds(100, 600, 100, 50);
    b2.setBackground(Color.BLUE);
    b2.setForeground(Color.WHITE);
    b2.setFont(font1);
    b2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dispose();
          new home(name);
        }
      }
    );
    l_new.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","guhan");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select LOAN_AMOUNT from Accounts where USERNAME LIKE '%" +name +"%'");
            rs.next();
            amt=rs.getDouble(1);
            if (amt==0) {
              new nloan(name);
              dispose();
              } 
            else 
              JOptionPane.showMessageDialog(null,"Pay the balance loan to get new loan","Warning",JOptionPane.INFORMATION_MESSAGE);
            con.close();
          } catch (Exception e9) {
            System.out.println("Exception Occurred" + e9);
          }
        }
      }
    );
    l_bal.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","guhan");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select LOAN_AMOUNT from Accounts where USERNAME LIKE '%" +name +"%'");
            rs.next();
            amt=rs.getDouble(1);
            if (amt==0) 
              JOptionPane.showMessageDialog(null,"You don't have a loan ","Message",JOptionPane.INFORMATION_MESSAGE); 
            else {
              dispose();
              con.close();
              new display(name, String.valueOf(amt), 2);
            }
          } catch (Exception e9) {
            System.out.println("Exception Occurred" + e9);
          }
        }
      }
    );
    l_pay.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","guhan");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select LOAN_AMOUNT,SAVINGS_AMOUNT,INTEREST from Accounts where USERNAME LIKE '%" +name +"%'");
            rs.next();
            amount=rs.getDouble(1);
            amount1=rs.getDouble(2);
            interest=rs.getDouble(3);
            if (amount!=0) {
              if (amount1 >= interest && interest != 0) {
                amount1 -= interest;
                amount -= interest;
                if (amount <= 5) 
                {
                  amount = 0;
                  stmt.executeUpdate("UPDATE ACCOUNTS SET INTEREST="+0+" WHERE USERNAME LIKE '%" +name +"%'");
                }
                stmt.executeUpdate("UPDATE ACCOUNTS SET LOAN_AMOUNT="+amount+" WHERE USERNAME LIKE '%" +name +"%'");
                stmt.executeUpdate("UPDATE ACCOUNTS SET SAVINGS_AMOUNT="+amount1+" WHERE USERNAME LIKE '%" +name +"%'");
                JOptionPane.showMessageDialog(null,"Paid Successfully","Success",JOptionPane.INFORMATION_MESSAGE);
              } 
              else 
                JOptionPane.showMessageDialog(null,"No Sufficient balance!!!!","Warning",JOptionPane.INFORMATION_MESSAGE);
            } 
            else 
              JOptionPane.showMessageDialog(null,"You dont have a present loan to pay for!!!","Warning",JOptionPane.INFORMATION_MESSAGE);
            con.close();
          } catch (Exception e9) {
            System.out.println("Exception Occurred" + e9);
          }
        }
      }
    );
    add(l_new);
    add(l_bal);
    add(l_pay);
    add(b2);
    try {
      backgroundImage =ImageIO.read(new File("D:\\Java\\Mini-Project\\BankApp\\src\\background.jpg"));
    } catch (IOException ex) {
      System.out.println("Error");
    }
    addWindowListener (new WindowAdapter() {    
        public void windowClosing (WindowEvent e) {    
            dispose();    
        }    
    });
    setSize(1300, 700);
    setLayout(null);
    setVisible(true);
  }
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
  }
}

class transaction extends Frame {
  Label l1, l2;
  TextField t1, t2;
  String d, c, e1, p, q;
  Button b1, b2;
  double amount, deposit;
  int limit;
  BufferedImage backgroundImage;
  transaction(String name) {
    Font font1 = new Font("SansSerif", Font.BOLD, 20);
    limit = 90000;
    String name1 = name;
    l1 = new Label("Enter the amount to be paid(limit Rs90000)");
    l1.setFont(font1);
    l1.setBounds(300, 250, 430, 30);
    l1.setForeground(Color.WHITE);
    Color c1 = new Color(0, 0, 153);
    l1.setBackground(c1);
    t1 = new TextField();
    t1.setBounds(800, 250, 250, 40);
    t1.setBackground(Color.LIGHT_GRAY);
    l2 = new Label("Enter the account number of the receiver");
    l2.setBounds(300, 350, 430, 30);
    l2.setForeground(Color.WHITE);
    l2.setFont(font1);
    l2.setBackground(c1);
    t2 = new TextField();
    t2.setBounds(800, 350, 250, 40);
    t2.setBackground(Color.LIGHT_GRAY);
    b1 = new Button("Transfer");
    b1.setBounds(800, 500, 100, 50);
    b1.setBackground(Color.BLUE);
    b1.setForeground(Color.WHITE);
    b1.setFont(font1);
    b1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          e1 = t2.getText();
          c = t1.getText();
          amount = Double.parseDouble(c);
          try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","guhan");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select SAVINGS_AMOUNT from Accounts where USERNAME LIKE '%" +name1 +"%'");
            rs.next();
            deposit = rs.getDouble(1);
            ResultSet rs1 = stmt.executeQuery("select username from Accounts where ACC_NO LIKE '%" + e1 + "%'");
            if (!rs1.next()) 
              JOptionPane.showMessageDialog(null,"Reciever not found","Warning",JOptionPane.INFORMATION_MESSAGE); 
            else {
              p = rs1.getString(1);
              if (p.equals(name1)) 
                JOptionPane.showMessageDialog(null,"Illegal transaction!!!!","Warning",JOptionPane.INFORMATION_MESSAGE);
              else if (amount > limit || amount > deposit) 
                JOptionPane.showMessageDialog(null,"limit exceeded or not sufficient balance","Warning",JOptionPane.INFORMATION_MESSAGE); 
              else {
                JOptionPane.showMessageDialog(null,"TRANSACTION TO THE ACCOUNT SUCCESS","Success",JOptionPane.INFORMATION_MESSAGE);
                deposit -= amount;
                ResultSet rs2 = stmt.executeQuery("select SAVINGS_AMOUNT from Accounts where ACC_NO LIKE '%" +e1 +"%'");
                rs2.next();
                double amt = rs2.getDouble(1);
                amt += amount;
                stmt.executeUpdate("Update accounts set SAVINGS_AMOUNT=" +deposit +" where USERNAME LIKE '%" +name1 +"%'");
                stmt.executeUpdate("Update accounts set SAVINGS_AMOUNT=" +amt +" where ACC_NO LIKE '%" +e1 +"%'");
              }
            }
            con.close();
          } catch (Exception e10) {
            System.out.println("Exception Occurred" + e10);
          }
        }
      }
    );
    b2 = new Button("Back");
    b2.setBounds(100, 500, 100, 50);
    b2.setBackground(Color.BLUE);
    b2.setForeground(Color.WHITE);
    b2.setFont(font1);
    b2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dispose();
          new savings(name1);
        }
      }
    );
    add(l1);
    add(t1);
    add(l2);
    add(t2);
    add(b1);
    add(b2);
    try {
      backgroundImage =ImageIO.read(new File("D:\\Java\\Mini-Project\\BankApp\\src\\background.jpg"));
    } catch (IOException ex) {
      System.out.println("Error");
    }
    addWindowListener (new WindowAdapter() {    
        public void windowClosing (WindowEvent e) {    
            dispose();    
        }
    });
    setSize(1300, 700);
    setLayout(null);
    setVisible(true);
  }

  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
  }
}

class signup extends Frame {
  TextField t1, t2, t3;
  Button b1, b2;
  Label l1, l2, l3;
  BufferedImage backgroundImage;
  signup() {
    Font font1 = new Font("SansSerif", Font.BOLD, 20);
    l1 = new Label("Username");
    l2 = new Label("Password");
    l3 = new Label("Account Number");
    l1.setFont(font1);
    l2.setFont(font1);
    l3.setFont(font1);
    l1.setBounds(580, 250, 100, 30);
    l1.setForeground(Color.WHITE);
    Color c1 = new Color(0, 0, 153);
    l1.setBackground(c1);
    l2.setBounds(580, 350, 100, 30);
    l2.setForeground(Color.WHITE);
    l2.setBackground(c1);
    l3.setBounds(580, 450, 200, 30);
    l3.setForeground(Color.WHITE);
    l3.setBackground(c1);
    t1 = new TextField();
    t2 = new TextField();
    t3 = new TextField();
    t1.setFont(font1);
    t2.setFont(font1);
    t3.setFont(font1);
    t1.setBounds(800, 250, 250, 40);
    t1.setBackground(Color.LIGHT_GRAY);
    t2.setBounds(800, 350, 250, 40);
    t2.setBackground(Color.LIGHT_GRAY);
    t3.setBounds(800, 450, 250, 40);
    t3.setBackground(Color.LIGHT_GRAY);
    b1 = new Button("Confirm");
    b1.setBounds(800, 600, 100, 50);
    b1.setBackground(Color.BLUE);
    b1.setForeground(Color.WHITE);
    b1.setFont(font1);
    b1.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          String a, b, c, p;
          try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","guhan");
            Statement stmt = con.createStatement();
            a = t1.getText();
            p = t2.getText();
            b = t3.getText();
            ResultSet rs = stmt.executeQuery("select apps from Accounts where ACC_NO LIKE '%" + b + "%'");
            if (rs.next())
            {
              c = rs.getString(1);
              if (c.equals("yes")) 
                JOptionPane.showMessageDialog(null,"User already present!!!","Warning",JOptionPane.INFORMATION_MESSAGE); 
              else 
              {
                ResultSet rs1 = stmt.executeQuery("select * from Accounts where USERNAME LIKE '%" + a + "%'");
                if (rs1.next())
                  JOptionPane.showMessageDialog(null,"User already present!!!Try another username","Warning",JOptionPane.INFORMATION_MESSAGE); 
                else
                {
                  stmt.executeUpdate("update Accounts set apps='yes' where ACC_NO LIKE '%" +b +"%'");
                  stmt.executeUpdate("update Accounts set username='" +a +"' where ACC_NO LIKE '%" +b +"%'");
                  stmt.executeUpdate("update Accounts set passwords='" +p +"' where ACC_NO LIKE '%" +b +"%'");
                  JOptionPane.showMessageDialog(null,"User created successfully","Success",JOptionPane.INFORMATION_MESSAGE);
                  dispose();
                  new login();
                }
              }
            } 
            else 
              JOptionPane.showMessageDialog(null,"Account number does not belong to this bank!!!!Check your account number","Warning",JOptionPane.INFORMATION_MESSAGE);
            con.close();
          } catch (Exception event) {
            System.out.println("Exception Occurred" + event);
          }
        }
      }
    );
    b2 = new Button("Back");
    b2.setBounds(100, 600, 100, 50);
    b2.setBackground(Color.BLUE);
    b2.setForeground(Color.WHITE);
    b2.setFont(font1);
    b2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dispose();
          new login();
        }
      }
    );
    add(l1);
    add(t1);
    add(l2);
    add(t2);
    add(l3);
    add(t3);
    add(b1);
    add(b2);
    try {
      backgroundImage =ImageIO.read(new File("D:\\Java\\Mini-Project\\BankApp\\src\\background.jpg"));
    } catch (IOException ex) {
      System.out.println("Error");
    }
    addWindowListener (new WindowAdapter() {    
        public void windowClosing (WindowEvent e) {    
            dispose();    
        }    
    });
    setSize(1300, 700);
    setLayout(null);
    setVisible(true);
  }
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
  }
}

class home extends Frame {
  Button s_acc, l_acc, tran, b2;
  String name;
  BufferedImage backgroundImage;
  home(String num) {
    Font font1 = new Font("SansSerif", Font.BOLD, 20);
    name = num;
    s_acc = new Button("Savings Account");
    s_acc.setBounds(800, 200, 250, 100);
    l_acc = new Button("Loan Account");
    l_acc.setBounds(800, 500, 250, 100);
    s_acc.setBackground(Color.BLUE);
    s_acc.setForeground(Color.WHITE);
    l_acc.setBackground(Color.BLUE);
    l_acc.setForeground(Color.WHITE);
    s_acc.setFont(font1);
    l_acc.setFont(font1);
    b2 = new Button("Back");
    b2.setBounds(100, 600, 100, 50);
    b2.setBackground(Color.BLUE);
    b2.setForeground(Color.WHITE);
    b2.setFont(font1);
    b2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dispose();
          new login();
        }
      }
    );
    s_acc.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dispose();
          new savings(name);
        }
      }
    );
    l_acc.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dispose();
          new loan(name);
        }
      }
    );
    add(s_acc);
    add(l_acc);
    add(b2);
    try {
      backgroundImage =ImageIO.read(new File("D:\\Java\\Mini-Project\\BankApp\\src\\background.jpg"));
    } catch (IOException ex) {
      System.out.println("Error");
    }
    addWindowListener (new WindowAdapter() {    
        public void windowClosing (WindowEvent e) {    
            dispose();    
        }    
    });
    setSize(1300, 700);
    setLayout(null);
    setVisible(true);
  }
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
  }
}

class login extends Frame {
  Button login1, signup;
  TextField t1;
  Label l1, l2;
  JPasswordField t2;
  Font font1 = new Font("SansSerif", Font.BOLD, 20);
  BufferedImage backgroundImage;
  login() {
    signup = new Button("SIGNUP");
    l1 = new Label("Username");
    l2 = new Label("Password");
    l1.setFont(font1);
    l2.setFont(font1);
    l1.setBounds(680, 250, 200, 30);
    l1.setForeground(Color.WHITE);
    Color c1 = new Color(0, 0, 153);
    l1.setBackground(c1);
    l2.setBounds(680, 350, 200, 30);
    l2.setForeground(Color.WHITE);
    l2.setBackground(c1);
    login1 = new Button("LOGIN");
    login1.setBounds(750, 500, 100, 50);
    signup.setBounds(1000, 500, 100, 50);
    add(login1);
    add(signup);
    login1.setBackground(Color.BLUE);
    login1.setForeground(Color.WHITE);
    signup.setBackground(Color.BLUE);
    signup.setForeground(Color.WHITE);
    signup.setFont(font1);
    login1.setFont(font1);
    t1 = new TextField();	
    t2 = new JPasswordField();
    t1.setFont(font1);
    t2.setFont(font1);
    t1.setBounds(800, 250, 250, 40);
    t1.setBackground(Color.LIGHT_GRAY);
    t2.setBounds(800, 350, 250, 40);
    t2.setBackground(Color.LIGHT_GRAY);
    add(t1);
    add(t2);
    add(l1);
    add(l2);
    login1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          String a, b;
          try 
          {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","guhan");
            Statement stmt = con.createStatement();
            a = t1.getText();
            b = String.valueOf(t2.getPassword());
            ResultSet rs = stmt.executeQuery("select username,passwords from Accounts where username LIKE '%" +a +"%' AND passwords LIKE '%" +b +"%'");
            if (rs.next())
            {
              dispose();
              new home(a);
            }
            con.close();
          } catch (Exception f) {
            System.out.println("Exception Occurred" + f);
          }
        }
      }
    );
    signup.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dispose();
          new signup();
        }
      }
    );
    try {
      backgroundImage =ImageIO.read(new File("D:\\Java\\Mini-Project\\BankApp\\src\\background.jpg"));
    } catch (IOException ex) {
      System.out.println("Error");
    }
    addWindowListener (new WindowAdapter() {    
        public void windowClosing (WindowEvent e) {    
            dispose();    
        }    
    }); 
    setSize(1300, 700);
    setLayout(null);
    setVisible(true);
  }
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
  }
}

class BankApp {
  public static void main(String[] args) {
    new login();
  }
}
