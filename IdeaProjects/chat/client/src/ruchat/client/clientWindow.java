package ruchat.client;

import ruchat.network.TCPConnection;
import ruchat.network.TCPConnectionListner;

import  javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class clientWindow extends JFrame implements ActionListener, TCPConnectionListner
{
    private  static  final String ipAddr = "192.168.43.160";
    private  static  final int PORT = 8801;
    private  static final int WIDTH = 600;
    private  static final int HEIGHT = 400;
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                new clientWindow();
            }
        });
    }
    private JTextArea log = new JTextArea();
    private final JTextField fieldNikname = new JTextField();
    private final JTextField fieldInput = new JTextField();
    private TCPConnection connection;
    private  clientWindow()
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        log.setEditable(false);
        log.setLineWrap(true);
        fieldInput.addActionListener(this);
        add(log, BorderLayout.CENTER);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNikname, BorderLayout.NORTH);
        setVisible(true);
        try {
            connection = new TCPConnection(this, ipAddr, PORT);
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String msg = fieldInput.getText();
        if(msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendString(fieldNikname.getText() + ": " + msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection)
    {
        printMsg("Connection ready... ");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value)
    {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection)
    {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e)
    {
        printMsg("Connection exception: " + e);
    }
    private synchronized void printMsg(String msg)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}