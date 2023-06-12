package com.example.expensemanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private Context context;
    private OnItemClick onItemClick;
    private List<ExpenseModel> expenseModelList;

    public ExpenseAdapter(Context context,OnItemClick onItemClick) {
        this.context = context;
        expenseModelList=new ArrayList<>();
        this.onItemClick=onItemClick;
    }
    public void add(ExpenseModel expenseModel){
        expenseModelList.add(expenseModel);
        notifyDataSetChanged();
    }
    public void clear(){
        expenseModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpenseModel expenseModel=expenseModelList.get(position);
        holder.note.setText(expenseModel.getNote());
        holder.category.setText(expenseModel.getCategory());
        holder.amount.setText(String.valueOf(expenseModel.getAmount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onClick(expenseModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView note,category,amount,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            note=itemView.findViewById(R.id.txtNote);
            category=itemView.findViewById(R.id.txtCategory);
            amount=itemView.findViewById(R.id.txtAmount);
            date=itemView.findViewById(R.id.txtDate);
        }
    }


}
