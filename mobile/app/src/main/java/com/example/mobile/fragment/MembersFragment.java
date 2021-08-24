package com.example.mobile.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mobile.R;
import com.example.mobile.model.Member;
import com.example.mobile.service.adapter.MemberAdapter;
import java.util.List;

public class MembersFragment extends Fragment {

    private List<Member> members;

    public MembersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_members, container, false);

        if (getArguments() != null)
            members = getArguments().getParcelableArrayList("members");

        RecyclerView membersWalletRv = view.findViewById(R.id.members_wallet_rv);
        membersWalletRv.setLayoutManager(new LinearLayoutManager(getContext()));

        MemberAdapter memberAdapter = new MemberAdapter(getContext(), members);
        membersWalletRv.setAdapter(memberAdapter);
        memberAdapter.notifyDataSetChanged();
        return view;
    }
}