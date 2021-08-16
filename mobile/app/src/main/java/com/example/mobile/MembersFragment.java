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
import com.example.mobile.service.WalletAdapter;

import java.util.List;

public class MembersFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView member_rv;
    private List<Member> members;

    private String mParam1;
    private String mParam2;

    public MembersFragment() {
        // Required empty public constructor
    }

    public static MembersFragment newInstance(String param1, String param2) {
        MembersFragment fragment = new MembersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            members = getArguments().getParcelableArrayList("members");

            for(int i = 0; i <members.size() ;i++){
                System.out.println(members.get(i).getLogin());
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_members, container, false);
        member_rv = view.findViewById(R.id.members_rv);
        if (getArguments() != null) {
            members = getArguments().getParcelableArrayList("members");

            for(int i = 0; i <members.size() ;i++){
                System.out.println(members.get(i).getLogin());
            }
        }
        member_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        MemberAdapter memberAdapter = new MemberAdapter(getContext(), members);
        member_rv.setAdapter(memberAdapter);
        memberAdapter.notifyDataSetChanged();
        return view;
    }
}