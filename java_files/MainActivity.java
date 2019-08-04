/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package xyz.fork20.mine.simple_wifi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    public static esp_screen_handler m_esp_hndl;

    public static TextView esptxt;

    public static ScrollView espscrl;

    public static Button get_status, scan_ips;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esp_screen);

        esptxt = findViewById(R.id.esptxt);

        espscrl = findViewById(R.id.espscrl);

        m_esp_hndl = new esp_screen_handler();

        get_status = findViewById(R.id.get_status);
        get_status.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                web.esp_msg_type = web.normal_type;
                web.esp_thread();
            }
        });

        scan_ips = findViewById(R.id.scan_ips);
        scan_ips.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent ag = new Intent(getApplicationContext(), ip_scan_screen.class);
                startActivity(ag);
            }
        });
    }


    @Override
    public void onResume() {

        super.onResume();

        scroll_to_bottom();
    }

    public static void add_a_text_line(String s){

        esptxt.append("\n" + s);

        scroll_to_bottom();
    }


    public static void scroll_to_bottom(){

        int lines = esptxt.getLineCount();

        if (lines > 300){

            for (int i=0;i<50;i++){
                Editable ed = esptxt.getEditableText();
                int start = esptxt.getLayout().getLineStart(0);
                int end = esptxt.getLayout().getLineEnd(0);

                ed.delete(start,end);
            }
        }

        espscrl.post(new Runnable() {
            @Override
            public void run() {
                espscrl.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
