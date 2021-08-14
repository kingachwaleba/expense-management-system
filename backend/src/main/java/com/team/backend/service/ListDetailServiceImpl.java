package com.team.backend.service;

import com.team.backend.model.List;
import com.team.backend.model.ListDetail;
import com.team.backend.model.Status;
import com.team.backend.repository.ListDetailRepository;
import com.team.backend.repository.StatusRepository;
import org.springframework.stereotype.Service;

@Service
public class ListDetailServiceImpl implements ListDetailService {

    private final StatusRepository statusRepository;
    private final ListDetailRepository listDetailRepository;

    public ListDetailServiceImpl(StatusRepository statusRepository, ListDetailRepository listDetailRepository) {
        this.statusRepository = statusRepository;
        this.listDetailRepository = listDetailRepository;
    }

    @Override
    public void save(ListDetail listDetail, List shoppingList) {
        Status status = statusRepository.findById(3).orElseThrow(RuntimeException::new);

        listDetail.setUser(null);
        listDetail.setStatus(status);
        listDetail.setList(shoppingList);
        shoppingList.addListDetail(listDetail);

        listDetailRepository.save(listDetail);
    }
}
