package com.uwec.wellnessapp.login;

import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.uwec.wellnessappbackend.myApi.MyApi;

/**
 * Created by butlernc on 12/3/2014.
 */
public class EndPointConnector {
    MyApi myApiService;

    public EndPointConnector() {
        MyApi.Builder builder = new MyApi.Builder(new ApacheHttpTransport(), new JacksonFactory(), null);
    }

    public synchronized void pushToRemote(boolean overridePreference, boolean overwrite) {

    }

    public synchronized void pullFromRemote(boolean overridePreference) {

    }
}
