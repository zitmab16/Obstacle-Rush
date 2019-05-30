package gui;

import bl.DataBase;
import bl.Highscore;
import bl.Obstacle;
import bl.Player;
import bl.User;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vizug
 */
public class MainGUI extends javax.swing.JFrame implements Runnable {

    private Player player = new Player(50, 50);
    private LinkedList<Obstacle> obstacles = new LinkedList<>();
    private Random rand = new Random();
    private Thread threadobstacle;
    private Thread threadpanel = new Thread(this);
    private CreateUserDialog dlg;
    private User user;
    private Thread threadscore;
    private int score = 0;
    public DataBase db;

    /**
     * Creates new form MainGUI
     */
    public MainGUI() throws SQLException {
        this.db = new DataBase();
        initComponents();
        this.setVisible(true);
        this.setFocusable(false);
        panelGame.setFocusable(true);
        createDlg();
        createObstacle();
        threadpanel.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelGame = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        meGame = new javax.swing.JMenu();
        miCreateUser = new javax.swing.JMenuItem();
        miSelectDifficulty = new javax.swing.JMenuItem();
        miStartGame = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelGame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                panelGameKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelGameLayout = new javax.swing.GroupLayout(panelGame);
        panelGame.setLayout(panelGameLayout);
        panelGameLayout.setHorizontalGroup(
            panelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        panelGameLayout.setVerticalGroup(
            panelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );

        meGame.setText("Game");

        miCreateUser.setText("Create User");
        meGame.add(miCreateUser);

        miSelectDifficulty.setText("Select Difficulty");
        meGame.add(miSelectDifficulty);

        miStartGame.setText("Start Game");
        meGame.add(miStartGame);

        jMenuBar1.add(meGame);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void createDlg() {
        dlg = new CreateUserDialog(this, true);
        dlg.setVisible(true);

        if (dlg.isOk()) {
            user = dlg.getUser();
        }
    }

    private void panelGameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_panelGameKeyPressed

        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.x -= 10;
                break;
            case KeyEvent.VK_RIGHT:
                player.x += 10;
                break;
            case KeyEvent.VK_UP:
                player.y -= 10;
                break;
            case KeyEvent.VK_DOWN:
                player.y += 10;
                break;
            default:
                break;
        }
        repaint();

    }//GEN-LAST:event_panelGameKeyPressed

    public void createObstacle() {
        threadobstacle = new Thread() {
            @Override
            public void run() {
                while (true) {
                    obstacles.add(new Obstacle(((((rand.nextInt(panelGame.getWidth() - 0 + 1) + 0) + 9) / 10) * 10), 0));
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        };
        threadobstacle.start();

    }

    public void createHighscore() throws SQLException{
        Highscore hs = new Highscore(this.score,user.getUsername());
        insertIntoDataBase(hs);
    }
    public void insertIntoDataBase(Highscore hs) throws SQLException{
        db.insertHighscore(hs);
    }
    public void countScore() {
        threadscore = new Thread() {
            

            @Override
            public void run() {
                while (true) {
                    score += 20;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            public int getScore() {
                return score;
            }
            
        };
        threadscore.start();
    }

    public void checkPlayerObstacles() throws SQLException {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.x == player.x && obstacle.y == player.y) {
                threadobstacle.stop();
                threadpanel.stop();
                threadscore.stop();
                createHighscore();
                JOptionPane.showMessageDialog(null, "You have lost!");
            }
        }
    }

    @Override
    public void paint(Graphics gParent) {
        Graphics2D g = (Graphics2D) panelGame.getGraphics();
        g.clearRect(0, 0, panelGame.getWidth(), panelGame.getHeight());

        for (Obstacle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, 10, 10);
            obstacle.y += 1;
        }

        try {
            g.setColor(user.getColor());
            g.fillRect(player.x, player.y, 10, 10);
            checkPlayerObstacles();
        } catch (Exception ex) {

        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainGUI();
                } catch (SQLException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu meGame;
    private javax.swing.JMenuItem miCreateUser;
    private javax.swing.JMenuItem miSelectDifficulty;
    private javax.swing.JMenuItem miStartGame;
    private javax.swing.JPanel panelGame;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
                repaint();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
