package com.example.heartsound.Quiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;

import com.example.heartsound.Communication.Connect;
import com.example.heartsound.Quiz.Basic_measurement.StartNormal;
import com.example.heartsound.Quiz.Department.StartDepartment;
import com.example.heartsound.R;


public class StartFragment extends Fragment {

    private View mainView;
    private Button btn_normal,btn_department;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Fragment即將在螢幕上顯示時執行此方法
        mainView = inflater.inflate(R.layout.fragment_start,container,false);
        btn_normal = mainView.findViewById(R.id.btn_normal);
        btn_department = mainView.findViewById(R.id.btn_department);

        btn_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect.clear();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container,new StartNormal()).commit();
            }
        });
        btn_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect.clear();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container,new StartDepartment()).commit();
            }
        });
        return mainView;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState)
//    {
//        // Fragment剛被建立時執行此方法
//        super.onCreate(savedInstanceState);
//    }
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState)
//    {
//        // 在這個方法中取得並定義Fragment的介面元件
//        super.onActivityCreated(savedInstanceState);
//        Button botton = (Button) getView().findViewById(R.id.start_narmal);
//
//    }
//
//    @Override
//    public void onPause()
//    {
//        // 當Fragment要從螢幕消失時執行此方法
//        super.onPause();
//    }
}
