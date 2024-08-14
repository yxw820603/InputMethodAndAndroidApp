package com.example.androidapp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;

public class UdpSender {
    private DatagramSocket socket;
    private String address;
    private int port;

    public UdpSender(String address, int port) throws IOException {
        this.address = address;
        this.port = port;
        this.socket = new DatagramSocket();
    }

    public void send(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(address), port);
        socket.send(packet);
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
