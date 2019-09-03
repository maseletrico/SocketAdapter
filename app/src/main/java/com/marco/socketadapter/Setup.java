package com.marco.socketadapter;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.os.SystemClock.sleep;


public class Setup extends AppCompatActivity {

    private ProgressBar progressBar;
    private WebView myWebView;
    private WifiManager wifiManager;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private int size =0;
    private int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION =100;
    //private String url ="http://setup.com/";
    private String url ="http://10.10.10.1:80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        myWebView = findViewById(R.id.webview_gecko);
        progressBar = findViewById(R.id.progressBar_webview);

        setProgressBarVisibility(View.VISIBLE);

        //Bach Arrow
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);

        myWebView.getSettings().setJavaScriptEnabled(true);
//        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        myWebView.getSettings().setSupportMultipleWindows(true);
        myWebView.setWebViewClient(new Callback());
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        //myWebView.setWebChromeClient(new WebChromeClient());
        //myWebView.loadUrl("http://setup.com");

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
        // scanWifi();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        }else{
            scanWifi();
            //do something, permission was previously granted; or legacy device
        }




    }

    private class Callback extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.i("GECKO","Page Started "+ url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setProgressBarVisibility(View.GONE);
            Log.i("GECKO","Page Finished "+ url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            setProgressBarVisibility(View.VISIBLE);
            Log.i("GECKO","Page On Load "+ url);

//            if(url.equals("http://10.10.10.1/command")){
//                Toast.makeText(Setup.this, "Setup is Done", Toast.LENGTH_SHORT).show();
//                onBackPressed();
//            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Toast.makeText(Setup.this, "Cannot load page", Toast.LENGTH_SHORT).show();
            setProgressBarVisibility(View.GONE);
            Log.i("GECKO","Page Error "+ error);
            onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Do something with granted permission
            scanWifi();
        }
    }
    private void scanWifi() {
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                scanSuccess();
                //context.stopService(intent);
                unregisterReceiver(wifiReceiver);
                sleep(3000);
//                try {
//                    StringBuilder sb = new StringBuilder();
//                    URL urlGecko = new URL(url);
//
//                    BufferedReader in;
//                    in = new BufferedReader(
//                            new InputStreamReader(
//                                    urlGecko.openStream()));
//
//                    String inputLine;
//                    while ((inputLine = in.readLine()) != null)
//                        sb.append(inputLine);
//
//                    in.close();
//
//                    //return sb.toString();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                myWebView.loadUrl(url);
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(browserIntent);
            } else {
                // scan failure handling
                scanFailure();

            }
//            results = wifiManager.getScanResults();

//
//            for (ScanResult scanResult : results) {
//                arrayList.add(scanResult.SSID + " - " + scanResult.capabilities);
//                //adapter.notifyDataSetChanged();
//            }
        }
    };

    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();
        size = results.size();
        int i=0;
        for(i=0;i<size;i++){
            if(results.get(i).SSID.contains("Gecko OS Web Setup")){
                Toast.makeText(this, "Socket Adapter Found", Toast.LENGTH_SHORT).show();
                Log.i("GECKO", String.valueOf(results.get(i).capabilities));
                WifiConfiguration conf = new WifiConfiguration();
                conf.SSID = "\"" + results.get(i).SSID + "\"";
                conf.preSharedKey = "\""+ "password"  +"\"";
                wifiManager.addNetwork(conf);

                int networkId = wifiManager.addNetwork(conf);
                wifiManager.disconnect();
                wifiManager.enableNetwork(networkId, true);
                wifiManager.reconnect();
                int wifistate = wifiManager.getWifiState();
                while(wifistate!=WifiManager.WIFI_STATE_ENABLED){
                    Log.i("GECKO", String.valueOf(wifistate));
                }
                Log.i("GECKO= ", String.valueOf(wifistate));

            }else{
                //Log.i("WIFI GECKO", String.valueOf(results.get(i).SSID));
            }
        }
//        for (ScanResult scanResult : results) {
//                arrayList.add(scanResult.SSID + " - " + scanResult.capabilities);
//                //adapter.notifyDataSetChanged();
//            }
    }

    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        List<ScanResult> results = wifiManager.getScanResults();
  //... potentially use older scan results ...
    }

    private void setProgressBarVisibility(int visibility) {
        // If a user returns back, a NPE may occur if WebView is still loading a page and then tries to hide a ProgressBar.
        if (progressBar != null) {
            progressBar.setVisibility(visibility);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        //wifiManager.disconnect();
    }
}
