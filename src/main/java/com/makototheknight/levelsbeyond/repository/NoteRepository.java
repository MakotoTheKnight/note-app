package com.makototheknight.levelsbeyond.repository;

import com.makototheknight.levelsbeyond.entity.Note;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepository extends CrudRepository<Note, Long> {

    Iterable<Note> findAllByQuery(String query);
}
