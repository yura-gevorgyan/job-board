package am.itspace.jobboard.controller.endpoint;

import am.itspace.jobboard.entity.Country;
import am.itspace.jobboard.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CountryEndPoint {

    private final CountryService countryService;

    @GetMapping("/getPhoneCode/{countryId}")
    public ResponseEntity<Country> getPhoneCode(@PathVariable("countryId") int id) {
        return ResponseEntity.ok(countryService.findPhoneCodeById(id));
    }
}
