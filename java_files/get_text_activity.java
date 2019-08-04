package your_package.simple_wifi;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class get_text_activity extends AppCompatActivity {

	public TextView txt1;

	public EditText ed1;

	public Button b_ret_edit;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

    	get_a_number();
	}

	public void get_a_number(){

		setContentView(R.layout.enter_text);

		txt1 = (TextView) findViewById(R.id.txt1);

		txt1.setText(String.format("Current value %d)", ip_scan_screen.byte255 & 0xFF));

		ed1 = (EditText) findViewById(R.id.ed1);

		ed1.setInputType(InputType.TYPE_CLASS_NUMBER);

		ed1.setOnEditorActionListener(new EditText.OnEditorActionListener(){
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

				if (actionId == EditorInfo.IME_ACTION_DONE){

					String in = ed1.getText().toString();

                    if (in.length() > 0) {
                        ip_scan_screen.byte255 = Integer.parseInt(in);
                    }

                    if (ip_scan_screen.byte255 > 255) {

                        ip_scan_screen.byte255 = 255;
                    }
                    txt1.setText(String.format("Current value %d", ip_scan_screen.byte255 & 0xFF));

					return false;  // return true leaves keyboard visible and it's a pain to get rid of it
				}
				return false;
			}
		});

		b_ret_edit = (Button) findViewById(R.id.b_ret_edit);

		b_ret_edit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				finish();
			}
		});

		b_ret_edit.setText("return");
	}
}
