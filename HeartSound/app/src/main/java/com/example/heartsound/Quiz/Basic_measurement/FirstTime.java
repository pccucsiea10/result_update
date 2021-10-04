package com.example.heartsound.Quiz.Basic_measurement;


import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.heartsound.Communication.Connect;
import com.example.heartsound.R;

public class FirstTime extends Fragment {

    private View mainView;
    private Button department_stop_test,next;
    public Chronometer count_down2;
    private String USERAGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.137 Safari/537.36";
    public static TextView First;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){

        mainView = inflater.inflate(R.layout.activity_first_time,container,false);
        count_down2 = mainView.findViewById(R.id.count_down);
        department_stop_test = mainView.findViewById(R.id.department_stop_test);
        next = mainView.findViewById(R.id.next);
        First = mainView.findViewById(R.id.First);
        count_down2.setBase(SystemClock.elapsedRealtime() + 600000);
        count_down2.start();
        WebView webView = (WebView)mainView.findViewById(R.id.sd);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setAllowContentAccess(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        webView.getSettings().setUserAgentString(USERAGENT);//Important to auto play video
        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://www.youtube.com/embed/VA0bCAil3F0");


        webView.setWebViewClient(new WebViewClient() {
            // autoplay when finished loading via javascript injection
            public void onPageFinished(WebView view, String url) { webView.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()"); }
        });
        webView.loadUrl("javascript:(function() { document.getElementsByClassName('ytp-large-play-button ytp-button')[0].click(); })()");

//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                webView.setWebChromeClient(new WebChromeClient());
//                webView.loadUrl(url);
//                return false;
//            }
//        });


        department_stop_test.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                count_down2.stop();
                Connect.sync(First.getText().toString());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect.sync(First.getText().toString());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container,new SecondTime()).commit();
            }
        });
        count_down2.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {

                count_down2.setText(count_down2.getText().toString().substring(1));
                if (SystemClock.elapsedRealtime()-count_down2.getBase()>=0){
                    count_down2.stop();
                }
            }
        });
        return mainView;


    }


}


