package com.carolinawings.webapp.dto;

import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.model.Restaurant;

import java.util.List;

public class CreateMenuRequest {
    private String name;
    private Restaurant restaurant;
    private List<MenuItem> menuItemList;
}
