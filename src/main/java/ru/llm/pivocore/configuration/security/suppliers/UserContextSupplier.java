package ru.llm.pivocore.configuration.security.suppliers;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ru.llm.pivocore.model.entity.AppUserEntity;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;

@Component
public interface UserContextSupplier {
    RestaurantUserEntity getRestaurantUserEntityFromSecContext();
    AppUserEntity getAppUserEntityFromSecContext();
}
