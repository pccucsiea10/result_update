package com.example.heartsound.Result_analysis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.heartsound.REST_API.ElasticRestClient;
import com.example.heartsound.Result_analysis.LineChartData.LineChartData_3;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import com.example.heartsound.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ResultDepartment_3 extends Fragment {
    public static ArrayList<Entry> enties = new ArrayList<Entry>();
    private View mainView;
    private TextView tital_startnormal,mTextView;
    LineChartData_3 lineChartData;
    LineChart lineChart;
    ArrayList<String> xData = new ArrayList<>();
    ArrayList<Entry> yData = new ArrayList<>();
    ProgressBar progressBar =null;
    private int progress = 0;//變化參數




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RequestParams params = new RequestParams();
        ElasticRestClient.get("hh3/_search?q=subject:\"商業與管理群測量\"&size=100", params, new JsonHttpResponseHandler());

        mainView = inflater.inflate(R.layout.result_department_3, container, false);

        tital_startnormal = mainView.findViewById(R.id.First);
        progressBar = mainView.findViewById(R.id.progressBar);
        mTextView=mainView.findViewById(R.id.progress_tv);
        lineChart = mainView.findViewById(R.id.lineChart);
        lineChartData = new LineChartData_3(lineChart,this);

//            RequestParams params = new RequestParams();

//        ElasticRestClient.get("hh2/_search?q=name:張", params, new JsonHttpResponseHandler());


//        ElasticRestClient.get("hh2/_search?q=name:張&size=100", params, new JsonHttpResponseHandler());

//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        for(int i = 0;i<enties.size();i++){
            xData.add("" + i );
        }

        lineChartData.initX(xData);
        lineChartData.initY(50F,150F);
        lineChartData.initDataSet(enties);

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
