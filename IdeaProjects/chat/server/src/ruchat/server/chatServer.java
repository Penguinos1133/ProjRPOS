package ruchat.server;

import ruchat.network.TCPConnectionListner;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class chatServer implements TCPConnectionListner
{
    public static void main(String[] args)
    {
        new chatServer();
    }
    private final ArrayList<ruchat.network.TCPConnection> connections = new ArrayList<>();
    private chatServer()
    {
        System.out.println("Server running...");
        try(ServerSocket serverSocket = new ServerSocket(8801))
        {
            while (true)
            {
                try
                {
                    new ruchat.network.TCPConnection(this, serverSocket.accept());
                }
                catch (IOException e)
                {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        }
        catch (IOException e)
        {
           throw new RuntimeException(e);
        }
    }


    @Override
    public synchronized void onConnectionReady(ruchat.network.TCPConnection tcpConnection)
    {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(ruchat.network.TCPConnection tcpConnection, String value)
    {
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(ruchat.network.TCPConnection tcpConnection)
    {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(ruchat.network.TCPConnection tcpConnection, Exception e)
    {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections(String value)
    {
        System.out.println(value);
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++)
        {
            connections.get(i).sendString(value);
        }
    }
}
