package com.makototheknight.levelsbeyond.repository;

import com.makototheknight.levelsbeyond.entity.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note, Long> {

    List<Note> findAllByOrderByIdDesc();
    List<Note> findAllByBodyContainingOrderByIdDesc(String query);
}
