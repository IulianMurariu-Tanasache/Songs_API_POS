package com.pos.user_data.api;

import com.pos.commons.api.UserDataAPI;
import com.pos.commons.dto.UserDataDTO;
import com.pos.user_data.api.factory.RequestParamFilterCreator;
import com.pos.user_data.entity.UserData;
import com.pos.user_data.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserDataController implements UserDataAPI {

    private final UserDataService userDataService;
    private final ModelMapper mapper;

    @Override
    public ResponseEntity<List<UserDataDTO>> getAllUserData(String name, String username, String email, Integer page, Integer itemsPerPage) {
        RequestParamFilterCreator creator = new RequestParamFilterCreator();
        AbstractMap.SimpleEntry<String, String> paramToFilterBy = creator.getParamToFilterBy(name,username,email);
        return ResponseEntity.ok(
                creator.getRequestParamFilter(userDataService, paramToFilterBy.getKey())
                    .getFilteredList(paramToFilterBy.getValue(), page, itemsPerPage).stream()
                    .map(userData -> mapper.map(userData, UserDataDTO.class))
                    .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<Void> addUserData(UserDataDTO userDataDTO) {
        UserData savedData = userDataService.saveUserData(mapper.map(userDataDTO, UserData.class));
        return ResponseEntity.created(URI.create("/api/userdata/" + savedData.getUserId())).build();
    }

    @Override
    public ResponseEntity<UserDataDTO> getUserDataById(Integer id) {
        return  ResponseEntity.of(
                userDataService.getUserDataByUserId(id)
                    .map(userData -> mapper.map(userData, UserDataDTO.class))
        );
    }

    @Override
    public ResponseEntity<Void> addOrUpdateUserData(Integer id, UserDataDTO userDataDTO) {
        Optional<UserData> userDataOptional = userDataService.updateUserDataByUserId(id, mapper.map(userDataDTO, UserData.class));
        if(userDataOptional.isPresent())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.created(URI.create("/api/userdata/" + id)).build();
    }

    @Override
    public ResponseEntity<Void> deleteUserDataById(Integer id) {
        try{
            userDataService.deleteUserDataByUserId(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
