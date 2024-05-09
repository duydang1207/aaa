package fit.api.social_network.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class ApiResponse<T> {
    private StatusEnum status;
    private T payload;
    private Map<String, String> error;
    private Map<String, Object> metadata;
    private Long totalElements;
    private Integer totalPages;

    public void ok() {
        this.status = StatusEnum.SUCCESS;
    }

    public void ok(T data) {
        this.status = StatusEnum.SUCCESS;
        this.payload = data;
    }
    public void ok(T data, Long totalElements, Integer totalPages) {
        this.status = StatusEnum.SUCCESS;
        this.payload = data;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public void ok(T data, HashMap<String, Object> metadata) {
        this.status = StatusEnum.SUCCESS;
        this.payload = data;
        this.metadata = metadata;
    }

    public void error(Map<String, String> error) {
        this.status = StatusEnum.ERROR;
        this.error = error;
    }
    public void error(String message) {
        Map<String,String> error = new HashMap<>();
        error.put("error",message);
        this.status = StatusEnum.ERROR;
        this.error = error;
    }

}
