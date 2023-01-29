package com.example.schedule.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.schedule.R;
import com.example.schedule.adapter.CalendarGridAdapter;
import com.example.schedule.util.DateUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CalendarFragment";

    // TODO: Rename and change types of parameters
    private FragmentActivity mContext;
    private int m_month = 0;
    private View mView;
    private GridView gv_calendar;
    private int jumpMonth = 0;
    private int jumpYear=0;
    private CalendarGridAdapter cal;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param seq Parameter 1.
     *
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(int seq) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        Log.d(TAG, "seq===month: "+seq);
        args.putInt("month",seq);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        if(getArguments() !=null){
            m_month = getArguments().getInt("month", 1);
        }
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_calendar, container, false);
        gv_calendar = (GridView) mView.findViewById(R.id.gv_calendar);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initDate();
    }

    private void initDate() {
        String currentDate = DateUtil.getNowDateTime("yyyy-MM-dd");
        int year_c = Integer.parseInt(currentDate.split("-")[0]);
        int month_c = Integer.parseInt(currentDate.split("-")[1]);
        int day_c = Integer.parseInt(currentDate.split("-")[2]);
        jumpMonth = m_month - month_c;
        cal = new CalendarGridAdapter(mContext, jumpYear, jumpMonth, year_c, month_c, day_c);
        gv_calendar.setAdapter(cal);
    }
}