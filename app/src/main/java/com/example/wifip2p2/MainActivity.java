package com.example.wifip2p2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ConnectivityManager.NetworkCallback mWifiNetworkCallback, mMobileNetworkCallback;
    private Network mWifiNetwork, mMobileNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWifiNetworkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(final Network network) {
                    try {
                        //Save this network for later use
                        mWifiNetwork = network;
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                }
            };

            mMobileNetworkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(final Network network) {
                    try {
                        //Save this network for later use
                        mMobileNetwork = network;
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                }
            };
            NetworkRequest.Builder wifiBuilder;
            wifiBuilder = new NetworkRequest.Builder();
            wifiBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            connectivityManager.requestNetwork(wifiBuilder.build(), mWifiNetworkCallback);

            NetworkRequest.Builder mobileNwBuilder;
            mobileNwBuilder = new NetworkRequest.Builder();
            mobileNwBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
            connectivityManager.requestNetwork(mobileNwBuilder.build(), mMobileNetworkCallback);
        }

        findViewById(R.id.lteReq).setOnClickListener(view -> {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                URL url = new URL("https://us-central1-lightbharat-283808.cloudfunctions.net/httpHook");
                HttpURLConnection httpURLConnection = null;
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    httpURLConnection = (HttpURLConnection) mMobileNetwork.openConnection(url);
                }
                else {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                }

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                httpURLConnection.setRequestMethod("GET");

                final int responseCode = httpURLConnection.getResponseCode();

                Log.i("hsbk","reponse code: "+ responseCode);

                if (responseCode == 202) {
                    final String statusMessage = httpURLConnection.getResponseMessage();
                    Log.i("hsbk","reponse code: "+ responseCode + "   status message: " + statusMessage);
                }

            }
            catch (ConnectException e) {
                Log.i("hsbk","request unsuccessful");
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        });

        findViewById(R.id.wifiReq).setOnClickListener(view -> {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                URL url = new URL("https://us-central1-lightbharat-283808.cloudfunctions.net/httpHook");
                HttpURLConnection httpURLConnection = null;
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    httpURLConnection = (HttpURLConnection) mWifiNetwork.openConnection(url);
                }
                else {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                }

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                httpURLConnection.setRequestMethod("GET");

                final int responseCode = httpURLConnection.getResponseCode();

                Log.i("hsbk","reponse code: "+ responseCode);
                if (responseCode == 202) {
                    final String statusMessage = httpURLConnection.getResponseMessage();
                    Log.i("hsbk","reponse code: "+ responseCode + "   status message: " + statusMessage);
                }

            }
            catch (ConnectException e) {
                Log.i("hsbk","request unsuccessful");
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}