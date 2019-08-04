package xyz.fork20.mine.simple_wifi;

import android.os.Handler;
import android.os.Message;


public class esp_screen_handler   extends Handler {

	@Override
	public void handleMessage(Message msg) {

        MainActivity.add_a_text_line((String) msg.obj);   // this prepends "\n"
	}
}