package com.androidbeacon;

import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by honggi on 2015-12-01.
 */
public class UserInformation {
    public static UserInformation m_UserInformation=null;
    private String m_strID,m_strPW;
    private boolean m_isChecked;
    private UserInformation()
    {
        m_strID=null;
        m_strPW=null;
        m_isChecked=false;
    }
    public static UserInformation getInstance(){
        if(m_UserInformation == null)
            m_UserInformation= new UserInformation();

        return m_UserInformation;
    }

    public String getID(){
        return m_strID;
    }
    public String getPW(){
        return m_strPW;
    }
    public void setAutoAcess(String _strID,String _strPW)
    {
        m_isChecked=true;
        m_strPW=_strPW;
        m_strID=_strID;

    }
    public void setManualAcess(){
        m_isChecked=false;
        m_strPW = null;
        m_strID=null;
    }
    public boolean checkAuto(EditText edtID,EditText edtPW,CheckBox _autoCheck){
        boolean isCheck=false;
        if(m_isChecked == true)
        {
            edtID.setText(m_strID);
            edtPW.setText(m_strPW);
            _autoCheck.setChecked(true);
            isCheck= true;
        }
        return isCheck;
    }
}
