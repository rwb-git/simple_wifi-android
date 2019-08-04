package your_package.simple_wifi;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class ip_screen_handler   extends Handler {

	private final WeakReference<ip_scan_screen> myActivity;

	// weak reference allows garbage collection to clean up even if delayed messages still link to finished activities.
    // otherwise it can leak memory because of reference to dead activity or something like that

	public ip_screen_handler(ip_scan_screen act) {

		myActivity = new WeakReference<ip_scan_screen>(act);

	}

	@Override
	public void handleMessage(Message msg) {

		ip_scan_screen act2 = myActivity.get();

		if (act2 != null) {

            ip_scan_screen.add_a_text_line((String) msg.obj);   // this prepends "\n"
		}
	}
}
