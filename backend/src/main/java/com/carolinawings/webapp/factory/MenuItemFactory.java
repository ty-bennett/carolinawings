/*
Ty Bennett
 */
package com.carolinawings.webapp.factory;

import com.carolinawings.webapp.domain.MenuItem;

import java.math.BigDecimal;

public class MenuItemFactory {
    public static MenuItem createMenuItem(String name,
                                          String description,
                                          String url,
                                          BigDecimal price,
                                          String category) {
        return new MenuItem(name, description, url, price, category);
    }
}
