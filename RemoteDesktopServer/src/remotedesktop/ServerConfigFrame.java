package remotedesktop;

import java.awt.AWTException;
import java.awt.Color;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ServerConfigFrame extends javax.swing.JFrame {

    private String ip;
    private int portTCP, portUDP;

    public ServerConfigFrame() {
        initComponents();
        this.setVisible(true);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ipField = new javax.swing.JTextField();
        tcpPortField = new javax.swing.JTextField();
        udpPortField = new javax.swing.JTextField();
        startButton = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        helpButton = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Server");
        setLocation(new java.awt.Point(800, 300));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Configuration:");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("Server IP: ");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("TCP Port: ");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("UDP Port: ");

        ipField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        ipField.setText("31.216.99.191");

        tcpPortField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tcpPortField.setText("7777");

        udpPortField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        udpPortField.setText("9876");

        startButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/startSmall.jpg"))); // NOI18N
        startButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        startButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButtonMouseExited(evt);
            }
        });

        helpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/helpSmall.jpg"))); // NOI18N
        helpButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        helpButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        helpButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                helpButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                helpButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                helpButtonMouseExited(evt);
            }
        });

        jMenuBar1.setMinimumSize(new java.awt.Dimension(0, 3));

        jMenu1.setText("File");

        jMenuItem1.setText("Load ");
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Save ");
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(helpButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tcpPortField))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(udpPortField, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))))
                .addGap(3, 3, 3))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(startButton)
                .addGap(80, 80, 80))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(helpButton)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tcpPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(udpPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startButton)
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseEntered
        startButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.gray, Color.black));
    }//GEN-LAST:event_startButtonMouseEntered

    private void startButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseExited
        startButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    }//GEN-LAST:event_startButtonMouseExited

    private void startButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseClicked
        try {
            
            loadComponents();
            new Server(ip, portTCP, portUDP);
            
        } catch (InvalidInputException ex) {           
            // colorises the error component
            ex.getFautyComponent().setForeground(Color.red);
            //displayes error msg
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // sets the color back to black
            ex.getFautyComponent().setForeground(Color.black);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (AWTException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_startButtonMouseClicked

    private void helpButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpButtonMouseEntered
        helpButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.gray, Color.black));
    }//GEN-LAST:event_helpButtonMouseEntered

    private void helpButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpButtonMouseExited
        helpButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    }//GEN-LAST:event_helpButtonMouseExited

    private void helpButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpButtonMouseClicked
                    JOptionPane.showMessageDialog(helpButton, " How to setup your router in order to forward the traffic to your computer:\n\n"
                            + " 1) Navigate to your router's configuration page on your browser.\n"
                            + " 2) Find your pc's private ip address ( execute 'ipconfig' command in command line).\n"
                            + " 3) In your router configuration page, find the option for port forwarding.\n"
                            + " 4) Set a new port forwarding with port number the server Port you selected \n"
                            + "    and ip address the ip of your pc\n"
                            + " 5) Submit\n\n"
                            + " -NOTE- Your windows firewall might be blocking the traffic so\n"
                            + "        either disable it or allow the traffic from this application.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_helpButtonMouseClicked

    private void loadComponents() throws InvalidInputException {
        //read IP
        String ip = ipField.getText();
        if (isValidIP(ip)) {
            this.ip = ip;
        } else {
            throw new InvalidInputException(" The ip is not valid!", ipField);
        }
        
        //read TCP port
        String tcpPort = tcpPortField.getText();
        if (isValidPort(tcpPort)) {
            this.portTCP = Integer.parseInt(tcpPort);
        } else {
            throw new InvalidInputException(" The TCP port is not valid!( 0 <= port <= 65535 )", tcpPortField);
        }
        
        //read TCP port
        String udpPort = udpPortField.getText();
        if (isValidPort(udpPort)) {
            this.portUDP = Integer.parseInt(udpPort);
        } else {
            throw new InvalidInputException(" The UDP port is not valid!( 0 <= port <= 65535 )", udpPortField);
        }

    }

    private boolean isValidIP(String IP) {

        IP = IP.trim();
        if (IP == null) {
            return false;
        }
        StringTokenizer octets = new StringTokenizer(IP, ".");

        if (octets.countTokens() != 4) {
            return false;
        } else {
            while (octets.hasMoreTokens()) {
                String token = (String) octets.nextToken(".");
                if (!isInteger(token)) {
                    return false;
                } else if (Integer.parseInt(token) > 255) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isValidPort(String port){
        if(isInteger(port)){
            int portNum = Integer.parseInt(port);
            if(portNum <= 65535 &&isAvailable(portNum)) return true;
        }
        return false;
    }
    
    //TO BE CODED...
    private boolean isAvailable(int port){
        return true;
    }
    
    private boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        str.trim();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '-') {
                if (str.length() == 1) {
                    return false;
                }
                continue;
            }
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel helpButton;
    private javax.swing.JTextField ipField;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel startButton;
    private javax.swing.JTextField tcpPortField;
    private javax.swing.JTextField udpPortField;
    // End of variables declaration//GEN-END:variables
}
