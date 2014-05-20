/*
 * SkreenMeApp
 * Copyright (c) 2014, GSLab Pvt. Ltd., All rights reserved.
 * Company Name: GSLab Pvt. Ltd.
 * Website: http://www.gslab.com
 * Skreenme Website: http://skreen.me
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 * 
 */
package com.skreen.skreenapp;

import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ViewSkreenActivity extends Activity {
    
    private static final String TAG = ViewSkreenActivity.class.getSimpleName()
            + ":";
    
    private static final String DEFAULT_VIEW_URL = "http://go.skreen.me/view/mobile";
    
    private WebView mWebView;
    private ProgressDialog mProgressDialog;
    
    /**
     * 
     */
    public ViewSkreenActivity() {
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_skreen_activity);
        
        if (savedInstanceState!=null) {
            // Restore webview state 
            this.mWebView.restoreState(savedInstanceState);
        }
        
        getActionBar().setIcon(R.drawable.ic_launcher);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        this.initWebView();
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        Log.v(TAG, "initWebView: called");
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setMessage(getString(R.string.loading));
        
        this.mWebView = (WebView) this.findViewById(R.id.sm_webview);
        this.mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView aView, String aUrl) {
                super.shouldOverrideUrlLoading(aView, aUrl);
                Log.v(TAG, "shouldOverrideUrlLoading#called");
                return false;
            }

            @Override
            public void onPageFinished(WebView aView, String aUrl) {
                super.onPageFinished(aView, aUrl);
                Log.v(TAG, "onPageFinished#called");
                ViewSkreenActivity.this.mProgressDialog.hide();
            }
        });
        this.mWebView.setWebChromeClient(new WebChromeClient());
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.requestFocus();

        try {
            new URL(DEFAULT_VIEW_URL); // just to check if valid url
            this.mWebView.loadUrl(DEFAULT_VIEW_URL);
            this.mProgressDialog.show();
            Log.v(TAG, "initWebView: loading url: " + DEFAULT_VIEW_URL);
        } catch (MalformedURLException e) {
            // aUrlString = GOOGLE_SEARCH_URL + aUrlString;
            // this.mWebView.loadUrl(aUrlString);
            Log.e(TAG, "initWebView: error loading url: " + DEFAULT_VIEW_URL);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mProgressDialog.cancel();
        this.mProgressDialog = null;
        Log.v(TAG, "onDestroy#called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {    
            case android.R.id.home: {
                this.finish();
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu aMenu) {
        return super.onPrepareOptionsMenu(aMenu);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
         //Save the state of Webview
         this.mWebView.saveState(savedInstanceState);
         super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.mWebView.restoreState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }
}