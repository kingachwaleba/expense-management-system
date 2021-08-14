package com.team.backend.helpers;

import com.team.backend.model.List;
import com.team.backend.model.ListDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListHolder {

    private List list;
    private java.util.List<ListDetail> listDetailList;
}
