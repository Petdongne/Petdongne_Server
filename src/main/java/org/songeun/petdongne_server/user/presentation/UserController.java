package org.songeun.petdongne_server.user.presentation;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.user.application.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

}
