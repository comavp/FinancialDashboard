package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.model.entity.IssuerInfo;
import ru.comavp.dashboard.repository.IssuerInfoRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class IssuerInfoService {

    private IssuerInfoRepository issuerInfoRepository;

    public void saveAllUniqueIssuerInfoItems(List<IssuerInfo> issuerInfoList) {
        issuerInfoList.forEach(item -> {
            if (!issuerInfoRepository.existsByIssuerName(item.getIssuerName())) {
                issuerInfoRepository.save(item);
            }
        });
    }

    public List<IssuerInfo> findAll() {
        return issuerInfoRepository.findAll();
    }
}
