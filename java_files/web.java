package xyz.fork20.mine.simple_wifi;

public class web {

    public static int esp_msg_type = 0;

    public static final int normal_type = 7;

    public static final int IP_type = 14;

    public static espthread2 espThread;

    public static void esp_thread(){

        if (espThread != null) {

            if (espThread.isAlive()){

                if (esp_msg_type == IP_type) { // scanning IPs so kill thread that got no response
                    espThread.interrupt();

                } else {

                    return; // don't send too quickly
                }
            }

            espThread = null;
        }

        espThread = new espthread2();

        espThread.start();
    }


    public static class espthread2 extends Thread {

        public espthread2() {

        }

        public void simple_msg(int i){

            led_bt.esp_socket(String.format("%d 0",i));
            // the second arg, 0, is not actually used for anything, but the esp8266 sketch expects it so it has to be provided. it tells the sketch that
            // there are no further bytes coming. so, it could be deleted there as well. I left it in both places because I derived this code from a
            // project that sends additional data and it was easier to leave it in place, and theoretically if someone uses this code in their project
            // it will make it easier for them to send addtional data. or maybe not. my other project "tiny_wifi" has similar code that does not send
            // the "0", so if you want to get rid of it you might want to just use code from that app and sketch which are on github as well.
        }

        public void run() {

            switch(esp_msg_type){

                case web.normal_type:

                    if (led_bt.IP_4_good != 2000){  // IP_4_good is a flag that is < 2000 if you scanned and found your IP. otherwise it uses the hard-coded IP in ipb[]
                        led_bt.ipb[3] = (byte) (led_bt.IP_4_good & 0x00FF);
                    }
                    break;

                case web.IP_type:

                    led_bt.ipb[3] = ip_scan_screen.test_IP_4;

                    break;
            }

            simple_msg(esp_msg_type);

            return;
        }
    }
}
