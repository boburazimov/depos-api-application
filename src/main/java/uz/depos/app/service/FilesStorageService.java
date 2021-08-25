package uz.depos.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.depos.app.domain.Attachment;
import uz.depos.app.repository.AttachmentRepository;
import uz.depos.app.service.dto.AttachmentMeetingDTO;
import uz.depos.app.service.mapper.AttachmentMapper;

//@RequiredArgsConstructor
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

    public AttachmentMeetingDTO save(MultipartFile file, Long meetingId) { // , Long agendaId
        Attachment attachment = new Attachment();
        try {
            UUID uuid = UUID.randomUUID();
            Path savedPath = this.root.resolve(uuid.toString());
            if (!Files.exists(root)) {
                this.init();
            }
            Files.copy(file.getInputStream(), savedPath);
            attachment.setPath(savedPath.toString()); //C://users/user/uploads - absolute path, /uploads - relative path
            attachment.setFileSize(file.getSize());
            attachment.setMeetingId(meetingId);
            //            if (agendaId != null) attachment.setAgendaId(agendaId);
            attachment.setContentType(file.getContentType());
            attachment.setFileName(uuid.toString());
            attachment.setOriginalFileName(file.getOriginalFilename());
            attachment = attachmentRepository.save(attachment);
            log.debug("Saved Information for Attachment: {}", attachment);
            AttachmentMeetingDTO attachmentMeetingDTO = attachmentMapper.attachmentToAttachmentMeetingDTO(attachment);
            return attachmentMeetingDTO;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Attachment load(Long id) {
        return attachmentRepository.findById(id).orElse(null);
    }

    public void deleteAll() {
        //        FileSystemUtils.deleteRecursively(root.toFile());
        File file = root.toFile();
        file.delete();
    }

    public List<Attachment> loadAllByMeetingId(Long meetingId) {
        List<Attachment> attachments = attachmentRepository.findAllByMeetingId(meetingId);
        return attachments;
    }
}
