package org.songeun.petdongne_server.compare.application.service;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.compare.domain.AddressTableNameCreator;
import org.springframework.stereotype.Service;

// todo 제거
@Service
@RequiredArgsConstructor
public class AddressTableNameService {

    private final AddressTableNameCreator addressTableNameCreator;

    public String createNew() {
        return addressTableNameCreator.createNewTableName();
    }

    public String createBackup() {
        return addressTableNameCreator.createBackupTableName();
    }

}
