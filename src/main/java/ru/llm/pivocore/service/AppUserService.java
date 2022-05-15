package ru.llm.pivocore.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Primary;
import lombok.val;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.llm.pivocore.enums.UserRoles;
import ru.llm.pivocore.exception.AppUserServiceException;
import ru.llm.pivocore.exception.RestaurantUserServiceException;
import ru.llm.pivocore.mapper.AppUserMapper;
import ru.llm.pivocore.model.request.AppUserRegisterRequest;
import ru.llm.pivocore.model.dto.AppUserDto;
import ru.llm.pivocore.model.entity.AppUserEntity;
import ru.llm.pivocore.repository.AppUserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    private final AppUserMapper appUserMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public List<AppUserDto> getAll() {
        try {
            var appUserEntities = appUserRepository.findAll();
            return appUserEntities.stream()
                    .map(appUserMapper::entityToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new AppUserServiceException(e.getMessage(), e.getCause());
        }
    }

    @Transactional
    public AppUserEntity save(AppUserEntity appUserEntity) {
        appUserEntity.setPasswordHash(passwordEncoder.encode(appUserEntity.getPasswordHash()));
        return appUserRepository.save(appUserEntity);
    }

    @Transactional
    public Optional<AppUserDto> getById(Long id) {
        val entity = appUserRepository.findById(id);
        if (entity.isEmpty()) return Optional.empty();
        return Optional.of(appUserMapper.entityToDto(entity.get()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var appUserEntity = appUserRepository.findByUsername(username);
        if (appUserEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        var authorities = List.of(new SimpleGrantedAuthority(UserRoles.APP_USER.name()));
        return new User(appUserEntity.getUsername(), appUserEntity.getPasswordHash(), authorities);
    }

    public AppUserDto registerUser(AppUserRegisterRequest registerRequest) {
        var appUserDto = AppUserDto.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .middleName(registerRequest.getMiddleName())
                .username(registerRequest.getUsername())
                .dateOfBirth(registerRequest.getDateOfBirth())
                .email(registerRequest.getEmail())
                .passwordHash(registerRequest.getPassword())
                .dateOfRegistration(LocalDate.now().toString())
                .build();
        var entity = appUserMapper.dtoToEntity(appUserDto);
        return appUserMapper.entityToDto(save(entity));
    }

    @Transactional
    public AppUserEntity getCurrentUserFromSecContext() {
        val currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return appUserRepository.findByUsername(currentUsername);
        } catch (Exception e) {
            throw new AppUserServiceException(
                    "Couldn't retrieve app user for current user:%s in session".formatted(currentUsername)
            );
        }
    }

}
