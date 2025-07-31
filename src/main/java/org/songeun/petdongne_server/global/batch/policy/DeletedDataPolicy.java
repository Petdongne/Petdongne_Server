package org.songeun.petdongne_server.global.batch.policy;

import java.time.LocalDate;

public interface DeletedDataPolicy {

    boolean isValidData(LocalDate deletedDate, LocalDate currentDate);

}
