package com.carolinawings.webapp.mapper;

import com.carolinawings.webapp.dto.UserResponseDTO;
import com.carolinawings.webapp.model.User;

public class UserMapper
{
   public static UserResponseDTO toDTO(User user)
   {
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setId(user.getId().toString());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setNewsletterMember(user.getNewsletterMember().toString());
        return userDTO;
   }
}
