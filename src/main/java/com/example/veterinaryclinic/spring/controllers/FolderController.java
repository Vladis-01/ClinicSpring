package com.example.veterinaryclinic.spring.controllers;

import com.example.veterinaryclinic.spring.models.DoctorModel;
import com.example.veterinaryclinic.spring.models.FolderModel;
import com.example.veterinaryclinic.spring.models.NoteModel;
import com.example.veterinaryclinic.spring.models.PatientModel;
import com.example.veterinaryclinic.spring.repositories.DoctorRepo;
import com.example.veterinaryclinic.spring.repositories.FolderRepo;
import com.example.veterinaryclinic.spring.repositories.NoteRepo;
import com.example.veterinaryclinic.spring.repositories.PatientRepo;
import com.example.veterinaryclinic.spring.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
@RequestMapping("/doctor/folders")
public class FolderController {
    private final FolderRepo folderRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final NoteRepo noteRepo;
    private final NoteService noteService;

    FolderModel currentFolder;

    @Autowired
    public FolderController(FolderRepo folderRepo, DoctorRepo doctorRepo, PatientRepo patientRepo, NoteRepo noteRepo, NoteService noteService) {
        this.folderRepo = folderRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.noteRepo = noteRepo;
        this.noteService = noteService;
    }

    @GetMapping(value={"", "/{nextFolder}"})
    public String getFolders(@PathVariable(required = false) FolderModel nextFolder, @RequestParam  Map<String, String> form, HashMap<String, Object> model) {
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
    public String createFolder(@RequestParam("currentFolder") FolderModel currentFolder, HashMap<String, Object> model) {
        model.put("currentFolder", currentFolder);
        return "createFolder";
    }

    @PostMapping("/createFolder")
    public String createFolder(FolderModel folderModel) {
        FolderModel folderFromDb = folderRepo.findByParentAndName(folderModel.getParent(), folderModel.getName());

        if (folderFromDb != null) {
            return "redirect:/doctor/folders/createFolder";
        }

        folderModel.setPath(folderModel.getParent().getPath() + folderModel.getParent().getName() + "/");

        folderRepo.save(folderModel);
        currentFolder.setFolders(folderRepo.findByParent(currentFolder));
        return "redirect:/doctor/folders/";
    }

    @DeleteMapping("/{folder}")
    public String deleteFolder(@PathVariable FolderModel folder) {
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

    @GetMapping("/editNote/{note}")
    public String editNote(@PathVariable(value = "note") NoteModel note, HashMap<String, Object> model) {
        model.put("note", note);
        model.put("folderModel", currentFolder);
        return "editNote";
    }

    @DeleteMapping("/deleteNote/{note}")
    public String deleteNote(@PathVariable(value = "note") NoteModel note) {
        noteRepo.delete(note);
        currentFolder.setNotes(noteRepo.findByFolderModelOrderByPatientModelAscUpdateDateDesc(currentFolder));
        return "redirect:/doctor/folders/";
    }

    @PostMapping("/createNote")
    public String createNote(NoteModel noteModel, @RequestParam(value = "patientModel") PatientModel patientModel ) {
        noteService.createOrUpdateNote(noteModel, patientModel);
        return "redirect:/doctor/folders";
    }

    @GetMapping("/notes")
    public String getAllNotes(HashMap<String, Object> model) {
        model.put("notes", noteRepo.findAll());
        return "notes";
    }

    private void setCurrentFolder(FolderModel nextFolder){
        currentFolder = nextFolder;
    }
    private void setCurrentFolder(){
        if(currentFolder == null){
            currentFolder = folderRepo.findByName("root");
        }
        if(currentFolder == null){
            currentFolder = new FolderModel();
            currentFolder.setName("root");
            currentFolder.setPath("/");
            folderRepo.save(currentFolder);
            currentFolder = folderRepo.findByName("root");
        }
    }

    private Long getParent(){
        FolderModel root = folderRepo.findByName("root");
        if(currentFolder.getParent() != null){
            return currentFolder.getParent().getId();
        } else{
            return root.getId();
        }
    }

    private List<NoteModel> getNotes(Map<String, String> form) {
        List notes;
        if (form.get("patientModel") != null && !form.get("patientModel").equals("")) {
            PatientModel patientModel = patientRepo.getReferenceById(Long.valueOf(form.get("patientModel")));
            notes = noteRepo.findByFolderModelAndPatientModelOrderByPatientModelAscUpdateDateDesc(currentFolder, patientModel);
        } else {
            notes = noteRepo.findByFolderModelOrderByPatientModelAscUpdateDateDesc(currentFolder);
        }
        return notes;
    }
}