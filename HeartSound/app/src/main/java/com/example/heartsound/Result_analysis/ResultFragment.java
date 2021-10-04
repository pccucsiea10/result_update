package com.example.heartsound.Result_analysis;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.heartsound.REST_API.ElasticRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.example.heartsound.R;


public class ResultFragment extends Fragment implements View.OnClickListener {

    private View mainView;
    private TextView tital_list;
    static public Button choose1, choose2, choose3, choose4, choose5, choose6, choose7, choose8,
            choose9, choose10;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.list_department, container, false);

        tital_list = mainView.findViewById(R.id.tital_list);
        tital_list.setText("結果分析 - 科系列表");


        choose1 = mainView.findViewById(R.id.choose1);
        choose2 = mainView.findViewById(R.id.choose2);
        choose3 = mainView.findViewById(R.id.choose3);
        choose4 = mainView.findViewById(R.id.choose4);
        choose5 = mainView.findViewById(R.id.choose5);
        choose6 = mainView.findViewById(R.id.choose6);
        choose7 = mainView.findViewById(R.id.choose7);
        choose8 = mainView.findViewById(R.id.choose8);
        choose9 = mainView.findViewById(R.id.choose9);
        choose10 = mainView.findViewById(R.id.choose10);

        choose1.setOnClickListener(this);
        choose2.setOnClickListener(this);
        choose3.setOnClickListener(this);
        choose4.setOnClickListener(this);
        choose5.setOnClickListener(this);
        choose6.setOnClickListener(this);
        choose7.setOnClickListener(this);
        choose8.setOnClickListener(this);
        choose9.setOnClickListener(this);
        choose10.setOnClickListener(this);

//        choose1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                fm.beginTransaction().replace(R.id.fragment_container, new ResultDepartment()).commit();
//
//            }
//
//        });

//        RequestParams params = new RequestParams();
//        ElasticRestClient.get("hh3/_search?q=subject:\"電機及資訊科系測量\"&size=100",params,new JsonHttpResponseHandler());
//        ElasticRestClient.get("hh3/_search?q=subject:\"土木與建築群測量\"&size=100", params, new JsonHttpResponseHandler());
//        ElasticRestClient.get("hh3/_search?q=subject:\"化工群測量\"&size=100", params, new JsonHttpResponseHandler());
//        ElasticRestClient.get("hh3/_search?q=subject:\"商業與管理群測量\"&size=100", params, new JsonHttpResponseHandler());
//        ElasticRestClient.get("hh3/_search?q=subject:\"設計群測量\"&size=100", params, new JsonHttpResponseHandler());
//        ElasticRestClient.get("hh3/_search?q=subject:\"農業群測量\"&size=100", params, new JsonHttpResponseHandler());
//        ElasticRestClient.get("hh3/_search?q=subject:\"家政與餐旅群測量\"&size=100", params, new JsonHttpResponseHandler());
//        ElasticRestClient.get("hh3/_search?q=subject:\"外語群測量\"&size=100", params, new JsonHttpResponseHandler());
//        ElasticRestClient.get("hh3/_search?q=subject:\"藝術群測量\"&size=100", params, new JsonHttpResponseHandler());
//        ElasticRestClient.get("hh3/_search?q=subject:\"醫學與醫護群測量\"&size=100", params, new JsonHttpResponseHandler());

        return mainView;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        switch (v.getId()){
            case R.id.choose1:{
                fm.beginTransaction().replace(R.id.fragment_container, new ResultDepartment()).commit();
                break;
            }
            case R.id.choose2: {
                fm.beginTransaction().replace(R.id.fragment_container, new ResultDepartment_1()).commit();
                break;
            }
            case R.id.choose3: {
                fm.beginTransaction().replace(R.id.fragment_container, new ResultDepartment_2()).commit();
                break;
            }
            case R.id.choose4: {
                fm.beginTransaction().replace(R.id.fragment_container, new ResultDepartment_3()).commit();
                break;
            }
            case R.id.choose5: {
                fm.beginTransaction().replace(R.id.fragment_container, new ResultDepartment_4()).commit();
                break;
            }
            case R.id.choose6: {
                fm.beginTransaction().replace(R.id.fragment_container, new ResultDepartment_5()).commit();
                break;
            }
            case R.id.choose7: {
                fm.beginTransaction().replace(R.id.fragment_container, new ResultDepartment_6()).commit();
                break;
            }
            case R.id.choose8: {
                fm.beginTransaction().replace(R.id.fragment_container, new ResultDepartment_7()).commit();
                break;
            }
            case R.id.choose9: {
                fm.beginTransaction().replace(R.id.fragment_container, new ResultDepartment_8()).commit();
                break;
            }
            case R.id.choose10:{
                fm.beginTransaction().replace(R.id.fragment_container,new ResultDepartment_9()).commit();
                break;
            }
            default:{
                fm.beginTransaction().replace(R.id.fragment_container,new ResultDepartment()).commit();
                break;

            }

        }
    }


}
