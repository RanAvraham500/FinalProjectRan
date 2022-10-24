package com.example.finalprojectran.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectran.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Context context;
    LoginBasic.CardComponents[] content;

    public RecyclerViewAdapter(Context context, LoginBasic.CardComponents[] content) {
        this.context = context;
        this.content = content;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);

        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.cardTv.setText(content[position].text+"");
        holder.cardImage.setBackgroundResource(content[position].imageRes);
        holder.card.setCardBackgroundColor(ContextCompat.getColor(context ,content[position].carBackgroundRes));
    }

    @Override
    public int getItemCount() {
        return content.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView cardTv;
        ImageView cardImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            cardTv = itemView.findViewById(R.id.cardTv);
            cardImage = itemView.findViewById(R.id.cardImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginActivity.recyclerOnClick();
                }
            });
        }
    }
}
