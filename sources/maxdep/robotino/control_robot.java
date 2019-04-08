package maxdep.robotino;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.UUID;

public class control_robot extends AppCompatActivity {
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String address_device = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    private Runnable motor_1 = new Runnable() {
        public void run() {
            if (control_robot.this.vitesse < 100 && control_robot.this.vitesse >= 10) {
                control_robot.this.send_msg_bt("1m10" + control_robot.this.vitesse);
            } else if (control_robot.this.vitesse < 10) {
                control_robot.this.send_msg_bt("1m100" + control_robot.this.vitesse);
            } else {
                control_robot.this.send_msg_bt("1m1" + control_robot.this.vitesse);
            }
        }
    };
    private Runnable motor_2 = new Runnable() {
        public void run() {
            if (control_robot.this.vitesse < 100 && control_robot.this.vitesse >= 10) {
                control_robot.this.send_msg_bt("1m20" + control_robot.this.vitesse);
            } else if (control_robot.this.vitesse < 10) {
                control_robot.this.send_msg_bt("1m200" + control_robot.this.vitesse);
            } else {
                control_robot.this.send_msg_bt("1m2" + control_robot.this.vitesse);
            }
        }
    };
    BluetoothAdapter myBluetooth = null;
    Handler myHandler = new Handler();
    private ProgressDialog progress;
    String value_ordre;
    int vitesse;

    /* renamed from: maxdep.robotino.control_robot$3 */
    class C02153 implements OnClickListener {
        C02153() {
        }

        public void onClick(View v) {
            control_robot.this.Disconnect();
        }
    }

    /* renamed from: maxdep.robotino.control_robot$4 */
    class C02164 implements OnClickListener {
        C02164() {
        }

        public void onClick(View v) {
            control_robot.this.myHandler.postDelayed(control_robot.this.motor_2, 0);
        }
    }

    /* renamed from: maxdep.robotino.control_robot$5 */
    class C02175 implements OnClickListener {
        C02175() {
        }

        public void onClick(View v) {
            control_robot.this.myHandler.postDelayed(control_robot.this.motor_1, 0);
            control_robot.this.myHandler.postDelayed(control_robot.this.motor_2, 2000);
        }
    }

    /* renamed from: maxdep.robotino.control_robot$6 */
    class C02186 implements OnClickListener {
        C02186() {
        }

        public void onClick(View v) {
            control_robot.this.myHandler.postDelayed(control_robot.this.motor_1, 0);
            control_robot.this.myHandler.postDelayed(control_robot.this.motor_2, 3000);
        }
    }

    /* renamed from: maxdep.robotino.control_robot$7 */
    class C02197 implements OnClickListener {
        C02197() {
        }

        public void onClick(View v) {
            control_robot.this.myHandler.postDelayed(control_robot.this.motor_1, 0);
            control_robot.this.myHandler.postDelayed(control_robot.this.motor_2, 1000);
        }
    }

    /* renamed from: maxdep.robotino.control_robot$8 */
    class C02208 implements OnClickListener {
        C02208() {
        }

        public void onClick(View v) {
            control_robot.this.send_msg_bt("1m0000");
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess;

        private ConnectBT() {
            this.ConnectSuccess = true;
        }

        protected void onPreExecute() {
            control_robot.this.progress = ProgressDialog.show(control_robot.this, "Connection...", "En cours de traitement");
        }

        protected Void doInBackground(Void... devices) {
            try {
                if (control_robot.this.btSocket == null || !control_robot.this.isBtConnected) {
                    control_robot.this.myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = control_robot.this.myBluetooth.getRemoteDevice(control_robot.this.address_device);
                    control_robot.this.btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(control_robot.myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    control_robot.this.btSocket.connect();
                }
            } catch (IOException e) {
                this.ConnectSuccess = false;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (this.ConnectSuccess) {
                control_robot.this.msg("Connecté.");
                control_robot.this.isBtConnected = true;
            } else {
                control_robot.this.msg("La connexion a échoué. Est-ce un Bluetooth SPP? Réessayer.");
            }
            control_robot.this.progress.dismiss();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0212R.layout.activity_control_robot);
        this.address_device = getIntent().getStringExtra(Choix_Bluetooth.EXTRA_ADDRESS);
        final EditText terminal = (EditText) findViewById(C0212R.id.input_msg_test);
        final TextView txt_term = (TextView) findViewById(C0212R.id.text_command);
        final Switch bt_test = (Switch) findViewById(C0212R.id.switch_modeTest);
        Button btn_deco = (Button) findViewById(C0212R.id.button_deco);
        Button bt_forward = (Button) findViewById(C0212R.id.bt_forward);
        Button bt_backward = (Button) findViewById(C0212R.id.bt_backward);
        Button bt_left = (Button) findViewById(C0212R.id.bt_left);
        Button bt_right = (Button) findViewById(C0212R.id.bt_right);
        Button bt_stop = (Button) findViewById(C0212R.id.bt_stop);
        final TextView value_progress = (TextView) findViewById(C0212R.id.textView2);
        SeekBar progress_value = (SeekBar) findViewById(C0212R.id.seekBar);
        new ConnectBT().execute(new Void[0]);
        bt_test.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (bt_test.isChecked()) {
                    control_robot.this.send_msg_bt("1m0000");
                    control_robot.this.vitesse = 0;
                    control_robot.this.findViewById(C0212R.id.txt_input).setEnabled(true);
                    control_robot.this.findViewById(C0212R.id.txt_input).setVisibility(0);
                    terminal.setEnabled(true);
                    terminal.setVisibility(0);
                    return;
                }
                control_robot.this.findViewById(C0212R.id.txt_input).setEnabled(false);
                control_robot.this.findViewById(C0212R.id.txt_input).setVisibility(4);
                terminal.setEnabled(false);
                terminal.setVisibility(4);
            }
        });
        progress_value.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value_progress.setText("Скорость : " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                control_robot.this.vitesse = seekBar.getProgress();
            }
        });
        btn_deco.setOnClickListener(new C02153());
        bt_forward.setOnClickListener(new C02164());
        bt_backward.setOnClickListener(new C02175());
        bt_left.setOnClickListener(new C02186());
        bt_right.setOnClickListener(new C02197());
        bt_stop.setOnClickListener(new C02208());
        terminal.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode != 66) {
                    return false;
                }
                if (event.getAction() != 1) {
                    return true;
                }
                control_robot.this.value_ordre = terminal.getText().toString();
                control_robot.this.send_msg_bt(control_robot.this.value_ordre);
                txt_term.setText("$: " + control_robot.this.value_ordre.getBytes());
                terminal.setText("");
                control_robot.this.msg("Ordre envoyé");
                return true;
            }
        });
    }

    private void Disconnect() {
        if (this.btSocket != null) {
            try {
                this.btSocket.close();
            } catch (IOException e) {
                msg("Error");
            }
        }
        finish();
    }

    private void send_msg_bt(String msg) {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write(msg.getBytes());
                msg(msg);
            } catch (IOException e) {
                msg("Error, message : " + msg);
            }
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, 1).show();
    }
}
