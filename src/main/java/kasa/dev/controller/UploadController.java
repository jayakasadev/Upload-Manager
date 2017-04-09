package kasa.dev.controller;

import kasa.dev.model.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Entry point into the web service.
 *
 * Using @RestController, so no need to autowire anything.
 */
@RestController
@RequestMapping(value = "")
public class UploadController {
    private final Log log = LogFactory.getLog(UploadController.class);
    private final UploadRepository repository;

    /**
     * Required if I want the UploadRespository to be autowired.
     *
     * @param repository
     */
    public UploadController(UploadRepository repository){
        this.repository = repository;
    }

    /**
     * Landing Page/ Welcome Message
     *
     * @return String greeting
     */
    @RequestMapping(method = GET, produces = "application/json")
    public String greeting(){
        log.info("greeting has been accessed");
        return "Hello There! Welcome to the Upload Management System.\n\tBuilt By Jaya Kasa.";
    }

    /**
     * Method for getting a list of all files
     *
     * @return Collection containing record of all uploads
     */
    @RequestMapping(value = "/all", method = GET, produces = "application/json")
    public Collection<Upload> findAll(){
        log.info("findAll");
        return repository.findAll();
    }

    /**
     * Method for uploading a file
     *
     * @param owner
     * @param description
     * @param filename
     * @param file
     * @return Upload object
     */
    @RequestMapping(value = "/upload/{owner}/{description}", method = POST, produces = "application/json")
    public Upload upload(@PathVariable(value = "owner") String owner,
                         @PathVariable(value = "description") String description,
                         @PathVariable(value = "filename") String filename,
                         @RequestParam MultipartFile file){
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        // log.info("uploading file owned by " + owner +" description: " + description);
        log.info("uploading " + file.getOriginalFilename() + " to uploads directory");

        // TODO implement file storage

        return repository.save(new Upload(file.getOriginalFilename(), owner, extension, description));
    }

    /**
     * Method for searching for file information by fileid
     *
     * @param id
     * @return Upload object if it exists
     */
    @RequestMapping(value = "/details/{id}", method = GET, produces = "application/json")
    public Optional<Upload> findByID(@PathVariable(value = "id") long id){
        log.info("findByID " + repository != null);
        return repository.findByFileId(id);
    }

    /**
     * Method for searching for file information by filename.
     *
     * @param filename
     * @return Upload object if it exists
     */
    @RequestMapping(value = "/details/{filename:.*}", method = GET, produces = "application/json")
    public Optional<Upload> findByFilename(@PathVariable(value = "filename") String filename){
        log.info("findByFilename " + repository != null);
        return repository.findByFilename(filename);
    }
}