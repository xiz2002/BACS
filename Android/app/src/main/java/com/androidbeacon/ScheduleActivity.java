package com.androidbeacon;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wizturn.sdk.central.CentralManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;


public class ScheduleActivity extends Activity {
    private final int REQUEST_ENABLE_BT = 1000;
    private int iGangMode = 0;
    private final int hang = 8;
    private final int yeol = 5;
    private final int COLOR[] = {
                                Color.argb(69, 216, 74, 251),
                                Color.argb(119, 43, 222, 215),
                                Color.argb(49, 174, 73, 166),
                                Color.argb(177, 73, 152, 81),
                                Color.argb(177, 73, 144, 213),
                                Color.argb(77, 220, 186, 99),
                                Color.argb(192, 83, 32, 13),
                                Color.argb(192, 205, 210, 13),
                                Color.argb(96, 23, 59, 13),
                                Color.argb(96, 23, 137, 206),
                                Color.argb(109, 186, 236, 93),
                                Color.argb(109, 62, 91, 167)
                                };
    private int m_iColorCnt = 0;
    View alert;
    TextView textgang[];
    Timer timer = new Timer();
    Context m_Context;
    SlidingDrawer slidingdrawer;
    String[] m_strTimeTable = {"9:30", "10:30", "11:30", "12:30", "13:30", "14:30", "15:30", "16:30", "17:30"},
            m_strDayTable = {"일", "월", "화", "수", "목", "금", "토"};
    int[][] textIDs = {{R.id.SC01, R.id.SC11, R.id.SC21, R.id.SC31, R.id.SC41},
            {R.id.SC02, R.id.SC12, R.id.SC22, R.id.SC32, R.id.SC42},
            {R.id.SC03, R.id.SC13, R.id.SC23, R.id.SC33, R.id.SC43},
            {R.id.SC04, R.id.SC14, R.id.SC24, R.id.SC34, R.id.SC44},
            {R.id.SC05, R.id.SC15, R.id.SC25, R.id.SC35, R.id.SC45},
            {R.id.SC06, R.id.SC16, R.id.SC26, R.id.SC36, R.id.SC46},
            {R.id.SC07, R.id.SC17, R.id.SC27, R.id.SC37, R.id.SC47},
            {R.id.SC08, R.id.SC18, R.id.SC28, R.id.SC38, R.id.SC48}};
    TableLayout schedule;
    LinearLayout handle;
    TextView[][] textviews = new TextView[hang][yeol];
    LinearLayout LoadingLayout;
    ListView listView;
    Intent intent;
    //    private final int gangID[] = {R.id.gang1, R.id.gang2, R.id.gang3, R.id.gang4};
    private ArrayList<ArrayList<String>> m_BeaconMacsList, m_TimeTableList, m_SupplessonList;
    private ArrayList<String> checkedLesson;


    int i, j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        for (i = 0; i < hang; i++) {
            for (j = 0; j < yeol; j++) {
                textviews[i][j] = (TextView) findViewById(textIDs[i][j]);
            }
        }
      /*  alert = (View) findViewById(R.id.textEc3);
        alert = (View) findViewById(R.id.textEc4);
        alert = (View) findViewById(R.id.textEc5);
        alert = (View) findViewById(R.id.textEc6);

     */
        slidingdrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
        schedule = (TableLayout) findViewById(R.id.schedule);
        handle = (LinearLayout) findViewById(R.id.handle);
     /*   textgang = new TextView[gangID.length];
        for (int i = 0; i < gangID.length; i++) {
            textgang[i] = (TextView) findViewById(gangID[i]);
        }
*/
        LoadingLayout = (LinearLayout) findViewById(R.id.llLoading);
        listView = (ListView) findViewById(R.id.listView);
        checkedLesson = new ArrayList<String>();

