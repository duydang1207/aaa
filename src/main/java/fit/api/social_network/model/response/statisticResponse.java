package fit.api.social_network.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class statisticResponse {
    List<Long> columnY;
    List<String> columnX;
}
