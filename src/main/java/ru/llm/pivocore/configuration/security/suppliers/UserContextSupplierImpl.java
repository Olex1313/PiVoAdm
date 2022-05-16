package ru.llm.pivocore.configuration.security.suppliers;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.llm.pivocore.configuration.security.facade.IAuthenticationFacade;
import ru.llm.pivocore.exception.AppUserNotFoundException;
import ru.llm.pivocore.exception.AppUserServiceException;
import ru.llm.pivocore.exception.RestaurantUserNotFoundException;
import ru.llm.pivocore.model.entity.AppUserEntity;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;
import ru.llm.pivocore.model.request.RestaurantUserRegisterRequest;
import ru.llm.pivocore.repository.AppUserRepository;
import ru.llm.pivocore.repository.RestaurantUsersRepository;

@Component
@AllArgsConstructor
public class UserContextSupplierImpl implements UserContextSupplier{
    private IAuthenticationFacade authenticationFacade;
    private RestaurantUsersRepository restaurantUsersRepository;
    private AppUserRepository appUserRepository;

    @Override
    public RestaurantUserEntity getRestaurantUserEntityFromSecContext() {
        val currentUsername = authenticationFacade.getAuthentication().getName();
        try {
            return restaurantUsersRepository.findByUsername(currentUsername);
        } catch (Exception e) {
            throw new RestaurantUserNotFoundException(
                    "Couldn't retrieve app user for current user:%s in session".formatted(currentUsername)
            );
        }
    }

    @Override
    public AppUserEntity getAppUserEntityFromSecContext() {
        val currentUsername = authenticationFacade.getAuthentication().getName();
        try {
            return appUserRepository.findByUsername(currentUsername);
        } catch (Exception e) {
            throw new AppUserNotFoundException(
                    "Couldn't retrieve app user for current user:%s in session".formatted(currentUsername)
            );
        }
    }
}
