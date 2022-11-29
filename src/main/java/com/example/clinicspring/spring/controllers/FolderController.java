package com.example.clinicspring.spring.controllers;

import com.example.clinicspring.spring.DTO.FolderDto;
import com.example.clinicspring.spring.DTO.NoteDto;
import com.example.clinicspring.spring.DTO.PatientDto;
import com.example.clinicspring.spring.entities.Folder;
import com.example.clinicspring.spring.entities.Note;
import com.example.clinicspring.spring.services.FolderService;
import com.example.clinicspring.spring.services.NoteService;
import com.example.clinicspring.spring.services.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/doctor/folders")
public class FolderController {
    private final FolderService folderService;
    private final PatientService patientService;
    private final NoteService noteService;

    private FolderDto currentFolder;

    public FolderController(FolderService folderService, PatientService patientService, NoteService noteService) {
        this.folderService = folderService;
        this.patientService = patientService;
        this.noteService = noteService;

        currentFolder = folderService.getFolderByName("root");
    }

    @GetMapping(value={"", "/{nextFolder}"})
    public String getFolders(@PathVariable(required = false) Long nextFolder, @RequestParam  Map<String, String> form, HashMap<String, Object> model) {
        if (nextFolder != null) {
            setCurrentFolder(folderService.getFolderById(nextFolder));
        } else {
            setCurrentFolder();
        }

        model.put("parentID", getParent());
        model.put("notes", getNotes(form));
        model.put("root", folderService.getFolderByName("root"));
        model.put("folders", currentFolder.getFoldersDto());
        model.put("patients", patientService.getAllPatients());
        model.put("currentFolder", currentFolder);

        return "folders";
    }

    @GetMapping("/createFolder")
    public String createFolder(@RequestParam("currentFolder") Folder currentFolder, HashMap<String, Object> model) {
        model.put("currentFolder", currentFolder);
        return "createFolder";
    }

    @PostMapping("/createFolder")
    public String createFolder(FolderDto folder) {
        folder.setParentFolderDto(currentFolder);
        FolderDto folderFromDb = folderService.getFolderByParentAndName(folder.getParentFolderDto(), folder.getName());

        if (folderFromDb != null) {
            return "redirect:/doctor/folders/createFolder";
        }

        folder.setPath(folder.getParentFolderDto().getPath() + folder.getParentFolderDto().getName() + "/");

        folderService.createOrUpdateFolder(folder);
        currentFolder.setFoldersDto(folderService.getFoldersByParent(currentFolder));
        return "redirect:/doctor/folders/";
    }

    @DeleteMapping("/{folderId}")
    public String deleteFolder(@PathVariable Long folderId) {
        folderService.deleteFolderById(folderId);
        currentFolder.setFoldersDto(folderService.getFoldersByParent(currentFolder));
        return "redirect:/doctor/folders/";
    }

    @GetMapping("/createNote")
    public String createNote(@RequestParam(value = "patientName", required = false) String patientName, HashMap<String, Object> model) {
        if(patientName != null){
            model.put("patients", patientService.getPatientsByFullName(patientName));
        }
        model.put("folderModel", currentFolder);
        return "createNote";
    }

    @PostMapping("/editNote")
    public String editNotePost(@Valid NoteDto note, HashMap<String, Object> model, @RequestParam(value = "patient") Long patientId) {
        NoteDto noteFromDb = noteService.getNoteByNameAndFolder(note.getName(), note.getFolderDto());

        if (noteFromDb != null && noteFromDb.getId() != noteFromDb.getId()) {
            model.put("note", note);
            model.put("folderModel", currentFolder);
            return "redirect:/doctor/folders/editNote";
        }

        note.setFolderDto(currentFolder);
        note.setPatientDto(patientService.getPatientById(patientId));

        noteService.createOrUpdateNote(note);
        return "redirect:/doctor/folders";
    }

    @GetMapping("/editNote/{noteId}")
    public String editNote(@PathVariable(value = "noteId") Long noteId, HashMap<String, Object> model) {
        model.put("note", noteService.getNoteById(noteId));
        model.put("folder", currentFolder);
        return "editNote";
    }

    @DeleteMapping("/deleteNote/{noteId}")
    public String deleteNote(@PathVariable(value = "noteId") Long noteId) {
        noteService.deleteNoteById(noteId);
        currentFolder.setNotesDto(noteService.getNotesByFolderOrderByPatientAscUpdateDateDesc(currentFolder));
        return "redirect:/doctor/folders/";
    }

    @PostMapping("/createNote")
    public String createNote(@Valid NoteDto note, @RequestParam(value = "patient") Long patientId) {
        note.setFolderDto(currentFolder);
        note.setPatientDto(patientService.getPatientById(patientId));
        noteService.createOrUpdateNote(note);
        return "redirect:/doctor/folders";
    }

    @GetMapping("/notes")
    public String getAllNotes(HashMap<String, Object> model) {
        model.put("notes", noteService.getAllNotes());
        return "notes";
    }

    private void setCurrentFolder(FolderDto nextFolder){
        currentFolder = nextFolder;
    }
    private void setCurrentFolder(){
        if(currentFolder == null){
            currentFolder = folderService.getFolderByName("root");
        }
    }

    private Long getParent(){
        FolderDto root = folderService.getFolderByName("root");
        if(currentFolder.getParentFolderDto() != null){
            return currentFolder.getParentFolderDto().getId();
        } else{
            return root.getId();
        }
    }

    private List<Note> getNotes(Map<String, String> form) {
        List notes;
        if (form.get("patient") != null && !form.get("patient").equals("")) {
            PatientDto patient = patientService.getPatientById(Long.valueOf(form.get("patient")));
            notes = noteService.getNotesByFolderAndPatientOrderByPatientAscUpdateDateDesc(currentFolder, patient);
        } else {
            notes = noteService.getNotesByFolderOrderByPatientAscUpdateDateDesc(currentFolder);
        }
        return notes;
    }
}