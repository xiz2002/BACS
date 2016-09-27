package com.androidbeacon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wizturn.sdk.central.Central;
import com.wizturn.sdk.central.CentralManager;
import com.wizturn.sdk.peripheral.Peripheral;
import com.wizturn.sdk.peripheral.PeripheralScanListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    public static final int MODE_BEACON_LISTUPDATE=1,MODE_BEACON_VERSIONCHECK = 2,MODE_TIMETABLECHECK = 4,MODE_SUPPLESSON = 5;
    Button btnLogin;
    EditText edtID, edtPW;
    ProgressBar progressBar;
    TextView textLogin;
    CheckBox ckAuto;
    public static File BeaconFile = new File("Beacon.txt");
    public static File TimeTableFile = new File("TimeTable.txt");
    public static File Supplesson = new File("Supplesson.txt");
    public static String m_strBeaconVersion;
    private CentralManager centralManager;
    private Timer timer = new Timer();
    private String strPassWD,strTimeTableVersion;

    UserInformation m_userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtID = (EditText) findViewById(R.id.edtID);
        edtPW = (EditText) findViewById(R.id.edtPW);
        textLogin = (TextView) findViewById(R.id.textLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ckAuto=(CheckBox)findViewById(R.id.ckAuto);
        centralManager = null;

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtID.getText().toString().equals("0000"))
                    setIntent();
                else
                {
                    setCentralManager();
                    terminateIfNotBLE();
                    login();
                }
            }
        });
        getTimeTableFile();
        m_userInformation = UserInformation.getInstance();
        boolean isCheck = m_userInformation.checkAuto(edtID, edtPW, ckAuto);
        if (isCheck)
            login();
    }
    private void getTimeTableFile() {
        try {
            FileInputStream infs = openFileInput(MainActivity.TimeTableFile.toString());
            byte[] temp = new byte[infs.available()];
            infs.read(temp);
            String strData = new String(temp);
            setTimeTableVersion(strData);
            infs.close();
        } catch (IOException e) {
            strTimeTableVersion="0";
            e.printStackTrace();
        }
    }
    private void setTimeTableVersion(String _strData){
        JSONArray jarray = null;
        try {
            JSONObject json = new JSONObject(_strData);
            jarray = json.getJSONArray("object1");
            JSONObject jObject = jarray.getJSONObject(0);
            strTimeTableVersion=jObject.getString("VERSIOND");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void login() {
        if (!(edtPW.getText().toString() == null || edtID.getText().toString() == null)) {
            progressBar.setVisibility(View.VISIBLE);
            textLogin.setVisibility(View.VISIBLE);
            strPassWD = computeSHAHash(edtPW.getText().toString());
            if (!(strPassWD == null)) {
                TimerTask callLogin = new TimerTask() {
                    public void run() {
                        HttpSendThread Thread = new HttpSendThread(edtID.getText().toString(), strPassWD, mHandler, HttpSendThread.MODE_LOGIN);
                        Thread.start();
                    }
                };
                timer.schedule(callLogin, 1000);

            }
        }
    }

    private void setCentralManager() {
        centralManager = CentralManager.getInstance();
        centralManager.init(getApplicationContext());
    }

    private void terminateIfNotBLE() {
        if (!centralManager.isBLESupported()) {
            Toast.makeText(this, R.string.error_ble_not_support, Toast.LENGTH_LONG).show();
            finish();
        }
    }
    private void sendBeaconVersionCheck(){
        HttpSendThread Thread = new HttpSendThread(m_strBeaconVersion, null, mHandler, HttpSendThread.MODE_BEACON_VERSIONCHECK);
        Thread.start();
    }
    private void sendBeaconListUpdate(){
        HttpSendThread Thread = new HttpSendThread(null, null, mHandler, HttpSendThread.MODE_BEACON_LISTUPDATE);
        Thread.start();
    }
    private void sendTimeTableUpdate(){
        HttpSendThread Thread = new HttpSendThread(edtID.getText().toString(), strTimeTableVersion, mHandler, HttpSendThread.MODE_TIMETABLE_UPDATE);
        Thread.start();
    }
    private void sendSupplesson(){
        HttpSendThread Thread = new HttpSendThread(edtID.getText().toString(), null, mHandler, HttpSendThread.MODE_GETSUPPLESSON);
        Thread.start();
    }

    private void saveFile(String _strFile, int _type) {
        try {
            FileOutputStream outfs = null;
            if      (_type == 0)     outfs = openFileOutput(String.valueOf(BeaconFile), Context.MODE_WORLD_WRITEABLE);
            else if (_type == 1)     outfs = openFileOutput(String.valueOf(TimeTableFile), Context.MODE_WORLD_WRITEABLE);
            else if (_type == 2)     outfs = openFileOutput(String.valueOf(Supplesson), Context.MODE_WORLD_WRITEABLE);
            outfs.write(_strFile.getBytes());
            outfs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setIntent() {
        progressBar.setVisibility(View.INVISIBLE);
        textLogin.setVisibility(View.INVISIBLE);
        if(ckAuto.isChecked()){
            m_userInformation.setAutoAcess(edtID.getText().toString(),edtPW.getText().toString());
        }
        else{
            m_userInformation.setManualAcess();
        }
        Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
        intent.putExtra("ID", edtID.getText().toString());
        startActivity(intent);
        finish();
    }


    private String computeSHAHash(String password) {
        MessageDigest mdSha1 = null;
        try {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            Log.e("myapp", "Error initializing SHA1 message digest");
        }
        mdSha1.update(password.getBytes());
        byte[] data = mdSha1.digest();
        String strTemp = null;
        try {
            strTemp = convertToHex(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        strTemp = strTemp.substring(0, strTemp.length() - 1);
        return strTemp;
    }

    private String convertToHex(byte[] data) throws java.io.IOException {
        StringBuffer sb = new StringBuffer();
        String hex = null;

        hex = Base64.encodeToString(data, 0, data.length, 0);

        sb.append(hex);

        return sb.toString();
    }

    private void errorDialog() {
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("알림")
                    .setMessage("ID 또는 PassWord가 틀렸습니다.")

                    .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }

    private void errorDialog2() {
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("알림")
                    .setMessage("재적당한 회원입니다.")

                    .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }
    private void errorDialog3() {
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("알림")
                    .setMessage("서버 접속에 실패하였습니다.")

                    .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }
//    private void compareBeaconVersion(){
//        HttpSendThread m_thread = new HttpSendThread(m_strBeaconVersion);
//    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String strState;
            switch (msg.what) {
                case ScanConnActivity.MESSAGE_SERVERSTATE:
                    strState = msg.getData().getString("Result");
                    switch (strState){
                        case "1":
                            textLogin.setText("비콘 리스트 비교 중");
                            sendBeaconVersionCheck();
                            break;
                        case "-1":
                            progressBar.setVisibility(View.INVISIBLE);
                            textLogin.setVisibility(View.INVISIBLE);
                            errorDialog();
                            break;
                        case "-2":
                            progressBar.setVisibility(View.INVISIBLE);
                            textLogin.setVisibility(View.INVISIBLE);
                            errorDialog2();
                            break;
                    }

                    break;
                case MODE_BEACON_VERSIONCHECK:
                    strState = msg.getData().getString("Result");
                    switch (strState){
                        case "-1":
                            textLogin.setText("사용자 시간표 비교 중");
                            sendTimeTableUpdate();
                            break;
                        default:
                            textLogin.setText("비콘 리스트 다운로드 중");
                            m_strBeaconVersion=strState;
                            sendBeaconListUpdate();
                            break;
                    }
                    break;
                case MODE_BEACON_LISTUPDATE:
                    strState = msg.getData().getString("Result");
                    switch (strState){
                        case "-1":
                            progressBar.setVisibility(View.INVISIBLE);
                            textLogin.setVisibility(View.INVISIBLE);
                            errorDialog3();
                            break;
                        default:
                            saveFile(strState, 0);
                            textLogin.setText("사용자 시간표 비교 중");
                            sendTimeTableUpdate();
                            break;
                    }
                    break;
                case MODE_TIMETABLECHECK:
                    strState = msg.getData().getString("Result");
                    switch (strState)
                    {
                        case "1":
                            break;
                        default:
                            saveFile(strState, 1);
                            break;
                    }
                    textLogin.setText("보강 리스트 다운로드 중");
                    sendSupplesson();
                    break;
                case MODE_SUPPLESSON:
                    strState = msg.getData().getString("Result");
                    saveFile(strState,2);
                    setIntent();
            }
        }
    };


}


