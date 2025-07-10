/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu findByName(String name);
//    List<Menu> findAll();
//    Optional<Menu> findMenuById(int id);
//    Optional<Menu> findMenuByName(String name);
//    void deleteMenuById(int id);
//    Optional<Menu> createMenu(Menu menu);
//

}
