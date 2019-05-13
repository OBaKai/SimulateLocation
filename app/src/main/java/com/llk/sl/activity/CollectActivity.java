package com.llk.sl.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.llk.sl.R;
import com.llk.sl.db.CollectDao;
import com.llk.sl.db.CollectModel;
import com.llk.sl.eventbus.CollectEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CollectAdapter adapter;

    private List<CollectModel> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_collect);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        recyclerView.addItemDecoration(RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));

        data = CollectDao.getInstance().findAll();
        if (data == null) {
            data = new ArrayList<>();
        }
        adapter = new CollectAdapter(this, data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("CollectActivity", "onDestroy!!!");
    }

    private class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ItemViewHolder> {
        private LayoutInflater inflater;
        private List<CollectModel> data;
        private Context context;

        public CollectAdapter(Context context, List<CollectModel> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_collect, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, int position) {
            final CollectModel collectModel = data.get(position);

            holder.idTv.setText(collectModel.getId() + "");
            holder.addrTv.setText("经纬度：longitude=" + collectModel.getLongitude() + " latitude=" + collectModel.getLatitude());
            holder.noteTv.setText(collectModel.getNote());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new CollectEvent(collectModel.getLongitude(), collectModel.getLatitude()));
                    CollectActivity.this.finish();
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context).setTitle("要删除这条收藏吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CollectDao.getInstance().delete(collectModel.getId());
                            data.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();

                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            private TextView idTv;
            private TextView addrTv;
            private TextView noteTv;

            public ItemViewHolder(View view ) {
                super(view);

                idTv = view.findViewById(R.id.tv_item_id);
                addrTv = view.findViewById(R.id.tv_item_address);
                noteTv = view.findViewById(R.id.tv_item_note);
            }
        }
    }
}
