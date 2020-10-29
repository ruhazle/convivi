package com.example.project_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project_1.Adapter.HomeBuyAdapter;
import com.example.project_1.Adapter.HomeShareAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.ShareModel;
import model.UserModel;

public class HomeFragment extends Fragment {
    //HomeBuy RecyclerView 관련 변수
    private ArrayList<HomeBuyItem> buyList;
    private HomeBuyAdapter homeBuyAdapter;
    private RecyclerView homeBuyRecyclerView;
    private LinearLayoutManager buyLayoutManager;
    private HomeListDecoration homeListDecoration;

    //HomeShare Recyclerview 관련 변수
    private ArrayList<HomeShareItem> shareList;
    private HomeShareAdapter homeShareAdapter;
    private RecyclerView homeShareRecyclerView;
    private LinearLayoutManager shareLayoutManager;

    //Firebase
    private DatabaseReference share = FirebaseDatabase.getInstance().getReference().child("Share");
    private DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("Users");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);

        //HomeBuyRecyclerView
        homeBuyRecyclerView = (RecyclerView) view.findViewById(R.id.buy_listView);

        buyLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        homeBuyRecyclerView.setLayoutManager(buyLayoutManager);
        buyList = new ArrayList<>();
        homeBuyAdapter = new HomeBuyAdapter(buyList);

        buyLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        homeBuyRecyclerView.setLayoutManager(buyLayoutManager);
        buyList = new ArrayList<>();
        homeBuyAdapter = new HomeBuyAdapter(buyList);

        homeBuyRecyclerView.setAdapter(homeBuyAdapter);
        homeListDecoration = new HomeListDecoration();
        homeBuyRecyclerView.addItemDecoration(homeListDecoration);

        HomeBuyItem homeBuyItem = new HomeBuyItem(R.drawable.polarbear, "공구 제목", "OO동 XX아파트", "3명", "5명");
        buyList.add(homeBuyItem);
        buyList.add(homeBuyItem);
        buyList.add(homeBuyItem);
        buyList.add(homeBuyItem);
        buyList.add(homeBuyItem);
        homeBuyAdapter.notifyDataSetChanged();

        //HomeShareRecyclerView
        homeShareRecyclerView = (RecyclerView) view.findViewById(R.id.share_listView);
        shareLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        homeShareRecyclerView.setLayoutManager(shareLayoutManager);
        shareList = new ArrayList<>();
        homeShareAdapter = new HomeShareAdapter(shareList);
        homeShareRecyclerView.setAdapter(homeShareAdapter);
        homeShareRecyclerView.addItemDecoration(homeListDecoration);

            //Share DB에서 최근 5개의 글 가져와 RecyclerView로 보여주기
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userDataSnapshot) {

                share.orderByKey().limitToLast(5).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot latestShare : dataSnapshot.getChildren()){
                            ShareModel shareModel = latestShare.getValue(ShareModel.class);

                            UserModel userModel = userDataSnapshot.child(shareModel.host).getValue(UserModel.class);
                            HomeShareItem homeShareItem = new HomeShareItem(shareModel.idNum, userModel.imgURL, shareModel.title, "OO동 XX아파트");
                            shareList.add(homeShareItem);

                        }
                        homeShareAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //대여 및 나눔 더보기 클릭 시 ViewMoreActivity로 이동
        TextView view_more_share = view.findViewById(R.id.view_more_share);
        view_more_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), ViewMoreActivity.class));
            }
        });




        //글쓰기 버튼
        FloatingActionButton write_button = (FloatingActionButton) view.findViewById(R.id.write_button);
        write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), WriteActivity.class));
            }
        });
        return view;
    }
}
