/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat_ant;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import javax.swing.JTextArea;


public class ClientHandler implements Runnable {
    private String nick;
    private InetAddress group;
    private int port;
    private MulticastSocket socket;
    private JTextArea chatArea;

    public ClientHandler(String nick, InetAddress group, int port, JTextArea chatArea) throws IOException {
        this.nick = nick;
        this.group = group;
        this.port = port;
        this.socket = new MulticastSocket(port);
        this.chatArea = chatArea;
        socket.joinGroup(group);
       
    }

    private ClientHandler(String nick, InetAddress group, int port) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void run() {
        while (true) {
            try {
                DatagramPacket packet = receivePacket();
                String message = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
                updateChatArea(message); // Thay đổi TextArea thông qua phương thức nà
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void updateChatArea(String message) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                chatArea.append(message + "\n");
            }
        });
    }

    private DatagramPacket receivePacket() throws IOException {
        byte[] buffer = new byte[65507];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return packet;
    }

    public void sendMessage(String message) throws IOException {
        byte[] utf = (nick + ": " + message).getBytes("UTF-8");
        DatagramPacket packet = new DatagramPacket(utf, utf.length, group, port);
        socket.send(packet);
    }

}
