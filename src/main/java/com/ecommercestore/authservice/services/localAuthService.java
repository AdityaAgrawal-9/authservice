package com.ecommercestore.authservice.services;

import com.ecommercestore.authservice.dtos.UserDto;
import com.ecommercestore.authservice.dtos.ValidateResponseDto;
import com.ecommercestore.authservice.exceptions.InvalidPasswordException;
import com.ecommercestore.authservice.exceptions.InvalidSessionException;
import com.ecommercestore.authservice.exceptions.UserNotFoundException;
import com.ecommercestore.authservice.models.Session;
import com.ecommercestore.authservice.models.SessionStatus;
import com.ecommercestore.authservice.models.User;
import com.ecommercestore.authservice.repositories.SessionRepository;
import com.ecommercestore.authservice.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("localAuthService")
public class localAuthService implements IAuthService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SessionRepository sessionRepository;
    private final String encodedKey;

    public localAuthService(UserRepository userRepository,
                            BCryptPasswordEncoder bCryptPasswordEncoder,
                            SessionRepository sessionRepository,
                            String encodedKey) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepository = sessionRepository;
        this.encodedKey = encodedKey;
    }

    @Override
    public ResponseEntity<UserDto> loginUser(String email, String password) throws UserNotFoundException, InvalidPasswordException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty() ){
            throw new UserNotFoundException(" User with email : " + email +" not found.");
        }

        if((user.isPresent() && !bCryptPasswordEncoder.matches(password, user.get().getPassword()))) {
            throw new InvalidPasswordException("Invalid Password");
        }

        Session session = new Session();
        session.setUser(user.get());
        session.setSessionStatus(SessionStatus.ACTIVE);

//        /*Creating a Random 20 character String JWT*/
//        String token = RandomStringUtils.randomAscii(20);

//        Creating a SecretKey to Sign the JWT
//        You can either create it like this but then you will have to store it somewhere to be able to
//        validate later.
//        SecretKey secretKey = Jwts.SIG.HS256.key().build();

//        ******* Important Note -
//        Currently using a hardcoded SHA256 compatible encoded key as a secret
//        which is being passed from the application.properties file.
//        It should be passed from the Environment Variables to the application.properties file.
//        This is as good as below.
//        String encodedKey = "h1w8ZbXrj6JFcmnPuNjaouOSVZz5wIsmj8OMa/WpO6c=";

        byte[] rawKey = Base64.getDecoder().decode(encodedKey);
        SecretKey secretKey = new SecretKeySpec(rawKey, 0, rawKey.length, "HmacSHA256");

        /* Creating a Signed JWT*/
        String token = Jwts.builder()
                .claim("Id", user.get().getId())
                .claim("User Name", user.get().getName())
                .claim("e-mail", user.get().getEmail())
                .issuer("Auth Service e-commerce Store")
                .expiration(Date.from(LocalDateTime.now()
                                        .plusDays(15)
                                        .atZone(ZoneId.systemDefault())
                                        .toInstant()))
                .signWith(secretKey)
                .compact();

        session.setSessionToken(token);
        session.setExpiringAt(LocalDateTime.now().plusDays(15));
        sessionRepository.save(session);
        UserDto userDto = UserDto.fromUser(user.get());
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("AUTH_TOKEN", token);
        return new ResponseEntity(userDto,
                headers,
                HttpStatus.OK);
    }

    public ResponseEntity<?> logoutUser(Long userId, String token) throws
            UserNotFoundException,
            InvalidSessionException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException("User not found.");
        }
        Optional<List<Session>> sessionsList = sessionRepository.findByUser_Id(userId);
        if(sessionsList.isEmpty()){
            throw new InvalidSessionException("Session is Invalid");
        }
        for(Session session : sessionsList.get()){
            if(session.getSessionToken().equals(token)){
                session.setExpiringAt(LocalDateTime.now());
                session.setIsDeleted(true);
                session.setSessionStatus(SessionStatus.LOGGED_OUT);
                sessionRepository.save(session);
                return new ResponseEntity<String>(
                        "User Logged Out Successfully",
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<String>(
                "Invalid Request",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ValidateResponseDto> validateUser(User user, String token)
            throws UserNotFoundException, InvalidSessionException {
        // Check if user exists
        Optional<User> existingUser = userRepository.findByIdOrEmail(user.getId(), user.getEmail());
        if (existingUser.isEmpty() || existingUser.get().getIsDeleted()==true) {
            throw new UserNotFoundException("User not found.");
        }

        // Fetch all sessions for the user
        Optional<List<Session>> sessionsList = sessionRepository.findByUser_Id(user.getId());
        if (sessionsList.isEmpty()) {
            throw new InvalidSessionException("No sessions found for user.");
        }

        // Find the session with the provided token
        Session validSession = null;
        for (Session session : sessionsList.get()) {
            if (session.getSessionToken().equals(token)) {
                validSession = session;
                break;
            }
        }

        // If no session found with the token
        if (validSession == null) {
            throw new InvalidSessionException("Invalid session token.");
        }

        // Check if session is expired
        if (LocalDateTime.now().isAfter(validSession.getExpiringAt())) {
            throw new InvalidSessionException("Session has expired.");
        }

        // Check if session is still active
        if (!validSession.getSessionStatus().equals(SessionStatus.ACTIVE)) {
            throw new InvalidSessionException("Session is not active.");
        }

        // Build the response DTO
        ValidateResponseDto validateResponseDto = new ValidateResponseDto();
        UserDto userDto = new UserDto();
        userDto.setId(existingUser.get().getId());
        userDto.setName(existingUser.get().getName());
        userDto.setEmail(existingUser.get().getEmail());
        userDto.setRoles(existingUser.get().getRoles());

        validateResponseDto.setUserDto(userDto);
        validateResponseDto.setSessionStatus(validSession.getSessionStatus());

        return new ResponseEntity<>(validateResponseDto, HttpStatus.OK);
    }

}
