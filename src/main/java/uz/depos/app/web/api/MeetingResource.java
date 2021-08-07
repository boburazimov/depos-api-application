package uz.depos.app.web.api;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.depos.app.config.Constants;
import uz.depos.app.domain.Meeting;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.service.MeetingService;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.ReqMeeting;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping(path = "/api/meeting")
public class MeetingResource {

    final MeetingService meetingService;
    final MeetingRepository meetingRepository;

    public MeetingResource(MeetingService meetingService, MeetingRepository meetingRepository) {
        this.meetingService = meetingService;
        this.meetingRepository = meetingRepository;
    }

    @PostMapping
    public HttpEntity<?> addMeeting(@RequestBody ReqMeeting request) {
        ApiResponse response = meetingService.addMeeting(request);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getMeeting(@PathVariable Long id) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getMeeting"));
        ApiResponse response = meetingService.getMeeting(meeting);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping
    public HttpEntity<?> getMeetings(
        @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE) int page,
        @RequestParam(value = "size", defaultValue = Constants.DEFAULT_SIZE) int size,
        @RequestParam(value = "sort", defaultValue = "false") boolean sort
    ) throws BadRequestAlertException {
        return ResponseEntity.ok(meetingService.getMeetings(page, size, sort));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteMeeting(@PathVariable Long id) {
        ApiResponse response = meetingService.deleteMeeting(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }
}
