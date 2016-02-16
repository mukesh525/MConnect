package in.vmc.mcubeconnect.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import in.vmc.mcubeconnect.R;
import in.vmc.mcubeconnect.utils.Utils;

public class Welcome extends AppCompatActivity implements RangeNotifier, BeaconConsumer {
    private BeaconManager mBeaconManager;
    private ListView lv;
    private Boolean enable = false;
    private Boolean first = false;
    private ProgressDialog pd;
    private Button navigate;
    private ArrayList<Beacon> mbeacons = new ArrayList<Beacon>();
    private int widthPixels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


//        lv = (ListView) findViewById(R.id.listView);
//        enableBluetooth();
//        navigate= (Button) findViewById(R.id.button);
//      //  pd = ProgressDialog.show(this, "Processing Your Location", "Please wait..!!", true);
//        adapter = new BeaconAdapter(mbeacons, Welcome.this);
//        lv.setAdapter(adapter);
//        navigate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(Utils.isLogin(Welcome.this))
//                {
//                    startActivity(new Intent(Welcome.this, Home.class));
//                    overridePendingTransition(0, 0);
//                    Log.d("LOG","User is Loggedin");
//                }
//                else {
//                    Log.d("LOG","User is not Loggedin");
//                    startActivity(new Intent(Welcome.this, LoginActivity.class));
//                    overridePendingTransition(0, 0);
//
//                }
//            }
//        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.isLogin(Welcome.this)) {
                    startActivity(new Intent(Welcome.this, Home.class));
                    overridePendingTransition(0, 0);
                    Log.d("LOG", "User is Loggedin");
                } else {
                    Log.d("LOG", "User is not Loggedin");
                    startActivity(new Intent(Welcome.this, LoginActivity.class));
                    overridePendingTransition(0, 0);

                }

            }
        }, 800);

    }

    private void enableBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            enable = true;
        } else {
            enable = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
//        // Detect the main Eddystone-UID frame:
//        //  mBeaconManager.getBeaconParsers().add(new BeaconParser().
//        //        setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
//        //  mBeaconManager.setBackgroundBetweenScanPeriod(15000);
//        mBeaconManager.setBackgroundScanPeriod(1000);
//        mBeaconManager.setBackgroundBetweenScanPeriod(500);
//        mBeaconManager.bind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        mBeaconManager.unbind(this);
    }


    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
            // mBeaconManager.setBackgroundBetweenScanPeriod(15000);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // mBeaconManager.setBackgroundScanPeriod(1000);
        //mBeaconManager.setBackgroundBetweenScanPeriod(500);
        mBeaconManager.setRangeNotifier(this);
    }


    @Override
    public void onBackPressed() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!enable) {
            super.onBackPressed();

        }
        if (mBluetoothAdapter.isEnabled() && enable) {
            mBluetoothAdapter.disable();
            // enable = false;
            super.onBackPressed();
        }


    }


    @Override
    public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, org.altbeacon.beacon.Region region) {

        for (final Beacon beacon : beacons) {
            mbeacons = new ArrayList<Beacon>(beacons);
            first = true;
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                final Identifier namespaceId = beacon.getId1();
                final Identifier instanceId = beacon.getId2();
                Log.d("RangingActivity", "I see a beacon transmitting namespace id: " + namespaceId +
                        " and instance id: " + instanceId +
                        " approximately " + beacon.getDistance() + " meters away.");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Collections.sort(mbeacons, new DistanceCompare());
//                        adapter = new BeaconAdapter(mbeacons, Welcome.this);
//                        lv.setAdapter(adapter);


                    }
                });
            }


        }


    }


    public class DistanceCompare implements Comparator<Beacon> {

        @Override
        public int compare(Beacon lhs, Beacon rhs) {
            Double distance = lhs.getDistance();
            Double distance1 = rhs.getDistance();
            if (distance.compareTo(distance1) < 0)
                return -1;
            else if (distance.compareTo(distance1) > 0)
                return 1;
            else
                return 0;
        }
    }


}

