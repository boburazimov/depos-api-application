package uz.depos.app.service;

import org.springframework.stereotype.Service;

@Service
public class AttachmentService {
    //    final AttachmentRepository attachmentRepository;
    //    final AttachmentContentRepository attachmentContentRepository;
    //
    //    public AttachmentService(AttachmentRepository attachmentRepository, AttachmentContentRepository attachmentContentRepository) {
    //        this.attachmentRepository = attachmentRepository;
    //        this.attachmentContentRepository = attachmentContentRepository;
    //    }
    //
    //    public ApiResponse uploadFile(MultipartHttpServletRequest request) {
    //        try {
    //            Iterator<String> itr = request.getFileNames();
    //            MultipartFile file = request.getFile(itr.next());
    //
    //            Attachment attachment = new Attachment();
    //            attachment.setName(Objects.requireNonNull(file).getOriginalFilename());
    //            attachment.setContentType(file.getContentType());
    //            attachment.setSize(file.getSize());
    //            Attachment savedAttachment = attachmentRepository.save(attachment);
    //
    //            AttachmentContent attachmentContent = new AttachmentContent();
    //            attachmentContent.setAttachment(savedAttachment);
    //            attachmentContent.setContent(file.getBytes());
    //            attachmentContentRepository.save(attachmentContent);
    //
    //            ResUploadFile resUploadFile = new ResUploadFile();
    //            resUploadFile.setFileId(savedAttachment.getId());
    //            resUploadFile.setFileName(savedAttachment.getName());
    //            resUploadFile.setFileType(savedAttachment.getContentType());
    //            resUploadFile.setSize(savedAttachment.getSize());
    //            resUploadFile.setFileDownloadUri(
    //                ServletUriComponentsBuilder
    //                    .fromCurrentContextPath()
    //                    .path("/api/file/")
    //                    .path(savedAttachment.getId().toString())
    //                    .toUriString()
    //            );
    //            return new ApiResponse("Файл сохранен!", true, resUploadFile);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //            return new ApiResponse("Ошибка при сохранении!", false, e.getCause());
    //        }
    //    }
    //
    //    public HttpEntity<?> getFile(Long id) {
    //        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getFileId"));
    //        AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(id);
    //        return ResponseEntity
    //            .ok()
    //            .contentType(MediaType.parseMediaType(attachment.getContentType()))
    //            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getName() + "\"")
    //            .body(attachmentContent.getContent());
    //    }
}
