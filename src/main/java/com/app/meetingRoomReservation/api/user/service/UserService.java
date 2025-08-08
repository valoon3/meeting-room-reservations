package com.app.meetingRoomReservation.api.user.service;

import com.app.meetingRoomReservation.api.user.dto.JoinRequest;
import com.app.meetingRoomReservation.api.user.entity.User;
import com.app.meetingRoomReservation.api.user.repository.UserRepository;
import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(JoinRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if(optionalUser.isPresent()) {
            throw new BadRequestException(ErrorType.ALREADY_REGISTERED_EMAIL);
        }

        User user = User.create(request.getEmail(), request.getPassword(), request.getNickname(), request.getPhoneNumber());
        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }
}
