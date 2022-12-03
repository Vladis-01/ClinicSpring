package com.example.clinicspring.spring.controllers;

import com.example.clinicspring.spring.DTO.FolderDto;
import com.example.clinicspring.spring.DTO.MedicineDto;
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

    public FolderController(FolderService folderService, PatientService patientService, NoteService noteService) {
        this.folderService = folderService;
        this.patientService = patientService;
        this.noteService = noteService;
    }

    @GetMapping(value={"", "/{nextFolder}"})
    public String getFolders(@PathVariable(required = false) Long nextFolder, @RequestParam  Map<String, String> form, HashMap<String, Object> model) {
        FolderDto currentFolder;
        if (nextFolder != null) {
            currentFolder = folderService.getFolderById(nextFolder);
        } else {
            currentFolder = getRoot();
        }

        model.put("parentID", getParent(currentFolder));
        model.put("notes", getNotes(form, currentFolder));
        model.put("root", folderService.getFolderByName("root"));
        model.put("folders", currentFolder.getFoldersDto());
        model.put("patients", patientService.getAllPatients());
        model.put("currentFolder", currentFolder);

        return "folders";
    }

    @GetMapping("/createFolder")
    public String createFolder(@RequestParam("currentFolder") Long currentFolder, HashMap<String, Object> model) {
        model.put("currentFolder", folderService.getFolderById(currentFolder));
        return "createFolder";
    }

    @PostMapping("/createFolder")
    public String createFolder(@RequestParam("currentFolder") Long currentFolder, FolderDto folder) {
        FolderDto cf = folderService.getFolderById(currentFolder);
        folder.setParentFolderDto(cf);
        FolderDto folderFromDb = folderService.getFolderByParentAndName(folder.getParentFolderDto(), folder.getName());

        if (folderFromDb != null) {
            return "redirect:/doctor/folders/createFolder";
        }

        folder.setPath(folder.getParentFolderDto().getPath() + folder.getParentFolderDto().getName() + "/");
        folderService.createOrUpdateFolder(folder);
        cf.setFoldersDto(folderService.getFoldersByParent(cf));
        return "redirect:/doctor/folders/" + currentFolder;
    }

    @DeleteMapping("/{folderId}")
    public String deleteFolder(@PathVariable Long folderId, @RequestParam("currentFolder") Long currentFolder) {
        FolderDto cf = folderService.getFolderById(currentFolder);
        folderService.deleteFolderById(folderId);
        cf.setFoldersDto(folderService.getFoldersByParent(cf));
        return "redirect:/doctor/folders/" + currentFolder;
    }

    @GetMapping("/createNote")
    public String createNote(@RequestParam(value = "patientName", required = false) String patientName, @RequestParam("currentFolder") Long currentFolder, HashMap<String, Object> model) {
        if(patientName != null){
            model.put("patients", patientService.getPatientsByFullName(patientName));
        }
        model.put("folderModel", folderService.getFolderById(currentFolder));
        return "createNote";
    }

    @PostMapping("/editNote")
    public String editNotePost(@Valid NoteDto note, HashMap<String, Object> model, @RequestParam(value = "patient") Long patientId, @RequestParam("currentFolder") Long currentFolder) {
        note.setFolderDto(folderService.getFolderById(currentFolder));
        NoteDto noteFromDb = noteService.getNoteByNameAndFolder(note.getName(), note.getFolderDto());

        if (noteFromDb != null && noteFromDb.getId() != noteFromDb.getId()) {
            model.put("note", note);
            model.put("folderModel", folderService.getFolderById(currentFolder));
            return "redirect:/doctor/folders/editNote";
        }

        note.setFolderDto(folderService.getFolderById(currentFolder));
        note.setPatientDto(patientService.getPatientById(patientId));

        noteService.createOrUpdateNote(note);
        return "redirect:/doctor/folders/" + currentFolder;
    }

    @GetMapping("/editNote/{noteId}")
    public String editNote(@PathVariable(value = "noteId") Long noteId, HashMap<String, Object> model, @RequestParam("currentFolder") Long currentFolder) {
        model.put("note", noteService.getNoteById(noteId));
        model.put("folder", folderService.getFolderById(currentFolder));
        return "editNote";
    }

    @DeleteMapping("/deleteNote/{noteId}")
    public String deleteNote(@PathVariable(value = "noteId") Long noteId, @RequestParam("currentFolder") Long currentFolder) {
        FolderDto folder = folderService.getFolderById(currentFolder);
        noteService.deleteNoteById(noteId);
        folder.setNotesDto(noteService.getNotesByFolderOrderByPatientAscUpdateDateDesc(folder));
        return "redirect:/doctor/folders/" + currentFolder;
    }

    @PostMapping("/createNote")
    public String createNote(@Valid NoteDto note, @RequestParam(value = "patient") Long patientId, @RequestParam("currentFolder") Long currentFolder) {
        note.setFolderDto(folderService.getFolderById(currentFolder));
        note.setPatientDto(patientService.getPatientById(patientId));
        noteService.createOrUpdateNote(note);
        return "redirect:/doctor/folders/" + currentFolder;
    }

    @GetMapping("/notes")
    public String getAllNotes(HashMap<String, Object> model, @RequestParam("currentFolder") Long currentFolder) {
        model.put("notes", noteService.getAllNotes());
        model.put("currentFolder", folderService.getFolderById(currentFolder));
        return "notes";
    }

    private FolderDto getRoot(){
        return folderService.getFolderByName("root");
    }

    private Long getParent(FolderDto currentFolder){
        if(currentFolder.getParentFolderDto() != null){
            return currentFolder.getParentFolderDto().getId();
        } else{
            return getRoot().getId();
        }
    }

    private List<Note> getNotes(Map<String, String> form, FolderDto currentFolder) {
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