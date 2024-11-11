package com.iisc.consultation;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.iisc.R;

public class DeviceAnalytics extends AppCompatActivity {

    // Constants
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private static final int REQUEST_BLUETOOTH_SCAN_PERMISSION = 2;
    private static final int REQUEST_ENABLE_BT=0;

    // UI components
    TextView btStatus;
    Button turnOn, turnOff, search;
    BluetoothAdapter bluetoothAdapter;

    // ActivityResultLauncher for enabling Bluetooth
    private ActivityResultLauncher<Intent> enableBluetoothLauncher;
    private ActivityResultLauncher<Intent> discoverableLauncher;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_analytics);

        // Initialize UI components
        btStatus = findViewById(R.id.btStatus);
        turnOn = findViewById(R.id.turnOn);
        turnOff = findViewById(R.id.turnOff);
        search = findViewById(R.id.searchDevice);

        // Initialize BluetoothAdapter
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
         this.bluetoothAdapter = bluetoothManager.getAdapter();

        // Check if Bluetooth is available
        if (bluetoothAdapter != null) {
            btStatus.setText("Bluetooth is available.");
        } else {
            btStatus.setText("Bluetooth is not available.");
        }

        // Register ActivityResultLauncher for enabling Bluetooth
        enableBluetoothLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        btStatus.setText("Bluetooth is turned on.");
                    } else {
                        btStatus.setText("Failed to enable Bluetooth.");
                    }
                }
        );

        // Register ActivityResultLauncher for making the device discoverable
        discoverableLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle successful result
                        btStatus.setText("Device is discoverable.");
                    } else {
                        // Handle cancellation or other result codes
                        btStatus.setText("Discoverability request was cancelled.");
                    }
                }
        );

        // Set OnClickListener for turning on Bluetooth
        turnOn.setOnClickListener(view -> {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            }
//            if (!bluetoothAdapter.isEnabled()) {
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
//                    return;
//                }
//                btStatus.setText("Turning on Bluetooth...");
//                enableBluetoothLauncher.launch(intent);
//            } else {
//                btStatus.setText("Bluetooth is already on.");
//            }
        });

        // Set OnClickListener for searching devices
        search.setOnClickListener(view -> {
//            if (!bluetoothAdapter.isDiscovering()) {
//                btStatus.setText("Making your Device discoverable.");
//
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, REQUEST_BLUETOOTH_SCAN_PERMISSION);
//                } else {
//                    // Permission is granted, proceed with making the device discoverable
//                    makeDeviceDiscoverable();
//                }
//            }
        });

        // Set OnClickListener for turning off Bluetooth
        turnOff.setOnClickListener(view -> {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
                btStatus.setText("Bluetooth is turned off.");
            } else {
                btStatus.setText("Bluetooth is already off.");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_SCAN_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeDeviceDiscoverable();
            } else {
                btStatus.setText("Bluetooth scan permission is required to make the device discoverable.");
            }
        }
    }

    private void makeDeviceDiscoverable() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300); // Optional: set discoverable duration (300 seconds = 5 minutes)
        discoverableLauncher.launch(intent);
    }
}
