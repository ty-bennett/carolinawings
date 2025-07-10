/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemRequestDTO
{
    String name;
    String description;
    String url;
    String price;
    String category;
    String enabled;
}
