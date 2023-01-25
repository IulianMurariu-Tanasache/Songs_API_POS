package com.pos.user_data.service.impl;

import com.pos.user_data.entity.UserData;
import com.pos.user_data.repository.UserDataRepository;
import com.pos.user_data.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pos.commons.util.ServiceUtility.makePageRequest;

@Service
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService {

    private final UserDataRepository repository;



    @Override
    public List<UserData> getAllUsersData(Integer page, Integer itemsPerPage) {
        return repository.findAll(makePageRequest(page, itemsPerPage)).toList();
    }

    @Override
    public UserData saveUserData(UserData userData) {
        repository.save(userData);
        return userData;
    }

    @Override
    public Optional<UserData> getUserDataByUserId(Integer userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public void deleteUserDataByUserId(Integer userId) {
        if(!repository.existsById(userId))
            throw new EntityNotFoundException();
        repository.deleteByUserId(userId);
    }

    @Override
    public Optional<UserData> updateUserDataByUserId(Integer userId, UserData userData) {
        userData.setUserId(userId);
        Optional<UserData> existing = repository.findById(userId);
        repository.save(userData);
        return existing;
    }

    @Override
    public List<UserData> getAllUsersDataByName(String name, Integer page, Integer itemsPerPage) {
        String[] names = name.split(" ");
        PageRequest pageRequest = makePageRequest(page, itemsPerPage);

        if(names.length > 2) {
            throw new IllegalArgumentException("Name has to many spaces!");
        }
        if(names.length == 0) {
            return this.getAllUsersData(page, itemsPerPage);
        }
        if(names.length == 1) {
            List<UserData> firstList = new ArrayList<>(repository.findByLastNameLike(names[0], pageRequest));
            List<UserData> secondList = new ArrayList<>(repository.findByFirstNameLike(names[0], pageRequest));
            return Stream.concat(firstList.stream(), secondList.stream()).distinct().collect(Collectors.toList());
        }

        List<UserData> auxList;

        auxList = repository.findByFirstNameLike(names[1], pageRequest);
        List<UserData> firstList = repository.findByLastNameLike(names[0], pageRequest).stream()
                .filter(auxList::contains)
                .collect(Collectors.toList());

        auxList = repository.findByFirstNameLike(names[0], pageRequest);
        List<UserData> secondList = repository.findByLastNameLike(names[1], pageRequest).stream()
                .filter(auxList::contains)
                .collect(Collectors.toList());

        return Stream.concat(firstList.stream(), secondList.stream()).distinct().collect(Collectors.toList());
    }

    @Override
    public List<UserData> getAllUsersDataByUsername(String valueToFilterBy, Integer page, Integer itemsPerPage) {
        return repository.findByUsernameLike(valueToFilterBy, makePageRequest(page, itemsPerPage));
    }

    @Override
    public List<UserData> getAllUsersDataByEmail(String valueToFilterBy, Integer page, Integer itemsPerPage) {
        return repository.findByEmailLike(valueToFilterBy, makePageRequest(page, itemsPerPage));
    }
}
