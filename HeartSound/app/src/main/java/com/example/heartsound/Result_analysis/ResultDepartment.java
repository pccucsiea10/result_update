package com.example.heartsound.Result_analysis;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.heartsound.Basic_information.PersonFragment;
import com.example.heartsound.REST_API.ElasticRestClient;
import com.example.heartsound.Result_analysis.LineChartData.LineChartData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;

import com.example.heartsound.R;
import com.golife.database.table.TablePulseRecord;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;


public class ResultDepartment extends Fragment {
    public static ArrayList<Entry> enties = new ArrayList<Entry>();
    private View mainView;
    private TextView tital_startnormal,mTextView;
    LineChartData lineChartData;
    LineChart lineChart;
    ArrayList<String> xData = new ArrayList<>();
    ArrayList<Entry> yData = new ArrayList<>();
    ProgressBar progressBar =null;
    private int progress = 0;//變化參數



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        RequestParams params = new RequestParams();
        ElasticRestClient.get("hh3/_search?q=subject:\"電機及資訊科系測量\"&size=100",params,new JsonHttpResponseHandler());

        mainView = inflater.inflate(R.layout.result_department, container, false);

        progressBar = mainView.findViewById(R.id.progressBar);
        tital_startnormal = mainView.findViewById(R.id.First);
        mTextView=mainView.findViewById(R.id.progress_tv);
        lineChart = mainView.findViewById(R.id.lineChart);
        lineChartData = new LineChartData(lineChart, this);
//        maxprogress=progressBar.getMax();
//            RequestParams params = new RequestParams();

//        ElasticRestClient.get("hh2/_search?q=name:張", params, new JsonHttpResponseHandler());


//        ElasticRestClient.get("hh2/_search?q=name:張&size=100", params, new JsonHttpResponseHandler());

//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        for (int i = 0; i < enties.size(); i++) {
            xData.add("" + i);
        }

        lineChartData.initX(xData);
        lineChartData.initY(50F, 150F);
        lineChartData.initDataSet(enties);
//        StringEntity entity ;
//        try {
//                JSONObject j = new JSONObject();
//                j.put("name", "yy");
//                j.put("subject", "電機及資訊科系測量");
//                j.put("serial number", "1");
//                j.put("result","80");
//                j.put("time","Wed Sep 29 16:24:44 GMT+08:00 2021");
//                j.put("timeForCompare","1632903884000");
//                entity = new StringEntity(j.toString(), HTTP.UTF_8);
//                ElasticRestClient.post(null,"hh2/_doc", entity, "application/json",new JsonHttpResponseHandler() );
//            } catch (JSONException jsonException) {
//            jsonException.printStackTrace();
//        }
//
//
//        ElasticRestClient.get("hh2/_search?q=subject:\"電機及資訊科系測量\"&size=100",params,new JsonHttpResponseHandler());



//        progressBar.setMax(100);
//        progressBar.setProgress(20);

        new Thread(new Runnable(){
            @Override
            public void run(){
                //do something
                progress += 80;
                if (progress <= 100)
                    mTextView.setText(progress + "%");


                progressBar.setProgress(progress);


            }
        }).start();

        return mainView;
    }

}
