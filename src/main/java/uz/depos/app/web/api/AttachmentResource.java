package uz.depos.app.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.undertow.util.BadRequestException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import uz.depos.app.domain.Attachment;
import uz.depos.app.repository.AttachmentRepository;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.service.CompanyService;
import uz.depos.app.service.FilesStorageService;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.AttachLogoDTO;
import uz.depos.app.service.dto.AttachMeetingDTO;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

@Controller
@RequestMapping("/api/file")
@Api(tags = "File")
@CrossOrigin("*")
public class AttachmentResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    private final FilesStorageService filesStorageService;
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentResource(
        FilesStorageService filesStorageService,
        CompanyService companyService,
        CompanyRepository companyRepository,
        AttachmentRepository attachmentRepository
    ) {
        this.filesStorageService = filesStorageService;
        this.companyService = companyService;
        this.companyRepository = companyRepository;
        this.attachmentRepository = attachmentRepository;
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

    @RequestMapping(value = "/one/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get file", notes = "This method is to get any one file by ID")
    public ResponseEntity<Resource> getFile(@PathVariable Long id) {
        log.debug("REST request to get File by ID : {}", id);
        Attachment attachment = filesStorageService.load(id);
        Resource file = filesStorageService.loadFile(attachment);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .header(HttpHeaders.CONTENT_TYPE, attachment.getContentType())
            .body(file);
    }

    @GetMapping("/meeting/{meetingId}")
    @ApiOperation(value = "Get files", notes = "This method is to get all files by MeetingID")
    public ResponseEntity<List<AttachMeetingDTO>> getListFiles(@PathVariable("meetingId") Long meetingId) { //get all attachments by meeting id and return dtos
        log.debug("Get Information for Attachments by MeetingID: {}", meetingId);
        List<AttachMeetingDTO> fileInfos = filesStorageService.loadAllByMeetingId(meetingId);
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping(value = "/logo/{companyId}")
    @ApiOperation(value = "Get logo", notes = "This method is to get logo file by company ID")
    public ResponseEntity<Resource> getFileByCompany(@PathVariable Long companyId) {
        log.debug("REST request to get Logo by CompanyID : {}", companyId);
        Attachment attachment = attachmentRepository.findByCompanyIdAndMeetingIdIsNullAndIsReestrFalse(companyId).orElse(null);
        assert attachment != null;
        Resource file = filesStorageService.loadFile(attachment);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .header(HttpHeaders.CONTENT_TYPE, attachment.getContentType())
            .body(file);
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
            String url = MvcUriComponentsBuilder
                .fromMethodName(AttachmentResource.class, "getFileUrl", attachLogoDTO.getUrl())
                .build()
                .toString();
            attachLogoDTO.setUrl(url);
            return ResponseEntity.status(HttpStatus.OK).body(attachLogoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFileUrl(@PathVariable String filename) {
        Resource file = filesStorageService.load1(filename);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete file", notes = "This method to delete one of files by ID")
    public HttpEntity<ApiResponse> deleteFile(@PathVariable Long id) throws IOException {
        log.debug("REST request to delete File : {}", id);
        ApiResponse response = filesStorageService.delete(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }
}
