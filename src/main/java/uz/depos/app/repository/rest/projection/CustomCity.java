package uz.depos.app.repository.rest.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.depos.app.domain.City;

@Projection(name = "customCity", types = City.class)
public interface CustomCity {
    Integer getId();

    String getName();
}
