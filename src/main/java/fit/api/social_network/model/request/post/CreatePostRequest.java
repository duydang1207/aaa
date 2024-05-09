package fit.api.social_network.model.request.post;

import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.PostCriteria;
import fit.api.social_network.model.entity.Posts;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.model.response.post.PostResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CreatePostRequest implements Serializable {
    @NotEmpty
    private String image_url;
    private String caption;
    @NonNull
    private Integer kind; // Posts & Stories 1 & 2

}
