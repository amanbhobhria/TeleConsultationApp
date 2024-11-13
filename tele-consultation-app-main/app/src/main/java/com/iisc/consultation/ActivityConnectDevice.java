package com.iisc.consultation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.hrbledevice.CharacteristicsReceivedCallBacks;
import com.iisc.R;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.widget.Toast;

import java.util.UUID;

public class ActivityConnectDevice extends AppCompatActivity {

    private String macAddress;
    private BluetoothGatt bluetoothGatt;
    private static final UUID YOUR_SERVICE_UUID = UUID.fromString("YOUR_SERVICE_UUID");
    private static final UUID YOUR_ROTATION_UUID = UUID.fromString("YOUR_ROTATION_UUID");
    private static final UUID YOUR_BATTERY_UUID = UUID.fromString("YOUR_BATTERY_UUID");
    private BluetoothAdapter bluetoothAdapter;
    private CharacteristicsReceivedCallBacks characteristicsCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_device);

        // Retrieve MAC address from the intent
        macAddress = getIntent().getStringExtra("macAddress");

        // Initialize Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (macAddress != null && bluetoothAdapter != null) {
            initializeBluetoothConnection(macAddress);
        } else {
            Toast.makeText(this, "Bluetooth not available or MAC address missing.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set up callbacks for receiving data
        characteristicsCallbacks = new CharacteristicsReceivedCallBacks() {
            @Override
            public void onRotationCountReading(String count) {
                ((TextView) findViewById(R.id.countTxtView)).setText("Total Count: " + count);
            }

            @Override
            public void onBatteryReading(String batteryLevel) {
                ((TextView) findViewById(R.id.batteryTxtView)).setText("Battery Level: " + batteryLevel + "%");
            }

            @Override
            public void onConnected(String connectionStatus) {
                TextView connectionStatusTxt = findViewById(R.id.connectionStaus);
                boolean isConnected = Boolean.parseBoolean(connectionStatus);
                connectionStatusTxt.setText(isConnected ? "Connected" : "Failed");
                connectionStatusTxt.setTextColor(isConnected ? Color.GREEN : Color.RED);
            }
        };
    }

    private void initializeBluetoothConnection(String macAddress) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAddress);
        if (device != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback);
        } else {
            Toast.makeText(this, "Device not found.", Toast.LENGTH_SHORT).show();
        }
    }

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                runOnUiThread(() -> characteristicsCallbacks.onConnected("true"));
                if (ActivityCompat.checkSelfPermission(ActivityConnectDevice.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                runOnUiThread(() -> characteristicsCallbacks.onConnected("false"));
                closeBluetoothConnection();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Assuming that the device has characteristics for rotation count and battery level
                BluetoothGattService service = gatt.getService(YOUR_SERVICE_UUID);
                if (service != null) {
                    BluetoothGattCharacteristic rotationCharacteristic = service.getCharacteristic(YOUR_ROTATION_UUID);
                    BluetoothGattCharacteristic batteryCharacteristic = service.getCharacteristic(YOUR_BATTERY_UUID);

                    // Read rotation count characteristic
                    if (rotationCharacteristic != null) {
                        if (ActivityCompat.checkSelfPermission(ActivityConnectDevice.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        gatt.readCharacteristic(rotationCharacteristic);
                    }

                    // Read battery level characteristic
                    if (batteryCharacteristic != null) {
                        gatt.readCharacteristic(batteryCharacteristic);
                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                String value = characteristic.getStringValue(0);
                if (characteristic.getUuid().equals(YOUR_ROTATION_UUID)) {
                    runOnUiThread(() -> characteristicsCallbacks.onRotationCountReading(value));
                } else if (characteristic.getUuid().equals(YOUR_BATTERY_UUID)) {
                    runOnUiThread(() -> characteristicsCallbacks.onBatteryReading(value));
                }
            }
        }
    };

    private void closeBluetoothConnection() {
        if (bluetoothGatt != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeBluetoothConnection();
    }
}
