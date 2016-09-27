package com.androidbeacon;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wizturn.sdk.central.CentralManager;

/**
 * Created by honggi on 2015-11-07.
 */
public class BeaconScanThread extends Thread {
    CentralManager centralManager;
    private final Handler mHandler;
    public boolean m_flag=true,m_SendFlag=false;
    Message msg;
    public BeaconScanThread(CentralManager _central, Handler _scan){
        centralManager=_central;
        mHandler=_scan;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        super.run();
        centralManager.startScanning();
        while(m_flag)
        {

            try {
//                msg = mHandler.obtainMessage(ScanConnActivity.MESSAGE_SCANNING);
//                mHandler.sendMessage(msg);
                while (m_SendFlag);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (m_SendFlag);
                if(!m_flag)break;
                msg = mHandler.obtainMessage(ScanConnActivity.MESSAGE_SELECTING);
                mHandler.sendMessage(msg);
            }catch (Exception e)
            {
                Log.e("Thread", e.toString());
            }
        }
        centralManager.stopScanning();
    }

}

