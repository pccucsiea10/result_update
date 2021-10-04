package com.example.heartsound.Quiz.Department;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.heartsound.R;
import com.example.heartsound.Quiz.StartFragment;

public class StartDepartment4_8 extends Fragment {

    private View mainView;
    private Button back_department_start,back_vedio;
    private TextView tital_test;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.start_department4_8,container,false);

        back_department_start = mainView.findViewById(R.id.back_department_start);
        back_vedio = mainView.findViewById(R.id.back_vedio);
        tital_test = mainView.findViewById(R.id.tital_test);




        back_department_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container,new StartFragment()).commit();
            }
        });
//        fm.beginTransaction().add(R.id.fragment_container, StartDepartment3, "fragment1").add(R.id.fragment_container, StartDepartment3_1, "StartDepartment3_1").hide(StartDepartment3_1)
//                .add(R.id.fragment_container, StartDepartment3_2, "StartDepartment3_2").hide(StartDepartment3_2).commit();

        back_vedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container,new StartDepartment3_8()).addToBackStack(null).commit();
            }
        });
        return mainView;
    }

}
