package com.example.labw3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labw3.provider.PC;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PCListRecyclerViewAdapter extends RecyclerView.Adapter<PCListRecyclerViewAdapter.ViewHolder> {
    List<PC> pcs;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pc_card_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String pc_id = "" + pcs.get(position).getId();
        holder.pcIdTv.setText(pc_id);
        holder.cpuTv.setText(pcs.get(position).getCpu());
        String ram = "" + pcs.get(position).getRam();
        holder.ramTv.setText(ram);
        String ssd = "" + pcs.get(position).getSsd();
        holder.ssdTv.setText(ssd);
        holder.motherBoardTv.setText(pcs.get(position).getMotherBoard());
        holder.gCardTv.setText(pcs.get(position).getGraphicsCard());
        String price = "" + pcs.get(position).getPrice();
        holder.priceTv.setText(price);
    }

    @Override
    public int getItemCount() {
        if (pcs == null) //In case of the pcs arraylist is not set
            return 0;
        else
            return pcs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pcIdTv;
        public TextView cpuTv;
        public TextView ramTv;
        public TextView ssdTv;
        public TextView motherBoardTv;
        public TextView gCardTv;
        public TextView priceTv;

        public ViewHolder(View view) {
            super(view);
            pcIdTv = view.findViewById(R.id.pc_id_tv);
            cpuTv = view.findViewById(R.id.cpu_id_tv);
            ramTv = view.findViewById(R.id.ram_id_tv);
            ssdTv = view.findViewById(R.id.ssd_id_tv);
            motherBoardTv = view.findViewById(R.id.mb_id_tv);
            gCardTv = view.findViewById(R.id.gcard_id_tv);
            priceTv = view.findViewById(R.id.price_id_tv);
        }
    }

    public void setPCs(List<PC> pcs){
        this.pcs = pcs;
    }
}
