package fit.api.social_network.controller;

import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.PostCriteria;
import fit.api.social_network.model.criteria.RoomCriteria;
import fit.api.social_network.model.dto.MessageDto;
import fit.api.social_network.model.entity.*;
import fit.api.social_network.model.mapper.MessageMapper;
import fit.api.social_network.model.mapper.PostMapper;
import fit.api.social_network.model.mapper.RoomMapper;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.model.response.message.MessageResponse;
import fit.api.social_network.model.response.message.MessageResponseCustom;
import fit.api.social_network.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController extends AbasicMethod{
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomMemberRepository roomMemberRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageMapper messageMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            throw new NotFoundException("Room Not Found");
        }
        apiResponse.ok(roomMapper.toResponse(room));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/get-room/{userId}")
    public ResponseEntity<ApiResponse> getRoomByUser(@PathVariable Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        User userCurrent = userRepository.findById(getCurrentUserId()).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        List<User> users = new ArrayList<>();
        users.add(userCurrent);
        users.add(user);
        if(userCurrent == null || user==null){
            throw new NotFoundException("User not found");
        }
        List<Room> room = roomRepository.findRoomsByUser(userCurrent.getId());
        Room result = new Room();
        for (Room r: room){
            List<RoomMember> roomMembers = roomMemberRepository.findAllByRoom(r);
            boolean check = true;
            for (RoomMember rm: roomMembers) {
                if(!users.contains(rm.getUser())) {
                    check = false;
                    break;
                }
            }
            if(check && roomMembers.size()==users.size())
            {
                result = r;
            }
        }
        apiResponse.ok(roomMapper.toResponse(result));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> listRooms(RoomCriteria roomCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<Room> roomPage = roomRepository.findAll(roomCriteria.getSpecification(), pageable);
        apiResponse.ok(roomMapper.toResponseList(roomPage.getContent()), roomPage.getTotalElements(), roomPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/list-by-user")
    public ResponseEntity<ApiResponse> listRoomByUser() {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }

        List<Room> roomList = roomRepository.findRoomsByUser(user.getId());
        List<MessageResponseCustom> customs = new ArrayList<>();
        for(int i =0 ; i<roomList.size();i++){
            Room room = roomList.get(i);
            room.setName(getNameRoom(room,user));
            roomList.set(i,room);

            Message message = messageRepository.findLastByRoom(room).get(0);
            MessageDto messageDto = messageMapper.toDto(message);
            messageDto.setUsers(roomMemberRepository.findAllNotUser(room,user));
            MessageResponseCustom messageResponseCustom = messageMapper.toResponseCustom(messageDto);
            customs.add(messageResponseCustom);
        }
        apiResponse.ok(customs);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            throw new NotFoundException("Room Not Found");
        }
        roomRepository.deleteById(id);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    public String getNameRoom(Room room,User user){
        if(room.getName()!=null)
            return room.getName();
        RoomMember roomMember = roomMemberRepository.findByRoomNotUser(room,user);
        return roomMember.getUser().getName();
    }
}









