package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;

@Repository
public interface OptionGroupRepository extends JpaRepository<OptionGroup, Long> {
    <T> ScopedValue<T> findByNameIgnoreCase(String name);

    OptionGroup findByName(String name);
}
