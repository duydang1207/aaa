package fit.api.social_network.controller;

import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.BadRequestException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.BlockCriteria;
import fit.api.social_network.model.criteria.UserCriteria;
import fit.api.social_network.model.entity.Block;
import fit.api.social_network.model.entity.Followers;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.mapper.BlockMapper;
import fit.api.social_network.model.mapper.UserMapper;
import fit.api.social_network.model.request.block.CreateBlockRequest;
import fit.api.social_network.model.request.follow.CreateFollowRequest;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.repository.BlockRepository;
import fit.api.social_network.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
public class BlockController extends AbasicMethod {
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Block block = blockRepository.findById(id).orElse(null);
        if (block == null) {
            throw new NotFoundException("Block Not Found");
        }
        apiResponse.ok(blockMapper.toResponse(block));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> listBlocks(BlockCriteria blockCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<Block> blockPage = blockRepository.findAll(blockCriteria.getSpecification(), pageable);
        apiResponse.ok(blockMapper.toResponseList(blockPage.getContent()), blockPage.getTotalElements(), blockPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Block block = blockRepository.findById(id).orElse(null);
        if (block == null) {
            throw new NotFoundException("Block Not Found");
        }
        blockRepository.deleteById(id);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody CreateBlockRequest createBlockRequest, BindingResult bindingResult) throws BadRequestException {
        ApiResponse apiResponse = new ApiResponse();
        User blockingUser = userRepository.findById(getCurrentUserId()).orElse(null);
        if(blockingUser == null){
            throw new NotFoundException("blockingUser not found");
        }
        User user = userRepository.findById(createBlockRequest.getUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Block block = blockRepository.findFirstByUserIdAndBlockUserId(blockingUser.getId(),user.getId());
        if(block != null){
            throw new BadRequestException("user already block this user");
        }
        block = new Block();
        block.setUser(user);
        block.setBlockUser(blockingUser);
        blockRepository.save(block);
        apiResponse.ok("Create success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteByUserId(@PathVariable Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        User blockingUser = userRepository.findById(getCurrentUserId()).orElse(null);
        if(blockingUser == null){
            throw new NotFoundException("blockingUser not found");
        }
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Block block = blockRepository.findFirstByUserIdAndBlockUserId(blockingUser.getId(),user.getId());
        if(block == null){
            throw new BadRequestException("user not block this user");
        }
        blockRepository.deleteById(block.getId());
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}

