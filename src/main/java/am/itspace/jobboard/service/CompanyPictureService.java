package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.CompanyPicture;

import java.util.List;

public interface CompanyPictureService {

    List<CompanyPicture> findAllByCompanyId(int id);

}
