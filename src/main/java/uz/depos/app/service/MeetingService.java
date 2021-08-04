package uz.depos.app.service;

import uz.depos.app.domain.Meeting;
import uz.depos.app.domain.User;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.ReqMeeting;
import uz.depos.app.service.dto.ResPageable;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

public interface MeetingService {
    ApiResponse addMeeting(ReqMeeting request);

    ApiResponse getMeeting(Meeting meeting);

    ResPageable getMeetings(int page, int size, boolean sort) throws BadRequestAlertException;

    ApiResponse deleteMeeting(Long id);
}
