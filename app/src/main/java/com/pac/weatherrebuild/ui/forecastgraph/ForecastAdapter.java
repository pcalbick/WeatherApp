package com.pac.weatherrebuild.ui.forecastgraph;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.weatherrebuild.R;
import com.pac.weatherrebuild.viewmodel.WeatherViewModel;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.DayHolder> {

    public static final String DAY_CHANGE = "day_changed";
    public static final String SKY_CHANGE = "sky_changed";
    public static final String GRAPH_CHANGE = "graph_changed";

    private List<WeatherViewModel.ForecastDataSet> mDataSet;

    class DayHolder extends RecyclerView.ViewHolder {
        TextView day;
        TextView sky;
        TemperatureGraph graph;
        DayHolder(LinearLayout v){
            super(v);
            day = (TextView) v.getChildAt(0);
            sky = (TextView) v.getChildAt(1);
            graph = (TemperatureGraph) v.getChildAt(2);
        }
    }

    public ForecastAdapter(List<WeatherViewModel.ForecastDataSet> dataSet){
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public DayHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.forecast_item, viewGroup, false);

        return new DayHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DayHolder dayHolder, int i) {
        dayHolder.day.setText(mDataSet.get(i).getDay());
        dayHolder.sky.setText(mDataSet.get(i).getSky());
        dayHolder.graph.setBounds(mDataSet.get(i).getGraph(),mDataSet.get(i).getGraphBounds());
        dayHolder.graph.setText(Integer.toString(mDataSet.get(i).getGraph()[0]),
                Integer.toString(mDataSet.get(i).getGraph()[1]));
    }

    @Override
    public void onBindViewHolder(@NonNull DayHolder holder, int position, @NonNull List<Object> payloads) {
        if(payloads.contains(DAY_CHANGE)){
            holder.day.setText(mDataSet.get(position).getDay());
        }
        if(payloads.contains(SKY_CHANGE)){
            holder.sky.setText(mDataSet.get(position).getSky());
        }
        if(payloads.contains(GRAPH_CHANGE)){
            holder.graph.setBar(mDataSet.get(position).getGraph(),mDataSet.get(position).getGraphBounds());
            holder.graph.setText(Integer.toString(mDataSet.get(position).getGraph()[0]),
                    Integer.toString(mDataSet.get(position).getGraph()[1]));
            holder.graph.invalidate();
        }

        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        if(mDataSet != null)
            return mDataSet.size();
        return 0;
    }
}
