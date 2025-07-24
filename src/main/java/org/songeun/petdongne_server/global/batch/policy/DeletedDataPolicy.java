package org.songeun.petdongne_server.global.batch.policy;

import java.time.LocalDate;

public interface DeletedDataPolicy {

    boolean isDataValid(LocalDate deletedDate, LocalDate currentDate);

}
