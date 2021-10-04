package com.example.heartsound.Quiz.Department;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import com.example.heartsound.R;


public class StartDepartment2 extends Fragment implements View.OnClickListener {

    private View mainView;
    static public Button choose1,choose2,choose3,choose4,choose5,choose6,choose7,choose8,choose9,choose10;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.list_department,container,false);

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


        return mainView;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        switch (v.getId()){
            case R.id.choose1:{
                fm.beginTransaction().replace(R.id.fragment_container,new StartDepartment3()).commit();
                break;
            }
            case R.id.choose2: {
                fm.beginTransaction().replace(R.id.fragment_container, new StartDepartment3_1()).commit();
                break;
            }
            case R.id.choose3: {
                fm.beginTransaction().replace(R.id.fragment_container, new StartDepartment3_2()).commit();
                break;
            }
            case R.id.choose4: {
                fm.beginTransaction().replace(R.id.fragment_container, new StartDepartment3_3()).commit();
                break;
            }
            case R.id.choose5: {
                fm.beginTransaction().replace(R.id.fragment_container, new StartDepartment3_4()).commit();
                break;
            }
            case R.id.choose6: {
                fm.beginTransaction().replace(R.id.fragment_container, new StartDepartment3_5()).commit();
                break;
            }
            case R.id.choose7: {
                fm.beginTransaction().replace(R.id.fragment_container, new StartDepartment3_6()).commit();
                break;
            }
            case R.id.choose8: {
                fm.beginTransaction().replace(R.id.fragment_container, new StartDepartment3_7()).commit();
                break;
            }
            case R.id.choose9: {
                fm.beginTransaction().replace(R.id.fragment_container, new StartDepartment3_8()).commit();
                break;
            }
            case R.id.choose10:{
                fm.beginTransaction().replace(R.id.fragment_container,new StartDepartment3_9()).commit();
                break;
            }
            default:{
                fm.beginTransaction().replace(R.id.fragment_container,new StartDepartment3()).commit();
                break;

        }

        }

}
}


