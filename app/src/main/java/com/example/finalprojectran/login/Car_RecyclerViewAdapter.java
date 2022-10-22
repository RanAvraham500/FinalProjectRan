package com.example.finalprojectran.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectran.R;

public class Car_RecyclerViewAdapter extends RecyclerView.Adapter<Car_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    String[] tvIndexes;

    public Car_RecyclerViewAdapter(Context context, String[] carTvIndexes) {
        this.context = context;
        this.tvIndexes = carTvIndexes;
    }

    @NonNull
    @Override
    public Car_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_car_row, parent, false);

        return new Car_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Car_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvCarNum.setText(tvIndexes[position] + "");
        //holder.etCarModel.setId(position);
    }

    @Override
    public int getItemCount() {
        return tvIndexes.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvCarNum;
        EditText etCarModel, etCarMileage, etCarInsurance;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCarNum = itemView.findViewById(R.id.tvCarNum);
            etCarModel = itemView.findViewById(R.id.etCarNum);
            etCarMileage = itemView.findViewById(R.id.etCarMileage);
            etCarInsurance = itemView.findViewById(R.id.etCarInsurance);
        }
    }
}
