package com.example.clinicspring.spring.repositories;

import com.example.clinicspring.spring.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FolderRepo extends JpaRepository<Folder, Long> {
    Folder findByParentAndName(Folder folder, String name);
    Set<Folder> findByParent(Folder folder);
    Folder findByName(String name);
}
