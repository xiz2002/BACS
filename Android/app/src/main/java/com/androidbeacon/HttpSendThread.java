package com.androidbeacon;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.os.Handler;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by honggi on 2015. 11. 19..
 */
public class HttpSendThread extends Thread{
    public static final int MODE_LOGIN = 0,MODE_ATTENDANCECHECK=1,
            MODE_BEACON_VERSIONCHECK=2,MODE_BEACON_LISTUPDATE=3,
            MODE_TIMETABLE_UPDATE=4,MODE_GETSUPPLESSON=5;
    private String m_strId,m_strSecValue,m_strCou_Code;
    //String URL = "http://toybox.iptime.org:19080/param/"; / URL Change
    private Handler mHandler;
    private int m_type;
    Message msg;
    public HttpSendThread(String _strId, String _strSecValue,Handler _Handler,int _type)
    {
        m_strId=_strId;
        m_strSecValue=_strSecValue;
        mHandler=_Handler;
        m_type=_type;
        switch (m_type)
        {
            case MODE_LOGIN:
                URL += "login";
                break;
            case MODE_ATTENDANCECHECK:
                URL += "checkin";
                break;
            case MODE_BEACON_VERSIONCHECK:
                URL += "beaconchk";
                break;
            case MODE_BEACON_LISTUPDATE:
                URL += "beaconupdate";
                break;
            case MODE_TIMETABLE_UPDATE:
                URL += "gettimeschedule";
                break;
            case MODE_GETSUPPLESSON:
                URL += "GSTS";
                break;
        }
    }
    public HttpSendThread(String _strId, String _strSecValue,String _strCou_Code,Handler _Handler,int _type){
        m_strId=_strId;
        m_strSecValue=_strSecValue;
        m_strCou_Code = _strCou_Code;
        mHandler=_Handler;
        m_type=_type;
        switch (m_type)
        {
            case MODE_ATTENDANCECHECK:
                URL += "checkin";
                break;
        }
    }
    @Override
    public void run() {
        HttpResponse httpResponse=null;
        if(m_type == MODE_BEACON_LISTUPDATE)
            try {
                httpResponse=httpRequestGet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        else
            httpResponse=sendJsonDataToServer();
        String isOk = SendByHttp(httpResponse);
        Bundle bundle = new Bundle();
        bundle.putString("Result", isOk);
        switch (m_type)
        {
            case MODE_BEACON_LISTUPDATE:
                msg = mHandler.obtainMessage(MainActivity.MODE_BEACON_LISTUPDATE);
                break;
            case MODE_BEACON_VERSIONCHECK:
                msg = mHandler.obtainMessage(MainActivity.MODE_BEACON_VERSIONCHECK);
                break;
            case MODE_LOGIN:
            case MODE_ATTENDANCECHECK:
                msg = mHandler.obtainMessage(ScanConnActivity.MESSAGE_SERVERSTATE);
                break;
            case MODE_TIMETABLE_UPDATE:
                msg = mHandler.obtainMessage(MainActivity.MODE_TIMETABLECHECK);
                break;
            case MODE_GETSUPPLESSON:
                msg = mHandler.obtainMessage(MainActivity.MODE_SUPPLESSON);
                break;
        }
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }
    private List<NameValuePair> setList(){
        List<NameValuePair> nameValuePairs = null;
        switch (m_type) {
            case MODE_LOGIN:
                nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("ID", m_strId));
                nameValuePairs.add(new BasicNameValuePair("PASSWD", m_strSecValue));
                break;
            case MODE_ATTENDANCECHECK:
                nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("ID", m_strId));
                nameValuePairs.add(new BasicNameValuePair("LOC_CODE", m_strSecValue));
                nameValuePairs.add(new BasicNameValuePair("COU_ID", m_strCou_Code));
                break;
            case MODE_BEACON_VERSIONCHECK:
                nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("VERSION", m_strId));
                break;
            case MODE_TIMETABLE_UPDATE:
                nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("ID", m_strId));
                nameValuePairs.add(new BasicNameValuePair("VERSION", m_strSecValue));
                break;
            case MODE_GETSUPPLESSON:
                nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("ID",m_strId));
                break;
        }
        return nameValuePairs;
    }
    private HttpResponse httpRequestGet()throws Exception{
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(URL);
        HttpResponse response = client.execute(get);
        return response;
    }
    private HttpResponse sendJsonDataToServer(){
        HttpResponse response=null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        List<NameValuePair> nameValuePairs = setList();
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        try {
            response = httpClient.execute(httpPost);
            // write response to log
            Log.d("Http Post Response:", response.toString());
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }
        return response;
    }
    private String SendByHttp(HttpResponse response){
        String result = "";
        try{
            if(response ==null) return "-1";
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
            String line ;
            while ((line = bufReader.readLine()) != null)
                result += line;

        } catch (IOException e) {
            String strException = e.toString();
            Log.e("IOException",strException);
        }
        Log.e("Receive", result);
        return (result==null ? "-1" : result);
    }

//    private String[][] jsonParserList(String pRecvServerPage){
//        return "1";
//    }
}
