package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.HabitatCreationDTO;
import bg.softuni.zooarchitect.model.entity.Habitat;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class HabitatService {

    private final RestClient restClient;

    public HabitatService(@Qualifier("habitatsRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Habitat> getAll() {
        return restClient
                .get()
                .uri("/habitats")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }

    public void save(HabitatCreationDTO habitatCreationDTO) {
        restClient
                .post()
                .uri("/habitats/create")
                .body(habitatCreationDTO)
                .retrieve();
    }

    public Habitat getHabitatById(long habitatId) {
        return restClient.get()
                .uri("/habitats/{habitatId}", habitatId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Habitat.class);
    }

    public void deleteHabitatById(long habitatId) {
        restClient
                .delete()
                .uri("/habitats/{habitatId}", habitatId)
                .retrieve()
                .body(Habitat.class);
    }

    public void deleteAll() {
        restClient
                .delete()
                .uri("/habitats")
                .retrieve()
                .body(Habitat.class);
    }
}
