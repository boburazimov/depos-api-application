package uz.depos.app.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.depos.app.domain.Attachment;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.service.CompanyService;
import uz.depos.app.service.FilesStorageService;
import uz.depos.app.service.dto.AttachLogoDTO;
import uz.depos.app.service.dto.AttachMeetingDTO;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

@Controller
@RequestMapping("/api/file")
@Api(tags = "File")
public class AttachmentResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    private final FilesStorageService filesStorageService;
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    @Autowired
    public AttachmentResource(FilesStorageService filesStorageService, CompanyService companyService, CompanyRepository companyRepository) {
        this.filesStorageService = filesStorageService;
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Upload file", notes = "This method is upload file with MeetingID and AgendaID (if exist)")
    public ResponseEntity<?> uploadMeetingFile(@RequestPart MultipartFile file, @RequestParam("meetingId") Long meetingId, Long agendaId) {
        log.debug("REST request to save File : {}", file);
        if (meetingId == null) throw new BadRequestAlertException(
            "Meeting ID must not be null!",
            "attachmentManagement",
            "meetingNotExists"
        );
        try {
            AttachMeetingDTO attachMeetingDTO = filesStorageService.uploadMeetingFiles(file, meetingId, agendaId);
            return ResponseEntity.status(HttpStatus.OK).body(attachMeetingDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get file", notes = "This method is to get one file by ID")
    public void getFile(@PathVariable("id") Long id, HttpServletResponse response) {
        log.debug("REST request to get File by ID : {}", id);

        try {
            // get your file as InputStream
            Attachment loadedAttachment = filesStorageService.load(id);
            // copy it to response's OutputStream
            Files.copy(Paths.get(loadedAttachment.getPath()), response.getOutputStream());
            response.setContentType(loadedAttachment.getContentType());
            response.setHeader("Content-Disposition", "attachment; filename=" + loadedAttachment.getFileName());
            response.flushBuffer();
        } catch (IOException ex) {
            log.info("Error writing file to output stream. FileID was '{}'", id, ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @GetMapping("/meeting/{meetingId}")
    @ApiOperation(value = "Get files", notes = "This method is to get all files by MeetingID")
    public ResponseEntity<List<Attachment>> getListFiles(@PathVariable("meetingId") Long meetingId) { //get all attachments by meeting id and return dtos
        log.debug("Get Information for Attachments by MeetingID: {}", meetingId);
        List<Attachment> fileInfos = filesStorageService.loadAllByMeetingId(meetingId);
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @PostMapping(value = "/upload/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Upload logo", notes = "This method is upload Company logo with CompanyID")
    public ResponseEntity<?> uploadLogo(@RequestPart MultipartFile file, @RequestParam("companyId") Long companyId) {
        log.debug("REST request to save Logo : {}", file);
        if (companyId == null) {
            throw new BadRequestAlertException("Company ID must not be null!", "attachmentManagement", "companyNotExists");
        } else if (!companyRepository.findById(companyId).isPresent()) {
            throw new ResourceNotFoundException("Company not found by ID: " + companyId);
        } else if (file == null) {
            throw new BadRequestAlertException("File must not be null!", "attachmentManagement", "fileNullPointer");
        }

        try {
            // Clear previous company logo from current Company before upload.
            companyService.deleteCompanyLogo(companyId);

            AttachLogoDTO attachLogoDTO = filesStorageService.uploadCompanyLogo(file, companyId);
            return ResponseEntity.status(HttpStatus.OK).body(attachLogoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }
}
