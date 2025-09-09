package com.example.foodike.presentation.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.UUID

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    private val bluetoothAdapter: BluetoothAdapter? =
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

    private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val devices: StateFlow<List<BluetoothDevice>> = _devices

    private val scanner = bluetoothAdapter?.bluetoothLeScanner
    private var isScanning = false

    private var socket: BluetoothSocket? = null

    // BLE (Android 12+)
    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            addDevice(result.device)
        }
    }

    // Clásico (Android 11 o menor)
    private val discoveryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let { addDevice(it) }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun addDevice(device: BluetoothDevice) {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return

        if (device.name != null) {
            val current = _devices.value.toMutableList()
            if (current.none { it.address == device.address }) {
                current.add(device)
                _devices.value = current
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startScan() {
        if (isScanning) return
        _devices.value = emptyList()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
                    scanner?.startScan(leScanCallback)
                    isScanning = true
                }
            } else {
                if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                    context.registerReceiver(discoveryReceiver, filter)
                    bluetoothAdapter?.startDiscovery()
                    isScanning = true
                }
            }
        } catch (e: SecurityException) {
            Log.e("BT", "No se tienen permisos para escanear", e)
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        if (!isScanning) return
        isScanning = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
                scanner?.stopScan(leScanCallback)
            }
        } else {
            try {
                context.unregisterReceiver(discoveryReceiver)
            } catch (_: Exception) {}
            bluetoothAdapter?.cancelDiscovery()
        }
    }



    fun connectToDevice(device: BluetoothDevice) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !hasPermission(Manifest.permission.BLUETOOTH_CONNECT)
        ) {
            Log.e("BT", "No tiene permiso BLUETOOTH_CONNECT")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                socket = device.createRfcommSocketToServiceRecord(
                    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                )
                bluetoothAdapter?.cancelDiscovery()
                socket?.connect()
                Log.d("BT", "Conectado a ${device.name}")
            } catch (e: SecurityException) {
                Log.e("BT", "Permiso no concedido", e)
            } catch (e: Exception) {
                Log.e("BT", "Error conectando", e)
            }
        }
    }


    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                socket?.outputStream?.write(message.toByteArray())
            } catch (e: Exception) {
                Log.e("BT", "Error enviando mensaje", e)
            }
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

}
