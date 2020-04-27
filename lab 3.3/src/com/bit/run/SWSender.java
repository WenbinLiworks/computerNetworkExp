package com.bit.run;

import com.bit.tools.CRC;
import com.bit.tools.FileBytesList;
import org.ini4j.Ini;


import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Random;

public class SWSender {

    public static void main(String[] args) throws Exception {
        Ini ini = new Ini(new File("./src/com/bit/StayWait.ini"));
        int myPort = Integer.parseInt(ini.get("Port", "senderPort"));
        int hisPort = Integer.parseInt(ini.get("Port", "receiverPort"));
        int FilterError = Integer.parseInt(ini.get("Filter", "FilterError"));
        int FilterLost = Integer.parseInt(ini.get("Filter", "FilterLost"));

        DatagramSocket datagramSocket = new DatagramSocket(myPort);
        DatagramPacket datagramSendPacket;
        DatagramPacket datagramRACKPacket;
        InetAddress inetAddress = InetAddress.getLocalHost();

        ArrayList<byte[]> fileArrayList = FileBytesList.ReadFile("./src/com/bit/z.txt");

        byte next_frame_to_send;
        byte[] s = new byte[7];
        next_frame_to_send = 0;

        Random random = new Random();
        int lostNo = random.nextInt(FilterLost);
        int errorNo = random.nextInt(FilterError);
        boolean lostFlag = false, errorFlag = false;

        int i = 0, j = 0, fileLength = fileArrayList.size();
        byte[] fileLen = Integer.toString(fileLength).getBytes();   //长度的ascii值

        // 建立连接，双方确认文件的长度(byte[]数组的数量)
        while (true) {
            datagramSendPacket = new DatagramPacket(fileLen, fileLen.length, inetAddress, hisPort);
            datagramSocket.send(datagramSendPacket);
            datagramSocket.setSoTimeout(1000);
            try {
                byte[] ack = new byte[1];
                datagramRACKPacket = new DatagramPacket(ack, ack.length);
                datagramSocket.receive(datagramRACKPacket);
                if (ack[0] == 1) {
                    System.out.println("file length: " + fileLength);
                    System.out.println("file length successfully");
                    break;
                }
            }
            catch (SocketTimeoutException e) {
                System.out.println("time out");
            }
        }
        System.out.println();

        while (true) {
            byte[] networkDataWithCRC = CRC.CRC_Bytes(fileArrayList.get(i));
            s[0] = next_frame_to_send;
            System.arraycopy(networkDataWithCRC, 0, s, 1, 6);

            System.out.println("Frame Sending...");
            System.out.println("next_frame_to_send: " + next_frame_to_send + ", send_frame_number: " + i);
            // lost的选择
            if (i % FilterLost == lostNo && !lostFlag) {
                System.out.println("frame lost");
                lostFlag = true;
            }
            else {
                // error的选择
                if (i % FilterError == errorNo && !errorFlag) {
                    s[random.nextInt(7)] ^= 1;
                    System.out.println("frame error");
                    errorFlag = true;
                }
                else  {
                    System.out.println("sending correct");
                }
                Thread.sleep(100);
                datagramSendPacket = new DatagramPacket(s, s.length, inetAddress, hisPort);
                datagramSocket.send(datagramSendPacket);
            }
            if (i % FilterError == 0 && i != 0) {
                errorFlag = false;
                errorNo = random.nextInt(FilterError);
            }
            if (i % FilterLost == 0 && i != 0) {
                lostFlag = false;
                lostNo = random.nextInt(FilterLost);
            }

            System.out.println("\nACK Receiving...");
            byte[] ack = new byte[1];
            datagramRACKPacket = new DatagramPacket(ack, ack.length);
            datagramSocket.setSoTimeout(2000);
            try {
                datagramSocket.receive(datagramRACKPacket);
                if (ack[0] == 1) {
                    System.out.println("ack: " + ack[0] + ", receive_frame_number: " + j);
                    System.out.println("receiving correct");
                    i++;
                    if (i == fileLength) {
                        break;
                    }
                    next_frame_to_send ^= 1;
                    j++;
                }
            }
            catch (SocketTimeoutException e) {
                System.out.println("time out");
            }
            System.out.println();
        }
    }
}
