package com.example.finalprojectran.login;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectran.R;

import java.util.ArrayList;

public class Car_RecyclerViewAdapter extends RecyclerView.Adapter<Car_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> carTvIndexes;

    public Car_RecyclerViewAdapter(Context context, ArrayList<String> carTvIndexes) {
        this.context = context;
        this.carTvIndexes = carTvIndexes;
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
        holder.tvCarNum.setText(carTvIndexes.get(position) + "");
        int i = position;
        holder.etCarModel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LoginBasic.carModels.set(i, (String) s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return carTvIndexes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvCarNum;
        EditText etCarModel;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCarNum = itemView.findViewById(R.id.tvCarNum);
            etCarModel = itemView.findViewById(R.id.etCarNum);
        }
    }
}
