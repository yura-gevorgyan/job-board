package am.itspace.jobboard.controller.admin.endpoint;

import am.itspace.jobboard.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/companies")
public class AdminCompaniesEndpoint {

    private final CompanyService companyService;

    @DeleteMapping("/delete/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable int companyId) {
        companyService.deleteById(companyId);
        log.info("Company by {} id, was deleted by Admin", companyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
