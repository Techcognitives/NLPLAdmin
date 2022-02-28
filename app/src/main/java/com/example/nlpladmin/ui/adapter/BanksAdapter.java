package com.example.nlpladmin.ui.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.nlpladmin.R;
import com.example.nlpladmin.model.BankModel;
import com.example.nlpladmin.ui.activity.ViewBankDetailsActivity;

import java.util.ArrayList;

public class BanksAdapter extends RecyclerView.Adapter<BanksAdapter.BankViewHolder> {

    private ArrayList<BankModel> bankList;
    private ViewBankDetailsActivity activity;

    public BanksAdapter(ViewBankDetailsActivity activity, ArrayList<BankModel> bankList) {
        this.bankList = bankList;
        this.activity = activity;
    }

    @Override
    public BankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_list, parent, false);
        return new BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BankViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BankModel obj = bankList.get(position);
//---------------------------------- Set Title -----------------------------------------------------
        String name1 = obj.getAccountholder_name();
        Log.i("File Name:", name1);

        String accNumber = obj.getAccount_number();
        String ifsiNumber = obj.getIFSI_CODE();
        String bankName = obj.getBank_name();

        holder.list_bank_name.setText(bankName);
        holder.list_acc_no.setText(" A/C: " + accNumber);
        holder.list_ifsi.setText(" IFSI: "+ ifsiNumber);

//--------------------------------------------------------------------------------------------------
        holder.list_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getBankDetails(obj);
            }
        });

        String chequeURL = obj.getCancelled_cheque();
        if (chequeURL.length() < 6){
            holder.list_preview_bank_details.setVisibility(View.GONE);
        }else{
            holder.list_preview_bank_details.setVisibility(View.VISIBLE);
        }

        holder.list_preview_bank_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onClickPreviewBankDetails(obj);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    public void updateData(ArrayList<BankModel> bankList) {
        this.bankList = bankList;
        notifyDataSetChanged();
    }

    public class BankViewHolder extends RecyclerView.ViewHolder {
        private TextView list_acc_no, list_edit, list_bank_name, list_ifsi, list_preview_bank_details;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            list_acc_no = itemView.findViewById(R.id.bank_list_account_number_text);
            list_edit = itemView.findViewById(R.id.bank_list_edit_bank_details_text);
            list_bank_name = itemView.findViewById(R.id.bank_list_bank_name);
            list_ifsi = itemView.findViewById(R.id.bank_list_ifsi_text);
            list_preview_bank_details = itemView.findViewById(R.id.bank_list_preview_bank_details);
        }

    }
//--------------------------------------------------------------------------------------------------
}