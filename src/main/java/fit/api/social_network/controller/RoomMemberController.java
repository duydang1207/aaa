package fit.api.social_network.controller;

import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.RoomCriteria;
import fit.api.social_network.model.criteria.RoomMemberCriteria;
import fit.api.social_network.model.entity.Room;
import fit.api.social_network.model.entity.RoomMember;
import fit.api.social_network.model.mapper.RoomMapper;
import fit.api.social_network.model.mapper.RoomMemberMapper;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.repository.RoomMemberRepository;
import fit.api.social_network.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room-members")
@RequiredArgsConstructor
public class RoomMemberController {
    @Autowired
    private RoomMemberRepository roomMemberRepository;
    @Autowired
    private RoomMemberMapper roomMemberMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        RoomMember roomMember = roomMemberRepository.findById(id).orElse(null);
        if (roomMember == null) {
            throw new NotFoundException("Room Member Not Found");
        }
        apiResponse.ok(roomMemberMapper.toResponse(roomMember));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> listRoomMembers(RoomMemberCriteria roomMemberCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<RoomMember> roomMemberPage = roomMemberRepository.findAll(roomMemberCriteria.getSpecification(), pageable);
        apiResponse.ok(roomMemberMapper.toResponseList(roomMemberPage.getContent()), roomMemberPage.getTotalElements(), roomMemberPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        RoomMember roomMember = roomMemberRepository.findById(id).orElse(null);
        if (roomMember == null) {
            throw new NotFoundException("Room Member Not Found");
        }
        roomMemberRepository.deleteById(id);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}










