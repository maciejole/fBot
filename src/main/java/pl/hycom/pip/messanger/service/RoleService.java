/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.hycom.pip.messanger.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.hycom.pip.messanger.controller.model.RoleDTO;
import pl.hycom.pip.messanger.repository.RoleRepository;
import pl.hycom.pip.messanger.repository.model.Role;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Maciek on 2017-05-27.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    private MapperFacade orikaMapper;

    public List<RoleDTO> findAllRoles() {
        log.info("Searching all roles");

        return orikaMapper.mapAsList(StreamSupport.stream(roleRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()), RoleDTO.class);
    }

    public Role addRole(Role role) {
        log.info("Adding role " + role);
        return roleRepository.save(role);
    }

    public Optional<Role> findRoleByName(final String roleName) {
        log.info("Searching for role: " + roleName);
        return roleRepository.findByAuthorityIgnoreCase(roleName);
    }
}
