package com.yyl.socket;

import org.json.JSONObject;

public interface SocketRequestListener {

       public void onError(String errorStr);
       
       public void onSuccess(JSONObject result);
}
