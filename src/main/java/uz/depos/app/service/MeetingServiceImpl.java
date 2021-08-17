package uz.depos.app.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.depos.app.domain.Meeting;
import uz.depos.app.repository.*;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.ReqMeeting;
import uz.depos.app.service.dto.ResPageable;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

@Service
public class MeetingServiceImpl implements MeetingService {

    final MeetingRepository meetingRepository;
    final AttachmentRepository attachmentRepository;
    final UserRepository userRepository;
    final CompanyRepository companyRepository;
    final CityRepository cityRepository;
    final MemberRepository memberRepository;
    final AgendaRepository agendaRepository;

    public MeetingServiceImpl(
        MeetingRepository meetingRepository,
        AttachmentRepository attachmentRepository,
        UserRepository userRepository,
        CompanyRepository companyRepository,
        CityRepository cityRepository,
        MemberRepository memberRepository,
        AgendaRepository agendaRepository
    ) {
        this.meetingRepository = meetingRepository;
        this.attachmentRepository = attachmentRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.cityRepository = cityRepository;
        this.memberRepository = memberRepository;
        this.agendaRepository = agendaRepository;
    }

    @Override
    public ApiResponse addMeeting(ReqMeeting request) {
        try {
            Meeting meeting = request.getId() == null
                ? new Meeting()
                : meetingRepository.findById(request.getId()).orElseThrow(() -> new ResourceNotFoundException("getMeeting"));
            meeting.setStatus(request.getStatus());
            meeting.setStartDate(request.getStartDate());
            meeting.setStartRegistration(request.getStartRegistration());
            meeting.setEndRegistration(request.getEndRegistration());
            meeting.setCompany(
                request.getCompanyId() == null
                    ? null
                    : companyRepository.findById(request.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException("getCompany"))
            );
            meeting.setCity(
                request.getCityId() == null
                    ? null
                    : cityRepository.findById(request.getCityId()).orElseThrow(() -> new ResourceNotFoundException("getCity"))
            );
            meeting.setAddress(request.getAddress());
            meeting.setDescription(request.getDescription());
            meeting.setAttachments(
                request.getAttachmentsId() == null ? null : attachmentRepository.findAllById(request.getAttachmentsId())
            );
            meeting.setExtraInfo(request.getExtraInfo());
            Meeting savedMeeting = meetingRepository.save(meeting);
            return new ApiResponse(request.getId() == null ? "Заседания сохранен!" : "Заседания изменен!", true, savedMeeting);
        } catch (Exception e) {
            return new ApiResponse("Ошибка при сохранении", false, e.getCause());
        }
    }

    @Override
    public ApiResponse getMeeting(Meeting meeting) {
        return null;
    }

    @Override
    public ResPageable getMeetings(int page, int size, boolean sort) throws BadRequestAlertException {
        return null;
    }

    @Override
    public ApiResponse deleteMeeting(Long id) {
        return null;
    }
}
