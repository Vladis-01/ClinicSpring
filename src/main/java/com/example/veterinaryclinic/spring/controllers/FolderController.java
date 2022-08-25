package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.entities.Folder;
import com.example.veterinaryclinic.spring.entities.Note;
import com.example.veterinaryclinic.spring.entities.Patient;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.repositories.FolderRepo;
import com.example.veterinaryclinic.spring.repositories.NoteRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import com.example.veterinaryclinic.spring.services.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/doctor/folders")
public class FolderController {
    private final FolderRepo folderRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final NoteRepo noteRepo;
    private final NoteService noteService;

    Folder currentFolder;

    public FolderController(FolderRepo folderRepo, DoctorRepo doctorRepo, PatientRepo patientRepo, NoteRepo noteRepo, NoteService noteService) {
        this.folderRepo = folderRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.noteRepo = noteRepo;
        this.noteService = noteService;
    }

    @GetMapping(value={"", "/{nextFolder}"})
    public String getFolders(@PathVariable(required = false) Folder nextFolder, @RequestParam  Map<String, String> form, HashMap<String, Object> model) {
        if (nextFolder != null) {
            setCurrentFolder(nextFolder);
        } else {
            setCurrentFolder();
        }

        model.put("parentID", getParent());
        model.put("notes", getNotes(form));
        model.put("root", folderRepo.findByName("root"));
        model.put("folders", currentFolder.getFolders());
        model.put("patients", patientRepo.findAll());
        model.put("currentFolder", currentFolder);

        return "folders";
    }

    @GetMapping("/createFolder")
    public String createFolder(@RequestParam("currentFolder") Folder currentFolder, HashMap<String, Object> model) {
        model.put("currentFolder", currentFolder);
        return "createFolder";
    }

    @PostMapping("/createFolder")
    public String createFolder(@Valid Folder folder) {
        Folder folderFromDb = folderRepo.findByParentAndName(folder.getParent(), folder.getName());

        if (folderFromDb != null) {
            return "redirect:/doctor/folders/createFolder";
        }

        folder.setPath(folder.getParent().getPath() + folder.getParent().getName() + "/");

        folderRepo.save(folder);
        currentFolder.setFolders(folderRepo.findByParent(currentFolder));
        return "redirect:/doctor/folders/";
    }

    @DeleteMapping("/{folder}")
    public String deleteFolder(@PathVariable Folder folder) {
        folderRepo.delete(folder);
        currentFolder.setFolders(folderRepo.findByParent(currentFolder));
        return "redirect:/doctor/folders/";
    }

    @GetMapping("/createNote")
    public String createNote(@RequestParam(value = "patientName", required = false) String patientName, HashMap<String, Object> model) {
        if(patientName != null){
            model.put("patients", patientRepo.findByFullNameContainingIgnoreCase(patientName));
        }
        model.put("folderModel", currentFolder);
        return "createNote";
    }

    @PostMapping("/editNote")
    public String editNotePost(@Valid Note note, HashMap<String, Object> model) {
        Note noteFromDb = noteRepo.findByNameAndFolder(note.getName(), note.getFolder());

        if (noteFromDb != null && noteFromDb.getId() != noteFromDb.getId()) {
            model.put("note", note);
            model.put("folderModel", currentFolder);
            return "redirect:/doctor/folders/editNote";
        }
        note.setUpdateDate(Instant.now());

        noteRepo.save(note);
        return "redirect:/doctor/folders";
    }

    @GetMapping("/editNote/{note}")
    public String editNote(@PathVariable(value = "note") Note note, HashMap<String, Object> model) {
        model.put("note", note);
        model.put("folderModel", currentFolder);
        return "editNote";
    }

    @DeleteMapping("/deleteNote/{note}")
    public String deleteNote(@PathVariable(value = "note") Note note) {
        noteRepo.delete(note);
        currentFolder.setNotes(noteRepo.findByFolderOrderByPatientAscUpdateDateDesc(currentFolder));
        return "redirect:/doctor/folders/";
    }

    @PostMapping("/createNote")
    public String createNote(@Valid Note note, @RequestParam(value = "patient") Patient patient) {
        noteService.createOrUpdateNote(note, patient);
        return "redirect:/doctor/folders";
    }

    @GetMapping("/notes")
    public String getAllNotes(HashMap<String, Object> model) {
        model.put("notes", noteRepo.findAll());
        return "notes";
    }

    private void setCurrentFolder(Folder nextFolder){
        currentFolder = nextFolder;
    }
    private void setCurrentFolder(){
        if(currentFolder == null){
            currentFolder = folderRepo.findByName("root");
        }
        if(currentFolder == null){
            currentFolder = new Folder();
            currentFolder.setName("root");
            currentFolder.setPath("/");
            folderRepo.save(currentFolder);
            currentFolder = folderRepo.findByName("root");
        }
    }

    private Long getParent(){
        Folder root = folderRepo.findByName("root");
        if(currentFolder.getParent() != null){
            return currentFolder.getParent().getId();
        } else{
            return root.getId();
        }
    }

    private List<Note> getNotes(Map<String, String> form) {
        List notes;
        if (form.get("patient") != null && !form.get("patient").equals("")) {
            Patient patient = patientRepo.getReferenceById(Long.valueOf(form.get("patient")));
            notes = noteRepo.findByFolderAndPatientOrderByPatientAscUpdateDateDesc(currentFolder, patient);
        } else {
            notes = noteRepo.findByFolderOrderByPatientAscUpdateDateDesc(currentFolder);
        }
        return notes;
    }
}