package com.example.heartsound.Quiz.Basic_measurement;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.Chronometer;
import com.example.heartsound.R;


public class StartNormal2 extends Fragment {

    private View mainView;
    private Button normal_stop_test;
    public Chronometer count_down2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.start_normal2,container,false);
        normal_stop_test = mainView.findViewById(R.id.normal_stop_test);

        count_down2 = mainView.findViewById(R.id.count_down);
        count_down2.setBase(SystemClock.elapsedRealtime() + 300000);
        count_down2.start();

        normal_stop_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Connect.sync();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container,new StartNormal3()).commit();

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
