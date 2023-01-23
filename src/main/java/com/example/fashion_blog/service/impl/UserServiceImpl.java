package dev.decagon.fashion_blog_api.service.impl;

import dev.decagon.fashion_blog_api.dto.AddUserDto;
import dev.decagon.fashion_blog_api.dto.LoginUserDto;
import dev.decagon.fashion_blog_api.exceptions.AlreadyExistException;
import dev.decagon.fashion_blog_api.exceptions.BadRequestException;
import dev.decagon.fashion_blog_api.exceptions.NotFoundException;
import dev.decagon.fashion_blog_api.entities.User;
import dev.decagon.fashion_blog_api.repository.UserRepository;
import dev.decagon.fashion_blog_api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public void addUser(AddUserDto addUserDto) {

        boolean emailExists = userRepository.existsByEmail(addUserDto.getEmail());

        if (emailExists){
            throw new AlreadyExistException("Email Already Exist");
        }

        User user = new User();

        BeanUtils.copyProperties(addUserDto, user);

        userRepository.save(user);
    }

    @Override
    public void loginUser(LoginUserDto loginUserDto) {
        String email = loginUserDto.getEmail();
        Optional<User> foundUser = userRepository.findUserByEmail(email);

        if(foundUser.isEmpty()){
            throw new NotFoundException("User with email: " + email + " Not Found");
        }

        User user = foundUser.get();

        if(!user.getPassword().equals(loginUserDto.getPassword())){
            throw new BadRequestException("Incorrect Password");
        }
    }
}
