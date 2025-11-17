package com.example.quanlydodung.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydodung.R;
import com.example.quanlydodung.UpdateDoDungActivity;
import com.example.quanlydodung.database.DBHelper;
import com.example.quanlydodung.models.DoDung;

import java.util.ArrayList;

public class DoDungAdapter extends RecyclerView.Adapter<DoDungAdapter.ViewHolder> {

    private ArrayList<DoDung> list;
    private Context context;
    private DBHelper db;

    public DoDungAdapter(Context context, ArrayList<DoDung> list) {
        this.context = context;
        this.list = list;
        this.db = new DBHelper(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_do_dung, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DoDung d = list.get(position);
        holder.tvTen.setText(d.getTenDoDung());
        holder.tvGia.setText("Giá: " + (int)d.getGia() + " VND");
        holder.tvLoai.setText("Loại ID: " + d.getLoaiDoDung());

        if (d.getAnh() != null && !d.getAnh().isEmpty()) {
            Bitmap bmp = d.getBitmapImage();
            if (bmp != null) holder.img.setImageBitmap(bmp);
        } else {
            holder.img.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.btnEdit.setOnClickListener(v -> {
            Intent it = new Intent(context, UpdateDoDungActivity.class);
            it.putExtra("id", d.getId());
            context.startActivity(it);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa đồ dùng")
                    .setMessage("Bạn có chắc muốn xóa " + d.getTenDoDung() + "?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        db.deleteDoDung(d.getId());
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
        ImageView img;
        TextView tvTen, tvGia, tvLoai;
        ImageButton btnEdit, btnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgDoDung);
            tvTen = itemView.findViewById(R.id.tvTenDoDung);
            tvGia = itemView.findViewById(R.id.tvGiaDoDung);
            tvLoai = itemView.findViewById(R.id.tvLoaiDoDung);
            btnEdit = itemView.findViewById(R.id.btnEditDoDung);
            btnDelete = itemView.findViewById(R.id.btnDeleteDoDung);
        }
    }
}
