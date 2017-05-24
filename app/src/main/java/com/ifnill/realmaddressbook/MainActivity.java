package com.ifnill.realmaddressbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ifnill.realmaddressbook.dto.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button addBtn;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RealmRecyclerViewAdapter mRealmRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mLinearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRealmRecyclerViewAdapter = new RealmRecyclerViewAdapter(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mRealmRecyclerViewAdapter);

        addBtn = (Button) findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRealmRecyclerViewAdapter.notifyChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_btn:
                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                break;
        }
    }

    public class RealmRecyclerViewAdapter extends RecyclerView.Adapter<RealmRecyclerViewAdapter.ViewHolder>{
        RealmResults<User> users;
        Realm realm;
        Context context;

        public RealmRecyclerViewAdapter(Context context) {
            this.context = context;
            realm = Realm.getDefaultInstance();
            RealmQuery<User> query = realm.where(User.class);
            users = query.findAll();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final User user = users.get(position);
            holder.nameTxt.setText(user.getName());
            holder.phoneTxt.setText(user.getPhone());
            holder.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.where(User.class).equalTo("phone",user.getPhone()).findFirst().deleteFromRealm();
                        }
                    });
                    notifyChanged();
                }
            });
            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddActivity.class);
                    intent.putExtra("phone", user.getPhone());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        public void notifyChanged(){
            users = realm.where(User.class).findAll();
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView nameTxt, phoneTxt;
            public Button delBtn, editBtn;

            public ViewHolder(View itemView) {
                super(itemView);
                nameTxt = (TextView) itemView.findViewById(R.id.name_txt);
                phoneTxt = (TextView) itemView.findViewById(R.id.phone_txt);
                delBtn = (Button) itemView.findViewById(R.id.del_btn);
                editBtn = (Button) itemView.findViewById(R.id.edit_btn);
            }
        }
    }
}