package ra.ss4.entity.B3;

public enum FoodStatus {
    AVAILABLE("Có sẵn"),
    OUT_OF_STOCK("Hết hàng"),
    EXPIRED("Hết hạn"),
    DISCONTINUED("Ngừng kinh doanh");

    private final String displayName;

    FoodStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
