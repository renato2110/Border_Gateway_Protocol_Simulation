package com.gui;

import com.company.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

/**
 * Created by Renato on 12/11/2017.
 */
public class MenuInterface {
    private JPanel panel;
    private JButton btStarRouter;
    private JButton btStopRouter;
    private JButton btAddSubnet;
    private JButton btShowRoutes;
    private JButton btOpenLogbook;
    private JButton btExit;
    private Controller router;
    private boolean isRunningRouter;

    public MenuInterface() {

        btStarRouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunningRouter) {
                    JOptionPane.showMessageDialog(null, "The router is running!!", "", JOptionPane.WARNING_MESSAGE);
                } else {
                    String path = "";
                    File test;
                    boolean value = true;
                    while (value) {
                        path = JOptionPane.showInputDialog(null, "Insert the file path", "Insert file", -1);
                        if (path != null) {
                            test = new File(path);
                            if (test.exists()) {
                                value = false;
                                try {
                                    router = new Controller(path);
                                    isRunningRouter = true;
                                } catch (IOException e1) {
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "The route is not valid or the file does not exist!!", "", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            value = false;
                        }
                    }


                }
            }
        });

        btStopRouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunningRouter) {
                    int option = JOptionPane.showConfirmDialog(null, "¿Surely you want to stop the router?", "Stop Router", 2);
                    if (option == 0) {
                        router.stopClients();
                        router.stopServers();
                        isRunningRouter = false;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "The router is not running!!", "", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btAddSubnet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunningRouter) {
                    String subnet = JOptionPane.showInputDialog(null, "Enter the subnet", "Add subnet", -1);
                    while (router.containsRoute(subnet)) {
                        JOptionPane.showMessageDialog(null, "The route already exists!!", "", JOptionPane.WARNING_MESSAGE);
                        subnet = JOptionPane.showInputDialog(null, "Enter the subnet", "Add subnet", -1);
                    }
                    if ((subnet != null) && !subnet.equals("")) {
                        try {
                            router.addSubnet(subnet);
                        } catch (IOException e1) {
                            // e1.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "The router is not running!!", "", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btShowRoutes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunningRouter) {
                    JOptionPane.showMessageDialog(null, router.showRoutes(), "Show Routes", -1);
                } else {
                    JOptionPane.showMessageDialog(null, "The router is not running!!", "", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btOpenLogbook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunningRouter) {
                    try {
                        router.openLogbook();
                    } catch (IOException e1) {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "The router is not running!!", "", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "¿Surely you want to exit the router?", "Exit", 2);
                if (option == 0) {
                    System.exit(0);
                }
            }
        });
    }

    public void startInterface() {
        JFrame frame = new JFrame("");
        frame.setContentPane(new MenuInterface().panel);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener( new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                int option = JOptionPane.showConfirmDialog(null, "¿Surely you want to exit the router?", "Exit", 2);
                if (option == 0) {
                    System.exit(0);
                }
            }
        } );

        frame.pack();
        this.isRunningRouter = false;
        frame.setVisible(true);
    }
}
