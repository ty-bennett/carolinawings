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
public class Sauces {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

//MILD,
//    MILD_HONEY,
//    BBQ,
//    TERIYAKI,
//    TERI_BBQ,
//    DOCS_BBQ,
//    HONEY_BBQ,
//    HOT_GARLIC,
//    HONEY_MUSTARD,
//    GARLIC_PARMESAN,
//    MANGO_HABANERO,
//    DOCS_SPECIAL,
//    MEDIUM,
//    HOT_HONEY,
//    CAJUN_HONEY,
//    TERI_HOT,
//    BUFFALO_CAJUN_RANCH,
//    HOT_HONEY_MUSTARD,
//    HOT,
//    TERI_CAJUN,
//    CAJUN,
//    FIRE_ISLAND,
//    BLISTERING,
//    BEYOND_BLISTERING,
//    CLASSIC,
//    GOLD,
//    PIG_SAUCE,
//    CAROLINA_RED,
//    CAROLINA_RUB,
//    LEMON_PEPPER
