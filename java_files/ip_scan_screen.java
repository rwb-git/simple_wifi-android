package your_package.simple_wifi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class ip_scan_screen  extends AppCompatActivity {

    public static ip_screen_handler m_ip_scan_hndl;

    public static int mode = 0;

    public static byte test_IP_4 = 0;

    public static TextView iptxt;

    public static int byte255 = 0;

    public static ScrollView ipscrl;

    public static long time_start_millis,time_stop_millis,time_elapsed_millis;

    public static Button ip1,ip2,ip3,ip4h,ip4l,start_stop,reset,ret88,ip10,ip198;

    public ip_scan_screen() {

    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_scan_screen);

        iptxt = findViewById(R.id.iptxt);

        ipscrl = findViewById(R.id.ipscrl);

        m_ip_scan_hndl = new ip_screen_handler(this);

        ip1 = findViewById(R.id.ip1);
        ip1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                get_num(1,led_bt.IP_1);
            }
        });

        ip2 = findViewById(R.id.ip2);
        ip2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                get_num(2,led_bt.IP_2);
            }
        });

        ip3 = findViewById(R.id.ip3);
        ip3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                get_num(3,led_bt.IP_3);
            }
        });

        ip4h = findViewById(R.id.ip4h);
        ip4h.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                get_num(4,led_bt.IP_4_high);
            }
        });

        ip4l = findViewById(R.id.ip4l);
        ip4l.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                get_num(5,led_bt.IP_4_low);
            }
        });


        start_stop = findViewById(R.id.ipstart);
        start_stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //scan_ips();
                scan_multiple_ips();
            }
        });

        ret88 = findViewById(R.id.ret88823);
        ret88.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();
            }
        });

        reset = findViewById(R.id.restart);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                led_bt.IP_4_good = 2000;
            }
        });

        ip10 = findViewById(R.id.ip_10);
        ip10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                led_bt.IP_1 = 10;
                led_bt.IP_2 = 0;
                led_bt.IP_3 = 0;

                do_button_text();
            }
        });

        ip198 = findViewById(R.id.ip_192);
        ip198.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                led_bt.IP_1 = (byte)192;
                led_bt.IP_2 = (byte)168;
                led_bt.IP_3 = 0;

                do_button_text();

            }
        });


        do_button_text();

    }

    public void do_button_text(){

        ip1.setText(String.format("IP1\n%d",led_bt.IP_1 & 0xFF));

        ip2.setText(String.format("IP2\n%d",led_bt.IP_2 & 0xFF));

        ip3.setText(String.format("IP3\n%d",led_bt.IP_3 & 0xFF));

        ip4h.setText(String.format("IP4H\n%d",led_bt.IP_4_high & 0xFF));

        ip4l.setText(String.format("IP4L\n%d",led_bt.IP_4_low & 0xFF));

    }

    public void get_num(int mode1, int val){

        mode = mode1;

        byte255 = val;

        Intent ag = new Intent(getApplicationContext(), get_text_activity.class);
        startActivity(ag);
    }

    public void scan_multiple_ips(){


        if ((led_bt.IP_4 > led_bt.IP_4_high) || (led_bt.IP_4 < led_bt.IP_4_low)){
            led_bt.IP_4 = led_bt.IP_4_low;
        }
        led_bt.ipb[0] = (byte)led_bt.IP_1;
        led_bt.ipb[1] = (byte)led_bt.IP_2;
        led_bt.ipb[2] = (byte)led_bt.IP_3;
        led_bt.ipb[3] = (byte)led_bt.IP_4;


        if (led_bt.IP_4 < led_bt.IP_4_high){

            led_bt.IP_4++;
        } else {
            led_bt.IP_4 = led_bt.IP_4_low;
        }

        scroll_to_bottom();

        web.esp_msg_type = 14;

        time_start_millis = System.currentTimeMillis();

        for (int i=led_bt.IP_4_low;i<=led_bt.IP_4_high;i++) {

            test_IP_4 = (byte)i;

            web.esp_thread();

        }

    }



    @Override
    public void onResume() {

        super.onResume();



        switch (mode){

            case 1:

                led_bt.IP_1 = byte255;

                break;

            case 2:

                led_bt.IP_2 = byte255;

                break;

            case 3:

                led_bt.IP_3 = byte255;

                break;

            case 4:

                led_bt.IP_4_high = byte255;

                break;

            case 5:

                led_bt.IP_4_low = byte255;

                break;

        }

        do_button_text();

        mode = 0;

    }

    public static void add_a_text_line(String s){

        iptxt.append("\n" + s);

        scroll_to_bottom();
    }

    public static void scroll_to_bottom(){

        int lines = iptxt.getLineCount();

        if (lines > 300){

            for (int i=0;i<50;i++){
                Editable ed = iptxt.getEditableText();
                int start = iptxt.getLayout().getLineStart(0);
                int end = iptxt.getLayout().getLineEnd(0);

                ed.delete(start,end);
            }
        }

        ipscrl.post(new Runnable() {
            @Override
            public void run() {
                ipscrl.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }


}
