package com.androidbeacon;

import com.wizturn.sdk.peripheral.Peripheral;

/**
 * Created by honggi on 2015. 11. 21..
 */
public class BeaconDistanceThread extends Thread {

    private double m_fDistance=0.0;
    private int m_iCnt = 0;
    double max = 0,min = 200;
    public boolean whileFlag = true;
    private String m_MAC=null;
    Peripheral m_peripheral;
    public BeaconDistanceThread(Peripheral peripheral,String _MAC){
        m_peripheral=peripheral;
        m_MAC=_MAC;
    }
    public void setValue(){
        max=0;min=200;m_iCnt=0;
        m_fDistance=0;
    }
    public String getMAC()
    { return m_MAC;}
    public double getDistance(){
        m_fDistance = (m_fDistance-(max+min))/(m_iCnt-2);
        return m_fDistance;
    }
    @Override
    public void run() {

        while(whileFlag)
        {
            double dDistance = m_peripheral.getDistance();
            if(max<dDistance){
                max=dDistance;
            }
            if(min>dDistance){
                min=dDistance;
            }
            m_fDistance+=dDistance;
            m_iCnt++;
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
