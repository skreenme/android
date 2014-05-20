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

import com.kpoint.skreen.listener.SkreenMeListener;
import com.kpoint.skreen.skreenme.SkreenMe;
import com.kpoint.skreen.skreenme.SkreenMe.SessionState;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * This Skreen.Me android app demostrats skreen.me android lib
 * usage and skreen.me plateform capabilities.
 * 
 * This is a sample app to show you that it is quite easy to
 * get along with skreen.me android lib with any android app
 * and start sharing app's screen.
 * 
 * @author Pratik
 *
 */
public class ShareSkreenActivity extends Activity implements SkreenMeListener,
        OnClickListener, OnCheckedChangeListener {

    private static final String TAG = ShareSkreenActivity.class.getSimpleName()
            + ":";

    private static final String DEFAULT_URL = "http://go.skreen.me/mobile";
    private static final String GOOGLE_SEARCH_URL = "http://www.google.com/#q=";
    private static final String DROPBOX_URL = "http://www.dropbox.com";
    private static final String GOOGLE_DRIVE_URL = "http://www.drive.google.com";
    private static final String FACEBOOK_URL = "http://www.facebook.com";
    private static final String TWITTER_URL = "http://www.twitter.com";
    private static final String LINKEDIN_URL = "http://www.linkedin.com";
    private static final String FLICKER_URL = "http://www.flickr.com";
    private static final String PICASA_URL = "http://www.picasaweb.google.com";

    private static final String SUPPORT_EMAIL = "contact@skreenme.com";
    private static final String SHARE_SKREEN_TEXT = "View my android "
            + "application screen at <br/>"
            + "<a href='http://go.skreen.me/view/#'>http://go.skreen.me/view/#</a>";

    private SkreenMe skreenme;
    private String mSkreenMeId;
    private DisplayMetrics mDisplayMetrics;

    /* Browser views */
    private ImageButton mBackButton;
    private ImageButton mHomeButton;
    private ImageButton mGoButton;
    private EditText mAddressField;

    /* quick links */
    private TextView mDropboxLink;
    private TextView mGoogleDriveLink;
    private TextView mFacebookLink;
    private TextView mTwitterLink;
    private TextView mLinkedInLink;
    private TextView mFlickrLink;
    private TextView mPicasaLink;

    private WebView mWebView;

    /* skreen me actions */
    private TextView mContactUsLink;
    private TextView mSessionInfo;
    private Switch mSkreenMeSwitch;
    private ProgressDialog mProgressDialog;
    private Boolean mStopSkreenMeOnPause = true;
    private ImageButton mAddressFieldClearButton;

    private boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate#called");
        setContentView(R.layout.share_skreen_activity);
        getActionBar().setIcon(R.drawable.ic_launcher);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Create SkreenMe object.
        this.skreenme = new SkreenMe(this);
        this.skreenme = this.skreenme.initSkreenMe(this, this
                .getString(R.string.skreenme_app_key), this
                .getString(R.string.skreenme_app_secret), getResources()
                .getConfiguration().orientation);

        /*
         * Initialize skreen.me using the SKREENME_APP_KEY and 
         * SKREENME_APP_SECRET. 
         * 
         * You can get (SKREENME_APP_KEY, SKREENME_APP_SECRET) pair from
         * skreen.me wesite http://skreen.me/api or mail us on 
         * contact@skreen.me to get one.
         * 
         * Your activity must implement SkreenMeListener interface.
         * This helps passing status to parent app.
         * 
         */
//        this.skreenme = this.skreenme.initSkreenMe(this, 
//                <<SKREENME_APP_KEY>>, // app key obtained from skreen.me  
//                <<SKREENME_APP_SECRET>>, // app secret obtained from skreen.me
//                getResources().getConfiguration().orientation);
        this.initViews();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu aMenu) {
        Log.v(TAG, "onPrepareOptionsMenu#called");
        MenuItem shareItem = aMenu.findItem(R.id.action_share);
        if (this.skreenme.getCurrentSessionState() == SkreenMe.SessionState.CONNECTED_SKREENME
                || this.skreenme.getCurrentSessionState() == SkreenMe.SessionState.CONNECTING_SKREENME) {
            Log.v(TAG, "onPrepareOptionsMenu#sessionState: connected");
            shareItem.setEnabled(true);
            shareItem.setVisible(true);
        } else if (this.skreenme.getCurrentSessionState() == SkreenMe.SessionState.DISCONNECTED_SKREENME
                || this.skreenme.getCurrentSessionState() == SkreenMe.SessionState.DISCONNECTING_SKREENME) {
            Log.v(TAG, "onPrepareOptionsMenu#sessionState: disconnected");
            shareItem.setEnabled(false);
            shareItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(aMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.skreen_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem aItem) {
        switch (aItem.getItemId()) {
        case R.id.action_share: {
            this.shareSkreenMe();
            break;
        }
        
        case android.R.id.home: {
            this.finish();
        }
        
        default:
            break;
        }
        return super.onOptionsItemSelected(aItem);
    }

    @Override
    public Activity getSelfActivity() {
        return this;
    }

    @Override
    public void onSkreenMeSessionOpened(String skreenmeId) {
        if (skreenmeId == null) {
            Log.v(TAG, "onSkreenMeSessionOpened#Failed to start skreen me.. ");
            this.showDialog(getString(R.string.skreen_me_start_failure),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            return;
        }

        this.mSkreenMeId = skreenmeId;
        this.mSessionInfo.setText(getString(R.string.skreen_me_access_code)
                + " " + this.mSkreenMeId);
        this.mSessionInfo.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
        Log.v(TAG,
                "onSkreenMeSessionOpened#Skreen.me session started with id = "
                        + this.mSkreenMeId);
        // String msg = SKREEN_ME_SUCCESS_TEXT.replace("#", this.mSkreenMeId);
        String msg = getString(R.string.skreen_me_success_message);
        this.showDialog(Html.fromHtml(msg), getString(R.string.invite), true,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShareSkreenActivity.this.shareSkreenMe();
                    }
                });

        this.mSkreenMeSwitch.setEnabled(true);
    }

    @Override
    public void onSkreenMeSessionClosed() {
        this.invalidateOptionsMenu();
        this.mSkreenMeSwitch.setEnabled(true);
        this.showDialog(getString(R.string.skreen_me_closed),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface aDialog, int aWhich) {
                        aDialog.dismiss();
                    }
                });
    }

    @Override
    public void onSkreenMeFailure(String msg) {
        Log.v(TAG, "onSkreenMeFailure#" + msg);
        this.showDialog(msg, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }
        });
        this.mSkreenMeSwitch.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.isActive = false;
        if (this.mStopSkreenMeOnPause) {
            this.closeSkreenMe();

        }
        this.mStopSkreenMeOnPause = true;
        Log.v(TAG, "onPause#called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.isActive = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mProgressDialog.cancel();
        this.mProgressDialog = null;
        Log.v(TAG, "onDestroy#called");
    }

    @Override
    public void onConfigurationChanged(Configuration aNewConfig) {
        super.onConfigurationChanged(aNewConfig);
        if (aNewConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.v(TAG, "onConfigurationChanged#landscape");
            this.skreenme.changeOrientation(Configuration.ORIENTATION_PORTRAIT,
                    Configuration.ORIENTATION_LANDSCAPE);
        } else if (aNewConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.v(TAG, "onConfigurationChanged#portrait");
        }
    }

    // ================================================================================
    // Others
    // ================================================================================
    @SuppressLint("SetJavaScriptEnabled")
    private void initViews() {
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setMessage(getString(R.string.loading));
        /* Browser views */
        this.mBackButton = (ImageButton) this.findViewById(R.id.sm_back_button);
        this.mBackButton.setOnClickListener(this);
        this.mHomeButton = (ImageButton) this.findViewById(R.id.sm_home_button);
        this.mHomeButton.setOnClickListener(this);
        this.mGoButton = (ImageButton) this.findViewById(R.id.sm_go_button);
        this.mGoButton.setOnClickListener(this);
        this.mAddressField = (EditText) this
                .findViewById(R.id.sm_address_field);
        this.mAddressField
                .setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView aV, int aActionId,
                            KeyEvent aEvent) {
                        if (aActionId != EditorInfo.IME_ACTION_GO) {
                            return false;
                        }

                        String urlString = ShareSkreenActivity.this.mAddressField
                                .getText().toString();
                        ShareSkreenActivity.this.goToUrl(urlString);
                        return false;
                    }
                });
        this.mAddressFieldClearButton = (ImageButton) this
                .findViewById(R.id.sm_address_field_clear_button);
        this.mAddressFieldClearButton.setOnClickListener(this);

        /* quick links */
        this.mDropboxLink = (TextView) this.findViewById(R.id.sm_dropbox_link);
        if (this.mDropboxLink != null) {
            this.mDropboxLink.setOnClickListener(this);
        }

        this.mGoogleDriveLink = (TextView) this
                .findViewById(R.id.sm_google_drive_link);
        if (this.mGoogleDriveLink != null) {
            this.mGoogleDriveLink.setOnClickListener(this);
        }

        this.mFacebookLink = (TextView) this
                .findViewById(R.id.sm_facebook_link);
        if (this.mFacebookLink != null) {
            this.mFacebookLink.setOnClickListener(this);
        }

        this.mTwitterLink = (TextView) this.findViewById(R.id.sm_twitter_link);
        if (this.mTwitterLink != null) {
            this.mTwitterLink.setOnClickListener(this);
        }

        this.mLinkedInLink = (TextView) this
                .findViewById(R.id.sm_linkedin_link);
        if (this.mLinkedInLink != null) {
            this.mLinkedInLink.setOnClickListener(this);
        }

        this.mFlickrLink = (TextView) this.findViewById(R.id.sm_flickr_link);
        if (this.mFlickrLink != null) {
            this.mFlickrLink.setOnClickListener(this);
        }

        this.mPicasaLink = (TextView) this.findViewById(R.id.sm_picasa_link);
        if (this.mPicasaLink != null) {
            this.mPicasaLink.setOnClickListener(this);
        }

        this.mWebView = (WebView) this.findViewById(R.id.sm_webview);
        this.mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView aView, String aUrl) {
                super.shouldOverrideUrlLoading(aView, aUrl);
                Log.v(TAG, "shouldOverrideUrlLoading#called");
                ShareSkreenActivity.this.mAddressField.setText(aUrl);
                return false;
            }

            @Override
            public void onPageFinished(WebView aView, String aUrl) {
                super.onPageFinished(aView, aUrl);
                Log.v(TAG, "onPageFinished#called");
                ShareSkreenActivity.this.mProgressDialog.hide();
                ShareSkreenActivity.this.mAddressField.setText(aUrl);
            }
        });
        this.mWebView.setWebChromeClient(new WebChromeClient());
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.requestFocus();
        this.goToUrl(DEFAULT_URL);

        /* skreen me actions */
        this.mContactUsLink = (TextView) this
                .findViewById(R.id.sm_contactus_link);
        this.mContactUsLink.setOnClickListener(this);
        this.mSessionInfo = (TextView) this
                .findViewById(R.id.sm_session_info_text);

        this.mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(this.mDisplayMetrics);
        this.mSkreenMeSwitch = (Switch) this
                .findViewById(R.id.sm_skreen_me_switch);
        this.mSkreenMeSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton aButtonView, boolean aIsChecked) {
        if (aIsChecked) {
            ShareSkreenActivity.this.mSkreenMeSwitch.setEnabled(false);
            ShareSkreenActivity.this.openSkreenMe();
        } else {
            ShareSkreenActivity.this.closeSkreenMe();
            ShareSkreenActivity.this.mSkreenMeSwitch.setEnabled(false);
        }
    }

    /**
     * 
     */
    private void goBack() {
        if (!this.mWebView.canGoBack()) {
            return;
        }

        String url = this.mWebView.copyBackForwardList().getCurrentItem()
                .getUrl();
        Log.v(TAG, "goBack#url: " + url);
        this.mWebView.goBack();
        this.mAddressField.setText(url);
    }

    private void goHome() {
        this.goToUrl(DEFAULT_URL);
    }

    /**
     * Goes to a url or searches Google for it
     * 
     * @param aUrlString
     */
    private void goToUrl(String aUrlString) {
        Log.v(TAG, "goToUrl#called");
        if (aUrlString == null || aUrlString.length() == 0) {
            Log.e(TAG, "goToUrl#invalid url");
            return;
        }

        try {
            new URL(aUrlString); // just to check if valid url
            this.mWebView.loadUrl(aUrlString);
            this.mProgressDialog.show();
            Log.v(TAG, "goToUrl#loading url: " + aUrlString);
        } catch (MalformedURLException e) {
            aUrlString = GOOGLE_SEARCH_URL + aUrlString;
            this.mWebView.loadUrl(aUrlString);
            Log.v(TAG, "goToUrl#searching url: " + aUrlString);
        }
        Log.v(TAG, "goToUrl#backButton: " + this.mBackButton.isEnabled()
                + " webView: " + this.mWebView.canGoBack());
        if (!this.mBackButton.isEnabled() && this.mWebView.canGoBack()) {
            this.mBackButton.setEnabled(true);
        }
    }

    /**
     * 
     */
    private void contactSkreenMe() {
        this.mStopSkreenMeOnPause = false;
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { SUPPORT_EMAIL });
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getString(R.string.contact_us_subject));
        intent.putExtra(android.content.Intent.EXTRA_TEXT,
                getString(R.string.contact_us_body));
        /* Send it off to the Activity-Chooser */
        this.startActivity(Intent.createChooser(intent,
                getString(R.string.contact_us)));
    }

    /**
     * 
     */
    private void shareSkreenMe() {
        if (this.mSkreenMeId == null || this.mSkreenMeId.length() == 0) {
            Log.e(TAG, "shareSkreenMe#valid id not found");
            return;
        }

        this.mStopSkreenMeOnPause = false;
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getString(R.string.share_skreen_me_subject));
        String shareSkreenString = SHARE_SKREEN_TEXT.replace("#",
                this.mSkreenMeId);
        intent.putExtra(android.content.Intent.EXTRA_TEXT,
                Html.fromHtml(shareSkreenString));

        /* Send it off to the Activity-Chooser */
        this.startActivity(Intent.createChooser(intent,
                getString(R.string.share_skreen_me_title)));
    }
    
    /**
     * 
     */
    private void openSkreenMe() {
        /*
         * To start sharing your app skreen call openSkreenMeSession
         * on your SkreenMe class's object.
         * 
         */
        this.skreenme.openSkreenMeSession(getString(R.string.skreem_me_name),
                this.mDisplayMetrics.widthPixels,
                this.mDisplayMetrics.heightPixels);
    }

    /**
     * 
     */
    private void closeSkreenMe() {
        /*
         * To end sharing your app skreen call closeSkreen
         * on your SkreenMe class's object.
         * 
         */
        SessionState currSessionState = this.skreenme.getCurrentSessionState();
        if (currSessionState == SessionState.CONNECTED_SKREENME
                || currSessionState == SessionState.CONNECTING_SKREENME) {
            this.skreenme.closeSkreen();
            this.mSessionInfo.setText("");
            this.mSessionInfo.setVisibility(View.GONE);
            this.mSkreenMeSwitch.setChecked(false);
        }
    }

    /**
     * 
     * @param aMessge
     * @param aClickListener
     */
    private void showDialog(String aMessge,
            DialogInterface.OnClickListener aClickListener) {
        if (!this.isActive) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(aMessge).setTitle(R.string.app_name)
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton(android.R.string.ok, aClickListener)
                .setCancelable(false).create().show();
    }

    /**
     * 
     * @param aMessage
     * @param aPositiveButtonText
     * @param aIsCancelable
     * @param aClickListener
     */
    private void showDialog(Spanned aMessage, String aPositiveButtonText,
            boolean aIsCancelable,
            DialogInterface.OnClickListener aClickListener) {
        if (!this.isActive) {
            return;
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_alert_dialog_text, null);
        TextView textView = (TextView) view
                .findViewById(R.id.alert_dialog_text);
        textView.setText(aMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setTitle(R.string.app_name)
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton(aPositiveButtonText, aClickListener)
                .setCancelable(aIsCancelable).create().show();
    }

    // ================================================================================
    // Implemented interfaces/listeners
    // ================================================================================
    @Override
    public void onClick(View aV) {
        /* actions for quick links */
        if (aV == this.mDropboxLink) {
            this.goToUrl(DROPBOX_URL);
        } else if (aV == this.mGoogleDriveLink) {
            this.goToUrl(GOOGLE_DRIVE_URL);
        } else if (aV == this.mFacebookLink) {
            this.goToUrl(FACEBOOK_URL);
        } else if (aV == this.mTwitterLink) {
            this.goToUrl(TWITTER_URL);
        } else if (aV == this.mLinkedInLink) {
            this.goToUrl(LINKEDIN_URL);
        } else if (aV == this.mFlickrLink) {
            this.goToUrl(FLICKER_URL);
        } else if (aV == this.mPicasaLink) {
            this.goToUrl(PICASA_URL);
        } else if (aV == this.mGoButton) {
            String urlString = this.mAddressField.getText().toString();
            this.goToUrl(urlString);
        } else if (aV == this.mHomeButton) {
            this.goHome();
        } else if (aV == this.mBackButton) {
            this.goBack();
        } else if (aV == this.mContactUsLink) {
            this.contactSkreenMe();
        } else if (aV == this.mAddressFieldClearButton) {
            this.mAddressField.setText("");
            this.mAddressField.requestFocus();
        }
    }

    @Override
    public void onSkreenMeSessionCloseError(String aErrorMsg) {
        this.showDialog(aErrorMsg, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface aDialog, int aWhich) {
                aDialog.dismiss();
            }
        });
    }

    @Override
    public void onArchNotSupported() {
        this.showDialog(getString(R.string.arch_not_supported),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface aDialog, int aWhich) {
                        finish();
                    }
                });
    }
}

