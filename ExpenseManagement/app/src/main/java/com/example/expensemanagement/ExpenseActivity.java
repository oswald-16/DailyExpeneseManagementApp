package com.example.expensemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.expensemanagement.databinding.ActivityExpenseBinding;
import com.example.expensemanagement.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.UUID;

public class ExpenseActivity extends AppCompatActivity {
    ActivityExpenseBinding binding;
    private String type;
    private ExpenseModel expenseModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        type=getIntent().getStringExtra("type");
        expenseModel=(ExpenseModel) getIntent().getSerializableExtra("model");

        if(expenseModel!=null){
            type=expenseModel.getType();
            binding.amount.setText(String.valueOf(expenseModel.getAmount()));
            binding.category.setText(expenseModel.getCategory());
            binding.note.setText(expenseModel.getNote());
        }


        if(type.equals("Income")){
            binding.rb1.setChecked(true);
        }else {
            binding.rb2.setChecked(true);
        }

        binding.rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="Income";
            }
        });

        binding.rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="Expense";
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        if(expenseModel==null){
            menuInflater.inflate(R.menu.menu_item,menu);
        }else{
            menuInflater.inflate(R.menu.update_item,menu);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.save){
            if(expenseModel==null){
                createExpense();
            }else{
                updateExpense();
            }
            return true;
        }
        if(id==R.id.delete){
            deleteExpense();
        }
        return false;
    }

    private void deleteExpense() {
        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .document(expenseModel.getExpenseId())
                .delete();
        finish();
    }

    private void createExpense() {
        String expenseId= UUID.randomUUID().toString();
        String amount=binding.amount.getText().toString();
        String note=binding.note.getText().toString();
        String category=binding.category.getText().toString();
        boolean incomechecked=binding.rb1.isChecked();

        if(incomechecked){
            type="Income";
        }else{
            type="Expense";
        }

        if(amount.trim().length() == 0){
            binding.amount.setError("Amount is Empty");
            return;
        }
        ExpenseModel expenseModel;
        expenseModel = new ExpenseModel(expenseId,note,category,type,Long.parseLong(amount),Calendar.getInstance().getTimeInMillis(), FirebaseAuth.getInstance().getUid());

        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .document(expenseId)
                .set(expenseModel);
        finish();
    }

    private void updateExpense() {
        String expenseId= expenseModel.getExpenseId();
        String amount=binding.amount.getText().toString();
        String note=binding.note.getText().toString();
        String category=binding.category.getText().toString();
        boolean incomechecked=binding.rb1.isChecked();

        if(incomechecked){
            type="Income";
        }else{
            type="Expense";
        }

        if(amount.trim().length() == 0){
            binding.amount.setError("Amount is Empty");
            return;
        }
        ExpenseModel model=new ExpenseModel(expenseId,note,category,type,Long.parseLong(amount),expenseModel.getTime(),FirebaseAuth.getInstance().getUid());

        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .document(expenseId)
                .set(model);
        finish();
    }
}