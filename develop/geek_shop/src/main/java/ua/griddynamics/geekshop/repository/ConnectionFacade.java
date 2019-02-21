package ua.griddynamics.geekshop.repository;

import lombok.Getter;
import ua.griddynamics.geekshop.repository.postgres.geekshop.GeekShopConnection;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-21
 */
public class ConnectionFacade {
    @Getter
    private GeekShopConnection geekShopConnection = new GeekShopConnection("app/geek_shop_db.properties");
}
