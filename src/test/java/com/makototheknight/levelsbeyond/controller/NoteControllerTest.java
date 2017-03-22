package com.makototheknight.levelsbeyond.controller;

import com.makototheknight.levelsbeyond.entity.Note;
import com.makototheknight.levelsbeyond.repository.NoteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NoteRepository noteRepository;

    private HttpHeaders headers;

    @Before
    public void establishHeaders() {
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
    }

    @Test
    public void createNote_validNote_returns200OK() {
        // given
        final String postValue = "{\"body\": \"This is a test!\"}";
        final HttpEntity<String> entity = new HttpEntity<>(postValue, headers);

        // when
        final ResponseEntity<Note> actual = restTemplate.postForEntity("/api/notes", entity, Note.class);

        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void createNote_validNote_createsID() {
        // given
        final String postValue = "{\"body\": \"This is a test!\"}";
        final HttpEntity<String> entity = new HttpEntity<>(postValue, headers);

        // when
        final ResponseEntity<Note> actual = restTemplate.postForEntity("/api/notes", entity, Note.class);

        // then
        assertThat(actual.getBody().getId()).isNotNull();
    }

    @Test
    public void createNote_validNote_correctlyPopulatesBody() {
        // given
        final String postValue = "{\"body\": \"This is a test!\"}";
        final HttpEntity<String> entity = new HttpEntity<>(postValue, headers);

        // when
        final ResponseEntity<Note> actual = restTemplate.postForEntity("/api/notes", entity, Note.class);

        // then
        assertThat(actual.getBody().getBody()).isEqualTo("This is a test!");
    }

    @Test
    public void getNoteById_noSuchIdExists_returns404() {
        // given
        // when
        final ResponseEntity<Note> actual = restTemplate.getForEntity("/api/notes/1", Note.class);

        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getNoteById_idExists_returnsNote() {
        // given
        final Note preexistingNote = noteRepository.save(new Note("This already exists!"));

        // when
        final ResponseEntity<Note> actual = restTemplate.getForEntity("/api/notes/" + preexistingNote.getId(), Note.class);

        // then
        assertThat(actual.getBody().getBody()).isEqualTo(preexistingNote.getBody());
    }

    @Test
    public void getNotes_noParam_noNotes_returnsEmptyCollection() {
        // given
        // when
        final ResponseEntity<List> actual = restTemplate.getForEntity("/api/notes", List.class);

        //then
        assertThat(actual.getBody().size()).isEqualTo(0);
    }

    @Test
    public void getNotes_noParam_oneNote_returnsCollectionWithSizeOfOne() {
        // given
        noteRepository.save(new Note("This already exists!"));

        //when
        final ResponseEntity<List> actual = restTemplate.getForEntity("/api/notes", List.class);

        //then
        assertThat(actual.getBody().size()).isEqualTo(1);
    }

    @Test
    public void getNotes_noParam_multipleNotes_returnsCollectionWithSizeOfTen() {
        // given
        noteRepository.save(new Note("This already exists!"));
        noteRepository.save(new Note("This already exists!"));
        noteRepository.save(new Note("This already exists!"));
        noteRepository.save(new Note("This already exists!"));
        noteRepository.save(new Note("This already exists!"));
        noteRepository.save(new Note("This already exists!"));
        noteRepository.save(new Note("This already exists!"));
        noteRepository.save(new Note("This already exists!"));
        noteRepository.save(new Note("This already exists!"));
        noteRepository.save(new Note("This already exists!"));

        //when
        final ResponseEntity<List> actual = restTemplate.getForEntity("/api/notes", List.class);

        //then
        assertThat(actual.getBody().size()).isEqualTo(10);
    }

    @Test
    public void getNotes_withParam_noNotes_returnsEmptyCollection() {
        // given
        //when
        final ResponseEntity<List> actual = restTemplate.getForEntity("/api/notes?query=appointment", List.class);

        //then
        assertThat(actual.getBody().size()).isEqualTo(0);
    }

    @Test
    public void getNotes_withParam_fiveMatchingNotes_returnsFiveElements() {
        // given
        noteRepository.save(new Note("This is a note about deadlines."));
        noteRepository.save(new Note("This is a note about obligations."));
        noteRepository.save(new Note("This is a note about appointments."));
        noteRepository.save(new Note("This is a reminder about your appointment."));
        noteRepository.save(new Note("This is an intern scheduling an appointment."));
        noteRepository.save(new Note("Remember to set up your dentist appointment on the 5th."));
        noteRepository.save(new Note("This is a note about TPS reports."));
        noteRepository.save(new Note("This is a note about reports, again."));
        noteRepository.save(new Note("This is a note about notes.  Pretty meta, if you think about it."));
        noteRepository.save(new Note("This is a note about test appointments.  Please make more."));
        //when
        final ResponseEntity<List> actual = restTemplate.getForEntity("/api/notes?query=appointment", List.class);

        //then
        assertThat(actual.getBody().size()).isEqualTo(5);
    }
}