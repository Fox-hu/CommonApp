package com.silver.fox.hardware.bluetooth

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.text.TextUtils
import android.util.Log

class HeadSetHelp(val context: Context, val onHeadsetInterface: OnHeadsetInterface) {
    private val TAG = "HeadSetHelp"
    /** 用来判断耳机的状态：
     * 0：有线耳机已断开
     * 1：有线耳机已连接
     */
    companion object{
        const val HEAD_SET_OFF = 0
        const val HEAD_SET_ON = 1
        /**
         * 0：蓝牙耳机已断开
         * 1：蓝牙耳机已连接 */
        private const val HEAD_BLUE_SET_OFF = 0
        private const val HEAD_BLUE_SET_ON = 1
        private var headSetState = HEAD_SET_OFF
        private var blueHeadSetState = HEAD_BLUE_SET_OFF
    }

    private val audioManager: AudioManager
    private var isEarphone = false

    private val mBroadReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (TextUtils.equals(Intent.ACTION_HEADSET_PLUG, action)) {
                val flag = intent.getIntExtra("state", -1)
                val name = intent.getStringExtra("name")
                val hasMic = intent.getIntExtra("microphone", -1)
                Log.d(TAG,
                    "onReceive: headset plug: flag: $flag, name: $name, hasMic:$hasMic"
                )
                if (flag == 1) {
                    headSetState = HEAD_SET_ON
                } else if (flag == 0) {
                    headSetState = HEAD_SET_OFF
                }
                setSpeakState()
            } else if (TextUtils.equals(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED, action)) {
                val device: BluetoothDevice?
                val defaultAdapter = BluetoothAdapter.getDefaultAdapter()
                //记得加上蓝牙权限
                val state = defaultAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET)
                Log.d(TAG, "device connect state:$state")
                when (intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, -1)) {
                    BluetoothA2dp.STATE_CONNECTED -> {
                        device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        Log.d(TAG, "onReceive: name: ${device?.name} connected")
                        blueHeadSetState = HEAD_BLUE_SET_ON
                        setSpeakState()
                    }
                    BluetoothA2dp.STATE_DISCONNECTED -> {
                        device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        Log.d(TAG,"onReceive device name:${device?.name}  disconnect ")
                        blueHeadSetState = HEAD_BLUE_SET_OFF
                        setSpeakState()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    init {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        getBlueDeviceState() // 先获取蓝牙状态再添加广播，防止插拔事件广播先到
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_HEADSET_PLUG)
        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)
        context.registerReceiver(mBroadReceiver, filter, null, null)
    }

    private fun getBlueDeviceState() {
        val defaultAdapter = BluetoothAdapter.getDefaultAdapter()
        //记得加上蓝牙权限
        val state = defaultAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET)
        Log.d(TAG, "getBlueDeviceState connect state:$state")
        if (state == BluetoothHeadset.STATE_CONNECTED) {
            blueHeadSetState = HEAD_BLUE_SET_ON
        } else if (state == BluetoothHeadset.STATE_DISCONNECTED) {
            blueHeadSetState = HEAD_BLUE_SET_OFF
        }
        setSpeakState()
    }

    private fun setSpeakState() {
        Log.d(TAG,
            "setSpeakState headSetState:$headSetState blueHeadSetState:$blueHeadSetState isEarphone:$isEarphone")
        val tempEarphone = blueHeadSetState == HEAD_BLUE_SET_ON || headSetState == HEAD_SET_ON
        if (isEarphone == tempEarphone) {
            return
        }
        isEarphone = tempEarphone
        // todo 不插耳机的情况下是否走外放是否也要设置一下，但是业务端容易出问题？比如，会不会插耳机他也设外放
        audioManager.isSpeakerphoneOn = !isEarphone
        onHeadsetInterface.onHeadsetStateChange(isEarphone)
    }

    /**
     * 是否是耳机模式( 蓝牙或者有线耳机都包括)
     */
    fun isHeadSetMode(): Boolean {
        Log.d(TAG,
            "isHeadSetMode headSetState:$headSetState blueHeadSetState:$blueHeadSetState")
        return isEarphone
    }

    fun destroy() {
        try {
            this.context.unregisterReceiver(mBroadReceiver)
        } catch (e: Exception) {
            Log.e(TAG, "destroy: unregisterReceiver error:${e.message}" )
        }
    }

    interface OnHeadsetInterface{
        fun onHeadsetStateChange(earphone: Boolean)
    }
}