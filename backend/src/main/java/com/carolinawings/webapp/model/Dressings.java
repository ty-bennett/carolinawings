package com.carolinawings.webapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dressings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

//RANCH
//BLEU_CHEESE,
//REMOULADE,
//TARTAR,
//COCKTAIL,
//SALSA,
//SOUR_CREAM,
//SOUTHWEST,
//HONEY_MUSTARD,
//MARINARA,
//AU_JUS
