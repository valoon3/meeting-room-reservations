package com.app.meetingRoomReservation.api.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "users"
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String phoneNumber;

    public static User create(String email, String password, String nickname, String phoneNumber) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.nickname = nickname;
        user.phoneNumber = phoneNumber;
        return user;
    }

}
