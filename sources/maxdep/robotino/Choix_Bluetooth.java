package maxdep.robotino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

public class Choix_Bluetooth extends AppCompatActivity {
    public static String EXTRA_ADDRESS = "device_address";
    private Set<BluetoothDevice> Bluetooth_devices;
    private OnItemClickListener Device_choice = new C02112();
    ListView devicelist;
    private BluetoothAdapter myBluetooth = null;

    /* renamed from: maxdep.robotino.Choix_Bluetooth$1 */
    class C02101 implements OnClickListener {
        C02101() {
        }

        public void onClick(View view) {
            Snackbar.make(view, (CharSequence) "Recherche des robots...", 0).setAction((CharSequence) "Action", null).show();
            Choix_Bluetooth.this.rechercheBluetoothList();
        }
    }

    /* renamed from: maxdep.robotino.Choix_Bluetooth$2 */
    class C02112 implements OnItemClickListener {
        C02112() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int arg2, long arg3) {
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent i = new Intent(Choix_Bluetooth.this, control_robot.class);
            i.putExtra(Choix_Bluetooth.EXTRA_ADDRESS, address);
            Choix_Bluetooth.this.startActivity(i);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0212R.layout.activity_choix__bluetooth);
        setSupportActionBar((Toolbar) findViewById(C0212R.id.toolbar));
        this.devicelist = (ListView) findViewById(C0212R.id.listView);
        this.myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (this.myBluetooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth non disponible", 1).show();
            finish();
        } else if (!this.myBluetooth.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
        ((FloatingActionButton) findViewById(C0212R.id.fab)).setOnClickListener(new C02101());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0212R.menu.menu_choix__bluetooth, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C0212R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void rechercheBluetoothList() {
        this.Bluetooth_devices = this.myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();
        if (this.Bluetooth_devices.size() > 0) {
            for (BluetoothDevice bt : this.Bluetooth_devices) {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Aucun robot détecté.", 1).show();
        }
        this.devicelist.setAdapter(new ArrayAdapter(this, 17367043, list));
        this.devicelist.setOnItemClickListener(this.Device_choice);
    }
}
