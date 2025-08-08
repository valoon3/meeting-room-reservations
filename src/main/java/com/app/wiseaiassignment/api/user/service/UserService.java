package com.app.wiseaiassignment.api.user.service;

import com.app.wiseaiassignment.api.user.dto.JoinRequest;
import com.app.wiseaiassignment.api.user.entity.User;
import com.app.wiseaiassignment.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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
            // todo: throw custom exception
        }

        User user = User.create(request.getEmail(), request.getPassword(), request.getNickname(), request.getPhoneNumber());
        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }
}
