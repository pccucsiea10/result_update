package com.example.heartsound.Quiz.Basic_measurement;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import com.example.heartsound.R;

public class StartNormal extends Fragment {

    private View mainView;
    private Button normal_start;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.start_normal,container,false);
        normal_start = mainView.findViewById(R.id.normal_start);

        normal_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container, new FirstTime()).commit();

            }
        });

        return mainView;
    }

}