        m_BeaconMacsList = new ArrayList<ArrayList<String>>();
        m_SupplessonList = new ArrayList<>();
        m_TimeTableList = new ArrayList<>();    //강의실, 위치, 강의코드, id
        getBeaconFile();
        getTimeTableFile();
        getSupplesson();
//        slidingdrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
//
//            @Override
//
//            public void onDrawerOpened() {
//                schedule.setVisibility(View.GONE);
//            }
//        });
//
//
//        slidingdrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
//
//            @Override
//
//            public void onDrawerClosed() {
//                schedule.setVisibility(View.VISIBLE);
//            }
//        });
        m_Context = getApplicationContext();
        for (int i = 0; i < hang; i++) {
            for (int j = 0; j < yeol; j++) {
                textviews[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickTimeTable(view);
                    }
                });
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, checkedLesson);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean isCheck = false;
                String strId = m_SupplessonList.get(i).toString();
                for (ArrayList<String> arrin : m_SupplessonList) {
                    String strTemp = arrin.toString();
                    for (String strIn : arrin) {

                        final String strRoom = arrin.get(1);
                        final String strLessonCode = arrin.get(2);
                        LoadingLayout.setVisibility(View.VISIBLE);
                        TimerTask callETC = new TimerTask() {
                            private Handler updateUI = new Handler() {

                                @Override
                                public void dispatchMessage(Message msg) {
                                    setCentralManager(strRoom, strLessonCode);
                                }
                            };

                            public void run() {
                                try {
                                    updateUI.sendEmptyMessage(0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        timer.schedule(callETC, 1000);

                        isCheck = true;
                        break;

                    }
                    if (isCheck == true) {
                        break;
                    }
                }
            }
        });
    }

    private void getSupplesson() {
        try {
            FileInputStream infs = openFileInput(MainActivity.Supplesson.toString());
            byte[] temp = new byte[infs.available()];
            infs.read(temp);
            String strData = new String(temp);
            getSupplessonList(strData);
            infs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getSupplessonList(String strData) {
        JSONArray jarray = null;
        try {
            jarray = new JSONArray(strData);

            for (int i = 0; i < jarray.length(); i++) {
                ArrayList<String> SameSupplessonList = new ArrayList<String>();
                String strList = "", strObject = null;
                JSONObject jObject = jarray.getJSONObject(i);
                strObject = jObject.getString("NAME");
                strList += strObject + "/";
                SameSupplessonList.add(strObject);     //보강 강의 명
                strObject = jObject.getString("LOC_ID");
                strList += deleteZero(strObject) + "/";
                SameSupplessonList.add(strObject); //보강 강의 장소
                strObject = jObject.getString("COURSE_ID");
                SameSupplessonList.add(strObject);//보강 강의 코드
                strObject = m_strDayTable[Integer.valueOf(jObject.getString("DAY"))];
                strList += strObject + "/";
                SameSupplessonList.add(strObject);//보강 강의 요일
                strObject = jObject.getString("START");
                strList += m_strTimeTable[Integer.valueOf(strObject) - 1] + "~";
                SameSupplessonList.add(strObject);//보강 강의 시작시간
                strObject = jObject.getString("END");
                strList += m_strTimeTable[Integer.valueOf(strObject) - 1];
                SameSupplessonList.add(strObject);//보강 강의 끝시간
                m_SupplessonList.add(SameSupplessonList);
                checkedLesson.add(strList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBeaconFile() {
        try {
            FileInputStream infs = openFileInput(MainActivity.BeaconFile.toString());
            byte[] temp = new byte[infs.available()];
            infs.read(temp);
            String strData = new String(temp);
            getRoomNum(strData);
            infs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRoomNum(String strData) {
        JSONArray jarray = null;
        try {
            jarray = new JSONArray(strData);

            for (int i = 0; i < jarray.length(); i++) {
                ArrayList<String> SameRoombeaconList = new ArrayList<String>();
                JSONObject jObject = jarray.getJSONObject(i);
                SameRoombeaconList.add(jObject.getString("LOC_CODE"));
                SameRoombeaconList.add(jObject.getString("MAC"));
                m_BeaconMacsList.add(SameRoombeaconList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTimeTableFile() {
        try {
            FileInputStream infs = openFileInput(MainActivity.TimeTableFile.toString());
            byte[] temp = new byte[infs.available()];
            infs.read(temp);
            String strData = new String(temp);
            getTimeTable(strData);
            infs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String deleteZero(String strLocation) {
        for (int j = 0; j < strLocation.length(); j++) {
            String strTemp = strLocation.substring(j, j + 1);
            if (!strTemp.equals("0")) {
                break;
            }
        }
        strLocation = strLocation.substring(j);
        return strLocation;
    }


    public void clickTimeTable(View v) {
        boolean isCheck = false;
        String strId = String.valueOf(v.getId());
        for (ArrayList<String> arrin : m_TimeTableList) {
            String strTemp = arrin.toString();
            for (String strIn : arrin) {
                if (strId.equals(strIn)) {
                    final String strRoom = arrin.get(1);
                    final String strLessonCode = arrin.get(2);
                    LoadingLayout.setVisibility(View.VISIBLE);
                    TimerTask callETC = new TimerTask() {
                        private Handler updateUI = new Handler() {

                            @Override
                            public void dispatchMessage(Message msg) {
                                super.dispatchMessage(msg);
                                setCentralManager(strRoom, strLessonCode);
                            }
                        };

                        public void run() {
                            try {
                                updateUI.sendEmptyMessage(0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    timer.schedule(callETC, 1000);

                    isCheck = true;
                    break;
                }
            }
            if (isCheck == true) {
                break;
            }
        }
    }

    //액티비티에 시간표 뿌려주는 메소드
    private void getTimeTable(String strData) {
        JSONArray jarray = null;
        try {
            JSONObject json = new JSONObject(strData);
            jarray = json.getJSONArray("object2");

            for (int i = 0; i < jarray.length(); i++) {
                ArrayList<String> TimeTable = new ArrayList<String>();
                JSONObject jObject = jarray.getJSONObject(i);
                //이름과 위치 값 받음
                TimeTable.add(jObject.getString("COU_NAME"));
                String strLocation = deleteZero(jObject.getString("LOC_CODE"));
//                //앞에 00000 제거
//                for(int j=0;j<strLocation.length();j++) {
//                    String strTemp = strLocation.substring(j,j+1);
//                    if (!strTemp.equals("0")) {
//                        break;
//                    }
//                }
//                strLocation = strLocation.substring(j);
                TimeTable.add(strLocation);
                TimeTable.add(jObject.getString("COU_CODE"));
                int iDay = Integer.parseInt(jObject.getString("COU_DAY")) - 1;
                int iStart = Integer.parseInt(jObject.getString("COU_ST")) - 1;
                int iEnd = Integer.parseInt(jObject.getString("COU_ED"));
                int textAdd = iStart + ((iEnd - iStart) / 2);
                String strLineCheck = jObject.getString("COU_NAME");
                int lengthString = strLineCheck.length();
                //칸 마다 색입히고 해당 칸의 아이디를 리스트에 추가
                for (int j = iStart; j < iEnd; j++) {
                    textviews[j][iDay].setBackgroundColor(COLOR[m_iColorCnt]);
                    TimeTable.add(String.valueOf(textIDs[j][iDay]));

                    if (lengthString > 4) {
                        textviews[textAdd][iDay].setText((jObject.getString("COU_NAME")).substring(0, 4) + "...");
                    } else {
                        textviews[textAdd][iDay].setText(jObject.getString("COU_NAME"));
                    }
                }
                m_iColorCnt = (++m_iColorCnt) % COLOR.length;

                //타임테이블리스트에 추가
                m_TimeTableList.add(TimeTable);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setCentralManager(String _strRoom, String _strLessonCode) {
        LoadingLayout.setVisibility(View.INVISIBLE);
        String strLOC_CODE = _strRoom;
        int i, length = strLOC_CODE.length();
        for (i = 0; i < 9 - length; i++) {
            strLOC_CODE = "0" + strLOC_CODE;
        }
        boolean isExist = false;
        for (i = 0; i < m_BeaconMacsList.size(); i++) {
            if (m_BeaconMacsList.get(i).get(0).equals(strLOC_CODE)) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            intent = new Intent(m_Context, ScanConnActivity.class);
            intent.putExtra("RoomNum", _strRoom);
            intent.putExtra("LessonCode", _strLessonCode);
            intent.putExtra("BEACONMAC", m_BeaconMacsList.get(i));

            Intent _intent = getIntent();

            intent.putExtra("ID", _intent.getExtras().getString("ID"));

            if (!CentralManager.getInstance().isBluetoothEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                startActivity(intent);
            }
        } else {
            Toast.makeText(m_Context, "존재하지 않는 강의실 입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void touchURL1(View v) {
        /*LoadingLayout.setVisibility(View.VISIBLE);
//        progressBarScan.setVisibility(View.VISIBLE);
//        textScan.setVisibility(View.VISIBLE);
        TimerTask callETC = new TimerTask() {
            public void run() {
//            progressBarScan.setVisibility(View.INVISIBLE);
//            textScan.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        };
        timer.schedule(callETC, 1000);*/
    }


    public void touchURL2(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://m.gtec.ac.kr"));
        startActivity(intent);
    }

    public void touchURL3(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://lib.gtec.ac.kr/"));
        startActivity(intent);
    }


    public void touchURL4(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        Uri uriPhoneNumber = Uri.parse("tel:0314964555");
        intent.setData(uriPhoneNumber);
        intent.setData(Uri.parse("tel:0314964555"));
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingLayout.setVisibility(View.INVISIBLE);
    }

}
