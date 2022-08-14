package com.example.veterinaryclinic.spring.models;

import com.example.veterinaryclinic.spring.Enums.Status;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.Id;

import javax.management.ConstructorParameters;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="folders")
public class FolderModel {

    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "folder_id")
    private FolderModel parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<FolderModel> folders;

    @OneToMany(mappedBy = "folderModel", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<NoteModel> notes;

    private String path;

}