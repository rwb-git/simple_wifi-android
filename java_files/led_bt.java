package your_package.simple_wifi;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class led_bt {


    public static byte[] ipb = {10,0,0,(byte)200};  // 200 nodemcu   201 wemos

    public static int IP_4 =  1; // used for scanning IPs
    public static int IP_4_low =  198;
    public static int IP_4_high =  205;
    public static int IP_4_good = 2000;

    public static int IP_1 =  10;
    public static int IP_2 =  0;
    public static int IP_3 =  0;

    public static void esp_socket(String s){ // this might have to be in a thread or asyncTask

        String ipstr = null;

        Socket socket = new Socket();

        boolean no_reply = true;

        byte save_ip4;

        try {

            InetAddress ip4 = InetAddress.getByAddress(ipb);

            save_ip4 = ipb[3];

            ipstr = ip4.toString();

            log(String.format("IP %s",ipstr));

            try {

                socket.connect(new InetSocketAddress(ip4, 80)); // I think this line fails if there is nothing at the IP, because nothing is written to the screen for those. If some other device
                // is at that IP, it prints "send this to esp:...". If I scan the low IPs where all my devices are, it seems to hang after the first one or two. Also, if I try to scan all the way to 254 or
                // so it seems to stop at a lower number. Maybe scanning is not such a great idea unless I know that my device is in a small range with nothing else there, and in that case I probably
                // know the actual IP anyway.

                try {
                    socket.setSoTimeout(300); // this does not do what I want. it takes a second or so even with 100 here
                    //
                    // however, this showed that multiple quick presses of scan IPs starts a bunch of threads and sockets (using same port which I don't
                    // know anything about), and if one in the middle gets a good reply, that IP is successfully saved. so an automated scan can look
                    // at (?) IPs at the same time it seems, and quickly find the right one if it's in the range.
                    //
                    // just never have two threads on the same IP or I might have problems with them both accessing same data here
                    //
                    // timing tests showed that correct IP returned in 300 msec or less.

                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                    DataInputStream dis = new DataInputStream(socket.getInputStream());

                    dos.writeBytes(s + "\r");

                   log( "send this to esp: " + s);
                   log( " ");

                    dos.flush();

                    byte[] inb = new byte[2000];

                    int b, tokint;

                    String s2; // = new String(inb);

                    b = dis.read(inb);

                    while (b > 2) { // ignore \r\n

                        ip_scan_screen.time_stop_millis = System.currentTimeMillis();

                        ip_scan_screen.time_elapsed_millis = ip_scan_screen.time_stop_millis - ip_scan_screen.time_start_millis;

                        no_reply = false;   // if there was no reply, no_reply will be true

                        s2 = new String(inb, 0, b);

                       log( "packet received from esp: ");

                       log( s2);

                        s2 = s2.replaceAll("(\\r|\\n)", "");

                        String[] tokens = s2.split(" ");

                        tokint = -1;

                        try {
                            tokint = Integer.parseInt(tokens[0]);
                        } catch (NumberFormatException n) {


                        }

                        switch (tokint) {

                            case 7:
                               log( "IP " + ipstr);
                                do_status(tokens);
                                break;

                            case 14:

                                if (check_ack(tokens)) {

                                    IP_4_good = save_ip4;


                                    log("device found at " + ipstr);

                                    log(String.format("elapsed millisecs %d", ip_scan_screen.time_elapsed_millis));
                                    // success takes 300 msec or less

                                }

                                break;
                        }

                        b = dis.read(inb);
                    }

                    dis.close();

                    dos.close();

                   log( " ");

                } catch (SocketException so){

                    log("socket exception at " + ipstr);
                }

            } catch (IOException e){

                log("IO exception at " + ipstr);
            }

        }catch(UnknownHostException u){

            log("unknownw host exception at " + ipstr);
        }

        if (no_reply){

            log("no reponse at " + ipstr);
        }
    }



    public static void do_status(String [] tokens){

        if (MainActivity.m_esp_hndl == null) {

            return;
        }

        int tokint = -1;

        for (int i=0;i<tokens.length;i++){

            switch(i){

                case 1:

                      log( "heap " + tokens[i]);

                    break;

                case 2:

                    int hrs,days;
                    try {
                        tokint = Integer.parseInt(tokens[i]);

                        if (tokint > 1439) {

                            days = tokint / 1440;
                            tokint -= days * 1440;

                        } else {
                            days = 0;
                        }

                        if (tokint > 59) {

                            hrs = tokint / 60;

                            tokint -= hrs * 60;
                        } else {

                            hrs = 0;
                        }

                       log( days + " days " + hrs + " hours " + tokint + " minutes");


                    } catch (NumberFormatException n){
                       log( "data error...");
                    }
                    break;

                case 3:

                   log( "reset reason " + tokens[i]);

                    break;

                case 4:

                   log( "sdk " + tokens[i]);

                    break;

                case 5:

                    try {
                        tokint = Integer.parseInt(tokens[i]);

                       log("cpu mhz " + tokint);

                    } catch (NumberFormatException n){
                       log( "data error...");
                    }

                    break;

                case 6:

                    try {
                        tokint = Integer.parseInt(tokens[i]);

                        log("sketch space " + tokint);

                    } catch (NumberFormatException n){
                       log( "data error...");
                    }

                    break;
            }
        }

       log( " ");
    }


    public static boolean check_ack(String [] tokens){

        if (tokens.length < 3){
            return false;
        }

        int ack1 = Integer.parseInt(tokens[1]);
        int ack2 = Integer.parseInt(tokens[2]);

        if ((ack1 == 44) && (ack2 == 55)){

            return true;
        } else {
            return false;
        }
    }

    public static void log(String s){

        switch (web.esp_msg_type){

            case web.normal_type:
                if (MainActivity.m_esp_hndl != null) {
                    MainActivity.m_esp_hndl.obtainMessage(4, s).sendToTarget();
                }
                break;

            case web.IP_type:

                if (ip_scan_screen.m_ip_scan_hndl != null) {

                    ip_scan_screen.m_ip_scan_hndl.obtainMessage(4, s).sendToTarget();
                }
                break;
        }
    }
}
