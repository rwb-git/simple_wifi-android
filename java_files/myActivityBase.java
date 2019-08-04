package xyz.fork20.mine.simple_wifi;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by user on 2/1/18.
 */

public class myActivityBase  extends AppCompatActivity {
	// subclassed by main, info, prefs, second, plots_layout

	// DONT FORGET TO PUT ALL ACTIVITIES IN MANIFEST

	// this class starts a thread so you can perform activities regularly. set thread_timer in create_stuff in
	// your class, ot t.interrupt if you don't need it
	//
	// if you tap your screen and don't intercept it, this class returns to menu. before I changed all the mainactivity
	// views to lists which intercept taps, it would stop the app when you tapped mainactivity screen, so i used
	// a flag to work around that.
	//
	// this class has its own handler so you can finish() your activity and return to menu. otherwise I don't know
	// how to stop things like basic_plot

	public int thread_timer = 4; // default milliseconds to wake up your activity to do_stuff()

	public ActionBar actionBar;
	public GestureDetector mDetector;

	public ScaleGestureDetector mScale;

	public Thread t;
	public int ht;

	public String owner = "none";

	public static Handler handler;      // bluetoothchat handlers were final. all the other ones are ok being final. but
	// not this one. the reason appears to be that i have to assign it later, and it also
	// has to be static so that the intent instance gets a non-null handler (I don't knw
	// how to access an intent instance; the constructor hs to have zero args). so, mainactivity
	// assigns it (and my_ftdi) later, either to an unused instance or to class fields. in both
	// cases it has to be static and therefore it cannot be final unless it is created/assigned
	// here, and then it would be different from the one used elsewhere. or whatever.




	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// when actionbar is shown, tap does not close info screen. focus issue?
//		actionBar = getActionBar();

//		actionBar.hide();

		mDetector = new GestureDetector(this, new MyGestureListener());
		mScale = new ScaleGestureDetector(this, new ScaleListener());
		create_stuff();
	}

	public void create_stuff(){  // oncreate calls this
		// override this
	}


	public void do_stuff(){ // about twice per second.
		// override this

	}



	@Override
	public void onStop() {

		super.onStop();

		t.interrupt();

		//BluetoothChatFragment.navigate = Constants.nav_menu;
	}


	@Override
	public void onPause(){
		super.onPause();

	}

	@Override
	public void onResume() {

		super.onResume();

		hndlr_msg(" myactivitybase thread onResume");

		t = new Thread() {

			@Override
			public void run() {

				Thread.currentThread().setName(owner);
				try {
					while (!isInterrupted()) {

						Thread.sleep(thread_timer);              // change this to a var to let subclasses set it. use methods.

						runOnUiThread(new Runnable() {              // has to runonui for all the things on second_screen
							@Override
							public void run() {

								do_stuff();
							}
						});
					}

					hndlr_msg(" myactivitybase thread interrupted 1");


				} catch (InterruptedException e) {

					hndlr_msg(" myactivitybase thread interrupted 2");
				}
			}
		};
		t.start();
	}

	public void hndlr_msg(String s){

		if (handler != null) {
			handler.obtainMessage(158,owner + s).sendToTarget();

		}
	}
	public void draw_everything() {



	}


	@Override
	public boolean onTouchEvent(MotionEvent event){

		this.mDetector.onTouchEvent(event);

		this.mScale.onTouchEvent(event);

		draw_everything(); // if i do this in gridlines it lags
/*
        //int action = MotionEvent.getActionMasked();                   // this fails because it is non- while something about
        // the current context is .
        int action = MotionEventCompat.getActionMasked(event);          // this works because it is


        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                return true;

            case (MotionEvent.ACTION_MOVE) :
                return true;

            case (MotionEvent.ACTION_SCROLL) :

                //              last=0;
                return true;


            case (MotionEvent.ACTION_UP) :
                return true;

            default :
                return super.onTouchEvent(event);
        }
        */

		return false;
	}


	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		private  final String DEBUG_TAG = "Gestures";

		@Override
		public boolean onDown(MotionEvent event) {

			//        last=0;
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
							   float velocityX, float velocityY) {

			return true;
		}

		@Override
		public void onLongPress(MotionEvent event) {


		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
								float distanceY) {




			return true;
		}

		@Override
		public void onShowPress(MotionEvent event) {

		}

		@Override
		public boolean onSingleTapUp(MotionEvent event) {
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent event) {

			t.interrupt();

			//BluetoothChatFragment.navigate = Constants.nav_radio;

			finish(); // this only works if i tap a blank space below the text.

			return true;
		}


		@Override
		public boolean onDoubleTapEvent(MotionEvent event) {

			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {

			t.interrupt();

			//BluetoothChatFragment.navigate = Constants.nav_radio;

			finish(); // this only works if i tap a blank space below the text.

			return true;
		}
	}



	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

		@Override
		public boolean onScale(ScaleGestureDetector det){

			return true;
		}
	}





}
