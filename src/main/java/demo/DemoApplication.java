package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/")
    public String index() {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.github.com/repos/jetbrains/kotlin/tags";

        ResponseEntity<List<Release>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Release>>(){}
        );

        List<Release> releases = response.getBody();

        if (releases == null) {
            releases = Collections.emptyList();
        }

        return releases.stream()
                .filter(release -> !release.name.contains("-"))
                .findFirst()
                .map(Release::getName)
                .orElse("not found");
    }

    static class Release {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
