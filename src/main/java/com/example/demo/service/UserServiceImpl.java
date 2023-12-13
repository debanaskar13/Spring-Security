package com.example.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CreateUserDto;
import com.example.demo.dto.GetUserDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.entities.EmailDetails;
import com.example.demo.entities.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") String sender;

    @Override
    public GetUserDto createUser(CreateUserDto userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setRegisteredAt(new Date());
        user.setLastLogin(new Date());
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        try {
            User savedUser = this.userRepository.save(user);
            return modelMapper.map(savedUser, GetUserDto.class);
        } catch (DataIntegrityViolationException e) {
            String message = e.getRootCause().getMessage().split("Detail:")[1];
            throw new RuntimeException(message);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public GetUserDto getSingleUser(int userId) throws Exception {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new Exception("Resource Not Found"));
        return modelMapper.map(user, GetUserDto.class);
    }

    @Override
    public void deleteUser(int userId) throws Exception {
        this.userRepository.findById(userId).orElseThrow(() -> new Exception("Resource Not Found"));
    }

    @Override
    public List<GetUserDto> getAllUser() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, GetUserDto.class)).collect(Collectors.toList());
    }

    @Override
    public GetUserDto updateUser(int userId, UpdateUserDto userDto) throws Exception {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new Exception("Resource Not Found"));
        user.setLastLogin(new Date());
        try {
            copyUserDetails(user, userDto);
            User savedUser = this.userRepository.save(user);
            return this.modelMapper.map(savedUser, GetUserDto.class);
        } catch (ParseException e) {
            throw new Exception("Please Enter Date Of Birth in dd/MM/yyyy format");
        }catch (Exception ex){
            throw new Exception("Something Went Wrong");
        }
    }

    private void copyUserDetails(User user, UpdateUserDto userDto) throws ParseException {
        user.setFirstName(userDto.getFirstName() == null ? user.getFirstName() : userDto.getFirstName());
        user.setLastName(userDto.getLastName() == null ? user.getLastName() : userDto.getLastName());
        user.setDateOfBirth(userDto.getDateOfBirth() == null ? user.getDateOfBirth()
                : new SimpleDateFormat("dd/MM/yyyy").parse(userDto.getDateOfBirth()));
        user.setSecondaryEmail(
                userDto.getSecondaryEmail() == null ? user.getSecondaryEmail() : userDto.getSecondaryEmail());
        user.setMobileNumber(userDto.getMobileNumber() == null ? user.getMobileNumber() : userDto.getMobileNumber());
        user.setActive(userDto.isActive() ? true : user.isActive());
        user.setIntro(userDto.getIntro() == null ? user.getIntro() : userDto.getIntro());
        user.setProfilePhoto(userDto.getProfilePhoto() == null ? user.getProfilePhoto() : userDto.getProfilePhoto());
        user.setHost(userDto.isHost() ? true : user.isHost());
    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
        User user = this.userRepository.findByPrimaryEmail(email).orElseThrow(() -> new UsernameNotFoundException("Could not Found any User with this email !!"));
        user.setResetPasswordToken(token);
        this.userRepository.save(user);
    }

    @Override
    public User getUserByResetPasswordToken(String token) {
        return this.userRepository.findByResetPasswordToken(token).orElseThrow(() -> new UsernameNotFoundException("Invalid Token"));
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(this.passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        this.userRepository.save(user);
    }

    @Override
    public void sentEmail(EmailDetails emailDetails, String resetPasswordLink) {
        
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        buildMailDetails(emailDetails,resetPasswordLink);

        try {     
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMsgBody());
    
            this.javaMailSender.send(mailMessage);
        } catch (Exception e) {
            new RuntimeException("Error in Send Mail");
        }

    }

    private void buildMailDetails(EmailDetails emailDetails,String link) {
        String subject = "Here's the link to reset your password";
     
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        
        emailDetails.setSubject(subject);
        emailDetails.setMsgBody(content);
    }

}
