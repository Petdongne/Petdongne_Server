package org.songeun.petdongne_server.address.infrastructure.crawling.exception;

public class AddressDataCrawlingException extends RuntimeException {

    public AddressDataCrawlingException(String message) {
        super(message);
    }

    public AddressDataCrawlingException(String message, Throwable cause) {
        super(message, cause);
    }

}
