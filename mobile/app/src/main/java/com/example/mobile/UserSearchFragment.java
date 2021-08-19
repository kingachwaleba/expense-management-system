package com.example.mobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobile.model.Member;
import com.example.mobile.service.MemberAdapter;
import com.example.mobile.service.SearchUserAdapter;

import java.util.ArrayList;
import java.util.List;


public class UserSearchFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView member_rv;
    private ArrayList<String> members;

    public UserSearchFragment() {
        // Required empty public constructor
    }

    public static UserSearchFragment newInstance(String param1, String param2) {
        UserSearchFragment fragment = new UserSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_search, container, false);
        member_rv = view.findViewById(R.id.members_search_rv);
        member_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        member_rv.setAdapter(null);
        if (getArguments() != null) {
            members = getArguments().getStringArrayList("members_search");
        }
        if(members.size()>0){

            SearchUserAdapter searchUserAdapter = new SearchUserAdapter(getContext(), members);
            member_rv.setAdapter(searchUserAdapter);
            searchUserAdapter.notifyDataSetChanged();
        }
        return view;
    }
}