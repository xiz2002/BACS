package com.androidbeacon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.wizturn.sdk.central.Central;
import com.wizturn.sdk.central.CentralManager;
import com.wizturn.sdk.peripheral.Peripheral;
import com.wizturn.sdk.peripheral.PeripheralScanListener;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Handler;



/**
 * Created by honggi on 2015. 11. 5..
 */
public class ScanConnActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

//    Button button1;

    public String TAG = "ScanConnActivity";
    public static final int MESSAGE_SELECTING =  1;
    public static final int MESSAGE_SCANNING =  2;
    public static final int MESSAGE_SERVERSTATE =  3;
    public static final int MESSAGE_SCANNINGSTATE =  4;
    public static final int MESSAGE_SCANSTOP = 5;

    private CentralManager centralManager = null;
    private ArrayList<BeaconDistanceThread> m_BeaconDistance;

    private TextView tvState,tvReceive;
    private BeaconScanThread beaconScanThread;
    private String m_strId,m_strLOC_CODE,m_strCOU_CODE;

    private final double DISTANCE = 0.08;
    private ArrayList<String> m_strBeaconMacs;

    private TextToSpeech ttsClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_conn);
        Intent intent = getIntent();
        m_strId=intent.getExtras().getString("ID");
        m_strBeaconMacs=intent.getExtras().getStringArrayList("BEACONMAC");
        m_strLOC_CODE=m_strBeaconMacs.get(0);
        m_strCOU_CODE=intent.getExtras().getString("LessonCode");
        setTitle(m_strId + " : " + intent.getExtras().getString("RoomNum") + "에서 출석 체크 중");
        tvState=(TextView)findViewById(R.id.tvState);
        tvReceive=(TextView)findViewById(R.id.tvReceive);

        m_BeaconDistance = new ArrayList<BeaconDistanceThread>();
        setCentralManager();
        beaconScanThread = new BeaconScanThread(centralManager, m_Handler);
        beaconScanThread.start();

        ttsClient= new TextToSpeech(getApplicationContext(),this);
    }
    private void setCentralManager() {
        centralManager = CentralManager.getInstance();
        centralManager.init(getApplicationContext());
        centralManager.setPeripheralScanListener(new PeripheralScanListener() {
            @Override
            public void onPeripheralScan(Central central, final Peripheral peripheral) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        boolean isExist = false;
                        for(BeaconDistanceThread thread : m_BeaconDistance)
                        {
                            if(thread.getMAC().equals(peripheral.getBDAddress())) {
                                isExist = true;
                                break;
                            }
                        }
                        if(!isExist)
                        {
                            BeaconDistanceThread thread = new BeaconDistanceThread(peripheral,peripheral.getBDAddress());
                            thread.start();
                            m_BeaconDistance.add(thread);
                        }
                    }
                });
            }
        });
    }
    public void selectBeacon()
    {
        try {
            beaconScanThread.m_SendFlag=true;
            boolean isNear = false;
            boolean isThis = false;
            for(BeaconDistanceThread thread : m_BeaconDistance)
            {
                if(thread.getDistance()<DISTANCE){
                    isNear = true;
                    for(int i=1; i<m_strBeaconMacs.size();i++)
                    {
                        if(m_strBeaconMacs.get(i).equals(thread.getMAC()))
                        {
                            isThis=true;
                            break;
                        }
                    }
                }
            }
            if(isThis)
            {
                tvState.setText("서버에 전송 중");
                HttpSendThread thread = new HttpSendThread(m_strId,m_strLOC_CODE,m_strCOU_CODE,m_Handler,1);
                thread.start();
            }
            else if (isNear)
            {
                differentClassRoom();
                beaconScanThread.m_SendFlag=false;
            }
            else{
                tvState.setText("Scanning...");
                beaconScanThread.m_SendFlag=false;
            }
            for(BeaconDistanceThread thread : m_BeaconDistance)
                thread.setValue();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void differentClassRoom(){
        tvState.setText("해당 강의실이 아닙니다. 맞는 강의실로 가시기 바랍니다");
    }
//
//    private double RSSIdistance(double RSSI,double txPower)
//    {
//        double ratio = RSSI*1.0/txPower;
//        if (ratio < 1.0) {
//            return Math.pow(ratio, POW_CONSTANT);
//        }
//        else {
//            double accuracy =
//                    BEST_SIGNAL_CONSTANT *
//                            Math.pow(ratio, BEST_POW_CONSTANT) +
//                            BEST_ADDED_CONSTANT;
//            return accuracy;
//        }
//    }


    @Override
    public void finish() {
        super.finish();
        beaconScanThread.m_flag=false;
        for(BeaconDistanceThread thread : m_BeaconDistance)
            thread.whileFlag=false;
    }

    private final Handler m_Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_SELECTING:
                    selectBeacon();
                    break;
                case MESSAGE_SERVERSTATE:
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String strTemp = msg.getData().getString("Result");
                    if(strTemp.equals("1")) {
                        centralManager.stopScanning();
                        tvState.setText("출석이 완료되었습니다.");
                        ttsClient.speak("출석이 완료되었습니다.", TextToSpeech.QUEUE_FLUSH, null);
                        for(BeaconDistanceThread thread : m_BeaconDistance)
                            thread.whileFlag=false;
                        beaconScanThread.m_flag=false;
                    }
                    else
                        tvState.setText("서버에 전송 오류");
                    beaconScanThread.m_SendFlag=false;
                    break;
                case MESSAGE_SCANSTOP:
                    centralManager.stopScanning();;
                    break;
            }
        }
    };

    @Override
    public void onInit(int i) {
        ttsClient.setPitch(1.0f);
        ttsClient.speak("현재 강의실을 확인하기위해 주변비콘을 스캔합니다.",TextToSpeech.QUEUE_FLUSH,null);
    }

   /* public void onCreateContextMenu( ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater mInflater = getMenuInflater();
        if (v == button1) {
            menu.setHeaderTitle("취소");
            mInflater.inflate(R.menu.menu_scanning_back, menu);
        }
    }*/

}
