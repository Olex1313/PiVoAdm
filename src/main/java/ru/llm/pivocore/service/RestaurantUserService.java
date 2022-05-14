package ru.llm.pivocore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.llm.pivocore.enums.UserRoles;
import ru.llm.pivocore.exception.RestaurantUserServiceException;
import ru.llm.pivocore.mapper.RestaurantUserMapper;
import ru.llm.pivocore.model.request.RestaurantUserRegisterRequest;
import ru.llm.pivocore.model.dto.RestaurantUserDto;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;
import ru.llm.pivocore.repository.RestaurantUsersRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantUserService implements UserDetailsService {

    private final RestaurantUsersRepository restaurantUsersRepository;

    private final PasswordEncoder passwordEncoder;

    private final RestaurantUserMapper restaurantUserMapper;


    @Transactional
    public RestaurantUserEntity save(RestaurantUserEntity restaurantUserEntity) {
        restaurantUserEntity.setPasswordHash(passwordEncoder.encode(restaurantUserEntity.getPasswordHash()));
        return restaurantUsersRepository.save(restaurantUserEntity);
    }

    public List<RestaurantUserDto> getAll() {
        try {
            var restaurantUserEntities = restaurantUsersRepository.findAll();
            return restaurantUserEntities.stream()
                    .map(restaurantUserMapper::entityToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RestaurantUserServiceException(e.getMessage(), e.getCause());
        }
    }

    public RestaurantUserDto register(RestaurantUserRegisterRequest restaurantUserRegisterRequest) {
        var restaurantUserDto = RestaurantUserDto.builder()
                .username(restaurantUserRegisterRequest.getUsername())
                .email(restaurantUserRegisterRequest.getEmail())
                .firstName(restaurantUserRegisterRequest.getFirstName())
                .middleName(restaurantUserRegisterRequest.getMiddleName())
                .lastName(restaurantUserRegisterRequest.getLastName())
                .dateOfRegistration(LocalDate.now().toString())
                .passwordHash(restaurantUserRegisterRequest.getPassword())
                .enabled(true)
                .build();
        var entity = restaurantUserMapper.dtoToEntity(restaurantUserDto);
        return restaurantUserMapper.entityToDto(save(entity));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var restaurantUserEntity = restaurantUsersRepository.findByUsername(username);
        if (restaurantUserEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        var authorities = List.of(new SimpleGrantedAuthority(UserRoles.RESTAURANT_USER.name()));
        return new User(restaurantUserEntity.getUsername(), restaurantUserEntity.getPasswordHash(), authorities);
    }

}
