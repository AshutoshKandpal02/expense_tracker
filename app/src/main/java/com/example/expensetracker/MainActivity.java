package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerMonths;
    private TextView tvSelectedMonth, tvTotalIncome, tvTotalExpense;
    private EditText etExpenseName, etExpenseAmount;
    private Button btnAddExpense, btnLogout;
    private ListView listViewExpenses;
    private ArrayList<String> expensesList;
    private ArrayAdapter<String> adapter;
    private FirebaseAuth mAuth;

    // Simulated Income & Expenses (Replace with Firebase)
    private final HashMap<String, Integer> incomeMap = new HashMap<>();
    private final HashMap<String, Integer> expenseMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Initialize UI Elements
        spinnerMonths = findViewById(R.id.spinnerMonths);
        tvSelectedMonth = findViewById(R.id.tvSelectedMonth);
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        etExpenseName = findViewById(R.id.etExpenseName);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnLogout = findViewById(R.id.btnLogout);
        listViewExpenses = findViewById(R.id.listViewExpenses);

        expensesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expensesList);
        listViewExpenses.setAdapter(adapter);

        // Initialize Sample Data (Replace with Firebase)
        setupSampleData();

        // Spinner Data
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonths.setAdapter(spinnerAdapter);

        // Spinner Selection Listener
        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = months[position];
                tvSelectedMonth.setText(selectedMonth);
                updateIncomeAndExpense(selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Add Expense Button
        btnAddExpense.setOnClickListener(v -> addExpense());

        // Logout Button
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        // Expense List Click Listener
        listViewExpenses.setOnItemClickListener((parent, view, position, id) -> {
            String selectedExpense = expensesList.get(position);
            Toast.makeText(MainActivity.this, "Selected: " + selectedExpense, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void addExpense() {
        String name = etExpenseName.getText().toString().trim();
        String amount = etExpenseAmount.getText().toString().trim();

        if (name.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please enter expense details", Toast.LENGTH_SHORT).show();
            return;
        }

        String expense = name + " - ₹" + amount;
        expensesList.add(expense);
        adapter.notifyDataSetChanged();

        etExpenseName.setText("");
        etExpenseAmount.setText("");
    }

    private void setupSampleData() {
        // Dummy Income Data (Replace with Firebase)
        incomeMap.put("January", 50000);
        incomeMap.put("February", 48000);
        incomeMap.put("March", 55000);
        incomeMap.put("April", 47000);
        incomeMap.put("May", 60000);

        // Dummy Expense Data (Replace with Firebase)
        expenseMap.put("January", 30000);
        expenseMap.put("February", 25000);
        expenseMap.put("March", 28000);
        expenseMap.put("April", 26000);
        expenseMap.put("May", 32000);
    }

    private void updateIncomeAndExpense(String month) {
        int income = incomeMap.getOrDefault(month, 0);
        int expense = expenseMap.getOrDefault(month, 0);

        tvTotalIncome.setText("Total Income: ₹" + income);
        tvTotalExpense.setText("Total Expense: ₹" + expense);
    }
}
