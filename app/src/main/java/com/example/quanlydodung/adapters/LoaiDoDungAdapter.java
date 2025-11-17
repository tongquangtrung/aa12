package com.example.quanlydodunghoctap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydodunghoctap.R;
import com.example.quanlydodunghoctap.database.DBHelper;
import com.example.quanlydodunghoctap.models.LoaiDoDung;

import java.util.ArrayList;

public class LoaiDoDungAdapter extends RecyclerView.Adapter<LoaiDoDungAdapter.ViewHolder> {

    private ArrayList<LoaiDoDung> list;
    private Context context;
    private DBHelper db;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onEdit(LoaiDoDung loai);
        void onDelete(LoaiDoDung loai);
    }

    public LoaiDoDungAdapter(Context context, ArrayList<LoaiDoDung> list) {
        this.context = context;
        this.list = list;
        this.db = new DBHelper(context);
    }

    public void setOnItemActionListener(OnItemActionListener l) { this.listener = l; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_loaidodung, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LoaiDoDung loai = list.get(position);
        holder.tvTen.setText(loai.getTenLoai());
        holder.tvMoTa.setText(loai.getMoTa());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(loai);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa loại")
                    .setMessage("Xóa loại " + loai.getTenLoai() + " ?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        db.deleteLoaiDoDung(loai.getId());
                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, list.size());
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvMoTa;
        ImageButton btnEdit, btnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTenLoaiDoDung);
            tvMoTa = itemView.findViewById(R.id.tvMoTaLoai);
            btnEdit = itemView.findViewById(R.id.btnEditLoai);
            btnDelete = itemView.findViewById(R.id.btnDeleteLoai);
        }
    }
}
