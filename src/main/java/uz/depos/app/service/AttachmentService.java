package uz.depos.app.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.depos.app.domain.Attachment;
import uz.depos.app.domain.AttachmentContent;
import uz.depos.app.repository.AttachmentContentRepository;
import uz.depos.app.repository.AttachmentRepository;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.ResUploadFile;

@Service
public class AttachmentService {

    final AttachmentRepository attachmentRepository;
    final AttachmentContentRepository attachmentContentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository, AttachmentContentRepository attachmentContentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentContentRepository = attachmentContentRepository;
    }

    public ApiResponse uploadFile(MultipartHttpServletRequest request) {
        try {
            Iterator<String> itr = request.getFileNames();
            MultipartFile file = request.getFile(itr.next());

            Attachment attachment = new Attachment();
            attachment.setName(Objects.requireNonNull(file).getOriginalFilename());
            attachment.setContentType(file.getContentType());
            attachment.setSize(file.getSize());
            Attachment savedAttachment = attachmentRepository.save(attachment);

            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setAttachment(savedAttachment);
            attachmentContent.setContent(file.getBytes());
            attachmentContentRepository.save(attachmentContent);

            ResUploadFile resUploadFile = new ResUploadFile();
            resUploadFile.setFileId(savedAttachment.getId());
            resUploadFile.setFileName(savedAttachment.getName());
            resUploadFile.setFileType(savedAttachment.getContentType());
            resUploadFile.setSize(savedAttachment.getSize());
            resUploadFile.setFileDownloadUri(
                ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/file/")
                    .path(savedAttachment.getId().toString())
                    .toUriString()
            );
            return new ApiResponse("Файл сохранен!", true, resUploadFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiResponse("Ошибка при сохранении!", false, e.getCause());
        }
    }

    public HttpEntity<?> getFile(Long id) {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getFileId"));
        AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(id);
        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType(attachment.getContentType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getName() + "\"")
            .body(attachmentContent.getContent());
    }
}
