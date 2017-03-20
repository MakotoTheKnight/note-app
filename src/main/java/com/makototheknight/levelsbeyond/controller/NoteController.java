package com.makototheknight.levelsbeyond.controller;

import com.makototheknight.levelsbeyond.entity.Note;
import com.makototheknight.levelsbeyond.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NoteController {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @PostMapping(value = "/notes", consumes = {"application/json"}, produces = {"application/json"})
    public Note createNote(RequestEntity<Note> entity) {
        return noteRepository.save(entity.getBody());
    }

    @GetMapping("/notes/{id}")
    public Note getNoteById(@PathVariable("id") Long id) {
        return noteRepository.findOne(id);
    }

    @GetMapping("/notes")
    public Iterable<Note> getNotes(@RequestParam(value = "query", required = false) String query) {
        if(null == query) {
            return noteRepository.findAll();
        } else {
            return noteRepository.findAllByQuery(query);
        }

    }
}
