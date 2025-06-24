/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.Company;
import com.carolinawings.webapp.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repository Annotation
@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long>{
    Manager findByName(String name);
//    //Return all managers endpoint
//    List<Manager> findAll();
//    //Find a manager by their ID
//    Optional<Manager> findById(int id);
//    //Delete a manager using their ID
//    void deleteManagerById(int id);
//    //find a manager by name
//    Optional<Manager> findByName(String name);
//    //find a manager by email
//    Optional<Manager> findByEmail(String email);
//    //create manager
//    Optional<Manager> createManager(Manager manager);
//    //update manager and take in manager
//    Optional<Manager> updateManager(Manager manager);
//
}

