package ua.com.owu.sep2024.orderservice.exception;

public class ShopIsNotAccessibleException extends RuntimeException {
    public ShopIsNotAccessibleException(String shopId) {
        super("Shop id " + shopId + " is not accessible");
    }
}
