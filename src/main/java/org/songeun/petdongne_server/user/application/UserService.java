package org.songeun.petdongne_server.user.application;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

}
