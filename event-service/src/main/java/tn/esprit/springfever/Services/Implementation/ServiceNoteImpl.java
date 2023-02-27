package tn.esprit.springfever.Services.Implementation;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.NoteDTO;
import tn.esprit.springfever.Services.Interfaces.NoteMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceNote;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.NoteRepository;
import tn.esprit.springfever.repositories.ProjectRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

@Service
@Slf4j

public class ServiceNoteImpl implements IServiceNote {
    @Autowired
    NoteRepository noteRepository ;
    @Autowired
    ProjectRepository projectRepository ;

    @Autowired
    NoteMapper noteMapper;


    @Override
    public Note addNote(Note note) {
        log.info("event was successfully added !");
        return noteRepository.save(note);
    }
    /*
    @Override
    public Note addNote(Note note) throws IOException {

        // Load the tokenizer model
        InputStream modelIn = new FileInputStream("en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);

        // Create a new tokenizer
        Tokenizer tokenizer = new TokenizerME(model);

        // Analyze the keywords from the student's diploma
        String diploma = "Diploma in Computer Science with a specialization in Web Development";
        String[] tokens = tokenizer.tokenize(diploma);

        // Display the separated keywords
        for (String token : tokens)
            log.info(token);
        //log takone
        log.info("note was successfully added !");
        return noteRepository.save(note);
    }
*/
    @Override
    public List<Note> getAllNote() {return noteRepository.findAll();}


    @Override
    public boolean deleteNote(Long idNote) {
        Note existingNote = noteRepository.findById(idNote).orElse(null);
        if(existingNote!=null) {
            noteRepository.delete(existingNote);
            log.info("note deleted");
            return true ;
        }
        log.info(" this note is not existing");
        return false;
    }


    @Override
    public Note updateNote(Long idNote, NoteDTO noteDto) {
        Note note = noteRepository.findById(idNote).orElse(null);
        if (note != null) {
            noteMapper.updateNoteFromDto(noteDto, note);
            noteRepository.save(note);
            log.info("note was successfully updated !");
        }
        log.info("note not found !");
        return  note;

    }


    /*
    public Note addNoteToProject(Long projectId, Note note) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project with id " + projectId + " not found"));
        note.setProject(project);

        Double projectNote = (note.getSoftskillsNote() + note.getHardskillsNote() + note.getPresentationNote() +
                note.getConsistencyNote() + note.getOriginalityNote() + note.getContentNote() +
                note.getRelevanceNote()) / 7.0;

        note.setProjectNote(projectNote);

        noteRepository.save(note);
        return note;
    }*/

@Override
    public String assignNoteToProject(Long idNote, Long idProject) {
        Note note = noteRepository.findById(idNote).orElse(null);
        Project project = projectRepository.findById(idProject).orElse(null);

    if (note != null && project != null) {
        // Calculate the project note
        float projectNote = (note.getSoftskillsNote() + note.getHardskillsNote() + note.getPresentationNote() +
                note.getConsistencyNote() + note.getOriginalityNote() + note.getContentNote() + note.getRelevanceNote()) / 7;
        note.setProjectNote(projectNote);

        // Assign project note to project

        Note notes = project.getNote();
        project.setNote(notes);

    }
       // note.setProject( project);           hedhy lzm na7eha maach comm bch njm nsavi f project ama feha erreur !!!
        noteRepository.save(note);
        projectRepository.save(project);
        return "note is Affeced To project";

    }

    /*
@Override
    public String analyzeSentiment(long idNote) {
        Note note = noteRepository.getNoteById(idNote);
        //Note note = getNoteById(noteId);
        String comment = note.getComment();

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation document = new Annotation(comment);
        pipeline.annotate(document);

        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            return sentiment;
        }

        return null; // No sentiment found.
    }


    @Override
    public String analyzeSentiment(String comment) {

        StanfordCoreNLP pipeline = new StanfordCoreNLP("StanfordCoreNLP.properties");
        Annotation annotation = pipeline.process(comment);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        String sentiment = "";
        if (sentences != null) {
            for (CoreMap sentence : sentences) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                if (tree != null) {
                    sentiment = tree.label().value();
                }
            }
        }
        return sentiment;
    }

    @Override
    public String getSentimentAnalysis(Long idNote) {
        Note note = noteRepository.getNoteById(idNote);
        String sentiment = noteService.getSentimentAnalysis(Long.valueOf(note.getComment()));
        return sentiment;
    }

*/

@Override
    public void displayNoteStatistics() {

        // Calculate average softskills note
        Float avgSoftskillsNote = noteRepository.getAverageSoftskillsNote();
        System.out.println("Average softskills note: " + avgSoftskillsNote);

        // Calculate average presentation note
        Float avgPresentationNote = noteRepository.getAveragePresentationNote();
        System.out.println("Average presentation note: " + avgPresentationNote);

        // Calculate average consistency note
        Float avgConsistencyNote = noteRepository.getAverageConsistencyNote();
        System.out.println("Average consistency note: " + avgConsistencyNote);

        // Calculate average originality note
        Float avgOriginalityNote = noteRepository.getAverageOriginalityNote();
        System.out.println("Average originality note: " + avgOriginalityNote);

        // Calculate average content note
        Float avgContentNote = noteRepository.getAverageContentNote();
        System.out.println("Average content note: " + avgContentNote);

        // Calculate average relevance note
        Float avgRelevanceNote = noteRepository.getAverageRelevanceNote();
        System.out.println("Average relevance note: " + avgRelevanceNote);

    }


}
