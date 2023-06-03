package com.example.fitnessapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fitnessapp.R;

import java.util.ArrayList;

public class AdapterTrainingPlan extends BaseAdapter {

    Context context;
    ArrayList<String> trainingPlanList;


    public AdapterTrainingPlan(Context context, ArrayList<String> trainingPlanList) {
        this.context = context;
        this.trainingPlanList = trainingPlanList;
    }

    @Override
    public int getCount() {
        return trainingPlanList.size();
    }

    @Override
    public Object getItem(int position) {
        return trainingPlanList.get(position);
    }

    public String getName(int position) {
        return this.trainingPlanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.training_plan_item, parent,
                                                         false);
        }
        TextView nr = convertView.findViewById(R.id.textLVNr);
        TextView name = convertView.findViewById(R.id.trainingPlanLV);


        nr.setText(String.valueOf(position + 1));
        name.setText(trainingPlanList.get(position));

        return convertView;

    }

}
