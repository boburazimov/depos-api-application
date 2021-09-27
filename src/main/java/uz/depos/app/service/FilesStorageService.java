package uz.depos.app.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.depos.app.domain.Attachment;
import uz.depos.app.repository.AttachmentRepository;
import uz.depos.app.service.dto.AttachLogoDTO;
import uz.depos.app.service.dto.AttachMeetingDTO;
import uz.depos.app.service.dto.AttachReestrDTO;
import uz.depos.app.service.mapper.AttachmentMapper;

@Service
public class FilesStorageService {

    private final Logger log = LoggerFactory.getLogger(FilesStorageService.class);

    private final Path root = Paths.get("uploads");

    final AttachmentRepository attachmentRepository;
    final AttachmentMapper attachmentMapper;

    public FilesStorageService(AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
    }

    // If the static folder not create yet, this method is create it by the firs init.
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public AttachMeetingDTO uploadMeetingFiles(MultipartFile file, Long meetingId, Long agendaId) {
        Attachment attachment = uploadGeneral(file);
        try {
            attachment.setReestr(false);
            attachment.setMeetingId(meetingId);
            if (agendaId != null) attachment.setAgendaId(agendaId);
            attachment.setCompanyId(null);
            attachment = attachmentRepository.save(attachment);
            log.debug("Saved Information for Attachment: {}", attachment);
            return attachmentMapper.attachmentToAttachmentMeetingDTO(attachment);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public AttachReestrDTO uploadReestrExcel(MultipartFile file, Long meetingId) {
        Attachment attachment = uploadGeneral(file);
        try {
            attachment.setReestr(true);
            attachment.setMeetingId(meetingId);
            attachment.setAgendaId(null);
            attachment.setCompanyId(null);
            attachment = attachmentRepository.save(attachment);
            log.debug("Saved Information for Attachment: {}", attachment);
            return attachmentMapper.attachmentToAttachmentReestrDTO(attachment);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Attachment uploadGeneral(MultipartFile file) {
        Attachment attachment = new Attachment();
        try {
            UUID uuid = UUID.randomUUID();
            // Generate path for saving to static folder
            Path savedPath = this.root.resolve(uuid.toString());
            // Check for exist the static folder.
            if (!Files.exists(root)) {
                this.init();
            }
            // Save to static folder
            Files.copy(file.getInputStream(), savedPath);
            // Start to fill attachment model for write to Database
            attachment.setPath(savedPath.toString());
            attachment.setFileSize(file.getSize());
            attachment.setContentType(file.getContentType());
            attachment.setFileName(uuid.toString());
            attachment.setOriginalFileName(file.getOriginalFilename());
            return attachment;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Attachment load(Long id) {
        return attachmentRepository.findById(id).orElse(null);
    }

    public void deleteAll() {
        //        FileSystemUtils.deleteRecursively(root.toFile());
        File file = root.toFile();
        file.delete();
    }

    @Transactional(readOnly = true)
    public List<Attachment> loadAllByMeetingId(Long meetingId) {
        return attachmentRepository.findAllByMeetingIdAndIsReestrTrue(meetingId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Attachment getReestr(Long meetingId) {
        return attachmentRepository.findOneByMeetingIdAndIsReestrTrue(meetingId).orElse(null);
    }

    public void deleteReestrFile(Long meetingId) {
        attachmentRepository
            .findOneByMeetingIdAndIsReestrTrue(meetingId)
            .ifPresent(
                attachment -> {
                    try {
                        // Delete from static folder
                        Files.delete(Paths.get(attachment.getPath()));
                        // Delete from Database
                        attachmentRepository.deleteById(attachment.getId());
                        log.debug("Deleted Reestr Excel File by Meeting ID: {}", meetingId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            );
    }

    public AttachLogoDTO uploadCompanyLogo(MultipartFile file, Long companyId) {
        Attachment attachment = uploadGeneral(file);
        try {
            attachment.setReestr(false);
            attachment.setMeetingId(null);
            attachment.setAgendaId(null);
            attachment.setCompanyId(companyId);
            attachment = attachmentRepository.save(attachment);
            log.debug("Saved Information for Company logo Attachment: {}", attachment);
            return attachmentMapper.attachmentToAttachmentLogoDTO(attachment);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the Company logo. Error: " + e.getMessage());
        }
    }

    public void deleteCompanyLogo(Long companyId) {
        attachmentRepository
            .findByCompanyIdAndMeetingIdIsNullAndIsReestrFalse(companyId)
            .ifPresent(
                attachment -> {
                    try {
                        // Delete from static folder
                        Files.delete(Paths.get(attachment.getPath()));
                        // Delete from Database
                        attachmentRepository.deleteById(attachment.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            );
    }

    public Resource loadFile(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    // Need to finish for get files by meeting ID
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
